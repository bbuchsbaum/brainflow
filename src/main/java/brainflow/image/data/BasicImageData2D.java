package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.space.*;
import brainflow.utils.DataType;
import brainflow.utils.Dimension2D;
import brainflow.array.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public abstract class BasicImageData2D extends AbstractImageData2D {


    protected IArrayBuffer2D data;

    public static BasicImageData2D create(IImageSpace2D space, DataType dataType) {
        switch (dataType) {
            case UBYTE:
                throw new UnsupportedOperationException("do not support type " + dataType);
            case BOOLEAN:
                throw new UnsupportedOperationException("do not support type " + dataType);
            case BYTE:
                return new BasicImageData2D.Byte(space);
            case SHORT:
                return new BasicImageData2D.Short(space);
            case INTEGER:
                return new BasicImageData2D.Int(space);
            case FLOAT:
                return new BasicImageData2D.Float(space);
            case DOUBLE:
                return new BasicImageData2D.Double(space);
            case LONG:
                throw new UnsupportedOperationException("do not support type " + dataType);
            
        }

        throw new UnsupportedOperationException("do not support type " + dataType);
    }


    public BasicImageData2D(BasicImageData2D src) {
        super(src.getImageSpace(), src.getDataType());
    }

    public BasicImageData2D(IImageSpace2D space, DataType _type) {
        super(space, _type);
    }

    public BasicImageData2D(IImageSpace2D space, DataType _type, String _imageLabel) {
        super(space, _type, _imageLabel);
    }


    public ImageInfo getImageInfo() {
        return new ImageInfo(this);
    }

    public Dimension2D<Integer> dim() {
        return getImageSpace().getDimension();
    }

    public ImageSpace2D getImageSpace() {
        return (ImageSpace2D) space;

    }

    public double value(int index) {
        return data.value(index);
    }


    public final double value(double x, double y, InterpolationFunction2D interp) {
        return data.value(x, y, interp);
    }

    public final double worldValue(float realx, float realy, InterpolationFunction2D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).gridPosition(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).gridPosition(realy);
        return data.value(x, y, interp);
    }

    public final double value(int x, int y) {
        return data.value(x, y);
    }


    public ValueIterator valueIterator() {
        return data.valueIterator();
    }

    private static abstract class AbstractDouble extends BasicImageData2D {
        public AbstractDouble(BasicImageData2D.AbstractDouble src) {
            super(src.getImageSpace(), DataType.DOUBLE);
            data = new Array2D.Double(src.dim().getDim(0), src.dim().getDim(1), ((Array2D.Double) src.data).toArray());
        }

        public AbstractDouble(IImageSpace2D space) {
            super(space, DataType.DOUBLE);
            data = new Array2D.Double(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS));
        }

        public AbstractDouble(IImageSpace2D space, double[] _data) {
            super(space, DataType.DOUBLE);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array2D.Double(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), _data);
        }

        @Override
        public DoubleBuffer createBuffer(boolean clear) {
            if (clear) {
                return new DoubleBuffer(this.getImageSpace());
            } else {
                return new DoubleBuffer(this);
            }
        }

    }

    public static final class Double extends AbstractDouble {
        public Double(BasicImageData2D.AbstractDouble src) {
            super(src);
        }

        public Double(IImageSpace2D space) {
            super(space);

        }

        public Double(IImageSpace2D space, double[] _data) {
            super(space, _data);
        }

    }

    public static final class DoubleBuffer extends AbstractDouble implements ImageBuffer2D {
        public DoubleBuffer(BasicImageData2D.AbstractDouble src) {
            super(src);

        }

        public DoubleBuffer(IImageSpace2D space) {
            super(space);

        }

        public DoubleBuffer(IImageSpace2D space, double[] _data) {
            super(space, _data);
        }


        @Override
        public final void setValue(int x, int y, double val) {
            data.set(x, y, val);
        }

        @Override
        public final void setValue(int index, double value) {
            data.set(index, value);
        }

        @Override
        public DoubleBuffer createBuffer(boolean clear) {
            if (clear) {
                return new DoubleBuffer(this.getImageSpace());
            } else {
                return this;
            }
        }

    }

    private static abstract class AbstractFloat extends BasicImageData2D {
        public AbstractFloat(BasicImageData2D.AbstractFloat src) {
            super(src.getImageSpace(), DataType.DOUBLE);
            data = new Array2D.Float(src.dim().getDim(0), src.dim().getDim(1), ((Array2D.Float) src.data).toArray());
        }

        public AbstractFloat(IImageSpace2D space) {
            super(space, DataType.FLOAT);
            data = new Array2D.Double(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS));
        }

        public AbstractFloat(IImageSpace2D space, float[] _data) {
            super(space, DataType.FLOAT);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array2D.Float(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), _data);
        }

        @Override
        public FloatBuffer createBuffer(boolean clear) {
            if (clear) {
                return new FloatBuffer(this.getImageSpace());
            } else {
                return new FloatBuffer(this);
            }
        }

    }


    public static final class Float extends AbstractFloat {
        public Float(BasicImageData2D.AbstractFloat src) {
            super(src);
        }

        public Float(IImageSpace2D space) {
            super(space);
        }

        public Float(IImageSpace2D space, float[] _data) {
            super(space, _data);
        }

        @Override
        public FloatBuffer createBuffer(boolean clear) {
            if (clear) {
                return new FloatBuffer(this.getImageSpace());
            } else {
                return new FloatBuffer(this);
            }
        }
    }

    public static final class FloatBuffer extends AbstractFloat implements ImageBuffer2D {
        public FloatBuffer(BasicImageData2D.AbstractFloat src) {
            super(src.getImageSpace());
        }

        public FloatBuffer(IImageSpace2D space) {
            super(space);
        }

        public FloatBuffer(IImageSpace2D space, float[] _data) {
            super(space, _data);
        }

        @Override
        public void setValue(int index, double value) {
            data.set(index, value);
        }

        @Override
        public void setValue(int x, int y, double val) {
            data.set(x, y, val);
        }

        @Override
        public FloatBuffer createBuffer(boolean clear) {
            if (clear) {
                return new FloatBuffer(this.getImageSpace());
            } else {
                return this;
            }
        }

    }

    private static abstract class AbstractInt extends BasicImageData2D {
        public AbstractInt(BasicImageData2D.AbstractInt src) {
            super(src.getImageSpace(), DataType.INTEGER);
            data = new Array2D.Int(src.dim().getDim(0), src.dim().getDim(1), ((Array2D.Int) src.data).toArray());
        }

        public AbstractInt(IImageSpace2D space) {
            super(space, DataType.INTEGER);
            data = new Array2D.Int(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS));
        }

        public AbstractInt(IImageSpace2D space, int[] _data) {
            super(space, DataType.INTEGER);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array2D.Int(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), _data);
        }

        @Override
        public IntBuffer createBuffer(boolean clear) {
            if (clear) {
                return new IntBuffer(this.getImageSpace());
            } else {
                return new IntBuffer(this);
            }
        }

    }

    public static final class Int extends AbstractInt {
        public Int(BasicImageData2D.AbstractInt src) {
            super(src);
        }

        public Int(IImageSpace2D space) {
            super(space);

        }

        public Int(IImageSpace2D space, int[] _data) {
            super(space, _data);
        }

    }

    public static final class IntBuffer extends AbstractInt implements ImageBuffer2D {
        public IntBuffer(BasicImageData2D.AbstractInt src) {
            super(src);

        }

        public IntBuffer(IImageSpace2D space) {
            super(space);

        }

        public IntBuffer(IImageSpace2D space, int[] _data) {
            super(space, _data);
        }


        @Override
        public final void setValue(int x, int y, double val) {
            data.set(x, y, val);
        }

        @Override
        public final void setValue(int index, double value) {
            data.set(index, value);
        }

        @Override
        public IntBuffer createBuffer(boolean clear) {
            if (clear) {
                return new IntBuffer(this.getImageSpace());
            } else {
                return this;
            }
        }

    }

    private static abstract class AbstractShort extends BasicImageData2D {
        public AbstractShort(BasicImageData2D.AbstractShort src) {
            super(src.getImageSpace(), DataType.SHORT);
            data = new Array2D.Short(src.dim().getDim(0), src.dim().getDim(1), ((Array2D.Short) src.data).toArray());
        }

        public AbstractShort(IImageSpace2D space) {
            super(space, DataType.SHORT);
            data = new Array2D.Short(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS));
        }

        public AbstractShort(IImageSpace2D space, short[] _data) {
            super(space, DataType.SHORT);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array2D.Short(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), _data);
        }

        @Override
        public ShortBuffer createBuffer(boolean clear) {
            if (clear) {
                return new ShortBuffer(this.getImageSpace());
            } else {
                return new ShortBuffer(this);
            }
        }

    }

    public static final class Short extends AbstractShort {
        public Short(BasicImageData2D.AbstractShort src) {
            super(src);
        }

        public Short(IImageSpace2D space) {
            super(space);

        }

        public Short(IImageSpace2D space, short[] _data) {
            super(space, _data);
        }

    }

    public static final class ShortBuffer extends AbstractShort implements ImageBuffer2D {
        public ShortBuffer(BasicImageData2D.AbstractShort src) {
            super(src);

        }

        public ShortBuffer(IImageSpace2D space) {
            super(space);

        }

        public ShortBuffer(IImageSpace2D space, short[] _data) {
            super(space, _data);
        }


        @Override
        public final void setValue(int x, int y, double val) {
            data.set(x, y, val);
        }

        @Override
        public final void setValue(int index, double value) {
            data.set(index, value);
        }

        @Override
        public ShortBuffer createBuffer(boolean clear) {
            if (clear) {
                return new ShortBuffer(this.getImageSpace());
            } else {
                return this;
            }
        }

    }

    private static abstract class AbstractByte extends BasicImageData2D {
        public AbstractByte(BasicImageData2D.AbstractByte src) {
            super(src.getImageSpace(), DataType.BYTE);
            data = new Array2D.Byte(src.dim().getDim(0), src.dim().getDim(1), ((Array2D.Byte) src.data).toArray());
        }

        public AbstractByte(IImageSpace2D space) {
            super(space, DataType.BYTE);
            data = new Array2D.Byte(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS));
        }

        public AbstractByte(IImageSpace2D space, byte[] _data) {
            super(space, DataType.BYTE);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array2D.Byte(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), _data);
        }

        @Override
        public ByteBuffer createBuffer(boolean clear) {
            if (clear) {
                return new ByteBuffer(this.getImageSpace());
            } else {
                return new ByteBuffer(this);
            }
        }

    }

    public static final class Byte extends AbstractByte {
        public Byte(BasicImageData2D.AbstractByte src) {
            super(src);
        }

        public Byte(IImageSpace2D space) {
            super(space);

        }

        public Byte(IImageSpace2D space, byte[] _data) {
            super(space, _data);
        }

    }

    public static final class ByteBuffer extends AbstractByte implements ImageBuffer2D {
        public ByteBuffer(BasicImageData2D.AbstractByte src) {
            super(src);

        }

        public ByteBuffer(IImageSpace2D space) {
            super(space);

        }

        public ByteBuffer(IImageSpace2D space, byte[] _data) {
            super(space, _data);
        }


        @Override
        public final void setValue(int x, int y, double val) {
            data.set(x, y, val);
        }

        @Override
        public final void setValue(int index, double value) {
            data.set(index, value);
        }

        @Override
        public ByteBuffer createBuffer(boolean clear) {
            if (clear) {
                return new ByteBuffer(this.getImageSpace());
            } else {
                return this;
            }
        }

    }


}