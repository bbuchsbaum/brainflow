package brainflow.image.data;

import brainflow.image.space.ImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 9:03:48 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageData3D extends AbstractImageData implements IImageData3D {

    private int planeSize;

    private int dim0;

    protected AbstractImageData3D(ImageSpace3D space) {
        super(space);
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
    }

    protected AbstractImageData3D(IImageSpace3D space, DataType dtype) {
        super(space, dtype);
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
    }

    public IImageSpace3D getImageSpace() {
        return (IImageSpace3D) space;
    }

    public int indexOf(int x, int y, int z) {
        return (z * planeSize) + dim0 * y + x;
    }

    @Override
    public Anatomy3D getAnatomy() {
        return getImageSpace().getAnatomy();
    }


    /*public Index3D indexToGrid(int idx, Index3D voxel) {
      voxel.setZ(idx / planeSize);
      int remainder = (idx % planeSize);
      voxel.setY(remainder / space.getDimension(Axis.X_AXIS));
      voxel.setValue(remainder % space.getDimension(Axis.X_AXIS));

      return voxel;
  }  */
}


