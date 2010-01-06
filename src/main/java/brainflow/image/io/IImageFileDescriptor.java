package brainflow.image.io;

import brainflow.utils.FileObjectFilter;
import org.apache.commons.vfs.FileObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 5, 2010
 * Time: 9:27:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageFileDescriptor {

    public boolean isMatch(String headerFileName, String dataFileName);

    public boolean isHeaderMatch(String headerFileName);

    public boolean isDataMatch(String dataFileName);

    public String stripExtension(String fullName);

    public String getHeaderExtension();

    public String getDataExtension();

    public String getFileFormat();
      
    public FileObjectFilter createDataFileFilter();

    public FileObjectFilter createHeaderFileFilter();

    public IImageDataSource createDataSource(FileObject headerFile, FileObject dataFile);

    public ImageInfoReader createInfoReader(FileObject headerFile, FileObject dataFile);


}
