package brainflow.image.io;

import brainflow.core.BrainFlowException;
import brainflow.image.data.IImageData;
import brainflow.utils.ProgressListener;
import org.apache.commons.vfs.FileObject;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 15, 2010
 * Time: 9:37:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiImageDataSource implements IMultiImageDataSource {

    private List<IImageDataSource> sourceList;

    private int index = 0;

    public MultiImageDataSource(List<IImageDataSource> sourceList) {
        this.sourceList = sourceList;
    }

    public MultiImageDataSource(List<IImageDataSource> sourceList, int index) {
        if (index < 0 || index >= sourceList.size()) {
            throw new IllegalArgumentException("illegal index " + index);
        }
        this.sourceList = sourceList;
        this.index = index;
    }

    @Override
    public IImageData load(int index, ProgressListener plistener) throws BrainFlowException {
        return sourceList.get(index).load(plistener);
    }

    @Override
    public IImageData load(int index) throws BrainFlowException {
        return sourceList.get(index).load();

    }


    @Override
    public IImageDataSource getDataSource(int index) {
        return sourceList.get(index);
    }

    @Override
    public int size() {
        return sourceList.size();
    }

    @Override
    public int getImageIndex() {
        return index;
    }

    public MultiImageDataSource next() {
        if (index < (sourceList.size() - 1)) {
            return new MultiImageDataSource(sourceList, index + 1);
        } else {
            return new MultiImageDataSource(sourceList, 0);
        }
    }

    public MultiImageDataSource previous() {
        if (index > 0) {
            return new MultiImageDataSource(sourceList, index - 1);
        } else {
            return new MultiImageDataSource(sourceList, sourceList.size() - 1);
        }
    }


    @Override
    public boolean isLoaded() {
        return sourceList.get(index).isLoaded();
    }

    @Override
    public long whenRead() {
        return sourceList.get(index).whenRead();
    }

    @Override
    public IImageFileDescriptor getDescriptor() {
        return sourceList.get(index).getDescriptor();
    }

    @Override
    public String getStem() {
        return sourceList.get(index).getStem();
    }

    @Override
    public FileObject getDataFile() {
        return sourceList.get(index).getDataFile();
    }

    @Override
    public FileObject getHeaderFile() {
        return sourceList.get(index).getHeaderFile();
    }

    @Override
    public String getFileFormat() {
        return sourceList.get(index).getFileFormat();
    }

    @Override
    public ImageInfo getImageInfo() {
        return sourceList.get(index).getImageInfo();
    }

    @Override
    public List<ImageInfo> getImageInfoList() {
        //todo should return the flattened list of ImageInfo
        return sourceList.get(index).getImageInfoList();
    }

    @Override
    public BufferedImage getPreview() {
        throw new UnsupportedOperationException();
    }

    @Override
    public IImageData getData() {
        return sourceList.get(index).getData();
    }

    @Override
    public void releaseData() {
        sourceList.get(index).releaseData();
    }

    @Override
    public IImageData load(ProgressListener plistener) throws BrainFlowException {
        return sourceList.get(index).load(plistener);
    }

    @Override
    public IImageData load() throws BrainFlowException {
        return sourceList.get(index).load();
    }

    @Override
    public int getUniqueID() {
        return sourceList.get(index).getUniqueID();
    }
}
