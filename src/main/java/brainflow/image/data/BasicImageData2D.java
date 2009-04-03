package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.space.IImageSpace2D;
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

public class BasicImageData2D extends AbstractImageData2D  {

    private DataBufferSupport dataSupport;

    public BasicImageData2D(BasicImageData2D src) {
        super(src.getImageSpace(),src.getDataType());
        dataSupport = new DataBufferSupport(space, src.dataSupport.getStorage());

    }

    public BasicImageData2D(IImageSpace2D space, DataType _type) {
        super(space, _type);
        dataSupport = new DataBufferSupport(space, _type);

    }

    public BasicImageData2D(IImageSpace2D space, DataType _type, String _imageLabel) {
        super(space, _type, _imageLabel);
    }

     public BasicImageData2D(IImageSpace2D space, Object array) {
        super(space);
        dataSupport = new DataBufferSupport(space, array);
    }

    public BasicImageData2D(IImageSpace2D space, Object array, String imageLabel) {
        super(space, DataBufferSupport.establishDataType(array), imageLabel);
        dataSupport = new DataBufferSupport(space, array);

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
        return dataSupport.data.getElemDouble(index);
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
        return dataSupport.data.getElemDouble(indexOf(x, y));
    }


    private void setValue(int x, int y, double val) {
        dataSupport.data.setElemDouble(indexOf(x, y), val);
    }

     private void setValue(int idx, double val) {
        dataSupport.data.setElemDouble(idx, val);
    }


    public ImageIterator iterator() {
        return new Iterator2D();
    }

    public ImageBuffer2D createWriter(boolean clear) {
        return new ImageBuffer2D() {


            IImageSpace2D space = BasicImageData2D.this.getImageSpace();
            DataBuffer buffer = BasicImageData2D.this.dataSupport.copyBuffer();
            Object storage = BasicImageData2D.this.dataSupport.getStorage();

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

            public ImageIterator iterator() {
                return delegate.iterator();
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
            double dat = dataSupport.data.getElemDouble(index);
            index++;
            return dat;
        }

        public void set(double val) {
            dataSupport.data.setElemDouble(index, val);
        }

        public double previous() {
            return dataSupport.data.getElemDouble(--index);
        }

        public final boolean hasNext() {
            if (index < end) return true;
            return false;
        }

        public double element(int number) {
            index = number;
            return dataSupport.data.getElemDouble(number);
        }

        public double jump(int number) {
            index += number;
            return dataSupport.data.getElemDouble(number);
        }

        public boolean canJump(int number) {
            if ((index + number) < end && (index - number >= begin))
                return true;
            return false;
        }

        public double nextRow() {
            index += space.getDimension(Axis.X_AXIS);
            return dataSupport.data.getElemDouble(index);
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
            return dataSupport.data.getElemDouble(index);
        }

        public double previousPlane() {
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.previousPlane(): only zero plane in 2D iterator!");
        }

        public int index() {
            return index;
        }


    }


}