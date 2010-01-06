package brainflow.image.io;

import brainflow.utils.FileObjectFilter;
import org.apache.commons.vfs.FileObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 5, 2010
 * Time: 9:47:54 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageFileDescriptor implements IImageFileDescriptor {

    private String headerExtension;

    private String dataExtension;

    private String fileFormat;

    protected AbstractImageFileDescriptor(String headerExtension, String dataExtension, String fileFormat) {
        this.headerExtension = headerExtension;
        this.dataExtension = dataExtension;
        this.fileFormat = fileFormat;
    }

    @Override
    public boolean isMatch(String headerFileName, String dataFileName) {
        return isHeaderMatch(headerFileName) && isDataMatch(dataFileName);
    }

    @Override
    public boolean isHeaderMatch(String headerFileName) {
        return headerFileName.endsWith("." + headerExtension);
    }

    @Override
    public boolean isDataMatch(String dataFileName) {
        return dataFileName.endsWith("." + dataExtension);
    }

    

    @Override
    public String stripExtension(String fullName) {
        String ret = fullName;

        if (fullName.endsWith("." + headerExtension)) {
            ret = fullName.substring(0, fullName.length() - headerExtension.length());
        } else if (fullName.endsWith("." + dataExtension)) {
            ret = fullName.substring(0, fullName.length() - dataExtension.length());
        } //else {
          //  throw new IllegalArgumentException("String " + fullName + " does not match descriptor " + this);
        //}

        return ret;
    }

    @Override
    public String getHeaderExtension() {
        return headerExtension;
    }

    @Override
    public String getDataExtension() {
        return dataExtension;
    }

    @Override
    public String getFileFormat() {
        return fileFormat;
    }

    @Override
    public FileObjectFilter createDataFileFilter() {
        return new FileObjectFilter() {
            public boolean accept(FileObject fobj) {
                return isDataMatch(fobj.getName().getPath());
            }
        };
    }

    @Override
    public FileObjectFilter createHeaderFileFilter() {
        return new FileObjectFilter() {
            public boolean accept(FileObject fobj) {
                return isDataMatch(fobj.getName().getPath());
            }
        };

    }

    @Override
    public abstract IImageDataSource createDataSource(FileObject headerFile, FileObject dataFile);
}
