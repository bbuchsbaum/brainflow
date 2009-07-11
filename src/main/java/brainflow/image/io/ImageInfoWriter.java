/*
 * ImageInfoWriter.java
 *
 * Created on March 21, 2003, 1:27 PM
 */

package brainflow.image.io;

import brainflow.core.BrainFlowException;

import java.io.File;

import org.apache.commons.vfs.FileObject;

/**
 * @author Bradley
 */
public interface ImageInfoWriter<T extends ImageInfo> {

    /**
     * Creates a new instance of ImageInfoWriter
     */
    public long writeInfo(T info) throws BrainFlowException;

}
