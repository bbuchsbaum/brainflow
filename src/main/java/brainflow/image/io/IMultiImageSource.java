package brainflow.image.io;

import brainflow.image.data.IImageData;
import brainflow.utils.ProgressListener;
import brainflow.core.BrainFlowException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 2, 2008
 * Time: 2:03:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMultiImageSource extends IImageSource {


    public IMultiImageSource next();

    public IMultiImageSource previous();

    public IImageData load(int index, ProgressListener plistener) throws BrainFlowException;

    public IImageData load(int index) throws BrainFlowException;

    public IImageSource getDataSource(int index);

    public int size();

    




}
