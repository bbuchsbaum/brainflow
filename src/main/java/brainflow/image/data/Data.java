package brainflow.image.data;

import brainflow.array.Array3D;
import brainflow.array.ConstantValueIterator;
import brainflow.image.anatomy.SpatialLoc1D;
import brainflow.image.iterators.ImageValueIterator;
import brainflow.image.space.*;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.axis.ImageAxis;
import brainflow.utils.*;
import brainflow.math.Index3D;

import java.util.Arrays;

import brainflow.array.IDataGrid3D;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 9, 2008
 * Time: 9:10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Data {


    private Data() {}


    public static IImageData3D asImageData3D(IImageData2D data2d, SpatialLoc1D zvalue, double thickness) {
        IImageSpace2D space2d = data2d.getImageSpace();

        ImageAxis zaxis = new ImageAxis(zvalue.getValue() - (thickness / 2.0), zvalue.getValue() + (thickness / 2.0), zvalue.getAnatomy(), 1);
        IImageSpace3D space = new ImageSpace3D(space2d.getImageAxis(Axis.X_AXIS), space2d.getImageAxis(Axis.Y_AXIS), zaxis);
        IImageData3D data3d = BasicImageData3D.create(space, data2d.getDataType());

        ImageBuffer3D buffer = data3d.createBuffer(false);

        ValueIterator imageIterator = data2d.valueIterator();

        while(imageIterator.hasNext()) {
            int i = imageIterator.index();
            double val = imageIterator.next();
            buffer.setValue(i, val);
        }


        return buffer;

    }

    public static IImageData3D wrap(Array3D.Int dat, IImageSpace3D space) {
        return new BasicImageData3D.Int(space, dat);

    }

    public static ImageBuffer3D createWriter(IImageData3D input, DataType out) {
        final BasicImageData3D delegate = BasicImageData3D.create(input.getImageSpace(),out);
        return delegate.createBuffer(false);

    }

     public static IMaskedData3D createMask3D(MaskPredicate pred, IImageData3D dat) {
            return new MaskedData3D(dat, pred);
    }


    public static boolean elementsEquals(IImageData d1, IImageData d2, float tolerance) {
        if (d1.length() != d2.length()) return false;

        ValueIterator iter1 = d1.valueIterator();
        ValueIterator iter2 = d2.valueIterator();

        while (iter1.hasNext() && iter2.hasNext()) {
            if (!NumberUtils.equals(iter1.next(), iter2.next(), tolerance)) {
                return false;
            }

        }

        return true;

    }

    

    public static double[] toArray(IImageData data) {
        ValueIterator iter = data.valueIterator();
        double[] ret = new double[data.length()];

        while (iter.hasNext()) {
            ret[iter.index()] = iter.next();

        }

        return ret;
    }

    public static double[] sort(IImageData data) {
        double[] ret = toArray(data);
        Arrays.sort(ret);
        return ret;
    }


    public static double meanDeviation(IImageData data, double referenceVal) {
        ValueIterator iter = data.valueIterator();

        double sum = 0;
        int count = 0;

        while (iter.hasNext()) {
            double val = iter.next();
            if (val != 0) {
                sum = sum + Math.pow(val - referenceVal, 2);
                count++;
            }

        }

        return sum / count;


    }


    public static double nonzeroMean(IImageData data) {
        ValueIterator iter = data.valueIterator();

        double sum = 0;
        int count = 0;
        while (iter.hasNext()) {
            double val = iter.next();
            if (val != 0) {
                sum = sum + val;
                count++;
            }

        }

        return sum / count;

    }

    public static double mean(IImageData data) {
        ValueIterator iter = data.valueIterator();

        double sum = 0;

        while (iter.hasNext()) {
            sum = sum + iter.next();
        }

        return sum / iter.index();


    }


    public static IImageData3D negate(final IImageData3D data) {
        return createScaledData(data, -1);

    }

    public static IImageData3D createScaledData(final IImageData3D data, final double scaleFactor) {
        return new IImageData3D() {
            public IImageSpace3D getImageSpace() {
                return data.getImageSpace();
            }

            public final int indexOf(int x, int y, int z) {
                return data.indexOf(x, y, z);
            }

            public Index3D indexToGrid(int idx) {
                return data.indexToGrid(idx);
            }


            public Anatomy3D getAnatomy() {
                return data.getAnatomy();
            }

            public DataType getDataType() {
                return data.getDataType();
            }

            public int getDimension(Axis axisNum) {
                return data.getDimension(axisNum);
            }

            @Override
            public Dimension3D<Integer> dim() {
                return data.dim();
            }

            public ImageInfo getImageInfo() {
                return data.getImageInfo();
            }

            public String getImageLabel() {
                return data.getImageLabel();
            }

            public ImageBuffer3D createBuffer(boolean clear) {
                return data.createBuffer(clear);
            }


            public IDataGrid3D subGrid(int x0, int x1, int y0, int y1, int z0, int z1) {
                throw new UnsupportedOperationException();
                //return data.subGrid(x0, x1, y0, y1, z0, z1);
            }

            @Override
            public ValueIterator valueIterator() {
                return new ImageValueIterator(this);

            }

            public double maxValue() {
                return data.maxValue() * scaleFactor;
            }

            public double minValue() {
                return data.minValue() * scaleFactor;
            }


            public final double value(int index) {
                return data.value(index) * scaleFactor;
            }

            public int length() {
                return data.length();
            }

            public final double value(float x, float y, float z, InterpolationFunction3D interp) {
                return data.value(x, y, z, interp) * scaleFactor;
            }

            public final double value(int x, int y, int z) {
                return data.value(x, y, z) * scaleFactor;
            }

            public final double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
                return data.worldValue(realx, realy, realz, interp) * scaleFactor;
            }

            public String toString() {
                return data.toString();
            }

            public boolean equals(Object obj) {
                //todo how to check for equality of "scaleFactor"?
                return data.equals(obj);


            }
        };
    }


    public static IImageData3D createConstantData(final double value, final IImageSpace3D space) {
        return new AbstractImageData3D(space, DataType.DOUBLE) {
            public double value(int index) {
                return value;
            }



            public double minValue() {
                return value;
            }

            public double maxValue() {
                return value;
            }

            @Override
            public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
                //todo check bounds
                return value;
            }

            @Override
            public double value(float x, float y, float z, InterpolationFunction3D interp) {
                //todo check bounds
                return value;
            }

            @Override
            public double value(int x, int y, int z) {
                //todo check bounds
                return value;
            }

            @Override
            public ValueIterator valueIterator() {
                return new ConstantValueIterator(value, space.getNumSamples());
                
            }

            public ImageBuffer3D createBuffer(boolean clear) {
                throw new UnsupportedOperationException("Cannot create writer for class " + getClass());
            }

            public String getImageLabel() {
                return "constant: " + value;
            }


        };

    }

    










}
