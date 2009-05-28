package brainflow.image.io;

import brainflow.core.BrainFlowException;
import brainflow.image.data.IImageData;
import brainflow.utils.ProgressListener;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 11:07:34 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageReader {


    public IImageData readImage() throws BrainFlowException;

    public IImageData readImage(ProgressListener plistener) throws BrainFlowException;

    public ImageInfo getImageInfo();
}
