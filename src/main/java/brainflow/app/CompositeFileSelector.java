package brainflow.app;

import brainflow.image.io.IImageFileDescriptor;
import org.apache.commons.vfs.FileSelectInfo;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 3:25:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompositeFileSelector implements FileSelector {

    private List<IImageFileDescriptor> descriptors;

    public CompositeFileSelector(List<IImageFileDescriptor> _desc) {
        descriptors = _desc;
    }

    /**
     * Determines if a file or folder should be selected.
     */

    public boolean includeFile(final FileSelectInfo fileInfo) throws FileSystemException {
        if (fileInfo.getDepth() == 0) {            
            return false;
        }
        if (fileInfo.getFile().getType() == FileType.FOLDER) {
            return true;
        } else {

            for (IImageFileDescriptor desc : descriptors) {
                if (desc.isHeaderMatch(fileInfo.getFile().getName().getBaseName())) {
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * Determines whether a folder should be traversed.
     */
    public boolean traverseDescendents(final FileSelectInfo fileInfo) {
        return (fileInfo.getDepth() == 0);

    }
}
