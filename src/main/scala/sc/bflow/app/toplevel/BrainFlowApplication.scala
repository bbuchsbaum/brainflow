package sc.bflow.app.toplevel

import com.jidesoft.document.DocumentPane
import com.jidesoft.status.StatusBar
import brainflow.app.toplevel.DockWindowManager
import java.awt.{Toolkit, BorderLayout, Dimension}
import com.jidesoft.docking.{DockingManager, DefaultDockingManager, DockContext}
import javax.swing._
import border.EmptyBorder
import com.pietschy.command.factory. {MenuFactory, ButtonFactory}
import com.jidesoft.swing. {JideMenu, JideToggleButton, JideButton}
import com.pietschy.command.GuiCommands
import com.jidesoft.action. {CommandBar, CommandMenuBar, DefaultDockableBarDockableHolder}
import brainflow.app.actions. {ExitApplicationCommand, MountFileSystemCommand, GoToVoxelCommand}
import com.pietschy.command.group. {ExpansionPointBuilder, CommandGroup}
import sc.bflow.app.presentation. {SearchableImageFileExplorer, ImageFileExplorer}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 10, 2010
 * Time: 10:01:25 AM
 * To change this template use File | Settings | File Templates.
 */

trait BrainFlowApplication {
  this: BrainFlowContext =>

  val mainFrame = new DefaultDockableBarDockableHolder("BrainFlow")

  lazy val documentPane = new DocumentPane()

  lazy val statusBar = new StatusBar()

  lazy val fileExplorer =  new ImageFileExplorer(fileSystemService.currentDirectory())

  lazy val dockWindowManager = new DockWindowManager()



  //initMainFrame
  //initFileExplorer

  private[this] def initMainFrame = {
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    mainFrame.getDockingManager.getWorkspace.setLayout(new BorderLayout)
    mainFrame.getDockingManager.getWorkspace.add(documentPane, BorderLayout.CENTER)
    mainFrame.getDockingManager.beginLoadLayoutData
    mainFrame.getDockingManager.setInitSplitPriority(DockingManager.SPLIT_EAST_WEST_SOUTH_NORTH)


  }


  def initFileExplorer = {
    val dock = dockWindowManager.createDockableFrame("File Manager", "icons/fldr_obj.gif", DockContext.STATE_FRAMEDOCKED, DockContext.DOCK_SIDE_WEST)
    dock.setPreferredSize(new Dimension(275, 400))
    dock.getContentPane.add(fileExplorer)
    mainFrame.getDockingManager.addFrame(dock)

  }

  def show = {
    println("showing")
    initMenu
    initMainFrame

    initFileExplorer

    mainFrame.getDockableBarManager.loadLayoutData

    val screenSize: Dimension = Toolkit.getDefaultToolkit.getScreenSize
    mainFrame.setSize(screenSize.getWidth.asInstanceOf[Int], screenSize.getHeight.asInstanceOf[Int] - 50)

    mainFrame.getDockingManager.loadLayoutData
    mainFrame.setVisible(true)
    mainFrame.getDockingManager.loadLayoutData
  }

  def initMenu = {
    val gotoVoxelCommand: GoToVoxelCommand = new GoToVoxelCommand
    gotoVoxelCommand.bind(mainFrame)
    gotoVoxelCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)


    val fileMenuGroup: CommandGroup = new CommandGroup("file-menu")
    fileMenuGroup.bind(mainFrame)


    val menuBar: CommandBar = new CommandMenuBar
    menuBar.setBorder(new EmptyBorder(2, 2, 2, 2))
    menuBar.setStretch(true)


    // for nimbus look and feel
    menuBar.setPaintBackground(false)
    // for nimbus look and feel


    menuBar.add(fileMenuGroup.createMenuItem())
    //menuBar.add(viewMenuGroup.createMenuItem)
    //menuBar.add(gotoMenuGroup.createMenuItem)
    menuBar.setKey("menu")


    mainFrame.getDockableBarManager.addDockableBar(menuBar)



    //val mountFileSystemCommand: MountFileSystemCommand = new MountFileSystemCommand
    //mountFileSystemCommand.bind(mainFrame)


    val exitCommand: ExitApplicationCommand = new ExitApplicationCommand
    exitCommand.bind(mainFrame)


    //val builder: ExpansionPointBuilder = fileMenuGroup.getExpansionPointBuilder
    //builder.add(pathMenu.getCommandGroup)
    //builder.applyChanges

    //menuBar.add(DockWindowManager.getInstance.getDockMenu)
    //var favMenu: JMenuItem = favoritesMenu.getCommandGroup.createMenuItem

    //favMenu.setMnemonic('F')

    //menuBar.add(favMenu)



  }

}



object BrainFlowApplicationTest extends BrainFlowApplication with BrainFlowContext {
  override val fileSystemService = new FileSystemService

  def main(args: Array[String]) {
    BrainFlowInitialization.initLookAndFeel
    show

  }

}

