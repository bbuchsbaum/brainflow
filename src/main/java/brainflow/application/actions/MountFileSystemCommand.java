package brainflow.application.actions;

import com.jidesoft.swing.FolderChooser;
import brainflow.application.toplevel.DirectoryManager;
import brainflow.application.toplevel.BrainFlow;

import java.io.File;
import java.util.logging.Logger;

import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileSystemException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 6, 2008
 * Time: 1:54:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class MountFileSystemCommand extends BrainFlowCommand {

    private static final Logger log = Logger.getLogger(MountFileSystemCommand.class.getName());

    private FolderChooser chooser = null;

    public MountFileSystemCommand() {
        super("mount-filesystem");
    }

    protected void handleExecute() {
        if (chooser == null) {
            chooser = new FolderChooser(DirectoryManager.getInstance().getCurrentLocalDirectory());
        }


        final int res = chooser.showOpenDialog(BrainFlow.get().getApplicationFrame());
        try {
            if (res == FolderChooser.APPROVE_OPTION) {
                File[] files = chooser.getSelectedFiles();
                for (int i = 0; i < files.length; i++) {
                    DirectoryManager.getInstance().mountFileSystem(VFS.getManager().resolveFile(files[i].getAbsolutePath()));
                    DirectoryManager.getInstance().setCurrentLocalDirectory(files[files.length - 1]);
                }
            }
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }
    }
}

