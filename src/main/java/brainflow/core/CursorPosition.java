package brainflow.core;

import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 20, 2008
 * Time: 1:59:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class CursorPosition extends AnatomicalPoint3D {




    public CursorPosition(IImageSpace3D refSpace, double x, double y, double z) {
        super(refSpace, x, y, z);

    }

 
    public IImageSpace3D getSpace() {
        return (IImageSpace3D)super.getSpace();
    }



    public AnatomicalPoint3D getWorldPosition() {
        double gridx = getSpace().getImageAxis(Axis.X_AXIS).gridPosition(getX());
        double gridy = getSpace().getImageAxis(Axis.Y_AXIS).gridPosition(getY());
        double gridz = getSpace().getImageAxis(Axis.Z_AXIS).gridPosition(getZ());

        float[] ret = getSpace().gridToWorld((float)gridx, (float)gridy, (float)gridz);
        return new AnatomicalPoint3D(Anatomy3D.REFERENCE_ANATOMY, ret[0], ret[1], ret[2]);
    }



}
