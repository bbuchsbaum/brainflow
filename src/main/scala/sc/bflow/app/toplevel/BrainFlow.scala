package sc.bflow.app.toplevel


import com.pietschy.command.configuration.ParseException
import com.pietschy.command. {CommandContainer, GuiCommands}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 10, 2010
 * Time: 9:12:11 AM
 * To change this template use File | Settings | File Templates.
 */





object BrainFlow extends BrainFlowApplication with BrainFlowContext {
  self =>

  //val fileSystemService = new FileSystemService

  val commandContainer = new CommandContainer


  def launch() = {
    BrainFlowInitialization.initLookAndFeel
    loadCommands
    commandContainer.bind(self.mainFrame)
    self.show


  }


  private def loadCommands: Boolean = {
    try {
      GuiCommands.load("commands/BrainFlowCommands")
      GuiCommands.defaults.setButtonFactory(GuiFactory)
      GuiCommands.defaults.setMenuFactory(GuiFactory)

    } catch {
      case e: ParseException => throw new RuntimeException(e)
    }

    true
  }


  def main(args: Array[String]) = BrainFlow.launch()


}