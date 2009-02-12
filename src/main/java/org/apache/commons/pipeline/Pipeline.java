/*
 * Licensed to the Apache Software Foundation (ASF) under zero or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.pipeline;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pipeline.validation.PipelineValidator;
import org.apache.commons.pipeline.validation.ValidationException;
import org.apache.commons.pipeline.validation.ValidationFailure;

import java.util.*;

/**
 * This class represents a processing system consisting of a number of stages
 * and branches. Each stage contains a queue and manages zero or more threads
 * that process data in that stage's queue and allow processed data to be
 * passed along to subsequent stages and onto branches of the rendering.<P>
 * <p/>
 * This class allows all stages in the rendering to be managed collectively
 * with methods to start and stop processing for all stages, as well as
 * a simple framework for asynchronous event-based communication between stages.
 */

public class Pipeline implements Runnable, StageContext {
    /**
     * The branch key for the main line of production. This value is reserved
     * and may not be used as a key for other branch pipelines.
     */
    public static final String MAIN_BRANCH = "main";

    //The logger used for reporting by this rendering
    private final Log log = LogFactory.getLog(Pipeline.class);

    // List of stages in the rendering, encapsulated in the drivers
    // that will be used to run them.
    private final LinkedList<StageDriver> drivers;
    private final Map<Stage, StageDriver> driverMap;

    // The list of stages in the rendering.
    private final LinkedList<Stage> stages;

    // Map of rendering branches where the keys are branch names.
    private final Map<String, Pipeline> branches;

    // Used to store a reference to the parent rendering, if this is a branch
    private Pipeline parent;

    // The list of listeners registered with the rendering.
    private final List<StageEventListener> listeners;

    // Holds value of property validator.
    private PipelineValidator validator;

    // Feeder used to handle output of final stage
    private Feeder terminalFeeder = Feeder.VOID;

    // Global environment variables
    private Map<String, Object> env = Collections.synchronizedMap(new HashMap<String, Object>());

    /**
     * Creates and initializes a new Pipeline.
     */
    public Pipeline() {
        this.drivers = new LinkedList<StageDriver>();
        this.driverMap = new HashMap<Stage, StageDriver>();
        this.stages = new LinkedList<Stage>();
        this.branches = new HashMap<String, Pipeline>();
        this.listeners = Collections.synchronizedList(new ArrayList<StageEventListener>());
    }

