package brainflow.image.io;

import brainflow.image.space.IImageSpace;
import brainflow.utils.DataType;
import org.apache.commons.vfs.FileObject;

import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 4, 2009
 * Time: 11:32:06 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract  class AbstractImageReader implements ImageReader {

    private static final Logger log = Logger.getLogger(AbstractImageReader.class.getCanonicalName());

    private static final int NUM_CHUNKS = 20;

    private ImageInfo info;


    private DataType datatype;

    private IImageSpace imageSpace;

    private FileObject inputFile;

    private ByteOrder byteOrder = java.nio.ByteOrder.BIG_ENDIAN;

    private int byteOffset = 0;

    public AbstractImageReader(ImageInfo info) {
        setImageInfo(info);
    }

    private void setImageInfo(ImageInfo _info) {
        info = _info;

        byteOrder = info.getEndian();
        byteOffset = info.getDataOffset(info.getImageIndex());
        //fileDimensionality = info.getDimensionality();
        datatype = info.getDataType();
        imageSpace = info.createImageSpace();
        inputFile = info.getDataFile();
    }



    public ImageInfo getImageInfo() {
        return info;
    }

    public abstract int getDimensionality();

   

    public DataType getDatatype() {
        return datatype;
    }

    public IImageSpace getImageSpace() {
        return imageSpace;
    }

    public FileObject getInputFile() {
        return inputFile;
    }

    public ByteOrder getByteOrder() {
        return byteOrder;
    }

    public int getByteOffset() {
        return byteOffset;
    }
}
