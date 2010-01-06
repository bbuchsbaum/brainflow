package brainflow.app.toplevel;


import brainflow.image.io.IImageDataSource;
import brainflow.core.BrainFlowException;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.io.File;
import java.io.FileNotFoundException;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Argument;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 7, 2008
 * Time: 10:13:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrainFlowStarter {


    @Option(usage = "file path to mount", name = "-m")
    public String path;

    @Argument
    List<String> extras = new ArrayList<String>();


    private void initLogging() {
        try {
            Handler[] handlers = Logger.getLogger("").getHandlers();
            boolean foundConsoleHandler = false;
            for (int index = 0; index < handlers.length; index++) {
                // set console handler to WARNING
                if (handlers[index] instanceof ConsoleHandler) {
                    handlers[index].setLevel(Level.WARNING);
                    foundConsoleHandler = true;
                }
            }
            if (!foundConsoleHandler) {
                // no console handler found
                System.err.println("No consoleHandler found, adding one.");
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.WARNING);
                Logger.getLogger("").addHandler(consoleHandler);
            }
        } catch (Throwable t) {
            System.err.println("Unexpected Error setting up logging\n" + t);
        }
    }


    private String convertToAbsolutePath(String ipath) throws FileNotFoundException {
        File provis = new File(ipath);
        if (provis.isAbsolute()) {
            if (provis.exists()) {
                return provis.getAbsolutePath();
            } else {
                throw new FileNotFoundException(ipath);
            }

        } else {
            provis = new File(System.getProperty("user.dir") + File.separatorChar + ipath);
            if (provis.exists()) {
                return provis.getAbsolutePath();
            } else {
                throw new FileNotFoundException(provis.getAbsolutePath());
            }
        }

    }

    private List<String> convertToAbsolutePaths(List<String> paths) throws FileNotFoundException {
        List<String> opaths = new ArrayList<String>();

        for (String pname : paths) {
            opaths.add(convertToAbsolutePath(pname));

        }

        return opaths;

    }

    private IImageDataSource convertToDataSource(String path) throws BrainFlowException {

        return BrainFlow.get().createDataSource(path);


    }


    public void launch() {
        final BrainFlow bflow = BrainFlow.get();
        bflow.initImageIO();

        if (extras.size() > 0) {
            try {
                List<String> fnames = convertToAbsolutePaths(extras);
                List<IImageDataSource> sourceList = new ArrayList<IImageDataSource>();
                for (String str : fnames) {
                    sourceList.add(convertToDataSource(str));
                }




            } catch(FileNotFoundException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            } catch(BrainFlowException e2) {
                System.err.println(e2.getMessage());
                System.exit(1);
            }

        }


    }


    public void launchX() {
        initLogging();

        final BrainFlow bflow = BrainFlow.get();

        final List<IImageDataSource> fileSources = new ArrayList<IImageDataSource>();

        try {
            ImageIOManager.getInstance().initialize();
        } catch (Exception e) {
            System.err.println("couldn't launch app, failed to initialize ImageIOManager");
            System.exit(1);
        }

        for (String extra : extras) {

            File provis = new File(extra);


            if (provis.isAbsolute()) {
                if (ImageIOManager.getInstance().isLoadableImage(provis.getPath())) {
                    IImageDataSource[] sources = ImageIOManager.getInstance().findLoadableImages(new File[]{provis});
                    if (sources.length == 0) {
                        System.err.println("could not load file " + provis);
                        System.exit(1);
                    }

                    if (sources.length > 1) {
                        System.err.println("warning : found multiple sources for file " + provis);
                    }

                    fileSources.add(sources[0]);

                } else {
                    System.err.println("file " + provis + " is not a recognized file format, aborting");
                    System.exit(1);

                }

            } else {
                File file = new File(System.getProperty("user.dir") + File.separatorChar + extra);
                IImageDataSource[] sources = ImageIOManager.getInstance().findLoadableImages(new File[]{file});

                if (sources.length == 0) {
                    System.err.println("could not load file " + provis);
                    System.exit(1);
                }

                if (sources.length > 1) {
                    System.err.println("warning : found multiple sources for file " + provis);
                }

                fileSources.add(sources[0]);

            }
        }

        FutureTask<List<IImageDataSource>> loadTask = null;

        if (fileSources.size() > 0) {
            loadTask = new FutureTask<List<IImageDataSource>>(new Callable<List<IImageDataSource>>() {
                public List<IImageDataSource> call() throws Exception {
                    List<IImageDataSource> dataList = new ArrayList<IImageDataSource>();
                    for (IImageDataSource dsource : fileSources) {
                        DataSourceManager.get().register(dsource);
                        //System.out.println("loading data");
                        dsource.load();
                        dataList.add(dsource);
                        //System.out.println("finished loading data");
                    }

                    return dataList;

                }
            });

            ExecutorService service = Executors.newFixedThreadPool(1);
            //System.out.println("submitting load task");
            service.submit(loadTask);

        }


        //System.out.println("found " + fileSources.size() + " loadable files");

        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    try {
                        //System.out.println("launching brainflow");
                        bflow.launch();
                        //System.out.println("launched brainflow");
                        //System.out.println("getting loaded images");


                    } catch (Throwable e) {
                        Logger.getAnonymousLogger().severe("Error Launching BrainFlow, exiting");
                        e.printStackTrace();
                        System.exit(-1);

                    }

                }
            });
        } catch (Throwable e) {
            System.err.println("failed to launch BrainFlow: " + e.getMessage());
            System.exit(1);

        }

        try {
            if (loadTask != null) {
                List<IImageDataSource> dlist = loadTask.get();
                //IImageDisplayModel displayModel = ProjectManager.get().createViewModel(dataSource);
                //ImageView iview = ImageViewFactory.createAxialView(displayModel);

                //DisplayManager.get().getSelectedCanvas().addImageView(iview);
                //ProjectManager.get().getActiveProject().

            }
        } catch (Throwable e) {
            System.err.println("failed to launch BrainFlow: " + e.getMessage());
            System.exit(1);

        }

    }

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            this.launch();

            //System.out.println(this.extras);

        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
    }


    public static void main(String[] args) {

        new BrainFlowStarter().doMain(args);


    }


}
