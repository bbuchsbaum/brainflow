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
public interface IMultiImageDataSource extends IImageDataSource {


    public IMultiImageDataSource next();

    public IMultiImageDataSource previous();

    public IImageData load(int index, ProgressListener plistener) throws BrainFlowException;

    public IImageData load(int index) throws BrainFlowException;

    public IImageDataSource getDataSource(int index);

    public int size();

    




}
