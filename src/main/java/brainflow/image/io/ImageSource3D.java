package brainflow.image.io;

import brainflow.image.data.IImageData3D;
import brainflow.utils.ProgressListener;
import brainflow.utils.ProgressAdapter;
import brainflow.core.BrainFlowException;
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
public class ImageSource3D extends AbstractImageSource<IImageData3D> {

    final static Logger log = Logger.getLogger(ImageSource3D.class.getCanonicalName());

    private IImageData3D dataRef = null;

    private long lastRead = -1;


    public ImageSource3D(IImageFileDescriptor _descriptor, ImageInfo _info) {
        super(_descriptor, _info);
    }

    public ImageSource3D(IImageFileDescriptor _descriptor, FileObject _header, FileObject _data) {
        super(_descriptor, _header, _data);
    }

    public ImageSource3D(IImageFileDescriptor _descriptor, FileObject _header) {
        super(_descriptor, _header);
    }

    public ImageSource3D(IImageFileDescriptor _descriptor, List<ImageInfo> infoList, int _index) {
        super(_descriptor, infoList, _index);
    }


    public IImageData3D getData() {
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

    public IImageData3D load() throws BrainFlowException {
        return load(new ProgressAdapter());

    }

    public IImageData3D load(ProgressListener plistener) throws BrainFlowException {

        ImageInfo imageInfo = getImageInfoList().get(getImageIndex());
        ImageReader ireader = imageInfo.createImageReader();

        IImageData3D data = (IImageData3D)ireader.readImage(plistener);
        lastRead = System.currentTimeMillis();
        dataRef = data;

        return dataRef;

    }

    public void releaseData() {
        dataRef = null;
    }
}
