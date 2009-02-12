package brainflow.application;

import org.apache.commons.vfs.FileObject;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Feb 10, 2005
 * Time: 11:31:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemEvent extends EventObject {


    public enum EventID {
        FILE_SYSTEM_ADDED, FILE_SYSTEM_REMOVED;

    };

    private EventID id;
    
    private FileObject fobj;

    public FileSystemEvent(FileObject source, EventID _id) {
        super(source);
        fobj = source;
        id = _id;

        
    }

    public EventID getEventID() {
        return id;
    }

    public FileObject getFileObject() {
        return fobj;
    }
}
