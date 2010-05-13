package sc.brainflow.controls

import boxwood.io.RichFileObject._
import java.net.{URISyntaxException, URI}

import javax.swing.tree._
import collection.mutable.ArrayBuffer
import swing.{SwingApplication}
import javax.swing._
import boxwood.io.RichFileObject
import java.lang.String
import java.awt.{BorderLayout, Component}
import org.apache.commons.vfs.{FileType, FileSelectInfo, FileObject, FileSelector}
import sc.brainflow.swing.tree.{FileTreeNode, FolderTreeNode, GenericTreeNode}

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 6, 2010
 * Time: 9:38:42 AM
 * To change this template use File | Settings | File Templates.
 */

class FileExplorer(root: FileObject, val selector: FileSelector) extends JPanel {


  def this(root: FileObject) = this (root, new FileSelector() {
    def traverseDescendents(p1: FileSelectInfo) = p1.getDepth == 0

    def includeFile(p1: FileSelectInfo) = p1.getDepth > 0
  })


  private val fileRoots: ArrayBuffer[FileObject] = ArrayBuffer()

  val rootNode: DefaultMutableTreeNode = new DefaultMutableTreeNode("File Systems")

  val treeModel = new DefaultTreeModel(rootNode)

  val fileTree: JTree = new JTree(treeModel)


  init()

  private def init(): Unit = {

    fileTree.setCellRenderer(new FileTreeCellRenderer)

    addFileRoot(root)

    fileTree.setDragEnabled(true)
    fileTree.scrollPathToVisible(new TreePath(rootNode.getPath))

    setLayout(new BorderLayout())
    add(new JScrollPane(fileTree), BorderLayout.CENTER)
  }

  def addFileRoot(fobj: FileObject): Unit = {

    fileRoots append fobj
    val node = makeNode(fobj)
    val root = treeModel.getRoot.asInstanceOf[MutableTreeNode]
    treeModel.insertNodeInto(node, root, root.getChildCount)
    fileTree.scrollPathToVisible(new TreePath(node.getPath))
  }

  def makeNode(fileObject: FileObject) : GenericTreeNode[_] = {
    fileObject.getType match {
      case FileType.FOLDER => new FolderTreeNode(None, fileObject, selector)
      case FileType.FILE => new FileTreeNode(None, fileObject)
    }

  }

  private class FileTreeCellRenderer extends DefaultTreeCellRenderer {
 
    override def getTreeCellRendererComponent(tree: JTree, value: Any, sel: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean): Component = {

      val node = value match {
        case x: FolderTreeNode => value.asInstanceOf[FolderTreeNode]
        case y: FileTreeNode => value.asInstanceOf[FileTreeNode]
        case _ => return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)
      }

      var label = super.getTreeCellRendererComponent(tree, node.value.name, sel, expanded, leaf, row, hasFocus).asInstanceOf[JLabel]

      label
    }

  }


}

object TestFileExplorer extends SwingApplication {
  def startup(args: Array[String]) = {
    val jf = new JFrame()
    jf.add(new FileExplorer(RichFileObject("/home/brad")), BorderLayout.CENTER)
    jf.setSize(800, 800)
    println("setting frame visible")
    jf.setVisible(true)

  }
}



