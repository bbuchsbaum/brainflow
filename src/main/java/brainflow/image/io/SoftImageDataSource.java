package brainflow.image.io;

import brainflow.image.data.IImageData;
import brainflow.image.io.ImageInfo;
import brainflow.image.io.ImageReader;
import brainflow.utils.ProgressListener;
import brainflow.utils.ProgressAdapter;
import brainflow.image.io.AbstractImageDataSource;
import brainflow.image.io.ImageIODescriptor;
import brainflow.core.BrainFlowException;
import org.apache.commons.vfs.FileObject;

import java.lang.ref.SoftReference;
import java.util.logging.Logger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 1:32:12 PM
 * To change this template use File | Settings | File Templates.
 */


public class SoftImageDataSource extends AbstractImageDataSource {

    final static Logger log = Logger.getLogger(SoftImageDataSource.class.getCanonicalName());


    private SoftReference<IImageData> dataRef = new SoftReference<IImageData>(null);

    private long lastRead = -1;


    public SoftImageDataSource(IImageFileDescriptor _descriptor, ImageInfo _info) {
        super(_descriptor, _info);
    }

    public SoftImageDataSource(IImageFileDescriptor _descriptor, FileObject _header, FileObject _data) {
        super(_descriptor, _header, _data);
    }

    public SoftImageDataSource(IImageFileDescriptor _descriptor, FileObject _header) {
        super(_descriptor, _header);

    }

    public SoftImageDataSource(IImageFileDescriptor _descriptor, List<ImageInfo> infoList, int _index) {
        super(_descriptor, infoList, _index);
    }

    public void releaseData() {
        dataRef.enqueue();
    }

    public boolean isLoaded() {
        if (dataRef.get() == null) {
            return false;
        }

        return true;
    }

    @Override
    public long whenRead() {
        return lastRead;
    }

    //duplicate code (with ImageDataSource)


    public IImageData getData() {
        if (dataRef.get() == null) {
            try {
                load();
            } catch (BrainFlowException e) {
                log.severe("failed to load " + getDataFile().getName().getPath());
                throw new RuntimeException(e);
            }
        }

        return dataRef.get();
    }


    public IImageData load(ProgressListener plistener) throws BrainFlowException {

        ImageInfo imageInfo = getImageInfoList().get(getImageIndex());
        ImageReader ireader = imageInfo.createImageReader();

        IImageData data = ireader.readImage(plistener);
        lastRead = System.currentTimeMillis();
        dataRef = new SoftReference<IImageData>(data);


        return data;

    }


    public IImageData load() throws BrainFlowException {
       return load(new ProgressAdapter());
    }

    public int getUniqueID() {
        return hashCode();
    }


}
