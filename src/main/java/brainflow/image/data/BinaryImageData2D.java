package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.space.Axis;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.iterators.ImageIterator;

import cern.colt.bitvector.BitVector;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 2:47:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryImageData2D extends BinaryImageData implements IImageData2D {


    public BinaryImageData2D(ImageSpace2D space, BitVector bits) {
        super(space, bits);

    }

    public BinaryImageData2D(BinaryImageData2D src) {
        super(src.getImageSpace(), src.getBitVector().copy());
    }

    public BinaryImageData2D(MaskedData2D src) {
        super(src.getImageSpace());

        BitVector bits = getBitVector();
        ImageIterator iter = src.iterator();
        while (iter.hasNext()) {
            int i = iter.index();
            double val = iter.next();
            if (val > 0) {
                bits.set(i);
            }
        }

    }

    public BinaryImageData2D(ImageSpace2D _space) {
        super(_space);

    }

    public ImageSpace2D getImageSpace() {
        return (ImageSpace2D) space;
    }

    public BinaryImageData2D OR(BinaryImageData data) {
        if (data.numElements() != this.numElements()) {
            throw new IllegalArgumentException("cannot combine images of unequal size");
        }

        BitVector ret = getBitVector().copy();
        ret.or(data.getBitVector());
        return new BinaryImageData2D((ImageSpace2D) getImageSpace(), ret);
    }

    public BinaryImageData2D AND(BinaryImageData data) {
        if (data.numElements() != this.numElements()) {
            throw new IllegalArgumentException("cannot combine images of unequal size");
        }
        BitVector ret = getBitVector().copy();
        ret.or(data.getBitVector());
        return new BinaryImageData2D(getImageSpace(), ret);

    }

    public int indexOf(int x, int y) {
        return space.getDimension(Axis.X_AXIS) * y + x;
    }

    public double value(double x, double y, InterpolationFunction2D interp) {
        return interp.interpolate(x, y, this);
    }

    public double worldValue(double realx, double realy, InterpolationFunction2D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).gridPosition(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).gridPosition(realy);
        return interp.interpolate(x, y, this);

    }

    public double value(int x, int y) {
        return getBitVector().getQuick(indexOf(x, y)) ? 1.0 : 0.0;
    }

    public double value(int index) {
        return getBitVector().getQuick(index) ? 1.0 : 0.0;
    }

    public void setValue(int idx, double val) {
        getBitVector().putQuick(idx, val > 0);
    }

    public void setValue(int x, int y, double val) {
        getBitVector().putQuick(indexOf(x, y), val > 0);
    }

    public ImageBuffer2D createWriter(boolean clear) {
        throw new UnsupportedOperationException();
    }
}
