package brainflow.array;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.iterators.ValueIterator;
import brainflow.utils.Dimension2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 4, 2009
 * Time: 12:43:14 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Array2D implements IArray2D, IArrayBuffer2D {


    private int dim0, dim1, len;

    private Dimension2D<Integer> dim;

    @Override
    public abstract double value(int i, int j);

    protected Array2D(int dim0, int dim1) {
        this.dim0 = dim0;
        this.dim1 = dim1;
        len = dim0*dim1;
        dim = new Dimension2D<Integer>(dim0, dim1);
    }

    @Override
    public int indexOf(int i, int j) {
        return j*dim0 + i;
    }

    @Override
    public Dimension2D<Integer> dim() {
        return dim;
    }

    @Override
    public abstract double value(int i);

    @Override
    public int length() {
        return len;
    }

    private static boolean checkDim(int dim0, int dim1, int len) {
         if (len != (dim0*dim1)) {
            throw new IllegalArgumentException("array length must equal product of dimensions: " + len + " != " + (dim0*dim1));
        }

        return true;

    }

    public static Double doubles(int dim0, int dim1, double[] data) {
        checkDim(dim0, dim1, data.length);
        return new Double(dim0, dim1, data);

    }

    public static Int ints(int dim0, int dim1, int[] data) {
        checkDim(dim0, dim1, data.length);
        return new Int(dim0, dim1, data);

    }
    public static Float floats(int dim0, int dim1, float[] data) {
        checkDim(dim0, dim1, data.length);
        return new Float(dim0, dim1, data);

    }
    public static Short shorts(int dim0, int dim1, short[] data) {
        checkDim(dim0, dim1, data.length); 
        return new Short(dim0, dim1, data);
    }

    public final static class Double extends Array2D {

        private double[] data;

        public Double(int dim0, int dim1, double[] data) {
            super(dim0, dim1);
            this.data = data;
        }


        public Double(int dim0, int dim1) {
            super(dim0,dim1);
            data = new double[dim0*dim1];
        }

        @Override
        public double value(int i, int j) {
            return data[indexOf(i,j)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, double val) {
            data[i] = val;
        }

        @Override
        public void set(int i, int j, double val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, int j, int val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, int j, short val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, int j, float val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, int j, long val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, int j, byte val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, float val) {
            data[i] = val;
        }

        @Override
        public void set(int i, short val) {
            data[i] = val;
        }

        @Override
        public void set(int i, int val) {
            data[i] = val;
        }

        @Override
        public void set(int i, long val) {
            data[i] = val;
        }

        @Override
        public void set(int i, byte val) {
            data[i] = val;
        }


        public Class<?> getType() {
            return java.lang.Double.TYPE;
        }

        public double[] toArray() {
            return data;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(double x, double y, InterpolationFunction2D interp) {
            return interp.interpolate(x,y,this);
        }
    }

    public final static class Int extends Array2D {

        private int[] data;

        public Int(int dim0, int dim1, int[] data) {
            super(dim0, dim1);
            this.data = data;
        }

        public Int(int dim0, int dim1) {
            super(dim0,dim1);
            data = new int[dim0*dim1];
        }

        @Override
        public double value(int i, int j) {
            return data[indexOf(i,j)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, double val) {
            data[i] = (int)val;
        }


        public int intValue(int i) {
            return data[i];
        }

        public int intValue(int i, int j) {
            return data[indexOf(i,j)];
        }

        @Override
        public void set(int i, int j, double val) {
           data[indexOf(i,j)] = (int)val;
        }

        @Override
        public void set(int i, int j, int val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, int j, short val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, int j, float val) {
            data[indexOf(i,j)] = (int)val;
        }

        @Override
        public void set(int i, int j, long val) {
            data[indexOf(i,j)] = (int)val;
        }

        @Override
        public void set(int i, int j, byte val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, float val) {
            data[i] = (int)val;
        }

        @Override
        public void set(int i, short val) {
            data[i] = val;
        }

        @Override
        public void set(int i, int val) {
            data[i] = val;
        }

        @Override
        public void set(int i, long val) {
            data[i] = (int)val;
        }

        @Override
        public void set(int i, byte val) {
            data[i] = val;
        }


        public Class<?> getType() {
            return Integer.TYPE;
        }

        public int[] toArray() {
            return data;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(double x, double y, InterpolationFunction2D interp) {
            return interp.interpolate(x,y,this);
        }
    }

    public final static class Short extends Array2D {

        private short[] data;

        public Short(int dim0, int dim1, short[] data) {
            super(dim0, dim1);
            this.data = data;
        }

        public Short(int dim0, int dim1) {
            super(dim0,dim1);
            data = new short[dim0*dim1];
        }

        @Override
        public double value(int i, int j) {
            return data[indexOf(i,j)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, double val) {
            data[i] = (short)val;
        }

        @Override
        public void set(int i, int j, double val) {
            data[indexOf(i,j)] = (short)val;
        }

        @Override
        public void set(int i, int j, int val) {
            data[indexOf(i,j)] = (short)val;
        }

        @Override
        public void set(int i, int j, short val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, int j, float val) {
            data[indexOf(i,j)] = (short)val;
        }

        @Override
        public void set(int i, int j, long val) {
            data[indexOf(i,j)] = (short)val;
        }

        @Override
        public void set(int i, int j, byte val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, float val) {
            data[i] = (short)val;
        }

        @Override
        public void set(int i, short val) {
            data[i] = val;
        }

        @Override
        public void set(int i, int val) {
            data[i] = (short)val;
        }

        @Override
        public void set(int i, long val) {
            data[i] = (short)val;
        }

        @Override
        public void set(int i, byte val) {
            data[i] = val;
        }

        public short shortValue(int i) {
            return data[i];
        }

        public short shortValue(int i, int j) {
            return data[indexOf(i,j)];
        }


        public Class<?> getType() {
            return java.lang.Short.TYPE;
        }

        public short[] toArray() {
            return data;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(double x, double y, InterpolationFunction2D interp) {
            return interp.interpolate(x,y,this);
        }
    }

    public final static class Float extends Array2D {

        private float[] data;

        public Float(int dim0, int dim1, float[] data) {
            super(dim0, dim1);
            this.data = data;
        }

        public Float(int dim0, int dim1) {
            super(dim0,dim1);
            data = new float[dim0*dim1];
        }

        @Override
        public double value(int i, int j) {
            return data[indexOf(i,j)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, double val) {
            data[i] = (float)val;
        }

        @Override
        public void set(int i, int j, double val) {
            data[indexOf(i,j)] = (float)val;
        }

        @Override
        public void set(int i, int j, int val) {
            data[indexOf(i,j)] = (float)val;
        }

        @Override
        public void set(int i, int j, short val) {
            data[indexOf(i,j)] = (float)val;
        }

        @Override
        public void set(int i, int j, float val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, int j, long val) {
            data[indexOf(i,j)] = (float)val;
        }

        @Override
        public void set(int i, int j, byte val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, float val) {
            data[i] = val;
        }

        @Override
        public void set(int i, short val) {
            data[i] = val;

        }

        @Override
        public void set(int i, int val) {
            data[i] = val;
        }

        @Override
        public void set(int i, long val) {
            data[i] = val;
        }

        @Override
        public void set(int i, byte val) {
            data[i] = val;
        }

        public float floatValue(int i) {
            return data[i];
        }

        public float floatValue(int i, int j) {
            return data[indexOf(i,j)];
        }

        public Class<?> getType() {
            return java.lang.Float.TYPE;
        }

        public float[] toArray() {
            return data;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(double x, double y, InterpolationFunction2D interp) {
            return interp.interpolate(x,y,this);
        }
    }

    public final static class Byte extends Array2D {

        private byte[] data;

        public Byte(int dim0, int dim1, byte[] data) {
            super(dim0, dim1);
            this.data = data;
        }

        public Byte(int dim0, int dim1) {
            super(dim0,dim1);
            data = new byte[dim0*dim1];
        }

        @Override
        public double value(int i, int j) {
            return data[indexOf(i,j)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, double val) {
            data[i] = (byte)val;
        }

        @Override
        public void set(int i, int j, double val) {
            data[indexOf(i,j)] = (byte)val;
        }

        @Override
        public void set(int i, int j, int val) {
            data[indexOf(i,j)] = (byte)val;
        }

        @Override
        public void set(int i, int j, short val) {
            data[indexOf(i,j)] = (byte)val;
        }

        @Override
        public void set(int i, int j, float val) {
            data[indexOf(i,j)] = (byte)val;
        }

        @Override
        public void set(int i, int j, long val) {
            data[indexOf(i,j)] = (byte)val;
        }

        @Override
        public void set(int i, int j, byte val) {
            data[indexOf(i,j)] = val;
        }

        @Override
        public void set(int i, float val) {
            data[i] = (byte)val;
        }

        @Override
        public void set(int i, short val) {
            data[i] = (byte)val;

        }

        @Override
        public void set(int i, int val) {
            data[i] = (byte)val;
        }

        @Override
        public void set(int i, long val) {
            data[i] = (byte)val;
        }

        @Override
        public void set(int i, byte val) {
            data[i] = val;
        }

        public byte byteValue(int i) {
            return data[i];
        }

        public float byteValue(int i, int j) {
            return data[indexOf(i,j)];
        }
       
        public Class<?> getType() {
            return java.lang.Byte.TYPE;
        }

        public byte[] toArray() {
            return data;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(double x, double y, InterpolationFunction2D interp) {
            return interp.interpolate(x,y,this);
        }
    }

    public final static class Boolean extends Array2D {

        private boolean[] data;

        public Boolean(int dim0, int dim1, boolean[] data) {
            super(dim0, dim1);
            this.data = data;
        }

        public Boolean(int dim0, int dim1) {
            super(dim0,dim1);
            data = new boolean[dim0*dim1];
        }

        @Override
        public double value(int i, int j) {
            return data[indexOf(i,j)] ? 1 : 0;
        }

        @Override
        public double value(int i) {
            return data[i] ? 1 : 0;
        }

        @Override
        public void set(int i, double val) {
            data[i] = val != 0;
        }

        @Override
        public void set(int i, int j, double val) {
            data[indexOf(i,j)] = val != 0;
        }

        @Override
        public void set(int i, int j, int val) {
            data[indexOf(i,j)] = val != 0;
        }

        @Override
        public void set(int i, int j, short val) {
            data[indexOf(i,j)] = val != 0;
        }

        @Override
        public void set(int i, int j, float val) {
            data[indexOf(i,j)] = val != 0;
        }

        @Override
        public void set(int i, int j, long val) {
            data[indexOf(i,j)] = val != 0;
        }

        @Override
        public void set(int i, int j, byte val) {
            data[indexOf(i,j)] = val != 0;
        }

        @Override
        public void set(int i, float val) {
            data[i] = val != 0;
        }

        @Override
        public void set(int i, short val) {
            data[i] = val != 0;
        }

        @Override
        public void set(int i, int val) {
            data[i] = val != 0;
        }

        @Override
        public void set(int i, long val) {
            data[i] = val != 0;
        }

        @Override
        public void set(int i, byte val) {
            data[i] = val != 0;
        }

        public boolean booleanValue(int i) {
            return data[i];
        }

        public boolean booleanValue(int i, int j) {
            return data[indexOf(i,j)];
        }


        public Class<?> getType() {
            return java.lang.Boolean.TYPE;
        }

        public boolean[] toArray() {
            return data;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(double x, double y, InterpolationFunction2D interp) {
            return interp.interpolate(x,y,this);
        }
    }



}
