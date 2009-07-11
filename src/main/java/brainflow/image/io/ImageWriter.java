package brainflow.image.io;

import brainflow.image.data.IImageData;
import brainflow.core.BrainFlowException;
import brainflow.utils.ProgressListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 4, 2009
 * Time: 2:03:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageWriter<T extends ImageInfo> {

    public void writeImage(T info, IImageData data) throws BrainFlowException;


}
