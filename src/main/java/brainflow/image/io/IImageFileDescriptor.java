package brainflow.image.io;

import brainflow.utils.FileObjectFilter;
import org.apache.commons.vfs.FileObject;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 5, 2010
 * Time: 9:27:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageFileDescriptor {

    public BinaryEncoding getHeaderEncoding();

    public BinaryEncoding getDataEncoding();

    public boolean isMatch(String headerFileName, String dataFileName);

    public boolean isHeaderMatch(String headerFileName);

    public boolean isDataMatch(String dataFileName);

    public String stripExtension(String fullName);

    public String getHeaderExtension();

    public String getDataExtension();

    public String getFileFormat();

    public FileObject resolveDataFileObject(FileObject header) throws IOException;

    public FileObject resolveHeaderFileObject(FileObject dataFile) throws IOException;
      
    public FileObjectFilter createDataFileFilter();

    public FileObjectFilter createHeaderFileFilter();

    public IImageDataSource createDataSource(FileObject headerFile, FileObject dataFile);

    public IImageDataSource createDataSource(FileObject headerFile);

    public ImageInfoReader createInfoReader(FileObject headerFile, FileObject dataFile);


}
