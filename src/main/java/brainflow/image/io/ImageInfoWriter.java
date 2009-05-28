/*
 * ImageInfoWriter.java
 *
 * Created on March 21, 2003, 1:27 PM
 */

package brainflow.image.io;

import brainflow.core.BrainFlowException;

import java.io.File;

/**
 * @author Bradley
 */
public interface ImageInfoWriter {

    /**
     * Creates a new instance of ImageInfoWriter
     */
    public void writeInfo(File file, ImageInfo info) throws BrainFlowException;

}
