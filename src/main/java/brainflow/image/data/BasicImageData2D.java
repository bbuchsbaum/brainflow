package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.ImageSpace2D;
import brainflow.utils.DataType;
import brainflow.utils.IDimension;

import java.awt.image.DataBuffer;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class BasicImageData2D extends BasicImageData implements IImageData2D {


    public BasicImageData2D(BasicImageData2D src) {
        super(src.getImageSpace(),src.getDataType());
        fillBuffer(src.storage, space.getNumSamples());

    }

    public BasicImageData2D(ImageSpace2D space, DataType _type) {
        //todo ImageSpace2D should be IImageSpace2D
        super(space, _type);
        data = allocateBuffer(space.getNumSamples());

    }

    public BasicImageData2D(ImageSpace2D space, DataType _type, String _imageLabel) {
        super(space, _type, _imageLabel);
        data = allocateBuffer(space.getNumSamples());


    }

     public BasicImageData2D(ImageSpace2D space, Object array) {
        super(space, establishDataType(array));
        storage = array;

        data = allocateBuffer(space.getNumSamples());

    }

    public BasicImageData2D(ImageSpace2D space, Object array, String imageLabel) {
        super(space, establishDataType(array), imageLabel);
        storage = array;

        data = allocateBuffer(space.getNumSamples());

    }


    public ImageInfo getImageInfo() {
        return new ImageInfo(this);
    }

    public IDimension<Integer> getDimension() {
        return space.getDimension();
    }

    public ImageSpace2D getImageSpace() {
        return (ImageSpace2D)space;

    }

    public double value(int index) {
        return data.getElemDouble(index);
    }



    public final int indexOf(int x, int y) {
        //todo this is obviously slow
        return space.getDimension(Axis.X_AXIS) * y + x;
    }

    public final double value(double x, double y, InterpolationFunction2D interp) {
        return interp.interpolate(x, y, this);
    }

    public final double worldValue(double realx, double realy, InterpolationFunction2D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).gridPosition(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).gridPosition(realy);
        return interp.interpolate(x, y, this);
    }

    public final double value(int x, int y) {
        return data.getElemDouble(indexOf(x, y));
    }


    private void setValue(int x, int y, double val) {
        data.setElemDouble(indexOf(x, y), val);
    }

     private void setValue(int idx, double val) {
        data.setElemDouble(idx, val);
    }


    public ImageIterator iterator() {
        return new Iterator2D();
    }

    public DataWriter2D createWriter(boolean clear) {
        return new DataWriter2D() {

            ImageSpace2D space = BasicImageData2D.this.getImageSpace();
            DataBuffer buffer = BasicImageData2D.this.copyBuffer();
            Object storage = BasicImageData2D.this.storage;

            BasicImageData2D delegate = new BasicImageData2D(space, storage);

            public final int indexOf(int x, int y) {
                return delegate.indexOf(x,y);
            }

            public void setValue(int x, int y, double val) {
                delegate.setValue(x,y,val);
            }

            public IImageData2D asImageData() {
                return delegate;
            }

            public void setValue(int index, double value) {
                delegate.setValue(index, value);

            }

            public double value(int index) {
                return delegate.value(index);
            }

            public int numElements() {
                return delegate.numElements();
            }

            public ImageSpace2D getImageSpace() {
                return delegate.getImageSpace();

            }

            public double value(double x, double y, InterpolationFunction2D interp) {
                return delegate.value(x,y,interp);

            }

            public double worldValue(double realx, double realy, InterpolationFunction2D interp) {
                return delegate.worldValue(realx, realy, interp);
            }

            public double value(int x, int y) {
                return delegate.value(x,y);
            }
        };

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
            double dat = data.getElemDouble(index);
            index++;
            return dat;
        }

        public void set(double val) {
            data.setElemDouble(index, val);
        }

        public double previous() {
            return data.getElemDouble(--index);
        }

        public final boolean hasNext() {
            if (index < end) return true;
            return false;
        }

        public double element(int number) {
            index = number;
            return data.getElemDouble(number);
        }

        public double jump(int number) {
            index += number;
            return data.getElemDouble(number);
        }

        public boolean canJump(int number) {
            if ((index + number) < end && (index - number >= begin))
                return true;
            return false;
        }

        public double nextRow() {
            index += space.getDimension(Axis.X_AXIS);
            return data.getElemDouble(index);
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
            return data.getElemDouble(index);
        }

        public double previousPlane() {
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.previousPlane(): only zero plane in 2D iterator!");
        }

        public int index() {
            return index;
        }


    }


}