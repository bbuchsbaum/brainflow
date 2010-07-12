package sc.brainflow.app.presentation


import sc.brainflow.image.io.ImageFileDescriptors
import sc.brainflow.image.io._
import swing.SwingApplication
import java.awt.BorderLayout
import boxwood.io.RichFileObject._
import boxwood.io.{FilteredFileSelector, SystemResource, RichFileObject}
import javax.swing.event.{TreeExpansionEvent, TreeExpansionListener}
import collection.mutable.{Buffer, ArrayBuffer}
import javax.swing._
import tree._
import com.jidesoft.tree.{DefaultTreeModelWrapper, TreeUtils}
import org.apache.commons.vfs._
import org.apache.commons.vfs.impl.DefaultFileMonitor
import boxwood.binding.swing.tree._
import java.awt.datatransfer.Transferable

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 4, 2010
 * Time: 6:45:03 PM
 * To change this template use File | Settings | File Templates.
 */


trait ImageNodeFactory {
  def makeNode(fileObject: FileObject) : GenericTreeNode[ImageSourceNode]
}

object ImageFileExplorer {

  val imageSelector  = new FilteredFileSelector((fileInfo: FileSelectInfo) => NIFTI.isHeaderMatch(fileInfo.getFile.getName.getBaseName))


}



trait IconProvider {
  def icon : ImageIcon

  def expandedIcon : ImageIcon
}

class ImageFileExplorer(root: FileObject) extends JPanel with ImageNodeFactory {

  val rootNode = new DefaultBranchNode("File Systems")

  val tree: GenericTree = new GenericTree(rootNode)

  val fileRoots: ArrayBuffer[FileObject] = ArrayBuffer()

  //val treeModel = new DefaultTreeModel(rootNode)
  initRenderer
  initExpansionListener
  initDnD
  init

  def treeModel = tree.treeModel


  private def initExpansionListener {
    treeComponent.addTreeExpansionListener(new TreeExpansionListener() {
      def treeCollapsed(event: TreeExpansionEvent) = {}

      def treeExpanded(event: TreeExpansionEvent) = {
        println("tree expanded")
      }
    })
  }

  private def initRenderer = {
   treeComponent.setCellRenderer(new DefaultTreeCellRenderer() {
     override def getTreeCellRendererComponent(tree: JTree, value: Any, sel: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean) = {
       var label: JLabel = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus).asInstanceOf[JLabel]
       value match {
         case node : IconProvider => if (expanded) label.setIcon(node.expandedIcon) else label.setIcon(node.icon)
         case _ => ()
       }

       label
     }
   })
  }

  private def initDnD: Unit = {
    treeComponent.setDragEnabled(true)
    //tree.addMouseListener(this)
    //tree.addMouseMotionListener(this)
    val handler: TransferHandler = new TransferHandler {
      override def getSourceActions(c: JComponent): Int = {
        return TransferHandler.COPY
      }

      protected override def createTransferable(c: JComponent): Transferable = {
        var ret: Transferable = null
        if (c.isInstanceOf[JTree]) {
          val tree = c.asInstanceOf[JTree]
          var path = tree.getSelectionPath
          val obj = path.getPath
          val node: GenericTreeNode[_] = obj(obj.length - 1).asInstanceOf[GenericTreeNode[_]]
          println("dragged node is " + node)
        }

        ret
      }
    }
    
    treeComponent.setTransferHandler(handler)
  }


  def addFileRoot(fobj: FileObject): Unit = {
    fileRoots append fobj
    val node = makeNode(fobj)
    tree.addNode(node)

  }

  private[this] def init(): Unit = {

    addFileRoot(root)

    treeComponent.setDragEnabled(true)
    treeComponent.scrollPathToVisible(new TreePath(rootNode.getPath))

    setLayout(new BorderLayout())
    add(new JScrollPane(treeComponent), BorderLayout.CENTER)
  }

  private def defaultTreeModel: DefaultTreeModel = treeComponent.getModel match {
    case wrapper : DefaultTreeModelWrapper => wrapper.getActualModel.asInstanceOf[DefaultTreeModel]
    case model : DefaultTreeModel => model.asInstanceOf[DefaultTreeModel]
    case _ => error("model should be DefaultTreeMode, what gives?")
  }

  def updateNodeContent(node: TreeNode): Unit = {
    val dtm: DefaultTreeModel = defaultTreeModel
    val state = TreeUtils.saveExpansionStateByTreePath(treeComponent)
    dtm.nodeStructureChanged(node)
    TreeUtils.loadExpansionStateByTreePath(treeComponent, state)
  }

   def makeImageNode(parent: Option[MutableTreeNode], fileObject: FileObject) = {
    require(fileObject.getType == FileType.FILE)
    val infoOpt = ImageFileDescriptors.readMetaInfo(fileObject)
    infoOpt match {
      case Some(info) =>
        if (info.numVolumes == 1)
          ImageLeafNode3D(parent, ImageSource3D(0, info))
        else if (info.numVolumes > 1)
          ImageBucketNode(parent, new ImageSourceSeq3D(info.label, (0 until info.numVolumes).map(i => ImageSource3D(i, info))), fileObject)
        else error("number of volumes must be >= 0 " + "found " + info.numVolumes + " in " + fileObject.path)
      case None => error("could not load meta information for file " + fileObject)
    }

  }

  def makeFolderNode(parent: Option[MutableTreeNode], fileObject: FileObject, selector: FileSelector) : ImageFolderNode = {
    require(fileObject.getType == FileType.FOLDER)
    val node = ImageFolderNode(parent, fileObject, ImageFileExplorer.imageSelector, this)
    monitorFolder(node)
    node
  }


  def makeNode(fileObject: FileObject) = {
    fileObject.getType match {
      case FileType.FOLDER => makeFolderNode(None, fileObject, ImageFileExplorer.imageSelector)
      case FileType.FILE   => makeImageNode(None, fileObject)
    }

  }

  private def monitorFolder(folder: ImageFolderNode): Unit = {
    val fm: DefaultFileMonitor = new DefaultFileMonitor(new FileListener {
      def sync(fileChangeEvent: FileChangeEvent) = {
        folder.resync
        updateNodeContent(folder)
      }
      
      def fileCreated(fileChangeEvent: FileChangeEvent) = sync(fileChangeEvent)

      def fileDeleted(fileChangeEvent: FileChangeEvent) = sync(fileChangeEvent)

      def fileChanged(fileChangeEvent: FileChangeEvent) = sync(fileChangeEvent)

    })

    fm.addFile(folder.file)
    fm.start
  }

  def treeComponent = tree.treeComponent

}

