package brainflow.app.toplevel;

import brainflow.app.FileSystemEvent;
import brainflow.app.FileSystemEventListener;
import org.apache.commons.vfs.FileObject;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Feb 10, 2005
 * Time: 11:19:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryManager {

    private Logger log = Logger.getLogger(DirectoryManager.class.getCanonicalName());

    private List<FileObject> fileRoots = new ArrayList<FileObject>();

    private EventListenerList listeners = new EventListenerList();

    private File currentDirectory;


    protected DirectoryManager() {
        currentDirectory = new File(System.getProperty("user.dir"));

    }


    public static DirectoryManager getInstance() {
        return (DirectoryManager) SingletonRegistry.REGISTRY.getInstance(DirectoryManager.class.getName());

    }


    public void mountFileSystem(FileObject fobj) {
        if (fileRoots.contains(fobj)) {
            log.warning("DirectoryManager already contains file root: " + fobj);
        }

        fileRoots.add(fobj);
        fireFileSystemEvent(new FileSystemEvent(fobj, FileSystemEvent.EventID.FILE_SYSTEM_ADDED));

    }


    public void addFileSystemEventListener(FileSystemEventListener l) {
        listeners.add(FileSystemEventListener.class, l);
    }

    public File getCurrentLocalDirectory() {
        return currentDirectory;
    }

    public void setCurrentLocalDirectory(File file) {
        assert file.exists() : "Invalid file: " + file + " does not exist";
        if (file.isFile()) {
            currentDirectory = file.getParentFile();
        } else if (file.isDirectory()) {
            currentDirectory = file;
        } else {
            log.warning("retaining " + currentDirectory + " as current directory (supplied file was invalid)");
        }
    }


    private void fireFileSystemEvent(FileSystemEvent event) {

        FileSystemEventListener[] ls = listeners.getListeners(FileSystemEventListener.class);

        for (FileSystemEventListener i : ls) {
            i.eventOccurred(event);
        }

    }


}
