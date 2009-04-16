package brainflow.image.io;

import brainflow.image.data.IImageData;
import brainflow.image.io.ImageInfo;
import brainflow.image.io.ImageReader;
import brainflow.utils.ProgressListener;
import brainflow.utils.ProgressAdapter;
import brainflow.image.io.AbstractImageDataSource;
import brainflow.app.ImageIODescriptor;
import brainflow.app.BrainFlowException;
import org.apache.commons.vfs.FileObject;

import java.util.logging.Logger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 13, 2007
 * Time: 4:47:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageDataSource extends AbstractImageDataSource {

    final static Logger log = Logger.getLogger(ImageDataSource.class.getCanonicalName());


    private IImageData dataRef = null;

    private long lastRead = -1;


    public ImageDataSource(ImageIODescriptor _descriptor, ImageInfo _info) {
        super(_descriptor, _info);
    }

    public ImageDataSource(ImageIODescriptor _descriptor, FileObject _header, FileObject _data) {
        super(_descriptor, _header, _data);
    }

    public ImageDataSource(ImageIODescriptor _descriptor, FileObject _header) {
        super(_descriptor, _header);
    }

    public ImageDataSource(ImageIODescriptor _descriptor, List<ImageInfo> infoList, int _index) {
        super(_descriptor, infoList, _index);
    }


    public IImageData getData() {
        if (!isLoaded()) try {
            load();
        } catch (BrainFlowException e) {
            throw new RuntimeException(e);
        }

        return dataRef;
    }

    @Override
    public long whenRead() {
        return lastRead;
    }

    public boolean isLoaded() {
        return dataRef != null;
    }

    public IImageData load() throws BrainFlowException {
        try {

            ImageInfo imageInfo = getImageInfoList().get(getImageIndex());
            assert imageInfo.getDataFile() != null;
            //if (imageInfo.getDataFile() == null) {
            //todo this is truly horrid
            //imageInfo.setDataFile(getDataFile());
            //}

            ImageReader ireader = (ImageReader) getDescriptor().getDataReader().newInstance();
            IImageData data = ireader.readImage(imageInfo, new ProgressAdapter());
            lastRead = System.currentTimeMillis();

            //data.setImageLabel(getStem());
            dataRef = data;

        } catch (IllegalAccessException e) {
            log.warning("Error caught in DataBufferSupport.load()");
            throw new BrainFlowException(e);
        } catch (InstantiationException e) {
            log.warning("Error caught in DataBufferSupport.load()");
            throw new BrainFlowException(e);
        }

        return dataRef;
    }

    public IImageData load(ProgressListener plistener) throws BrainFlowException {
        try {

            ImageInfo imageInfo = getImageInfoList().get(getImageIndex());

            if (imageInfo.getDataFile() == null) {
                imageInfo.setDataFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) getDescriptor().getDataReader().newInstance();

            IImageData data = ireader.readImage(imageInfo, plistener);
            lastRead = System.currentTimeMillis();
            //data.setImageLabel(getStem());
            dataRef = data;
        } catch (IllegalAccessException e) {
            log.warning("Error caught in DataBufferSupport.load()");
            throw new BrainFlowException(e);
        } catch (InstantiationException e) {
            log.warning("Error caught in DataBufferSupport.load()");
            throw new BrainFlowException(e);
        }

        return dataRef;

    }

    public void releaseData() {
        dataRef = null;
    }
}
