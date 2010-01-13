package brainflow.core;

import brainflow.display.InterpolationType;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.VoxelLoc3D;
import brainflow.image.axis.AxisRange;
import brainflow.image.space.Space;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:22:25 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageProducer implements IImageProducer {


    private ImageViewModel model;


    private VoxelLoc3D slice = VoxelLoc3D.fromReal(0,0,0, Space.createImageSpace(1,1,1,1,1,1));

    private Rectangle screenSize;

    private InterpolationType screenInterpolation = InterpolationType.CUBIC;


    public void setModel(ImageViewModel model) {
        this.model = model;
        slice = VoxelLoc3D.fromReal(model.getImageSpace().getCentroid(), model.getImageSpace());
    }

    public ImageViewModel getModel() {
        return model;
    }


    public abstract Anatomy3D getDisplayAnatomy();

    public void setSlice(VoxelLoc3D slice) {
        this.slice = slice;
    }

    public InterpolationType getScreenInterpolation() {
        return screenInterpolation;
    }

    public void setScreenInterpolation(InterpolationType screenInterpolation) {
        this.screenInterpolation = screenInterpolation;
    }

    public abstract AxisRange getXAxis();


    public abstract AxisRange getYAxis();


    public VoxelLoc3D getSlice() {
        return slice;
    }

    public Rectangle getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Rectangle screenSize) {
        this.screenSize = screenSize;
    }
}
