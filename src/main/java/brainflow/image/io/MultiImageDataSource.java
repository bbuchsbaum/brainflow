package brainflow.image.io;

import brainflow.image.data.IImageData;
import brainflow.utils.ProgressListener;
import brainflow.utils.ProgressAdapter;
import brainflow.application.BrainFlowException;
import brainflow.application.ImageIODescriptor;
import org.apache.commons.vfs.FileObject;

import java.util.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 2, 2008
 * Time: 2:10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiImageDataSource implements IMultiImageDataSource {

    private List<ImageInfo> infoList;

    private ImageIODescriptor descriptor;

    private SortedMap<Integer, IImageDataSource> dataSourceMap = new TreeMap<Integer, IImageDataSource>();

    public MultiImageDataSource(ImageIODescriptor _descriptor, List<ImageInfo> info) {
        infoList = info;
        descriptor = _descriptor;
    }


    public int indexOf(String imageLabel) {
        for (int i = 0; i < infoList.size(); i++) {
            if (infoList.get(i).equals(imageLabel)) {
                return i;
            }

        }

        return -1;
    }

    public IImageData load(int index, ProgressListener plistener) throws BrainFlowException {
        if (index < 0 || index >= infoList.size()) {
            throw new IllegalArgumentException("illegal index " + index + ". max image index: " + infoList.size());
        }

        ImageInfo imageInfo = infoList.get(index);

        IImageDataSource source = new SoftImageDataSource(getDescriptor(), imageInfo);
        dataSourceMap.put(index, source);
        return source.load(plistener);


    }


    public IImageData load(int index) throws BrainFlowException {
        return load(index, new ProgressAdapter());
    }

    public IImageDataSource getDataSource(String label) {
        int i = indexOf(label);
        if (i < 0) throw new IllegalArgumentException("label " + label + " not found.");

        return getDataSource(i);


    }

    public IImageDataSource getDataSource(int index) {
        IImageDataSource source = dataSourceMap.get(index);
        if (source == null) {
            ImageInfo imageInfo = infoList.get(index);
            source = new SoftImageDataSource(getDescriptor(), imageInfo);
            dataSourceMap.put(index, source);
        }

        return source;
    }

    public int size() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getImageIndex() {
        return 0;
    }

    public boolean isLoaded() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ImageIODescriptor getDescriptor() {
        return descriptor;
    }

    public String getStem() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public FileObject getHeaderFile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public FileObject getDataFile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getFileFormat() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ImageInfo getImageInfo() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<ImageInfo> getImageInfoList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IImageData getData() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public BufferedImage getPreview() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void releaseData() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public IImageData load(ProgressListener plistener) throws BrainFlowException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IImageData load() throws BrainFlowException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getUniqueID() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
