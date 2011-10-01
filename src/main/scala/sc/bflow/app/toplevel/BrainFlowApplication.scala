package sc.bflow.app.toplevel

import com.jidesoft.status.StatusBar
import java.awt.{Toolkit, BorderLayout, Dimension}
import com.jidesoft.docking.{DockingManager, DefaultDockingManager, DockContext}
import javax.swing._
import border.EmptyBorder
import com.pietschy.command.factory.{MenuFactory, ButtonFactory}

import com.jidesoft.action.{CommandBar, CommandMenuBar, DefaultDockableBarDockableHolder}

import sc.bflow.app.presentation.{SearchableImageFileExplorer, ImageFileExplorer}
import org.apache.commons.vfs.FileObject
import boxwood.binding._
import sc.bflow.core.{ImageCanvasDesktop, ImageCanvas}
import com.jidesoft.document.{DocumentComponentEvent, DocumentComponentAdapter, DocumentComponent, DocumentPane}
import com.pietschy.command.ActionCommand
import sc.bflow.app.commands.{QuickCommand, RecentPathList, ExitApplication, MountFileSystem}
import com.pietschy.command.group.{GroupVisitor, ExpansionPointBuilder, CommandGroup}
import com.jidesoft.swing.{ButtonStyle, JideButton}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 10, 2010
 * Time: 10:01:25 AM
 * To change this template use File | Settings | File Templates.
 */

trait BrainFlowApplication extends Observing {
  this: BrainFlowContext =>

  val mainFrame = new DefaultDockableBarDockableHolder("BrainFlow")

  lazy val documentPane = new DocumentPane()

  lazy val statusBar = new StatusBar()

  lazy val fileExplorer = new SearchableImageFileExplorer(fileSystemService.currentDirectory())

  lazy val dockWindowManager = new DockWindowManager()

  lazy val selectedCanvas: ObservableVar[ImageCanvas] = Observable(new ImageCanvasDesktop())

  lazy val canvasList = ObservableBuffer[ImageCanvas](Seq(selectedCanvas()))

  implicit val context = this


  //initMainFrame
  //initFileExplorer

  private[this] def initMainFrame = {
    documentPane.setTabPlacement(SwingConstants.BOTTOM)
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    mainFrame.getDockingManager.getWorkspace.setLayout(new BorderLayout)
    mainFrame.getDockingManager.getWorkspace.add(documentPane, BorderLayout.CENTER)
    mainFrame.getDockingManager.beginLoadLayoutData
    mainFrame.getDockingManager.setInitSplitPriority(DockingManager.SPLIT_EAST_WEST_SOUTH_NORTH)


  }

  def makeCanvasDocument(canvas: ImageCanvas, name: String) = {
    val doc: DocumentComponent = new DocumentComponent(new JScrollPane(canvas.component), name)
    doc.addDocumentComponentListener(new DocumentComponentAdapter {
      override def documentComponentActivated(documentComponentEvent: DocumentComponentEvent): Unit = {
        selectedCanvas := canvas
      }
    })

    doc
  }

