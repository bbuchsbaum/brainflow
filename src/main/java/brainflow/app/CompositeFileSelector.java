package brainflow.app;

import brainflow.app.ImageIODescriptor;
import org.apache.commons.vfs.FileSelectInfo;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileType;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 3:25:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompositeFileSelector implements FileSelector {

    ImageIODescriptor[] desc;

    public CompositeFileSelector(ImageIODescriptor[] _desc) {
        desc = _desc;
    }

    /**
     * Determines if a file or folder should be selected.
     */

    public boolean includeFile(final FileSelectInfo fileInfo) throws FileSystemException {
        if (fileInfo.getDepth() == 0)
            return false;
        if (fileInfo.getFile().getType() == FileType.FOLDER) {
            return true;
        }
        else {
            for (int i=0; i<desc.length; i++) {
                if (desc[i].isHeaderMatch(fileInfo.getFile())) {
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
        if (fileInfo.getDepth() == 0) {
            return true;
        } else
            return false;
    }
}
