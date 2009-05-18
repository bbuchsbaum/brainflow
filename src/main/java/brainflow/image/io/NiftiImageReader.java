package brainflow.image.io;

import brainflow.image.data.IImageData;
import brainflow.app.BrainFlowException;
import brainflow.utils.ProgressListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2009
 * Time: 5:06:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiftiImageReader implements ImageReader {

    private NiftiImageInfo info;

    private BasicImageReader reader;

    public NiftiImageReader(NiftiImageInfo info) {
        this.info = info;
        reader = new BasicImageReader(info);
    }

    @Override
    public IImageData readImage() throws BrainFlowException {
        return reader.readImage();
    }

    @Override
    public IImageData readImage(ProgressListener plistener) throws BrainFlowException {
        return reader.readImage(plistener);
    }

    public NiftiImageInfo getImageInfo() {
        return info;
    }
}
