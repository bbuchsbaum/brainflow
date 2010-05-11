package brainflow.image.io;

import brainflow.utils.FileObjectFilter;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;

import java.io.File;
import java.io.IOException;

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

    private BinaryEncoding headerEncoding = BinaryEncoding.RAW;

    private BinaryEncoding dataEncoding = BinaryEncoding.RAW;

    protected AbstractImageFileDescriptor(String headerExtension, String dataExtension, String fileFormat) {
        this.headerExtension = headerExtension;
        this.dataExtension = dataExtension;
        this.fileFormat = fileFormat;
    }

    protected AbstractImageFileDescriptor(String headerExtension, String dataExtension, String fileFormat, BinaryEncoding headerEncoding, BinaryEncoding dataEncoding) {
        this.headerExtension = headerExtension;
        this.dataExtension = dataExtension;
        this.fileFormat = fileFormat;
        this.headerEncoding = headerEncoding;
        this.dataEncoding = dataEncoding;
    }

    @Override
    public boolean isMatch(String headerFileName, String dataFileName) {
        return isHeaderMatch(headerFileName) && isDataMatch(dataFileName);
    }

    @Override
    public boolean isHeaderMatch(String headerFileName) {
         return headerFileName.endsWith(getDottedHeaderExtension());
    }

    @Override
    public boolean isDataMatch(String dataFileName) {
        return dataFileName.endsWith(getDottedDataExtension());
    }

    @Override
    public boolean canResolve(FileObject fileObject) {
        FileObject other = null;

        try {
            if (isHeaderMatch(fileObject.getName().getBaseName())) {
                other = resolveDataFileObject(fileObject);

            } else if (isDataMatch(fileObject.getName().getBaseName())) {
                other = resolveHeaderFileObject(fileObject);
            }

            if (other == null || !other.exists()) {
                return false;
            }

        } catch(Exception e) {
             return false;
        }

        return true;


    }


    protected String getDottedHeaderExtension() {
        if (headerEncoding.getExtension().equals("")) {
            return "." + headerExtension;
        } else {
            return "." + headerExtension + "." + headerEncoding.getExtension();

        }


    }

    protected String getDottedDataExtension() {
        if (dataEncoding.getExtension().equals("")) {
            return "." + dataExtension;
        } else {
            return "." + dataExtension + "." + dataEncoding.getExtension();

        }
    }


    @Override
    public String stripExtension(String fullName) {
        String ret = fullName;

        if (fullName.endsWith(getDottedHeaderExtension())) {
            ret = fullName.substring(0, fullName.length() - (getDottedHeaderExtension().length()));
        } else if (fullName.endsWith(getDottedDataExtension())) {
            ret = fullName.substring(0, fullName.length() - (getDottedDataExtension().length()));
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
                return isHeaderMatch(fobj.getName().getPath());
            }
        };

    }

    @Override
    public BinaryEncoding getHeaderEncoding() {
        return headerEncoding;
    }

    @Override
    public BinaryEncoding getDataEncoding() {
        return dataEncoding;
    }

    public String getDataName(FileObject headerFile) {
        if (!isHeaderMatch(headerFile.getName().getBaseName())) {
            throw new IllegalArgumentException("headerFile " + headerFile.getName().getBaseName() + " does not match expected naming convention for file type: " + getFileFormat());
        }

        String stem = stripExtension(headerFile.getName().getBaseName());
        return stem + getDottedDataExtension();

    }


    public String getHeaderName(FileObject dataFile) {
        if (!isDataMatch(dataFile.getName().getBaseName())) {
            throw new IllegalArgumentException("headerFile " + dataFile.getName().getBaseName() + " does not match expected naming convention for file type: " + getFileFormat());
        }

        String stem = stripExtension(dataFile.getName().getBaseName());
        return stem + getDottedHeaderExtension();


    }

    @Override
    public FileObject resolveDataFileObject(FileObject headerFile) throws IOException {
        try {
            return resolveFile(headerFile.getParent(), getDataName(headerFile));
        } catch (FileSystemException e) {
            throw new IOException(e);
        }

    }

    @Override
    public FileObject resolveHeaderFileObject(FileObject dataFile) throws IOException {
        try {
            return resolveFile(dataFile.getParent(), getDataName(dataFile));
        } catch (FileSystemException e) {
            throw new IOException(e);
        }

    }

    public static FileObject resolveFile(String path, String name) throws FileSystemException {
        return VFS.getManager().resolveFile(path + File.separatorChar + name);
    }

    public static FileObject resolveFile(FileObject parent, String name) throws FileSystemException {
        String path = parent.getName().toString();
        return resolveFile(path, name);
    }

    public static FileObject resolveFile(File parent, String name) throws FileSystemException {
        return resolveFile(parent.getAbsolutePath(), name);

    }

    @Override
    public IImageSource createDataSource(FileObject headerFile) {

        if (!isHeaderMatch(headerFile.getName().getBaseName())) {
            throw new IllegalArgumentException("header " + headerFile.getName().getBaseName() + " does not have correct suffix for format : " + this.getFileFormat());
        }

        FileObject dataFile;

        try {

            dataFile = resolveFile(headerFile.getParent(), getDataName(headerFile));
            headerFile = resolveFile(headerFile.getParent(), headerFile.getName().getBaseName());

            if (!isDataMatch(dataFile.getName().getBaseName())) {
               throw new IllegalArgumentException("data file" + dataFile.getName().getBaseName() + " does not have correct suffix for format : " + this.getFileFormat());
            }

            if (!dataFile.exists()) {
                throw new IllegalArgumentException("data file" + dataFile.getName().getBaseName() + " does not exist");

            }

        } catch (FileSystemException e) {
            throw new IllegalArgumentException("Cannot locate matching data file for header " + headerFile);
        }

        return createDataSource(headerFile, dataFile);


    }

    @Override
    public abstract IImageSource createDataSource(FileObject headerFile, FileObject dataFile);
}
