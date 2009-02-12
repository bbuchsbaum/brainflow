package brainflow.application;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Feb 10, 2005
 * Time: 11:40:22 AM
 * To change this template use File | Settings | File Templates.
 */
public interface FileSystemEventListener extends EventListener {

    public void eventOccurred(FileSystemEvent e);



}