case class ImageBucketNode(override var parent: Option[MutableTreeNode] = None, override val value: ImageSourceSeq3D, val file: FileObject) extends GenericBranchNode[ImageSourceSeq3D, ImageSource3D] with IconProvider {

  lazy val icon = SystemResource("icons/bin_closed.png").toImageIcon

  def expandedIcon = SystemResource("icons/bin.png").toImageIcon

  def allowsChildren = true

  lazy val childNodes :  Buffer[GenericTreeNode[ImageSource3D]] = {
    value.children.map(x => ImageLeafNode3D(Some(this), x)).toBuffer
  }

  override def toString = value.label
}

case class ImageLeafNode3D(override var parent: Option[MutableTreeNode] = None, override val value: ImageSource3D)
        extends GenericLeafNode[ImageSource3D] with IconProvider {

  override def toString : String = value.label

  lazy val icon = SystemResource("icons/brick.png").toImageIcon

  def expandedIcon = icon
}

case class ImageFolderNode(override var parent: Option[MutableTreeNode] = None,  val file: FileObject, val selector: FileSelector, val nodeFactory: ImageNodeFactory)
        extends GenericBranchNode[ImageSourceNode, ImageSourceNode] with IconProvider {

  lazy val value = new ImageSourceGroup(file.getName.getBaseName, childNodes.map(_.value))

  var cachedNodes: Option[Buffer[GenericTreeNode[ImageSourceNode]]] = None

  def makeNode(child: FileObject) : GenericTreeNode[ImageSourceNode] = nodeFactory.makeNode(child)

  def childNodes: Buffer[GenericTreeNode[ImageSourceNode]] = {
    cachedNodes match {
      case Some(x) => x
      case None => {
        val nodes = ArrayBuffer(file.find(selector): _*).map(child => makeNode(child))
        cachedNodes = Some(nodes)
        nodes
        }
      }
  }

  def resync = {
    cachedNodes = None
    // this blocks ...
    childNodes
  }

  def allowsChildren = true

  override def toString: String = file.name

  lazy val icon = SystemResource("icons/folder.png").toImageIcon

  lazy val expandedIcon = SystemResource("icons/folderOpen.png").toImageIcon
}



object TestFileExplorer extends SwingApplication {
  def startup(args: Array[String]) = {
    val jf = new JFrame()

    val root = new SystemResource("data").toFileObject
   
    jf.add(new ImageFileExplorer(root), BorderLayout.CENTER)
    jf.setSize(800, 800)

    jf.setVisible(true)

  }
}




