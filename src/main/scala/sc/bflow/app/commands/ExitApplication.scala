package sc.bflow.app.commands

import com.pietschy.command.ActionCommand
import sc.bflow.app.toplevel.BrainFlowContext
import javax.swing. {JOptionPane, JFrame}
import java.awt.Window
/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/21/10
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */

class ExitApplication(implicit context: BrainFlowContext) extends ActionCommand("exit-application") {


  def handleExecute = {
    val window: Window = getInvokerWindow
    val frame: JFrame = JOptionPane.getFrameForComponent(window).asInstanceOf[JFrame]
    frame.setVisible(false)
    frame.dispose
    System.exit(0)
  }
}