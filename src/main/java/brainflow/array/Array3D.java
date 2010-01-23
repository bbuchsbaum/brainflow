package brainflow.array;

import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.iterators.ValueIterator;
import brainflow.utils.Dimension3D;
import brainflow.utils.NumberUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 6, 2009
 * Time: 11:38:32 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Array3D implements IArray3D, IArrayBuffer3D {

    private int dim0, dim1, dim2, len;

    private int dim_01;

    private Dimension3D<Integer> dim;

    @Override
    public abstract double value(int i, int j, int k);

    protected Array3D(int dim0, int dim1, int dim2) {
        this.dim0 = dim0;
        this.dim1 = dim1;
        this.dim2 = dim2;

        dim_01 = dim0 * dim1;

        len = dim0 * dim1 * dim2;
        dim = new Dimension3D<Integer>(dim0, dim1, dim2);
    }

    @Override
    public final int indexOf(int i, int j, int k) {
        return k * dim_01 + j * dim0 + i;
    }

    @Override
    public Dimension3D<Integer> dim() {
        return dim;
    }

    @Override
    public abstract double value(int i);

    @Override
    public int length() {
        return len;
    }

    private static boolean checkDim(int dim0, int dim1, int dim2, int len) {
        if (len != (dim0 * dim1 * dim2)) {
            throw new IllegalArgumentException("array length must equal product of dimensions: " + len + " != " + (dim0 * dim1 * dim2));
        }

        return true;

    }

    public static class Double extends Array3D {

        private double[] data;

        public Double(int dim0, int dim1, int dim2, double[] data) {
            super(dim0, dim1, dim2);
            this.data = data;
        }

        public Double(int dim0, int dim1, int dim2) {
            super(dim0, dim1, dim2);
            this.data = new double[dim0*dim1*dim2];
        }

        @Override
        public double value(int i, int j, int k) {
            return data[indexOf(i, j, k)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, int j, int k, double val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, int val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, short val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, float val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, long val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, byte val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, double val) {
            data[i] = val;
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

        public double[] toArray() {
            return data;
        }


        public Class getType() {
            return java.lang.Double.TYPE;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(float x, float y, float z, InterpolationFunction3D interp) {
            return interp.interpolate(x, y, z, this);
        }
    }

    public static class Float extends Array3D {

        private float[] data;

        public Float(int dim0, int dim1, int dim2, float[] data) {
            super(dim0, dim1, dim2);
            this.data = data;
        }

        public Float(int dim0, int dim1, int dim2) {
            super(dim0, dim1, dim2);
            this.data = new float[dim0*dim1*dim2];
        }

        @Override
        public double value(int i, int j, int k) {
            return data[indexOf(i, j, k)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, int j, int k, double val) {
            data[indexOf(i, j, k)] = (float) val;
        }

        @Override
        public void set(int i, int j, int k, int val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, short val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, float val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, long val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, byte val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, double val) {
            data[i] = (float) val;
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


        public Class getType() {
            return java.lang.Float.TYPE;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(float x, float y, float z, InterpolationFunction3D interp) {
            return interp.interpolate(x, y, z, this);
        }

        public float[] toArray() {
            return data;
        }
    }

    public static class Int extends Array3D {

        private int[] data;

        public Int(int dim0, int dim1, int dim2, int[] data) {
            super(dim0, dim1, dim2);
            this.data = data;
        }

        public Int(int dim0, int dim1, int dim2) {
            super(dim0, dim1, dim2);
            this.data = new int[dim0*dim1*dim2];
        }

        @Override
        public double value(int i, int j, int k) {
            return data[indexOf(i, j, k)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, int j, int k, double val) {
            data[indexOf(i, j, k)] = (int) val;
        }

        @Override
        public void set(int i, int j, int k, int val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, short val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, float val) {
            data[indexOf(i, j, k)] = (int) val;
        }

        @Override
        public void set(int i, int j, int k, long val) {
            data[indexOf(i, j, k)] = (int) val;
        }

        @Override
        public void set(int i, int j, int k, byte val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, double val) {
            data[i] = (int) val;
        }

        @Override
        public void set(int i, float val) {
            data[i] = (int) val;
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
            data[i] = (int) val;
        }

        @Override
        public void set(int i, byte val) {
            data[i] = val;
        }


        public Class getType() {
            return java.lang.Integer.TYPE;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(float x, float y, float z, InterpolationFunction3D interp) {
            return interp.interpolate(x, y, z, this);
        }

        public int[] toArray() {
            return data;
        }
    }

    public static class Short extends Array3D {

        private short[] data;

        public Short(int dim0, int dim1, int dim2, short[] data) {
            super(dim0, dim1, dim2);
            this.data = data;
        }

        public Short(int dim0, int dim1, int dim2) {
            super(dim0, dim1, dim2);
            this.data = new short[dim0*dim1*dim2];
        }

        @Override
        public double value(int i, int j, int k) {
            return data[indexOf(i, j, k)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, int j, int k, double val) {
            data[indexOf(i, j, k)] = (short) val;
        }

        @Override
        public void set(int i, int j, int k, int val) {
            data[indexOf(i, j, k)] = (short) val;
        }

        @Override
        public void set(int i, int j, int k, short val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, int j, int k, float val) {
            data[indexOf(i, j, k)] = (short) val;
        }

        @Override
        public void set(int i, int j, int k, long val) {
            data[indexOf(i, j, k)] = (short) val;
        }

        @Override
        public void set(int i, int j, int k, byte val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, double val) {
            data[i] = (short) val;
        }

        @Override
        public void set(int i, float val) {
            data[i] = (short) val;
        }

        @Override
        public void set(int i, short val) {
            data[i] = val;
        }

        @Override
        public void set(int i, int val) {
            data[i] = (short) val;
        }

        @Override
        public void set(int i, long val) {
            data[i] = (short) val;
        }

        @Override
        public void set(int i, byte val) {
            data[i] = val;
        }


        public Class getType() {
            return java.lang.Short.TYPE;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(float x, float y, float z, InterpolationFunction3D interp) {
            return interp.interpolate(x, y, z, this);
        }

        public short[] toArray() {
            return data;
        }
    }

    public static class Byte extends Array3D {

        private byte[] data;

        public Byte(int dim0, int dim1, int dim2, byte[] data) {
            super(dim0, dim1, dim2);
            this.data = data;
        }

        public Byte(int dim0, int dim1, int dim2) {
            super(dim0, dim1, dim2);
            this.data = new byte[dim0*dim1*dim2];
        }

        @Override
        public double value(int i, int j, int k) {
            return data[indexOf(i, j, k)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, int j, int k, double val) {
            data[indexOf(i, j, k)] = (byte) val;
        }

        @Override
        public void set(int i, int j, int k, int val) {
            data[indexOf(i, j, k)] = (byte) val;
        }

        @Override
        public void set(int i, int j, int k, short val) {
            data[indexOf(i, j, k)] = (byte) val;
        }

        @Override
        public void set(int i, int j, int k, float val) {
            data[indexOf(i, j, k)] = (byte) val;
        }

        @Override
        public void set(int i, int j, int k, long val) {
            data[indexOf(i, j, k)] = (byte) val;
        }

        @Override
        public void set(int i, int j, int k, byte val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, double val) {
            data[i] = (byte) val;
        }

        @Override
        public void set(int i, float val) {
            data[i] = (byte) val;
        }

        @Override
        public void set(int i, short val) {
            data[i] = (byte) val;
        }

        @Override
        public void set(int i, int val) {
            data[i] = (byte) val;
        }

        @Override
        public void set(int i, long val) {
            data[i] = (byte) val;
        }

        @Override
        public void set(int i, byte val) {
            data[i] = val;
        }


        public Class getType() {
            return java.lang.Byte.TYPE;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(float x, float y, float z, InterpolationFunction3D interp) {
            return interp.interpolate(x, y, z, this);
        }

        public byte[] toArray() {
            return data;
        }
    }

    public static class UByte extends Array3D {

        private byte[] data;

        public UByte(int dim0, int dim1, int dim2, byte[] data) {
            super(dim0, dim1, dim2);
            this.data = data;
        }

        public UByte(int dim0, int dim1, int dim2) {
            super(dim0, dim1, dim2);
            this.data = new byte[dim0*dim1*dim2];
        }

        @Override
        public double value(int i, int j, int k) {
            return NumberUtils.ubyte(data[indexOf(i, j, k)]);
        }

        @Override
        public double value(int i) {
            return NumberUtils.ubyte(data[i]);
        }

        @Override
        public void set(int i, int j, int k, double val) {
            data[indexOf(i, j, k)] = (byte) val;
        }

        @Override
        public void set(int i, int j, int k, int val) {
            data[indexOf(i, j, k)] = (byte) val;
        }

        @Override
        public void set(int i, int j, int k, short val) {
            data[indexOf(i, j, k)] = (byte) val;
        }

        @Override
        public void set(int i, int j, int k, float val) {
            data[indexOf(i, j, k)] = (byte) val;
        }

        @Override
        public void set(int i, int j, int k, long val) {
            data[indexOf(i, j, k)] = (byte) val;
        }

        @Override
        public void set(int i, int j, int k, byte val) {
            data[indexOf(i, j, k)] = val;
        }

        @Override
        public void set(int i, double val) {
            data[i] = (byte) val;
        }

        @Override
        public void set(int i, float val) {
            data[i] = (byte) val;
        }

        @Override
        public void set(int i, short val) {
            data[i] = (byte) val;
        }

        @Override
        public void set(int i, int val) {
            data[i] = (byte) val;
        }

        @Override
        public void set(int i, long val) {
            data[i] = (byte) val;
        }

        @Override
        public void set(int i, byte val) {
            data[i] = val;
        }


        public Class getType() {
            return java.lang.Byte.TYPE;
        }

        @Override
        public ValueIterator valueIterator() {
            return new ArrayValueIterator(this);
        }

        @Override
        public double value(float x, float y, float z, InterpolationFunction3D interp) {
            return interp.interpolate(x, y, z, this);
        }

        public byte[] toArray() {
            return data;
        }
    }


}
