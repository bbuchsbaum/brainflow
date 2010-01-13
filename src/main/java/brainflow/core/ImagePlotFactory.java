package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.VoxelLoc3D;
import brainflow.image.axis.AxisRange;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 30, 2008
 * Time: 12:41:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePlotFactory {

    private static ExecutorService threadService;

    public static IImagePlot createComponentPlot(ImageViewModel model, Anatomy3D displayAnatomy) {

        if (threadService == null) {
            threadService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }

        
        AxisRange xrange = model.getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = model.getImageAxis(displayAnatomy.YAXIS).getRange();

        IImagePlot plot = new ComponentImagePlot(model, new ViewBounds(model.getImageSpace(), displayAnatomy, xrange, yrange));
        plot.setName(displayAnatomy.XY_PLANE.getOrientation().toString());

        VoxelLoc3D slice = VoxelLoc3D.fromReal(model.getImageSpace().getCentroid(), model.getImageSpace());
        //IImageProducer producer = new CompositeImageProducer(plot, displayAnatomy, slice, threadService);
        IImageProducer producer = new CompositeImageProducer(plot, slice, threadService);
        plot.setImageProducer(producer);


        //plot.setSlice(getView().getCursorPos().value(displayAnatomy.ZAXIS));
        //plot.setScreenInterpolation(getView().getScreenInterpolation());
        return plot;


    }
}
