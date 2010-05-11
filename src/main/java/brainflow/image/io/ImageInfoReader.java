package brainflow.image.io;

import brainflow.core.BrainFlowException;
import org.apache.commons.vfs.FileObject;

import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public interface ImageInfoReader {


    public ImageInfoReader create(FileObject headerFile, FileObject dataFile);
    
    public List<ImageInfo> readInfoList() throws BrainFlowException;

    public ImageInfo readInfo();


}