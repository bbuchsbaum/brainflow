package brainflow.image.data;

import brainflow.array.ArrayValueIterator;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.interpolation.InterpolationFunction4D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.math.Index3D;
import brainflow.math.Index4D;
import brainflow.utils.DataType;
import brainflow.utils.Dimension3D;
import brainflow.utils.Dimension4D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 11, 2010
 * Time: 9:20:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicImageDataVector3D implements IImageDataVector3D {

    private IImageData3D[] dataVec;

    private Dimension4D<Integer> dim;

    private int numSamples;

    public BasicImageDataVector3D(IImageData3D[] dataVec) {
        if (dataVec == null || dataVec.length == 0) {
            throw new IllegalArgumentException("dataVec must have length > 0");
        }

        validate(dataVec);
        this.dataVec = dataVec;
        numSamples = dataVec[0].getImageSpace().getNumSamples();

        Dimension3D<Integer> vdim = dataVec[0].getImageSpace().getDimension();
        dim = new Dimension4D<Integer>(vdim.getDim(0), vdim.getDim(1), vdim.getDim(2), dataVec.length);
    }

    private void validate(IImageData3D[] dataVec) {
        IImageSpace3D space = dataVec[0].getImageSpace();
        DataType dtype = dataVec[0].getDataType();

        for (int i = 0; i < dataVec.length; i++) {
            if (!dataVec[i].getImageSpace().equals(space)) {
                throw new IllegalArgumentException("all elements of dataVec must have equal IImageSpace3D members: element #" + i + "not equal element #0");
            }
            if (dataVec[i].getDataType() != dtype) {
                throw new IllegalArgumentException("all elements of dataVec must have same data type: elements #" + i + " is " + dataVec[i].getDataType() + " while element #0 is " + dtype);
            }
        }

    }

    @Override
    public double worldValue(float wx, float wy, float wz, int wt, InterpolationFunction3D interp) {
        return dataVec[wt].worldValue(wx, wy, wz, interp);
    }

    @Override
    public Index3D indexToVolumeGrid(int idx) {
        return dataVec[0].indexToGrid(idx);

    }

    @Override
    public Index4D indexToGrid(int idx) {
        Index3D vox = dataVec[0].indexToGrid(idx % numSamples);
        return new Index4D(vox.i1(), vox.i2(), vox.i3(), idx/dataVec.length);
    }


    @Override
    public IImageSpace3D getImageSpace() {
        return dataVec[0].getImageSpace();
    }

    @Override
    public int getNumVolumes() {
        return dataVec.length;
    }

    @Override
    public Anatomy3D getAnatomy() {
        return dataVec[0].getAnatomy();
    }

    @Override
    public double value(double x, double y, double z, double v, InterpolationFunction4D interp) {
        return interp.interpolate(x,y,z,v,this);
    }

    @Override
    public double value(int i, int j, int k, int m) {
        return dataVec[m].value(i, j, k);
    }

    @Override
    public double value(int spatialIndex, int vectorIndex) {
        return dataVec[vectorIndex].value(spatialIndex);
    }

    @Override
    public int indexOf(int i, int j, int k, int m) {
        return dataVec[0].indexOf(i, j, k) + numSamples * m;
    }

    @Override
    public DataType getDataType() {
        return dataVec[0].getDataType();
    }

    @Override
    public int getDimension(Axis axisNum) {
        return dataVec[0].getDimension(axisNum);
    }

    @Override
    public Dimension4D<Integer> dim() {
        return dim;
    }


    @Override
    public ImageInfo getImageInfo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getImageLabel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueIterator valueIterator() {
        return new ArrayValueIterator(this);
    }

    @Override
    public double value(int i) {
        int volnum = i / numSamples;

        return dataVec[volnum].value(i % numSamples);
    }

    @Override
    public int length() {
        return numSamples * dataVec.length;
    }

    @Override
    public IImageData3D getVolume(int i) {
        return dataVec[i];
    }

    private boolean maxComputed;
    private boolean minComputed;

    private double maxValue;
    private double minValue;

    public double maxValue() {
        if (maxComputed) return maxValue;
        maxValue = computeMax();
        return maxValue;

    }

    public double minValue() {
        if (minComputed) return minValue;
        minValue = computeMin();
        return minValue;
    }


    protected double computeMin() {


        double _min = Double.MAX_VALUE;
        for (IImageData3D dat : dataVec) {
            double val = dat.minValue();
            if (val < _min) {
                _min = val;

            }
        }

        minComputed = true;
        return _min;

    }


    protected double computeMax() {
        double _max = Double.MIN_VALUE;
        for (IImageData3D dat : dataVec) {
            double val = dat.maxValue();
            if (val > _max) {
                _max = val;

            }
        }

        maxComputed = true;
        return _max;


    }

}
