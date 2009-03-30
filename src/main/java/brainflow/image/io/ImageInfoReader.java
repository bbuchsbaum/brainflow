package brainflow.image.io;

import brainflow.app.BrainFlowException;
import org.apache.commons.vfs.FileObject;

import java.io.File;
import java.util.List;
import java.net.URL;

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

    //static <A extends Comparable<A>> A max

    public List<ImageInfo> readInfo(File f) throws BrainFlowException;

    public List<ImageInfo> readInfo(FileObject fobj) throws BrainFlowException;

    public List<ImageInfo> readInfo(URL url) throws BrainFlowException;



}