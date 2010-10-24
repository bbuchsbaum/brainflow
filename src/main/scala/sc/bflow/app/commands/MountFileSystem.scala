package sc.bflow.app.commands

import com.pietschy.command.ActionCommand
import sc.bflow.app.toplevel.BrainFlowContext
import com.jidesoft.swing.FolderChooser
import boxwood.io._
import javax.swing.JFileChooser
import org.apache.commons.vfs.VFS

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/17/10
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */

class MountFileSystem(implicit val context: BrainFlowContext) extends ActionCommand("mount-filesystem") {

  lazy val chooser = new FolderChooser(context.fileSystemService.currentDirectory().toLocalFile)

  protected def handleExecute: Unit = {

    val res: Int = chooser.showOpenDialog(context.mainFrame)
    if (res == JFileChooser.APPROVE_OPTION) {
        val files = chooser.getSelectedFiles
        files.foreach(x => context.fileSystemService.mount(VFS.getManager.resolveFile(x.getAbsolutePath)))
    }

  }

}