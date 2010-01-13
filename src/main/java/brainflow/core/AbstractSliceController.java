package brainflow.core;

import brainflow.image.axis.ImageAxis;
import brainflow.image.anatomy.*;
import brainflow.image.space.Axis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 4, 2009
 * Time: 8:27:47 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSliceController implements SliceController {

    private ImageView view;

    protected AbstractSliceController(ImageView view) {
        this.view = view;
    }

    public ImageView getView() {
        return view;
    }

    public ImageAxis sliceAxis(Anatomy3D displayAnatomy) {
        Axis axis = getView().getViewport().getBounds().findAxis(displayAnatomy.ZAXIS);
        return getView().getModel().getImageAxis(axis);
    }

    public VoxelLoc3D incrementSlice(double incr, Anatomy3D anatomy) {
        VoxelLoc3D slice = getSlice();
        ImageAxis iaxis = sliceAxis(anatomy);
        VoxelLoc1D gp = slice.getValue(iaxis.getAnatomicalAxis(), false);
        SpatialLoc1D pt = new SpatialLoc1D(gp.getAnatomy(), gp.toReal().getValue() + incr);
        return slice.replace(pt);
    }
}
