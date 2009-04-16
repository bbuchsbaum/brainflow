package brainflow.app;

import brainflow.image.data.IImageData;
import brainflow.image.io.ImageInfo;
import brainflow.image.io.IImageDataSource;
import brainflow.utils.ProgressListener;
import org.apache.commons.vfs.FileObject;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2005
 * Time: 2:04:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemoryImageDataSource implements IImageDataSource {


    private IImageData data;

    public MemoryImageDataSource(IImageData _data) {
        data = _data;
    }

    public List<ImageInfo> getImageInfoList() {
        return Arrays.asList(data.getImageInfo());
    }

    public ImageInfo getImageInfo() {
        return data.getImageInfo();
    }

    public boolean isLoaded() {
        return true;
    }

    @Override
    public long whenRead() {
        //read time is unknown, so return -1
        return -1;
    }

    public int getImageIndex() {
        return 0;  
    }

    public String getStem() {
        return "IMAGE_DATA" + hashCode();
    }

    public BufferedImage getPreview() {
        throw new UnsupportedOperationException();
    }

    public ImageIODescriptor getDescriptor() {
        throw new UnsupportedOperationException("MemoryImage does not have associated descriptor object");
    }

    public FileObject getDataFile() {
        throw new UnsupportedOperationException("MemoryImage does not have associated data file");
    }

    public FileObject getHeaderFile() {
        throw new UnsupportedOperationException("MemoryImage does not have associated header file");
    }

    public String getFileFormat() {
        return "MEMORY";
    }

    public IImageData getData() {
        return data;
    }

    public IImageData load(ProgressListener plistener) throws BrainFlowException {
        plistener.finished();
        return data;
    }

    public IImageData load() throws BrainFlowException {
        return data;
    }

    public int getUniqueID() {
        return hashCode();
    }

    public void releaseData() {
        // no op
    }


}
