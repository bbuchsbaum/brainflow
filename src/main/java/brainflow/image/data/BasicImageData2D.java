package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.space.*;
import brainflow.utils.DataType;
import brainflow.utils.Dimension2D;


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

    public Dimension2D<Integer> dim() {
        return getImageSpace().getDimension();
    }

    public ImageSpace2D getImageSpace() {
        return (ImageSpace2D)space;

    }

    public double value(int index) {
        return dataSupport.getData().getElemDouble(index);
    }



    public final int indexOf(int x, int y) {
        //todo this is obviously slow
        return space.getDimension(Axis.X_AXIS) * y + x;
    }

    public final double value(float x, float y, InterpolationFunction2D interp) {
        return interp.interpolate(x, y, this);
    }

    public final double worldValue(float realx, float realy, InterpolationFunction2D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).gridPosition(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).gridPosition(realy);
        return interp.interpolate(x, y, this);
    }

    public final double value(int x, int y) {
        return dataSupport.getData().getElemDouble(indexOf(x, y));
    }


    private void setValue(int x, int y, double val) {
        dataSupport.getData().setElemDouble(indexOf(x, y), val);
    }

     private void setValue(int idx, double val) {
        dataSupport.getData().setElemDouble(idx, val);
    }


    public ImageIterator iterator() {
        return new Iterator2D();
    }

    public ImageBuffer2D createWriter(boolean clear) {
        final IImageSpace2D space = BasicImageData2D.this.getImageSpace();
        final Object storage = BasicImageData2D.this.dataSupport.getStorage();
        final BasicImageData2D delegate;

        if (!clear) {
            delegate = new BasicImageData2D(space, storage);
        } else {
            delegate = new BasicImageData2D(space, BasicImageData2D.this.getDataType());
        }

        return new ImageBuffer2D() {

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

            public int length() {
                return delegate.length();
            }

            public ImageSpace2D getImageSpace() {
                return delegate.getImageSpace();

            }

            @Override
            public double value(float x, float y, InterpolationFunction2D interp) {
                return delegate.value(x,y,interp);

            }

            //@Override
            //public double worldValue(float realx, float realy, InterpolationFunction2D interp) {
           //     return delegate.worldValue(realx, realy, interp);
            //}

            public double value(int x, int y) {
                return delegate.value(x,y);
            }

            public ImageIterator iterator() {
                return delegate.iterator();
            }

            @Override
            public Dimension2D<Integer> dim() {
                return delegate.dim();
            }
        };

    }

    final class Iterator2D implements ImageIterator {

        private int index;

        private int len = space.getNumSamples();
        private int end = len;

        public Iterator2D() {
            index = 0;
        }

        public void advance() {
            index++;
        }

        public final double next() {
            double dat = dataSupport.getData().getElemDouble(index);
            index++;
            return dat;
        }

        public void set(double val) {
            dataSupport.getData().setElemDouble(index, val);
        }



        public final boolean hasNext() {
            if (index < end) return true;
            return false;
        }

        public double element(int number) {
            index = number;
            return dataSupport.getData().getElemDouble(number);
        }


        public int index() {
            return index;
        }

        @Override
        public IImageSpace getImageSpace() {
            return BasicImageData2D.this.getImageSpace();
        }
    }


}