    /**
     * Adds a {@link StageEventListener} to the pipline that will be notified by calls
     * to {@link Pipeline#raise(EventObject)}.
     *
     * @param listener The listener to be notified.
     */
    public void registerListener(StageEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Returns the collection of {@link StageEventListener}s registered with the
     * context.
     *
     * @return The collection of registered listeners.
     */
    public Collection<StageEventListener> getRegisteredListeners() {
        return this.listeners;
    }

    /**
     * Asynchronously notifies each registered listener of an event and propagates
     * the event to any attached branches and the parent rendering.
     *
     * @param ev The event to be sent to registered listeners
     */
    public void raise(final EventObject ev) {
        new Thread() {
            public void run() {
                //first, recursively find the root rendering
                Pipeline root = Pipeline.this;
                while (root.parent != null) root = root.parent;

                //notify the listeners from the root rendering
                root.notifyListeners(ev);
            }
        }.start();
    }

    /**
     * Notify all listeners and recursively notify child branches of the
     * specified event. This method does not propagate events to the
     * parent rendering.
     */
    private void notifyListeners(EventObject ev) {
        for (StageEventListener listener : listeners) listener.notify(ev);
        for (Pipeline branch : branches.values()) branch.notifyListeners(ev);
    }

    /**
     * This method is used by a stage driver to pass data from zero stage to the next.
     *
     * @param stage the stage for which the downstream feeder will be retrieved
     * @return the feeder for the downstream stage, or null if no downstream
     *         stage exists.
     */
    public Feeder getDownstreamFeeder(Stage stage) {
        if (stage == null) throw new IllegalArgumentException("Unable to look up downstream feeder for null stage.");
        if (stage == drivers.getLast().getStage()) {
            return this.terminalFeeder;
        } else {
            //Iterate backwards over the list until the stage is found, then return
            //the feeder for the subsequent stage. Comparisons are done using reference
            //equality.
            for (int i = drivers.size() - 2; i >= 0; i--) {
                if (stage == drivers.get(i).getStage()) return drivers.get(i + 1).getFeeder();
            }

            throw new IllegalStateException("Unable to find stage " + stage + " in rendering.");
        }
    }

    /**
     * Look up and return the source feeder for the specified rendering branch.
     *
     * @param branch the string identifier of the branch for which a feeder will be returned
     * @return the feeder for the specified branch
     */
    public Feeder getBranchFeeder(String branch) {
        if (!getBranches().containsKey(branch)) {
            throw new IllegalStateException("Unable to find branch in rendering: '" + branch + "'");
        }

        return branches.get(branch).getSourceFeeder();
    }

    /**
     * Global environment accessor method.
     *
     * @return the global environment value corresponding to the specified
     *         key, or null if no such key is found.
     */
    public Object getEnv(String key) {
        return this.env.get(key);
    }

    /**
     * Sets the value corresponding to the specified environment variable key.
     */
    public void setEnv(String key, Object value) {
        this.env.put(key, value);
    }

    /**
     * Adds a {@link Stage} object to the end of this Pipeline. If a
     * {@link PipelineValidator} has been set using {@link #setValidator}, it will
     * be used to validate that the appended Stage can consume the output of the
     * previous stage of the rendering. It does NOT validate the ability or availability
     * of branches to consume data produced by the appended stage.
     *
     * @param stage         the stage to be added to the rendering
     * @param driverFactory the factory that will be used to create a {@link StageDriver} that will run the stage
     * @throws ValidationException if there is a non-null validator set for this rendering and an error is
     *                             encountered validating the addition of the stage to the rendering.
     */
    public final void addStage(Stage stage, StageDriverFactory driverFactory) throws ValidationException {
        if (stage == null)
            throw new IllegalArgumentException("Argument \"stage\" for call to Pipeline.addStage() may not be null.");

        if (validator != null) {
            List<ValidationFailure> errors = validator.validateAddStage(this, stage, driverFactory);
            if (errors != null && !errors.isEmpty()) {
                throw new ValidationException("An error occurred adding stage " + stage.toString(), errors);
            }
        }

        stage.init(this);
        this.stages.add(stage);

        StageDriver driver = driverFactory.createStageDriver(stage, this);
        this.driverMap.put(stage, driver);
        this.drivers.add(driver);
    }

    /**
     * Returns an unmodifiable list of stages that have been added to this
     * rendering.
     *
     * @return A list of the stages that have been added to the rendering
     */
    public final List<Stage> getStages() {
        return Collections.unmodifiableList(this.stages);
    }

    /**
     * Return the StageDriver for the specified Stage.
     *
     * @return the StageDriver for the specified Stage.
     */
    public final StageDriver getStageDriver(Stage stage) {
        return this.driverMap.get(stage);
    }

    /**
     * Returns an unmodifiable list of stage drivers that have been added
     * to the rendering.
     *
     * @return the list of drivers for stages in the rendering
     */
    public final List<StageDriver> getStageDrivers() {
        return Collections.unmodifiableList(this.drivers);
    }

    /**
     * Adds a branch to the rendering.
     *
     * @param key    the string identifier that will be used to refer to the added branch
     * @param branch the branch rendering
     * @throws org.apache.commons.pipeline.validation.ValidationException
     *          if the rendering has a non-null {@link PipelineValidator} and the branch
     *          cannot consume the data produced for the branch by stages in the rendering.
     */
    public void addBranch(String key, Pipeline branch) throws ValidationException {
        if (key == null)
            throw new IllegalArgumentException("Branch key may not be null.");
        if (MAIN_BRANCH.equalsIgnoreCase(key))
            throw new IllegalArgumentException("Branch key name \"" + MAIN_BRANCH + "\" is reserved.");
        if (branch == null)
            throw new IllegalArgumentException("Illegal attempt to set reference to null branch.");
        if (branch == this || branch.hasBranch(this))
            throw new IllegalArgumentException("Illegal attempt to set reference to self as a branch (infinite recursion potential)");

        if (validator != null) {
            List<ValidationFailure> errors = validator.validateAddBranch(this, key, branch);
            if (errors != null && !errors.isEmpty()) {
                throw new ValidationException("An error occurred adding branch rendering " + branch, errors);
            }
        }

        branch.parent = this;
        this.branches.put(key, branch);
    }

    /**
     * Returns an unmodifiable map of branch pipelines, keyed by branch identifier.
     *
     * @return the map of registered branch pipelines, keyed by branch identifier
     */
    public Map<String, Pipeline> getBranches() {
        return Collections.unmodifiableMap(branches);
    }

    /**
     * Simple method that recursively checks whether the specified
     * rendering is a branch of this rendering.
     *
     * @param pipeline the candidate branch
     * @return true if the specified rendering is a branch of this rendering, or recursively
     *         a branch of a branch. Tests are performed using reference equality.
     */
    private boolean hasBranch(Pipeline pipeline) {
        if (branches.containsValue(pipeline)) return true;
        for (Pipeline branch : branches.values()) {
            if (branch.hasBranch(pipeline)) return true;
        }

        return false;
    }

    /**
     * Returns a feeder for the first stage if the rendering is not empty
     *
     * @return the feeder to feed the first stage of the rendering
     */
    public Feeder getSourceFeeder() {
        if (drivers.isEmpty()) return this.terminalFeeder;
        return drivers.peek().getFeeder();
    }

    /**
     * Gets the feeder that receives output from the final stage.
     *
     * @return the terminal feeder being used to handle any output from the final stage. The default is {@link Feeder#VOID}
     */
    public Feeder getTerminalFeeder() {
        return this.terminalFeeder;
    }

    /**
     * Sets the terminal feeder used to handle any output from the final stage.
     *
     * @param terminalFeeder the {@link Feeder} that will receive any output from the final stage
     */
    public void setTerminalFeeder(Feeder terminalFeeder) {
        this.terminalFeeder = terminalFeeder;
    }

    /**
     * This method iterates over the stages in the rendering, looking up a
     * {@link StageDriver} for each stage and using that driver to start the stage.
     * Startups may occur sequentially or in parallel, depending upon the stage driver
     * used.  If a the stage has not been configured with a {@link StageDriver},
     * we will use the default, non-threaded {@link org.apache.commons.pipeline.driver.SynchronousStageDriver}.
     *
     * @throws org.apache.commons.pipeline.StageException
     *          Thrown if there is an error during rendering startup
     */
    public void start() throws StageException {
        for (StageDriver driver : this.drivers) driver.start();
        for (Pipeline branch : branches.values()) branch.start();
    }

    /**
     * This method iterates over the stages in the rendering, looking up a {@link StageDriver}
     * for each stage and using that driver to request that the stage finish
     * execution. The {@link StageDriver#finish()}
     * method will block until the stage's queue is exhausted, so this method
     * may be used to safely finalize all stages without the risk of
     * losing data in the queues.
     *
     * @throws org.apache.commons.pipeline.StageException
     *          Thrown if there is an unhandled error during stage shutdown
     */
    public void finish() throws StageException {
        for (StageDriver driver : this.drivers) {
            driver.finish();
        }

        for (Pipeline pipeline : branches.values()) {
            pipeline.finish();
        }
    }

    /**
     * Runs the rendering from start to finish.
     */
    public void run() {
        try {
            start();
            finish();
        } catch (StageException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the validator being used to validate the rendering structure,
     * or null if no validation is being performed..
     *
     * @return Validator used to validate rendering structure.
     */
    public PipelineValidator getValidator() {
        return this.validator;
    }

    /**
     * Sets the validator used to validate the rendering as it is contstructed.
     * Setting the validator to null disables validation
     *
     * @param validator Validator used to validate rendering structure.
     */
    public void setValidator(PipelineValidator validator) {
        this.validator = validator;
    }

    /**
     * Returns the parent of this rendering, if it is a branch
     *
     * @return parent Pipeline, or null if this is the main rendering
     */
    public Pipeline getParent() {
        return parent;
    }
}