  def addCanvas(canvas: ImageCanvas): Unit = {
    val comp: JComponent = canvas.component
    comp.setRequestFocusEnabled(true)
    comp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1))
    //var handler: BrainCanvasTransferHandler = new BrainCanvasTransferHandler
    //comp.setTransferHandler(handler)
    //var cbar: CanvasBar = new CanvasBar
    //canvas.getComponent.add(cbar.getComponent, BorderLayout.NORTH)
    val canvasName: String = "Canvas-" + (documentPane.getDocumentCount + 1)
    documentPane.openDocument(makeCanvasDocument(canvas, canvasName))
    documentPane.setActiveDocument(canvasName)
    selectedCanvas := canvas
  }

  def initCanvas {

    val canvas = selectedCanvas()
    addCanvas(canvas)


  }


  def initFileExplorer = {
    val dock = dockWindowManager.createDockableFrame("File Manager", "icons/fldr_obj.gif", DockContext.STATE_FRAMEDOCKED, DockContext.DOCK_SIDE_WEST)
    dock.setPreferredSize(new Dimension(275, 400))
    dock.getContentPane.add(fileExplorer)
    mainFrame.getDockingManager.addFrame(dock)

    observe(fileSystemService.fileRoots) {
      case ev@Add(_, ElemAdd(x: FileObject, i)) => println("adding file root: " + x); fileExplorer.addFileRoot(x)
      case _ =>
    }

    println("added observer to " + fileSystemService.fileRoots)
    println("added observer to " + System.identityHashCode(fileSystemService.fileRoots))

  }

  def show = {
    println("showing")

    initCanvas

    initFileExplorer





     initMainFrame



    mainFrame.getDockableBarManager.loadLayoutData

    val screenSize: Dimension = Toolkit.getDefaultToolkit.getScreenSize
    mainFrame.setSize(screenSize.getWidth.asInstanceOf[Int], screenSize.getHeight.asInstanceOf[Int] - 50)

    mainFrame.getDockingManager.loadLayoutData
    mainFrame.setVisible(true)
    mainFrame.getDockingManager.loadLayoutData

    initToolBar
    initMenu
  }

  def bindCommand(command: ActionCommand, installShortCut: Boolean=false): Unit = {
    command.bind(mainFrame)
    if (installShortCut) {
      command.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
    }
  }

  def initMenu = {
    ///val gotoVoxelCommand: GoToVoxelCommand = new GoToVoxelCommand
    //gotoVoxelCommand.bind(mainFrame)
    //gotoVoxelCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)


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

    bindCommand(new ExitApplication)
    bindCommand(new MountFileSystem)

    val pathMenu = new RecentPathList()
    val builder: ExpansionPointBuilder = fileMenuGroup.getExpansionPointBuilder
    builder.add(pathMenu.commandGroup)
    builder.applyChanges


    //menuBar.add(DockWindowManager.getInstance.getDockMenu)
    //var favMenu: JMenuItem = favoritesMenu.getCommandGroup.createMenuItem

    //favMenu.setMnemonic('F')

    //menuBar.add(favMenu)


  }

  def initToolBar = {
    val mainToolbarGroup: CommandGroup = new CommandGroup("main-toolbar")
    mainToolbarGroup.bind(mainFrame)

    val mainToolbar: CommandBar = new CommandBar
    mainToolbar.setPaintBackground(false)
    mainToolbar.setBorder(new EmptyBorder(0, 0, 0, 0))
    mainToolbar.setKey("toolbar")


    bindCommand(this.newCanvas, true)
    bindCommand(this.openImage, true)
    bindCommand(this.createAxial, true)
    bindCommand(this.createCoronal, true)
    bindCommand(this.createSagittal, true)
    bindCommand(this.prevSlice, true)
    bindCommand(this.nextSlice, true)
    bindCommand(this.pageBackSlice, true)
    bindCommand(this.pageForwardSlice, true)
    bindCommand(this.decreaseContrast, true)
    bindCommand(this.increaseContrast, true)
    bindCommand(this.createOrthoTriangular, true)



    mainToolbarGroup.visitMembers(new GroupVisitor {
      def visit(actionCommand: ActionCommand): Unit = {
        println("adding command to toolbar")
        val jb: JideButton = new JideButton(actionCommand.getActionAdapter)
        jb.setButtonStyle(ButtonStyle.TOOLBAR_STYLE)
        jb.setText("")
        mainToolbar.add(jb)
      }

      def visit(commandGroup: CommandGroup): Unit = {
        println("adding command group to toolbar")
        val jc: JComponent = commandGroup.createButton(GuiFactory)
        mainToolbar.add(jc)
      }
    })

    mainFrame.getDockableBarManager.addDockableBar(mainToolbar)



  }


}





