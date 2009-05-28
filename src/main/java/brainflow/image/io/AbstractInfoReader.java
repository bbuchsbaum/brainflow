package brainflow.image.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2009
 * Time: 10:35:48 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractInfoReader implements ImageInfoReader {


    protected FileObject headerFile;

    protected FileObject dataFile;

    protected AbstractInfoReader() {
    }

    public AbstractInfoReader(FileObject headerFile, FileObject dataFile) {
        try {
            headerFile = resolveObject(headerFile);
            dataFile = resolveObject(dataFile);
      
        } catch (FileSystemException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e2) {
            throw new IllegalArgumentException(e2);

        }


        this.headerFile = headerFile;
        this.dataFile = dataFile;
    }

    private FileObject resolveObject(FileObject fobj) throws IOException {
        if (fobj.getName().getBaseName().endsWith(".gz")) {
            fobj = VFS.getManager().resolveFile(replaceScheme(fobj, "gz"));
            FileObject[] children = fobj.getChildren();
            fobj = fobj.getChildren()[0];
            if (children == null || children.length == 0) {
                throw new IOException("Error reading gzipped file object : " + fobj.getName().getURI());
            }

            if (!children[0].exists()) {
                throw new IOException("Error, file " + children[0].getName() + " does not exist.");
            }
        }

        return fobj;

    }

    private String replaceScheme(FileObject fobj, String scheme) {
        String uri = fobj.getName().getURI();
        return uri.replace(fobj.getName().getScheme(), scheme);
    }

    public AbstractInfoReader(File headerFile, File dataFile) {
        try {
            this.headerFile = VFS.getManager().resolveFile(headerFile.getAbsolutePath());
            this.dataFile = VFS.getManager().resolveFile(dataFile.getAbsolutePath());
        } catch (FileSystemException e) {
            throw new IllegalArgumentException("invalid header file : " + e.getMessage());
        }
    }


    public FileObject getHeaderFile() {
        return headerFile;
    }

    public FileObject getDataFile() {
        return dataFile;
    }


}
