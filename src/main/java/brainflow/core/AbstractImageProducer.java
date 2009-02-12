package brainflow.core;

import brainflow.display.InterpolationType;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.AxisRange;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:22:25 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageProducer implements IImageProducer {


    private IImageDisplayModel model;


    private AnatomicalPoint3D slice = new AnatomicalPoint3D(Anatomy3D.AXIAL_LAI, 0,0,0);

    private Rectangle screenSize;


    private InterpolationType screenInterpolation = InterpolationType.CUBIC;


    public void setModel(IImageDisplayModel model) {
        this.model = model;
    }

    public IImageDisplayModel getModel() {
        return model;
    }


    public abstract Anatomy3D getDisplayAnatomy();

    public void setSlice(AnatomicalPoint3D slice) {
        this.slice = slice;

    }

    public InterpolationType getScreenInterpolation() {
        return screenInterpolation;
    }

    public void setScreenInterpolation(InterpolationType screenInterpolation) {
        this.screenInterpolation = screenInterpolation;
    }

    public abstract AxisRange getXAxis();

    //public void setXAxis(AxisRange xaxis) {
    //    this.xaxis = xaxis;
    //}

    public abstract AxisRange getYAxis();

    //public void setYAxis(AxisRange yaxis) {
    //    this.yaxis = yaxis;
    //}

    public AnatomicalPoint3D getSlice() {
        return slice;
    }


    public Rectangle getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Rectangle screenSize) {
        this.screenSize = screenSize;
    }
}
