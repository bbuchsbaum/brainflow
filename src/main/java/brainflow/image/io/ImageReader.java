package brainflow.image.io;

import brainflow.application.BrainFlowException;
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


    public IImageData readImage(ImageInfo info) throws BrainFlowException;

    public IImageData readImage(ImageInfo info, ProgressListener plistener) throws BrainFlowException;


}
