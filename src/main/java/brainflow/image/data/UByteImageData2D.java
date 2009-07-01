package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.space.IImageSpace2D;
import brainflow.utils.DataType;
import brainflow.utils.NumberUtils;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 14, 2007
 * Time: 11:36:20 PM
 * To change this template use File | Settings | File Templates.
 */


public class UByteImageData2D extends AbstractImageData implements IImageData2D {


    private byte[] data;

    


    public UByteImageData2D(IImageSpace2D _space) {
        super(_space,DataType.BYTE);
       
        data = new byte[space.getNumSamples()];


    }

    public UByteImageData2D(IImageSpace2D space, byte[] _data) {
        super(space, DataType.BYTE);
        if (_data.length != space.getNumSamples()) {
            throw new IllegalArgumentException("supplied data array has incorrect length");
        }


        data = _data;


    }

    public IImageSpace2D getImageSpace() {
        return (IImageSpace2D)space;
         
    }

    public ImageIterator iterator() {
        return new Iterator2D();
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

    public double value(int index) {
        return data[index];
    }

    public void setValue(int idx, double val) {
        data[idx] = (byte)val;
    }

    public byte[] getByteArray() {
        return data;
    }

    public final void set(int i, byte val) {
        data[i] = val;
    }

    public final byte get(int i) {
        return data[i];
    }

    public final byte get(int x, int y) {
        return data[indexOf(x, y)];
    }

    public double value(int x, int y) {
        return NumberUtils.ubyte(data[indexOf(x, y)]);
    }



    private void setValue(int x, int y, double val) {
        data[indexOf(x, y)] = (byte) val;
    }

    public ImageBuffer2D createWriter(boolean clear) {
        throw new UnsupportedOperationException();
    }

    final class Iterator2D implements ImageIterator {

        private int index;

        private int len = space.getNumSamples();
        private int end = len;
        private int begin = 0;

        public Iterator2D() {
            index = 0;
        }

        public void advance() {
            index++;
        }

        public final double next() {
            double dat = NumberUtils.ubyte(data[index]);
            index++;
            return dat;
        }

        public void set(double val) {
            data[index] = (byte) val;
        }

        public double previous() {
            return NumberUtils.ubyte(data[--index]);
        }

        public final boolean hasNext() {
            if (index < end) return true;
            return false;
        }

        public double element(int number) {
            index = number;
            return NumberUtils.ubyte(data[number]);
        }

        public double jump(int number) {
            index += number;
            return NumberUtils.ubyte(data[number]);
        }

        public boolean canJump(int number) {
            if ((index + number) < end && (index - number >= begin))
                return true;
            return false;
        }

        public double nextRow() {
            index += space.getDimension(Axis.X_AXIS);
            return NumberUtils.ubyte(data[index]);

        }

        public double nextPlane() {
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.nextPlane(): only zero plane in 2D iterator!");
        }

        public boolean hasNextRow() {
            if ((index + space.getDimension(Axis.X_AXIS)) < len)
                return true;
            return false;
        }

        public boolean hasNextPlane() {
            return false;
        }

        public boolean hasPreviousRow() {
            if ((index - space.getDimension(Axis.X_AXIS)) >= begin)
                return true;
            return false;
        }

        public boolean hasPreviousPlane() {
            return false;
        }

        public double previousRow() {
            index -= space.getDimension(Axis.X_AXIS);
            return NumberUtils.ubyte(data[index]);
        }

        public double previousPlane() {
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.previousPlane(): only zero plane in 2D iterator!");
        }

        public int index() {
            return index;
        }


    }


}
