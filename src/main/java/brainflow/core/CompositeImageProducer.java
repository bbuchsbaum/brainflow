package brainflow.core;

import brainflow.core.rendering.*;
import brainflow.core.layer.ImageLayerEvent;
import brainflow.core.layer.ImageLayerListener;
import brainflow.display.InterpolationType;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.AnatomicalPoint1D;
import brainflow.image.axis.AxisRange;
import brainflow.utils.OndeckTaskExecutor;
import brainflow.utils.NumberUtils;
import org.apache.commons.pipeline.Feeder;
import org.apache.commons.pipeline.driver.SynchronousStageDriverFactory;
import org.apache.commons.pipeline.validation.ValidationException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:36:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompositeImageProducer extends AbstractImageProducer {

    private static final Logger log = Logger.getLogger(CompositeImageProducer.class.getName());

    private IImagePlot plot;

    private ImagePlotPipeline pipeline;

    private ImageLayerListener layerListener;


    private boolean dirty = true;

    private BufferedImage lastImage;

    private final OndeckTaskExecutor<BufferedImage> renderQueue;


    public CompositeImageProducer(IImagePlot plot, Anatomy3D displayAnatomy) {
        this(plot, displayAnatomy, plot.getModel().getImageSpace().getCentroid());

    }

    public CompositeImageProducer(IImagePlot plot,
                                  Anatomy3D displayAnatomy, AnatomicalPoint3D slice) {
        this(plot, displayAnatomy, slice, Executors.newSingleThreadExecutor());
        //initPipeline();

    }

    public CompositeImageProducer(IImagePlot plot,
                                  Anatomy3D displayAnatomy, AnatomicalPoint3D slice, ExecutorService service) {
        this.plot = plot;
        //setDisplayAnatomy(displayAnatomy);

        //initPipeline();

        setSlice(slice);
        layerListener = new PipelineLayerListener();
        getModel().addImageLayerListener(layerListener);
        renderQueue = new OndeckTaskExecutor<BufferedImage>(service);

    }



    public void setScreenInterpolation(InterpolationType type) {
        super.setScreenInterpolation(type);
        //pipeline.clearPath(resizeImageStage);
        dirty = true;
    }


    public void setScreenSize(Rectangle screenSize) {
        super.setScreenSize(screenSize);
        //pipeline.clearPath(resizeImageStage);
        dirty = true;
    }

    public void reset() {
        //pipeline.clearPath(gatherRenderersStage);
        dirty = true;
        //getPlot().getComponent().repaint();
    }

    public void setSlice(AnatomicalPoint3D slice) {
        AnatomicalPoint1D pt = slice.getValue(getDisplayAnatomy().ZAXIS);
         if (NumberUtils.equals(pt.getValue(), getSlice().getValue(getDisplayAnatomy().ZAXIS).getValue(), .0001)) {
            return;
        }

        super.setSlice(slice);
        dirty = true;
    }

    public void setPlot(IImagePlot plot) {
        getModel().removeImageLayerListener(layerListener);
        this.plot = plot;
        getModel().addImageLayerListener(layerListener);

        //initPipeline();
        dirty = true;
    }

    @Override
    public Anatomy3D getDisplayAnatomy() {
        return plot.getDisplayAnatomy();
    }

    //@Override
    //public void setXAxis(AxisRange xaxis) {
    //    super.setXAxis(xaxis);
    //    //pipeline.clearPath(cropImageStage);
    //    dirty = true;
    //}

    //@Override
    //public void setYAxis(AxisRange yaxis) {
    //    super.setYAxis(yaxis);
    //    //pipeline.clearPath(cropImageStage);
    //    dirty = true;
    //}

    @Override
    public AxisRange getYAxis() {
        return plot.getYAxisRange();
    }

    @Override
    public AxisRange getXAxis() {
        return plot.getYAxisRange();
    }

    public IImageDisplayModel getModel() {
        return plot.getModel();
    }

    public IImagePlot getPlot() {
        return plot;
    }

    private ImagePlotPipeline createPipeline() {
        pipeline = new ImagePlotPipeline(getPlot());
        try {

            // when creating pipeline we could supply cache
            pipeline.addStage(new GatherRenderersStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new RenderLayersStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new CropImageStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new ResizeImageStage(), new SynchronousStageDriverFactory());

            pipeline.getSourceFeeder().feed(getModel());
            pipeline.setTerminalFeeder(new TerminalFeeder());
        }
        catch (ValidationException e) {
            // can'three really handle this exception, so throw uncheckedexception
            throw new RuntimeException(e);
        }

        return pipeline;


    }


    public synchronized BufferedImage render() {
        try {

            ImagePlotPipeline pipeline = createPipeline();

            pipeline.getSourceFeeder().feed(getModel());
            pipeline.run();


            lastImage = ((TerminalFeeder) pipeline.getTerminalFeeder()).getImage();
            dirty = false;
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("slice is " + this.getSlice());
            System.out.println("space is " + plot.getModel().getImageSpace());
        }


        return lastImage;
    }

    public synchronized BufferedImage getImage() {
        // does this spawn a new thread?
        // could be submitted to thread pool?

        if (dirty || lastImage == null) {
            return render();
        } else {
            return lastImage;
        }


    }


    private Callable<BufferedImage> createRenderTask() {
        return new Callable<BufferedImage>() {
            public BufferedImage call() throws Exception {
                BufferedImage buf = render();
                plot.getComponent().repaint();
                return buf;

            }


        };


    }


    class TerminalFeeder implements Feeder {

        BufferedImage finalImage;


        public void feed(Object obj) {
            finalImage = (BufferedImage) obj;
        }

        public BufferedImage getImage() {
            return finalImage;
        }
    }


    protected void finalize() throws Throwable {
        getModel().removeImageLayerListener(layerListener);

    }

    class PipelineLayerListener implements ImageLayerListener {

        public void thresholdChanged(ImageLayerEvent event) {
            //todo make this a bit more intelligent. "clearPath" flushes the renderers
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void smoothingChanged(ImageLayerEvent event) {
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());

        }

        public void colorMapChanged(ImageLayerEvent event) {
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());

        }

        public void opacityChanged(ImageLayerEvent event) {
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void interpolationMethodChanged(ImageLayerEvent event) {
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void visibilityChanged(ImageLayerEvent event) {
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void maskChanged(ImageLayerEvent event) {
            dirty = true;
            renderQueue.submitTask(createRenderTask());

        }

        public void clipRangeChanged(ImageLayerEvent event) {
            // no need to repaint because clip events are also detected as color map events ...

        }

        public String toString() {
            return "PipelineLayerListener for plot : " + plot.hashCode() + " with model " + plot.getModel();
        }
    }

    ;


}
