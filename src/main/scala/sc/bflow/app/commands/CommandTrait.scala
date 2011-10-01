package sc.bflow.app.commands

import sc.bflow.app.toplevel.BrainFlowContext
import com.pietschy.command.ActionCommand
import sc.bflow.core.ImageCanvasDesktop
import javax.swing.JFileChooser

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/24/10
 * Time: 8:02 PM
 * To change this template use File | Settings | File Templates.
 */


trait CommandTrait extends ActionCommand with Function0[Unit] {
  def handleExecute() = apply
}

