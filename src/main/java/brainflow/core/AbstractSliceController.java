package brainflow.core;

import brainflow.image.axis.ImageAxis;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.AnatomicalPoint1D;
import brainflow.image.anatomy.AnatomicalAxis;
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

    public AnatomicalPoint3D incrementSlice(double incr, Anatomy3D anatomy) {
        AnatomicalPoint3D slice = getSlice();
        ImageAxis iaxis = sliceAxis(anatomy);
        AnatomicalPoint1D pt = slice.getValue(iaxis.getAnatomicalAxis());
        pt = new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() + incr);
        return slice.replace(pt);
    }
}
