package org.boxwood.array;

import brainflow.utils.Dimension3D;
import brainflow.utils.Dimension4D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 6, 2009
 * Time: 11:38:32 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Array4D implements IArray4D {

    private int dim0, dim1, dim2, dim3, len;

    private int dim_01, dim_012;

    private Dimension4D<Integer> dim;

    @Override
    public abstract double value(int i, int j, int k, int m);

    protected Array4D(int dim0, int dim1, int dim2, int dim3) {
        this.dim0 = dim0;
        this.dim1 = dim1;
        this.dim2 = dim2;
        this.dim3 = dim3;

        dim_01 = dim0*dim1;
        dim_012 = dim0*dim1*dim2;

        len = dim0*dim1;
        dim = new Dimension4D<Integer>(dim0, dim1, dim2, dim3);
    }

    @Override
    public final int indexOf(int i, int j, int k, int m) {
        return k * dim_01 + j*dim0 + i;
    }

    @Override
    public Dimension4D<Integer> dim() {
        return dim;
    }

    @Override
    public abstract double value(int i);

    @Override
    public int length() {
        return len;
    }

    private static boolean checkDim(int dim0, int dim1, int dim2, int len) {
         if (len != (dim0*dim1*dim2)) {
            throw new IllegalArgumentException("array length must equal product of dimensions: " + len + " != " + (dim0*dim1*dim2));
        }

        return true;

    }

    public static class Double extends Array4D {

        private double[] data;

        public Double(int dim0, int dim1, int dim2, int dim3, double[] data) {
            super(dim0, dim1, dim2, dim3);
            this.data = data;
        }

        @Override
        public double value(int i, int j, int k, int m) {
            return data[indexOf(i,j,k,m)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, int j, int k, int m,double val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, int val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, short val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, float val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, long val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, byte val) {
            data[indexOf(i,j,k, m)] = val;
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

        @Override
        public Class getType() {
            return java.lang.Double.TYPE;
        }
    }

    public static class Float extends Array4D {

        private float[] data;

        public Float(int dim0, int dim1, int dim2, int dim3, float[] data) {
            super(dim0, dim1, dim2, dim3);
            this.data = data;
        }

        @Override
        public double value(int i, int j, int k, int m) {
            return data[indexOf(i,j,k,m)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, int j, int k, int m,double val) {
            data[indexOf(i,j,k,m)] = (float)val;
        }

        @Override
        public void set(int i, int j, int k, int m, int val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, short val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, float val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, long val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, byte val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, double val) {
            data[i] = (float)val;
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

        @Override
        public Class getType() {
            return java.lang.Float.TYPE;
        }
    }

    public static class Int extends Array4D {

        private int[] data;

        public Int(int dim0, int dim1, int dim2, int dim3, int[] data) {
            super(dim0, dim1, dim2, dim3);
            this.data = data;
        }

        @Override
        public double value(int i, int j, int k, int m) {
            return data[indexOf(i,j,k,m)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, int j, int k, int m, double val) {
            data[indexOf(i,j,k,m)] = (int)val;
        }

        @Override
        public void set(int i, int j, int k, int m, int val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, short val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, float val) {
            data[indexOf(i,j,k,m)] = (int)val;
        }

        @Override
        public void set(int i, int j, int k, int m, long val) {
            data[indexOf(i,j,k,m)] = (int)val;
        }

        @Override
        public void set(int i, int j, int k, int m, byte val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, double val) {
            data[i] = (int)val;
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

        @Override
        public Class getType() {
            return Integer.TYPE;
        }
    }

    public static class Short extends Array4D {

        private short[] data;

        public Short(int dim0, int dim1, int dim2, int dim3, short[] data) {
            super(dim0, dim1, dim2,dim3);
            this.data = data;
        }

        @Override
        public double value(int i, int j, int k, int m) {
            return data[indexOf(i,j,k,m)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, int j, int k, int m, double val) {
            data[indexOf(i,j,k,m)] = (short)val;
        }

        @Override
        public void set(int i, int j, int k, int m, int val) {
            data[indexOf(i,j,k,m)] = (short)val;
        }

        @Override
        public void set(int i, int j, int k, int m, short val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, int j, int k, int m, float val) {
            data[indexOf(i,j,k,m)] = (short)val;
        }

        @Override
        public void set(int i, int j, int k, int m, long val) {
            data[indexOf(i,j,k,m)] = (short)val;
        }

        @Override
        public void set(int i, int j, int k, int m, byte val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, double val) {
            data[i] = (short)val;
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

        @Override
        public Class getType() {
            return java.lang.Short.TYPE;
        }
    }
    public static class Byte extends Array4D {

        private byte[] data;

        public Byte(int dim0, int dim1, int dim2, int dim3, byte[] data) {
            super(dim0, dim1, dim2, dim3);
            this.data = data;
        }

        @Override
        public double value(int i, int j, int k, int m) {
            return data[indexOf(i,j,k,m)];
        }

        @Override
        public double value(int i) {
            return data[i];
        }

        @Override
        public void set(int i, int j, int k, int m, double val) {
            data[indexOf(i,j,k,m)] = (byte)val;
        }

        @Override
        public void set(int i, int j, int k, int m,int val) {
            data[indexOf(i,j,k,m)] = (byte)val;
        }

        @Override
        public void set(int i, int j, int k, int m,short val) {
            data[indexOf(i,j,k,m)] = (byte)val;
        }

        @Override
        public void set(int i, int j, int k, int m,float val) {
            data[indexOf(i,j,k,m)] = (byte)val;
        }

        @Override
        public void set(int i, int j, int k, int m,long val) {
            data[indexOf(i,j,k,m)] = (byte)val;
        }

        @Override
        public void set(int i, int j, int k, int m,byte val) {
            data[indexOf(i,j,k,m)] = val;
        }

        @Override
        public void set(int i, double val) {
            data[i] = (byte)val;
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

        @Override
        public Class getType() {
            return java.lang.Byte.TYPE;
        }
    }


}