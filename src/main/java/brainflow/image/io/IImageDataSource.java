package brainflow.image.io;

import brainflow.image.data.IImageData;
import brainflow.image.io.ImageInfo;
import brainflow.utils.ProgressListener;
import brainflow.application.ImageIODescriptor;
import brainflow.application.BrainFlowException;
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
public interface IImageDataSource {

    // todo a single file can give rise to an array of ImageInfos which leads to an array of DataSources.
    // read header in descriptor?
    // this yields dimensionality
    

    public int getImageIndex();
    
    public boolean isLoaded();

    public ImageIODescriptor getDescriptor();

    public String getStem();

    public FileObject getDataFile();

    public FileObject getHeaderFile();

    public String getFileFormat();

    public ImageInfo getImageInfo();

    public List<ImageInfo> getImageInfoList();

    public BufferedImage getPreview();

    public IImageData getData();

    public void releaseData();

    public IImageData load(ProgressListener plistener) throws BrainFlowException;

    public IImageData load() throws BrainFlowException;


    public int getUniqueID();

}
