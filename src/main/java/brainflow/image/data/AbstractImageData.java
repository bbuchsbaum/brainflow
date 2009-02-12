package brainflow.image.data;

import brainflow.image.anatomy.Anatomy;
import brainflow.image.io.ImageInfo;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.utils.DataType;
import brainflow.utils.IDimension;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 2:28:17 PM
 * To change this template use File | Settings | File Templates.
 */


public abstract class AbstractImageData implements IImageData {


    protected final IImageSpace space;

    protected final DataType datatype;

    private final String imageLabel;

    private ImageInfo info = new ImageInfo();


    public AbstractImageData(IImageSpace space) {
        this.space = space;
        datatype = DataType.DOUBLE;
        this.imageLabel = "";
    }

    public AbstractImageData(IImageSpace space, DataType dtype) {
        this.space = space;
        this.datatype = dtype;
        this.imageLabel = "";
    }

    public AbstractImageData(IImageSpace space, DataType dtype, String imageLabel) {
        this.space = space;
        this.datatype = dtype;
        this.imageLabel = imageLabel;
    }

     public IDimension<Integer> getDimension() {
        return space.getDimension();
    }

    public DataType getDataType() {
        return datatype;
    }

    public Anatomy getAnatomy() {
        return space.getAnatomy();
    }

    public int getDimension(Axis axis) {
        return space.getDimension(axis);
    }

    public int numElements() {
        return space.getNumSamples();
    }

    public ImageInfo getImageInfo() {
        return info;
    }

    
    public String getImageLabel() {
        return imageLabel;
    }

    
}
