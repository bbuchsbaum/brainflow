package brainflow.image.io;

import brainflow.image.io.ImageInfo;
import brainflow.image.io.ImageInfoReader;
import brainflow.image.io.IImageDataSource;
import brainflow.image.io.ImageIODescriptor;
import brainflow.core.BrainFlowException;
import org.apache.commons.vfs.FileObject;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 26, 2008
 * Time: 4:53:10 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageDataSource implements IImageDataSource {


    private static final BufferedImage BLANK = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);

    private IImageFileDescriptor descriptor;

    private FileObject header;

    private FileObject dataFile;

    private BufferedImage previewImage = BLANK;

    private List<ImageInfo> imageInfoList;

    private int index = 0;


  
    public AbstractImageDataSource(IImageFileDescriptor _descriptor, ImageInfo _info) {
        imageInfoList = Arrays.asList(_info);

        descriptor = _descriptor;

        //todo ensure ImageInfo matches descriptor
        dataFile = getImageInfo().getDataFile();
        header = getImageInfo().getHeaderFile();

    }


    public AbstractImageDataSource(IImageFileDescriptor _descriptor, List<ImageInfo> infoList, int _index) {
        imageInfoList = infoList;
        index = _index;
        descriptor = _descriptor;

        //todo ensure ImageInfo matches descriptor
        dataFile = getImageInfo().getDataFile();
        header = getImageInfo().getHeaderFile();

    }


    public AbstractImageDataSource(IImageFileDescriptor _descriptor, FileObject _header, FileObject _data) {
        descriptor = _descriptor;
        dataFile = _data;
        header = _header;
    }

    public AbstractImageDataSource(IImageFileDescriptor _descriptor, FileObject _header) {
        //todo why have this?
        descriptor = _descriptor;
        header = _header;
    }

    public List<ImageInfo> getImageInfoList() {
        if (imageInfoList == null) fetchImageInfo();
        return imageInfoList;
    }

    public ImageInfo getImageInfo() {
        if (imageInfoList == null) fetchImageInfo();
        return imageInfoList.get(getImageIndex());

    }

    protected void fetchImageInfo() {

        try {

            getDescriptor().createInfoReader(header, dataFile);
            //ImageInfoReader reader = ((ImageInfoReader) getDescriptor().getHeaderReader().newInstance()).create(header, dataFile);
            //imageInfoList = reader.readInfo();
            imageInfoList = getDescriptor().createInfoReader(header, dataFile).readInfo();

        } catch (BrainFlowException e) {
            throw new RuntimeException(e);
        } catch(Throwable t) {
            Logger.getAnonymousLogger().severe("failed to read image info for : " + getHeaderFile());
            throw new RuntimeException(t);
        }

    }


    public int getImageIndex() {
        return index;
    }

    public FileObject getDataFile() {
        return dataFile;
    }

    public String getFileFormat() {
        return descriptor.getFileFormat();
    }

    public FileObject getHeaderFile() {
        return header;
    }

    public IImageFileDescriptor getDescriptor() {
        return descriptor;
    }

    public BufferedImage getPreview() {
        return previewImage;
    }

    public String getStem() {
        return descriptor.stripExtension(header.getName().getBaseName());
    }

    public int getUniqueID() {
        return hashCode();
    }

    public String toString() {
        return header.getName().getBaseName();

    }



    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IImageDataSource that = (IImageDataSource) o;

        if (!dataFile.getName().getPath().equals(that.getDataFile().getName().getPath())) return false;
        if (!header.getName().getPath().equals(that.getHeaderFile().getName().getPath())) return false;
        if (!getFileFormat().equals(that.getFileFormat())) return false;
        if (!getImageInfo().getImageLabel().equals(that.getImageInfo().getImageLabel())) return false;

        return true;
    }


    public int hashCode() {
        int result;
        result = header.getName().getPath().hashCode();
        result = 31 * result + dataFile.getName().getPath().hashCode();
        result = 17 * result + getFileFormat().hashCode();
        result = 9 * result + getImageInfo().getImageLabel().hashCode();
        return result;
    }


}
