package brainflow.image.data;

import brainflow.array.*;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.math.Index3D;
import brainflow.utils.DataType;
import brainflow.utils.NumberUtils;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public abstract class BasicImageData3D extends AbstractImageData3D {


    protected IArrayBuffer3D data;


    public static BasicImageData3D create(IImageSpace3D space, DataType dataType) {
        switch (dataType) {
            case UBYTE:
                throw new UnsupportedOperationException("do not support type " + dataType);
            case BOOLEAN:
                throw new UnsupportedOperationException("do not support type " + dataType);
            case BYTE:
                return new BasicImageData3D.Byte(space);
            case SHORT:
                return new BasicImageData3D.Short(space);
            case INTEGER:
                return new BasicImageData3D.Int(space);
            case FLOAT:
                return new BasicImageData3D.Float(space);
            case DOUBLE:
                return new BasicImageData3D.Double(space);
            case LONG:
                throw new UnsupportedOperationException("do not support type " + dataType);
            
        }

        throw new UnsupportedOperationException("do not support type " + dataType);
    }

    public BasicImageData3D(BasicImageData3D src) {
        super(src.getImageSpace(), src.getDataType());
    }


    public BasicImageData3D(IImageSpace3D space, DataType type) {
        super(space, type);
    }

    public BasicImageData3D(IImageSpace3D space, DataType type, String imageLabel) {
        super(space, type, imageLabel);
    }

    

    @Override
    public Anatomy3D getAnatomy() {
        return (Anatomy3D) space.getAnatomy();
    }

    @Deprecated
    public ImageInfo getImageInfo() {
        // todo is this  what we really want to do?
        return new ImageInfo(this);
    }


    //public final Index3D indexToGrid(int idx) {
    //    return getImageSpace().indexToGrid(idx);
    //}


    public double value(float x, float y, float z, InterpolationFunction3D interp) {
        return data.value(x, y, z, interp);
    }


    public  double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
        float x = space3d.worldToGridX(realx, realy, realz) -.5f;
        float y = space3d.worldToGridY(realx, realy, realz) -.5f;
        float z = space3d.worldToGridZ(realx, realy, realz) -.5f;

        return data.value(x, y, z, interp);
    }

    public  double value(int index) {
        return data.value(index);
    }


    public  double value(int x, int y, int z) {
        return data.value(x, y, z);
    }


    public DataSubGrid3D subGrid(int x0, int x1, int y0, int y1, int z0, int z1) {
        if (x1 < x0) throw new IllegalArgumentException("x1 cannot be < x0 ");
        if (y1 < y0) throw new IllegalArgumentException("y1 cannot be < y0 ");
        if (z1 < z0) throw new IllegalArgumentException("z1 cannot be < z0 ");

        return new DataSubGrid3D(this, x0, x1 - x0 + 1, y0, y1 - y0 + 1, z0, z1 - z0 + 1);
    }


    public ValueIterator valueIterator() {
        return data.valueIterator();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicImageData3D that = (BasicImageData3D) o;

        if (!space3d.equals(that.space3d)) return false;
        if (!getImageLabel().equals(that.getImageLabel())) return false;


        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + space3d.hashCode();
        //check super.hashcode to make sure this is necessary
        result = 31 * result + getImageLabel().hashCode();
        return result;
    }

    public String toString() {
        return this.getImageLabel();
    }

    public static abstract class AbstractDouble extends BasicImageData3D {
        public AbstractDouble(BasicImageData3D.AbstractDouble src) {
            super(src.getImageSpace(), DataType.DOUBLE);
            data = new Array3D.Double(src.dim().getDim(0), src.dim().getDim(1), src.dim().getDim(2), ((Array3D.Double) src.data).toArray());
        }

        public AbstractDouble(IImageSpace3D space) {
            super(space, DataType.DOUBLE);
            data = new Array3D.Double(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS));
        }

        public AbstractDouble(IImageSpace3D space, double[] _data) {
            super(space, DataType.DOUBLE);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array3D.Double(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS), _data);
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
        public Double(BasicImageData3D.AbstractDouble src) {
            super(src);
        }

        public Double(IImageSpace3D space) {
            super(space);

        }

        public Double(IImageSpace3D space, double[] _data) {
            super(space, _data);
        }

    }

    public static final class DoubleBuffer extends AbstractDouble implements ImageBuffer3D {
        public DoubleBuffer(BasicImageData3D.AbstractDouble src) {
            super(src);

        }

        public DoubleBuffer(IImageSpace3D space) {
            super(space);

        }

        public DoubleBuffer(IImageSpace3D space, double[] _data) {
            super(space, _data);
        }


        @Override
        public final void setValue(int x, int y, int z, double val) {
            data.set(x, y, z, val);
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

    public static abstract class AbstractFloat extends BasicImageData3D {
        public AbstractFloat(BasicImageData3D.AbstractFloat src) {
            super(src.getImageSpace(), DataType.FLOAT);
            data = new Array3D.Float(src.dim().getDim(0), src.dim().getDim(1), src.dim().getDim(2), ((Array3D.Float) src.data).toArray());
        }

        public AbstractFloat(IImageSpace3D space) {
            super(space, DataType.FLOAT);
            data = new Array3D.Float(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS));
        }

        public AbstractFloat(IImageSpace3D space, float[] _data) {
            super(space, DataType.FLOAT);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array3D.Float(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS), _data);
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
        public Float(BasicImageData3D.AbstractFloat src) {
            super(src);
        }

        public Float(IImageSpace3D space) {
            super(space);

        }

        public Float(IImageSpace3D space, float[] _data) {
            super(space, _data);
        }

    }

    public static final class FloatBuffer extends AbstractFloat implements ImageBuffer3D {
        public FloatBuffer(BasicImageData3D.AbstractFloat src) {
            super(src);

        }

        public FloatBuffer(IImageSpace3D space) {
            super(space);

        }

        public FloatBuffer(IImageSpace3D space, float[] _data) {
            super(space, _data);
        }


        @Override
        public final void setValue(int x, int y, int z, double val) {
            data.set(x, y, z, val);
        }

        @Override
        public final void setValue(int index, double value) {
            data.set(index, value);
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

    public static abstract class AbstractInt extends BasicImageData3D {
        public AbstractInt(BasicImageData3D.AbstractInt src) {
            super(src.getImageSpace(), DataType.INTEGER);
            data = new Array3D.Int(src.dim().getDim(0), src.dim().getDim(1), src.dim().getDim(2), ((Array3D.Int) src.data).toArray());
        }

        public AbstractInt(IImageSpace3D space) {
            super(space, DataType.INTEGER);
            data = new Array3D.Int(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS));
        }

        public AbstractInt(IImageSpace3D space, int[] _data) {
            super(space, DataType.INTEGER);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array3D.Int(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS), _data);
        }

        public AbstractInt(IImageSpace3D space, Array3D.Int _data) {
            super(space, DataType.INTEGER);
            if (_data.length() != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length());
            }

            data = _data;
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
        public Int(BasicImageData3D.AbstractInt src) {
            super(src);
        }

        public Int(IImageSpace3D space) {
            super(space);

        }

        public Int(IImageSpace3D space, int[] _data) {
            super(space, _data);
        }

        public Int(IImageSpace3D space, Array3D.Int _data) {
            super(space, _data);
        }
    }

    public static final class IntBuffer extends AbstractInt implements ImageBuffer3D {
        public IntBuffer(BasicImageData3D.AbstractInt src) {
            super(src);

        }

        public IntBuffer(IImageSpace3D space) {
            super(space);

        }

        public IntBuffer(IImageSpace3D space, int[] _data) {
            super(space, _data);
        }


        @Override
        public final void setValue(int x, int y, int z, double val) {
            data.set(x, y, z, val);
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

    public static abstract class AbstractShort extends BasicImageData3D {
        public AbstractShort(BasicImageData3D.AbstractShort src) {
            super(src.getImageSpace(), DataType.SHORT);
            data = new Array3D.Short(src.dim().getDim(0), src.dim().getDim(1), src.dim().getDim(2), ((Array3D.Short) src.data).toArray());
        }

        public AbstractShort(IImageSpace3D space) {
            super(space, DataType.SHORT);
            data = new Array3D.Short(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS));
        }

        public AbstractShort(IImageSpace3D space, short[] _data) {
            super(space, DataType.SHORT);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array3D.Short(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS), _data);
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
        public Short(BasicImageData3D.AbstractShort src) {
            super(src);
        }

        public Short(IImageSpace3D space) {
            super(space);

        }

        public Short(IImageSpace3D space, short[] _data) {
            super(space, _data);
        }

    }

    public static final class ShortBuffer extends AbstractShort implements ImageBuffer3D {
        public ShortBuffer(BasicImageData3D.AbstractShort src) {
            super(src);

        }

        public ShortBuffer(IImageSpace3D space) {
            super(space);

        }

        public ShortBuffer(IImageSpace3D space, short[] _data) {
            super(space, _data);
        }


        @Override
        public final void setValue(int x, int y, int z, double val) {
            data.set(x, y, z, val);
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

    public static abstract class AbstractByte extends BasicImageData3D {
        public AbstractByte(BasicImageData3D.AbstractByte src) {
            super(src.getImageSpace(), DataType.BYTE);
            data = new Array3D.Byte(src.dim().getDim(0), src.dim().getDim(1), src.dim().getDim(2), ((Array3D.Byte) src.data).toArray());
        }

        public AbstractByte(IImageSpace3D space) {
            super(space, DataType.BYTE);
            data = new Array3D.Byte(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS));
        }

        public AbstractByte(IImageSpace3D space, byte[] _data) {
            super(space, DataType.BYTE);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array3D.Byte(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS), _data);
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
        public Byte(BasicImageData3D.AbstractByte src) {
            super(src);
        }

        public Byte(IImageSpace3D space) {
            super(space);

        }

        public Byte(IImageSpace3D space, byte[] _data) {
            super(space, _data);
        }

    }

    public static final class ByteBuffer extends AbstractByte implements ImageBuffer3D {
        public ByteBuffer(BasicImageData3D.AbstractByte src) {
            super(src);

        }

        public ByteBuffer(IImageSpace3D space) {
            super(space);

        }

        public ByteBuffer(IImageSpace3D space, byte[] _data) {
            super(space, _data);
        }


        @Override
        public final void setValue(int x, int y, int z, double val) {
            data.set(x, y, z, val);
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


    private static abstract class AbstractUByte extends BasicImageData3D {
        public AbstractUByte(BasicImageData3D.AbstractUByte src) {
            super(src.getImageSpace(), DataType.UBYTE);
            data = new Array3D.UByte(src.dim().getDim(0), src.dim().getDim(1), src.dim().getDim(2), ((Array3D.Byte) src.data).toArray());
        }

        public AbstractUByte(IImageSpace3D space) {
            super(space, DataType.BYTE);
            data = new Array3D.UByte(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS));
        }

        public AbstractUByte(IImageSpace3D space, byte[] _data) {
            super(space, DataType.UBYTE);
            if (_data.length != space.getNumSamples()) {
                throw new IllegalArgumentException("array has wrong number of elements: " + _data.length);
            }

            data = new Array3D.UByte(space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS), space.getDimension(Axis.Z_AXIS), _data);
        }

        

        @Override
        public UByteBuffer createBuffer(boolean clear) {
            if (clear) {
                return new UByteBuffer(this.getImageSpace());
            } else {
                return new UByteBuffer(this);
            }
        }

    }

    public static final class UByte extends AbstractUByte {
        public UByte(BasicImageData3D.AbstractUByte src) {
            super(src);
        }

        public UByte(IImageSpace3D space) {
            super(space);

        }

        public UByte(IImageSpace3D space, byte[] _data) {
            super(space, _data);
        }

    }

    public static final class UByteBuffer extends AbstractUByte implements ImageBuffer3D {
        public UByteBuffer(BasicImageData3D.AbstractUByte src) {
            super(src);

        }

        public UByteBuffer(IImageSpace3D space) {
            super(space);

        }

        public UByteBuffer(IImageSpace3D space, byte[] _data) {
            super(space, _data);
        }


        @Override
        public final void setValue(int x, int y, int z, double val) {
            data.set(x, y, z, val);
        }

        @Override
        public final void setValue(int index, double value) {
            data.set(index, value);
        }

        @Override
        public UByteBuffer createBuffer(boolean clear) {
            if (clear) {
                return new UByteBuffer(this.getImageSpace());
            } else {
                return this;
            }
        }

    }

    


}
