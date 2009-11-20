package brainflow.image.data;

import brainflow.array.ArrayValueIterator;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.interpolation.InterpolationFunction4D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.utils.DataType;
import brainflow.utils.Dimension4D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 11, 2009
 * Time: 3:49:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListImageData3D extends ArrayList<IImageData3D> implements IImageData4D {

    private String label = "image_list";


    int volLen;


    public ListImageData3D(Collection<? extends IImageData3D> c) {
        super(c);
    }

    public ListImageData3D(String label, Collection<? extends IImageData3D> c) {
        super(c);
        this.label = label;
    }

    public ListImageData3D(String label, IImageData3D ... c) {
        super(Arrays.asList(c));
        this.label = label;
    }
    

    private void init() {
        IImageSpace3D space = this.get(0).getImageSpace();
        DataType dtype = this.get(0).getDataType();

        for (IImageData3D dat : this) {
            if (!dat.getImageSpace().equals(space)) {
                throw new IllegalArgumentException("all images in ListImageData3D must have equivalent spatial geometries" +
                        " image " + dat.getImageLabel() + " does not match image " + dat.getImageLabel());
            }

            if (dat.getDataType() != dtype) {
                throw new IllegalArgumentException("all images in ListImageData3D must have same data type");
            }
        }

        volLen = space.getNumSamples();
    }

    @Override
    public DataType getDataType() {
        return get(0).getDataType();
    }

    @Override
    public String getImageLabel() {
        return label;
    }

    @Override
    public double value(int i) {
        int volind = i % volLen;
        if (volind > 0) {
            i = i - volind * volLen;
        }

        return get(volind).value(i);
    }

    private double maxval;
    private double minval;
    private boolean maxComputed = false;
    private boolean minComputed = false;

    @Override
    public double maxValue() {
        if (maxComputed) {
            return maxval;
        } else {
            double mv = -Double.MAX_VALUE;
            for (IImageData3D dat : this) {
                mv = Math.max(mv, dat.maxValue());
            }

            maxval=mv;
        }

        return maxval;

    }

    @Override
    public Anatomy3D getAnatomy() {
        return get(0).getAnatomy();
    }

    @Override
    public ImageBuffer4D createBuffer(boolean clear) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(int i, int j, int k, int m) {
        int n = get(0).indexOf(i, j, k);
        return n + m + volLen;

    }

    @Override
    public double value(double x, double y, double z, double t, InterpolationFunction4D interp) {
        return interp.interpolate(x, y, z, t, this);
    }

    @Override
    public double value(int i, int j, int k, int m) {
        return get(m).value(i, j, k);
    }

    @Override
    public IImageSpace3D getImageSpace() {
        return get(0).getImageSpace();
    }

    @Override
    public Dimension4D<Integer> dim() {
        return new Dimension4D<Integer>(get(0).getDimension(Axis.X_AXIS),
                get(0).getDimension(Axis.Y_AXIS),
                get(0).getDimension(Axis.Z_AXIS),
                length());
    }

    @Override
    public ImageInfo getImageInfo() {
        //todo fixme
        return new ImageInfo(this);

    }

    @Override
    public double worldValue(float wx, float wy, float wz, float wt, InterpolationFunction4D interp) {
        return interp.interpolate(wx, wy, wz, wt, this);
    }

    @Override
    public int length() {
        return volLen * size();
    }

    @Override
    public int getDimension(Axis axisNum) {
        return dim().getDim(axisNum.getId());
    }

    @Override
    public ValueIterator valueIterator() {
        return new ArrayValueIterator(this);
    }

    @Override
    public double minValue() {
        if (minComputed) {
            return minval;
        } else {
            double mv = Double.MAX_VALUE;
            for (IImageData3D dat : this) {
                mv = Math.min(mv, dat.minValue());
            }

            minval = mv;
        }


        return minval;


    }
}
