package brainflow.image.io;

import brainflow.utils.FileObjectFilter;
import brainflow.core.BrainFlowException;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 1:11:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageIODescriptor {

    private final String headerExtension;

    private final String dataExtension;

    // obsolete
    private Class dataReader;

    private Class headerReader;

    private String formatName;

    private FOF fof = new FOF();

    private static Logger log = Logger.getLogger(ImageIODescriptor.class.getName());

    public static final ImageIODescriptor AFNI = new ImageIODescriptor("HEAD", "BRIK", BasicImageReader3D.class, AFNIInfoReader.class, "AFNI");
    public static final ImageIODescriptor AFNI_GZ = new ImageIODescriptor("HEAD.GZ", "BRIK.GZ", BasicImageReader3D.class, AFNIInfoReader.class, "AFNI");
    public static final ImageIODescriptor NIFTI_ONE = new ImageIODescriptor("nii", "nii", BasicImageReader3D.class, NiftiInfoReader.class, "NIFTI");
    public static final ImageIODescriptor NIFTI_ONE_GZ = new ImageIODescriptor("nii.gz", "nii.gz", BasicImageReader3D.class, NiftiInfoReader.class, "NIFTI");

    //public static final ImageIODescriptor NIFTI_PAIR = new ImageIODescriptor("hdr", "img", BasicImageReader3D.class, NiftiInfoReader.class, "NIFTI");
    public static final ImageIODescriptor ANALYZE = new ImageIODescriptor("hdr", "img", BasicImageReader3D.class, AnalyzeInfoReader.class, "ANALYZE");

    //todo need to start over on this


    public ImageIODescriptor(String headerExtension, String dataExtension, Class dataReader, Class headerReader, String formatName) {
        this.formatName = formatName;
        this.headerExtension = headerExtension;
        this.dataExtension = dataExtension;
        this.dataReader = dataReader;
        this.headerReader = headerReader;

    }
    private ImageIODescriptor(Element formatElement) throws BrainFlowException {
        Element headerElement = formatElement.getChild("Header");
        Element dataElement = formatElement.getChild("Data");

        formatName = formatElement.getAttributeValue("name");

        headerExtension = headerElement.getAttributeValue("extension");
        dataExtension = dataElement.getAttributeValue("extension");

        String headerReaderClassName = headerElement.getAttributeValue("reader");
        String dataReaderClassName = dataElement.getAttributeValue("reader");


        try {
            dataReader = Class.forName(dataReaderClassName);
            headerReader = Class.forName(headerReaderClassName);

        } catch (ClassNotFoundException e) {
            throw new BrainFlowException("ImageIODescriptor() : couldn'three createSource class " + dataReaderClassName, e);
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


    public IImageSource[] findLoadableImages(FileObject[] fobjs) {

        List<IImageSource> limglist = new ArrayList<IImageSource>();

        for (int i = 0; i < fobjs.length; i++) {
            if (isHeaderMatch(fobjs[i])) {
                try {

                    FileObject dobj = resolveFile(fobjs[i].getParent(), getDataName(fobjs[i]));

                    if (dobj != null && dobj.exists()) {
                        //limglist.add(new ImageDataSource(this, fobjs[i], dobj));
                    } else {
                        log.warning("file " + fobjs[i] + " could not be resolved");
                    }

                } catch (FileSystemException e) {
                    log.warning("failed to resolve data file for image header: " + fobjs[i]);

                }

            }
        }

        IImageSource[] ret = new IImageSource[limglist.size()];
        limglist.toArray(ret);
        return ret;

    }


    public FileObject findDataMatch(FileObject header, FileObject[] candidates) {
        assert isHeaderMatch(header);

        for (int i = 0; i < candidates.length; i++) {
            String dataName = getDataName(header);
            String candyName = candidates[i].getName().getBaseName();


            if (dataName.equals(candyName)) {
                return candidates[i];
            }
        }

        return null;

    }

    public IImageSource createLoadableImage(FileObject header, FileObject data) {
        
        if (!isHeaderMatch(header)) {
            throw new IllegalArgumentException("header " + header.getName().getBaseName() + " does not have correct suffix for format : " + this.getFormatName());
        }
        if (!isDataMatch(data)) {
            throw new IllegalArgumentException("data " + data.getName().getBaseName() + " does not have correct suffix for format : " + this.getFormatName());
        }

        return new ImageSource3D(null/*this*/, header, data);

    }

    public IImageSource createLoadableImage(FileObject header) {

        if (!isHeaderMatch(header)) {
            throw new IllegalArgumentException("header " + header.getName().getBaseName() + " does not have correct suffix for format : " + this.getFormatName());
        }

        FileObject data;

        try {

            data = resolveFile(header.getParent(), getDataName(header));
            header = resolveFile(header.getParent(), header.getName().getBaseName());

        } catch (FileSystemException e) {
            throw new IllegalArgumentException("Cannot locate matching data file for header " + header);
        }


        return new ImageSource3D(null /*this*/, header, data);

    }


    public String getDataName(FileObject headerFile) {
        assert isHeaderMatch(headerFile);
        String stem = getStem(headerFile.getName().getBaseName());
        return stem + dataExtension;

    }


    public String getHeaderName(FileObject dataFile) {
        assert isDataMatch(dataFile);
        String stem = getStem(dataFile.getName().getBaseName());
        return stem + headerExtension;

    }

    public boolean isHeaderName(String hname) {
        if (hname.endsWith(headerExtension)) {
            return true;
        }

        return false;

    }

    public boolean isDataName(String dname) {
        if (dname.endsWith(dataExtension)) {
            return true;
        }

        return false;

    }


    public boolean isHeaderMatch(FileObject fobj) {
        if (fobj.getName().getPath().endsWith(headerExtension)) {
            return true;
        } else {
            return false;

        }
    }

    public String getStem(String fname) {
        String ret = null;

        if (fname.endsWith(headerExtension)) {
            ret = fname.substring(0, fname.length() - headerExtension.length());
        } else if (fname.endsWith(dataExtension)) {
            ret = fname.substring(0, fname.length() - dataExtension.length());
        }

        return ret;
    }


    public boolean isDataMatch(FileObject fobj) {
        if (fobj.getName().getPath().endsWith(dataExtension)) {
            return true;
        }

        return false;

    }

    public String getHeaderExtension() {
        return headerExtension;
    }

    public String getDataExtension() {
        return dataExtension;
    }

    public Class getDataReader() {
        return dataReader;
    }

    public Class getHeaderReader() {
        return headerReader;
    }

    public String getFormatName() {
        return formatName;
    }

    public String toString() {
        return getFormatName();
    }

    public static ImageIODescriptor loadFromXML(Element formatElement) throws BrainFlowException {
        return new ImageIODescriptor(formatElement);
    }

    public FileObjectFilter createFileObjectFilter() {
        return fof;
    }

    class FOF implements FileObjectFilter {
        // matches headers only!
        public boolean accept(FileObject fobj) {
            return isHeaderMatch(fobj);
        }
    }


}
