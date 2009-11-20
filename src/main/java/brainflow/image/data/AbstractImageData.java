package brainflow.image.data;

import brainflow.image.anatomy.Anatomy;
import brainflow.image.io.ImageInfo;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.image.iterators.ValueIterator;
import brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 2:28:17 PM
 * To change this template use File | Settings | File Templates.
 */


public abstract class AbstractImageData implements IImageData {


    protected final IImageSpace space;

    protected final DataType dataType;

    private final String imageLabel;

    private ImageInfo info;

    private boolean maxComputed = false;

    private boolean minComputed = false;

    private double maxValue;

    private double minValue;

    public AbstractImageData(IImageSpace space) {
        this.space = space;
        dataType = DataType.DOUBLE;
        this.imageLabel = "";
    }

    public AbstractImageData(IImageSpace space, DataType dtype) {
        this.space = space;
        this.dataType = dtype;
        this.imageLabel = "";
    }

    public AbstractImageData(IImageSpace space, DataType dtype, String imageLabel) {
        this.space = space;
        this.dataType = dtype;
        this.imageLabel = imageLabel;
    }

    //public IDimension<Integer> dim() {
    //    return space.getDimension();
    //}

    public DataType getDataType() {
        return dataType;
    }

    public Anatomy getAnatomy() {
        return space.getAnatomy();
    }

    public int getDimension(Axis axis) {
        return space.getDimension(axis);
    }

    public int length() {
        return space.getNumSamples();
    }

    public ImageInfo getImageInfo() {
        if (info == null) {
            info = new ImageInfo(this);
        }

        return info;
    }

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
        ValueIterator iter = this.valueIterator();

        double _min = Double.MAX_VALUE;
        while (iter.hasNext()) {
            double val = iter.next();
            if (val < _min) {
                _min = val;

            }
        }

        minComputed = true;
        return _min;

    }


    protected double computeMax() {
        ValueIterator iter = this.valueIterator();

        double _max = -Double.MAX_VALUE;
        while (iter.hasNext()) {
            double val = iter.next();
            if (val > _max) {
                _max = val;

            }
        }

        maxComputed = true;
        return _max;

    }


    public String getImageLabel() {
        return imageLabel;
    }


}
