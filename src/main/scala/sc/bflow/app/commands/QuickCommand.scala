package sc.bflow.app.commands

import com.pietschy.command.ActionCommand
import com.pietschy.command.face.Face

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/21/10
 * Time: 11:15 PM
 * To change this template use File | Settings | File Templates.
 */

object QuickCommand {

  def apply(f: => Unit) = {
    new ActionCommand {
      def handleExecute = f
    }
  }

  def apply(f: => Unit, text: String) = {
    val ret = new ActionCommand {
      def handleExecute = f
    }

    ret.getDefaultFace().setText(text)
    ret

  }

}