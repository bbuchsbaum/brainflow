package brainflow.image.io;

import brainflow.image.data.IImageData;
import brainflow.image.io.ImageInfo;
import brainflow.utils.ProgressListener;
import brainflow.image.io.ImageIODescriptor;
import brainflow.core.BrainFlowException;
import org.apache.commons.vfs.FileObject;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: Nov 24, 2004
 * Time: 2:49:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageSource<T extends IImageData> {

    public int getImageIndex();
    
    public boolean isLoaded();

    public long whenRead();

    public IImageFileDescriptor getDescriptor();

    public String getStem();

    public FileObject getDataFile();
      
    public FileObject getHeaderFile();

    public String getFileFormat();

    public ImageInfo getImageInfo();

    public List<ImageInfo> getImageInfoList();

    public BufferedImage getPreview();

    public T getData();

    public void releaseData();

    public T load(ProgressListener plistener) throws BrainFlowException;

    public T load() throws BrainFlowException;

    public int getUniqueID();

}
