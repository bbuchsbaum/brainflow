package brainflow.image.data;

import brainflow.array.IArray3D;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.iterators.BooleanIterator;
import brainflow.image.iterators.MaskIterator;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.ImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.math.Index3D;
import brainflow.utils.Dimension3D;
import cern.colt.bitvector.BitVector;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 11:53:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryImageData3D extends BinaryImageData implements IMaskedData3D {


    private int planeSize;

    private int dim0;

    public BinaryImageData3D(IImageSpace3D _space) {
        super(_space);

        init();
    }

    private BinaryImageData3D(IImageSpace3D space, BitVector bits) {
        super(space, bits);
        init();
    }

    public ImageSpace3D getImageSpace() {
        return (ImageSpace3D)space;
    }

    public BinaryImageData3D(IMaskedData3D src) {
        super(src.getImageSpace());

        
        BitVector bits = getBitVector();
        ValueIterator iter = src.valueIterator();

        while (iter.hasNext()) {
            int i = iter.index();
            double val = iter.next();
            if (val > 0) {
                bits.set(i);
            }
        }

        init();

    }


    public BinaryImageData3D(ImageSpace3D space, boolean[] elements) {
        super(space, elements);

        init();
    }

    private void init() {
        //todo these constants should be moved to image space
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);

    }



    public BinaryImageData3D OR(BinaryImageData data) {
        if (data.getImageSpace().getNumSamples() != this.getImageSpace().getNumSamples()) {
            throw new IllegalArgumentException("argument must have same dimensions as image for OR operation");
        }

        BitVector ret = getBitVector().copy();
        ret.or(data.getBitVector());
        return new BinaryImageData3D(getImageSpace(), ret);
    }

    public BinaryImageData3D AND(BinaryImageData data) {
        if (data.getImageSpace().getNumSamples() != this.getImageSpace().getNumSamples()) {
            throw new IllegalArgumentException("argument must have same dimensions as image for AND operation");
        }


        BitVector ret = getBitVector().copy();
        ret.and(data.getBitVector());
        return new BinaryImageData3D(getImageSpace(), ret);

    }

    @Override
    public boolean alwaysTrue() {
        //todo improve on this
        return false;
    }

    @Override
    public Dimension3D<Integer> dim() {
        return getImageSpace().getDimension();
    }

    public Index3D indexToGrid(int idx) {
       return getImageSpace().indexToGrid(idx);

    }

    public final int indexOf(int x, int y, int z) {
        return (z * planeSize) + dim0 * y + x;
    }

    @Override
    public Anatomy3D getAnatomy() {
        return getImageSpace().getAnatomy();
    }

    public double value(float x, float y, float z, InterpolationFunction3D interp) {
        return interp.interpolate(x, y, z, this);

    }

    public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).gridPosition(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).gridPosition(realy);
        double z = space.getImageAxis(Axis.Z_AXIS).gridPosition(realz);
        return interp.interpolate(x, y, z, this);
    }


    public IArray3D subGrid(int x0, int x1, int y0, int y1, int z0, int z1) {
        throw new UnsupportedOperationException();
    }

    public double value(int index) {
        return getBitVector().getQuick(index) ? 1.0 : 0.0;

    }

    public final int getInt(int index) {
        return getBitVector().getQuick(index) ? 1 : 0;

    }

    public final double value(int x, int y, int z) {
        return getBitVector().getQuick(indexOf(x, y, z)) ? 1.0 : 0.0;
    }

    public final float getFloat(int x, int y, int z) {
        return getBitVector().getQuick(indexOf(x, y, z)) ? 1f : 0f;
    }

    public int getInt(int x, int y, int z) {
        return getBitVector().getQuick(indexOf(x, y, z)) ? 1 : 0;
    }

    public final void setValue(int idx, double val) {
        getBitVector().putQuick(idx, val != 0);

    }

    public final void setValue(int x, int y, int z, double val) {
        getBitVector().putQuick(indexOf(x, y, z), val > 0);

    }

    public final void setFloat(int x, int y, int z, float val) {
        getBitVector().putQuick(indexOf(x, y, z), val > 0);
    }

    public final void setInt(int x, int y, int z, int val) {
        getBitVector().putQuick(indexOf(x, y, z), val > 0);
    }

    public final boolean isTrue(int x, int y, int z) {
        return getBitVector().getQuick(indexOf(x, y, z));
    }

    public final boolean isTrue(int index) {
        return getBitVector().getQuick(index);

    }

    @Override
    public BooleanIterator valueIterator() {
        return new MaskIterator(this);
        
    }

    public ImageBuffer3D createBuffer(boolean clear) {
        throw new UnsupportedOperationException();
    }
}
