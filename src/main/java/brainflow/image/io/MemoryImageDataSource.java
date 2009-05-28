package brainflow.image.io;

import brainflow.image.data.IImageData;
import brainflow.image.io.ImageInfo;
import brainflow.image.io.IImageDataSource;
import brainflow.utils.ProgressListener;
import brainflow.core.BrainFlowException;
import brainflow.image.io.ImageIODescriptor;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileSystemException;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2005
 * Time: 2:04:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemoryImageDataSource implements IImageDataSource {


    private IImageData data;

    private FileObject ramFile;

    public MemoryImageDataSource(IImageData _data) {
        data = _data;
    }

    public List<ImageInfo> getImageInfoList() {
        return Arrays.asList(data.getImageInfo());
    }

    public ImageInfo getImageInfo() {
        return data.getImageInfo();
    }

    public boolean isLoaded() {
        return true;
    }

    @Override
    public long whenRead() {
        //read time is unknown, so return -1
        return -1;
    }

    public int getImageIndex() {
        return 0;  
    }

    public String getStem() {
        return data.getImageLabel();
    }

    public BufferedImage getPreview() {
        throw new UnsupportedOperationException();
    }

    public ImageIODescriptor getDescriptor() {
        throw new UnsupportedOperationException("MemoryImage does not have associated descriptor object");
    }

    public FileObject getDataFile() {
        try {
            return VFS.getManager().resolveFile("ram://" + getStem());

        } catch(FileSystemException e) {
            throw new RuntimeException(e);
        }

    }

    public FileObject getHeaderFile() {
        try {
            return VFS.getManager().resolveFile("ram://" + getStem());

        } catch(FileSystemException e) {
            throw new RuntimeException(e);
        }

    }

    public String getFileFormat() {
        return "MEMORY";
    }

    public IImageData getData() {
        return data;
    }

    public IImageData load(ProgressListener plistener) throws BrainFlowException {
        plistener.finished();
        return data;
    }

    public IImageData load() throws BrainFlowException {
        return data;
    }

    public int getUniqueID() {
        return hashCode();
    }

    public void releaseData() {
        // no op
    }

    public static void main(String[] args) {
        try {
            FileObject ramobj = VFS.getManager().resolveFile("ram://garbage" );
            System.out.println(" ramobj " + ramobj);
        } catch(FileSystemException e) {
            e.printStackTrace();
        }
    }


}
