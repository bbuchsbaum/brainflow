package brainflow.utils;

import org.apache.commons.vfs.FileObject;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 2:49:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FileObjectFilter {


    public boolean accept(FileObject fobj);
}
