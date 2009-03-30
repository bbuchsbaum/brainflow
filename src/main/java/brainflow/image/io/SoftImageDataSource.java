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


    public SoftImageDataSource(ImageIODescriptor _descriptor, ImageInfo _info) {
        super(_descriptor, _info);
    }

    public SoftImageDataSource(ImageIODescriptor _descriptor, FileObject _header, FileObject _data) {
        super(_descriptor, _header, _data);
    }

    public SoftImageDataSource(ImageIODescriptor _descriptor, FileObject _header) {
        super(_descriptor, _header);

    }

    public SoftImageDataSource(ImageIODescriptor _descriptor, List<ImageInfo> infoList, int _index) {
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
        try {


            ImageInfo imageInfo = getImageInfoList().get(getImageIndex());

            if (imageInfo.getDataFile() == null) {
                //todo hack alert
                imageInfo.setDataFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) getDescriptor().getDataReader().newInstance();

            IImageData data = ireader.readImage(imageInfo, plistener);
            //data.setImageLabel(getStem());
            dataRef = new SoftReference(data);
        } catch (IllegalAccessException e) {
            log.warning("Error caught in DataBufferSupport.load()");
            throw new BrainFlowException(e);
        } catch (InstantiationException e) {
            log.warning("Error caught in DataBufferSupport.load()");
            throw new BrainFlowException(e);
        }

        return dataRef.get();

    }


    public IImageData load() throws BrainFlowException {
        try {

            ImageInfo imageInfo = getImageInfo();
            
            if (imageInfo.getDataFile() == null) {
                imageInfo.setDataFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) getDescriptor().getDataReader().newInstance();
            IImageData data = ireader.readImage(imageInfo, new ProgressAdapter() {
                public void setString(String message) {
                    log.info(message);
                }
            });
            //data.setImageLabel(getStem());
            dataRef = new SoftReference(data);

        } catch (IllegalAccessException e) {
            log.warning("Error caught in DataBufferSupport.load()");
            throw new BrainFlowException(e);
        } catch (InstantiationException e) {
            log.warning("Error caught in DataBufferSupport.load()");
            throw new BrainFlowException(e);
        }

        return (IImageData) dataRef.get();
    }

    public int getUniqueID() {
        return hashCode();
    }



    


}
