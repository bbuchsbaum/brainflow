package brainflow.image.data;

import brainflow.image.space.ImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ImageIterator;
import brainflow.utils.DataType;
import brainflow.math.Index3D;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 9, 2008
 * Time: 9:10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageData {


    public static IImageData3D createConstantData(final double value, final IImageSpace space) {
        if (space instanceof ImageSpace3D) {
            return ImageData.createConstantData(value, (ImageSpace3D) space);
        } else {
            throw new IllegalArgumentException("IImageSpace argumnet must be of class ImageSpace3D");
        }
    }

    public static IMaskedData createMask(MaskPredicate pred, IImageData dat) {
        if (dat instanceof IImageData2D) {
            return new MaskedData2D((IImageData2D) dat, pred);
        } else if (dat instanceof IImageData3D) {
            return new MaskedData3D((IImageData3D) dat, pred);
        }

        throw new IllegalArgumentException("could not create mask, wrong data class " + dat.getClass());
    }

    public static double meanDeviation(IImageData data, double referenceVal) {
        ImageIterator iter = data.iterator();

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
        ImageIterator iter = data.iterator();

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
        ImageIterator iter = data.iterator();

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


            public Anatomy getAnatomy() {
                return data.getAnatomy();
            }

            public DataType getDataType() {
                return data.getDataType();
            }

            public int getDimension(Axis axisNum) {
                return data.getDimension(axisNum);
            }

            public ImageInfo getImageInfo() {
                return data.getImageInfo();
            }

            public String getImageLabel() {
                return data.getImageLabel();
            }

            public DataWriter3D createWriter(boolean clear) {
                return data.createWriter(clear);
            }

            public ImageIterator iterator() {
                return new BasicImageData3D.Iterator3D(this);
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

            public int numElements() {
                return data.numElements();
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

            public Index3D indexToGrid(int idx) {
                throw new UnsupportedOperationException();
            }

            public void setValue(int idx, double val) {
                throw new UnsupportedOperationException();
            }

            public double minValue() {
                return value;
            }

            public double maxValue() {
                return value;
            }

            public ImageIterator iterator() {
                return new BasicImageData3D.Iterator3D(this);
            }

            public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
                return value;
            }

            public double value(float x, float y, float z, InterpolationFunction3D interp) {
                return value;
            }

            public double value(int x, int y, int z) {
                return value;
            }

            public void setValue(int x, int y, int z, double val) {
                throw new UnsupportedOperationException();
            }

            public DataWriter3D createWriter(boolean clear) {
                throw new UnsupportedOperationException("Cannot create writer for class " + getClass());
            }

            public String getImageLabel() {
                return "constant: " + value;
            }


        };


    }
}
