package brainflow.image.data;

import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.IImageSpace;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.iterators.Iterator3D;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.utils.DataType;
import brainflow.utils.Dimension3D;
import brainflow.math.Index3D;

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

    protected IImageSpace3D space3d;

    protected AbstractImageData3D(IImageSpace3D space) {
        super(space);
        init();
    }

    protected AbstractImageData3D(IImageSpace3D space, DataType dtype) {
        super(space, dtype);
        init();
    }
    public AbstractImageData3D(IImageSpace space, DataType dtype, String imageLabel) {
        super(space, dtype, imageLabel);
        init();
    }

    private void init() {
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
        space3d = (IImageSpace3D)space;

    }


    public IImageSpace3D getImageSpace() {
        return space3d;
    }

    @Override
    public Dimension3D<Integer> getDimensions() {
        return getImageSpace().getDimension();
    }

    protected final int planeSize() {
        return planeSize;
    }

    protected final int dim0() {
        return dim0;

    }

    public int indexOf(int x, int y, int z) {
        return (z * planeSize) + dim0 * y + x;
    }

    @Override
    public Anatomy3D getAnatomy() {
        return getImageSpace().getAnatomy();
    }

    public ValueIterator iterator() {
        return new Iterator3D(this);
    }

    public Index3D indexToGrid(int idx) {
        return getImageSpace().indexToGrid(idx);
    }

    public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).gridPosition(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).gridPosition(realy);
        double z = space.getImageAxis(Axis.Z_AXIS).gridPosition(realz);
        return interp.interpolate(x, y, z, this);
    }



    /*public Index3D indexToGrid(int idx, Index3D voxel) {
      voxel.setZ(idx / planeSize);
      int remainder = (idx % planeSize);
      voxel.setY(remainder / space.getDimensions(Axis.X_AXIS));
      voxel.setValue(remainder % space.getDimensions(Axis.X_AXIS));

      return voxel;
  }  */
}


