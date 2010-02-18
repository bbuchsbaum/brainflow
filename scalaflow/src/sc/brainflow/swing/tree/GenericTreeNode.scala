package sc.brainflow.swing.tree

import collection.mutable.ArrayBuffer
import boxwood.io.RichFileObject._
import swing.SwingApplication
import javax.swing.{JTree, JFrame}
import org.apache.commons.vfs.{FileSelectInfo, FileSelector, FileObject}
import javax.swing.tree._
import boxwood.io.RichFileObject
import java.awt.BorderLayout
import java.net.{URI, URISyntaxException}

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 6, 2010
 * Time: 2:00:57 PM
 * To change this template use File | Settings | File Templates.
 */

trait GenericTreeNode[A] extends MutableTreeNode {
  var parent: Option[MutableTreeNode]

  def value: A

  def childNodes: ArrayBuffer[GenericTreeNode[A]]

  def numChildren = childNodes.size

  def indexOf(node: TreeNode) = childNodes.indexOf(node)

  def allowsChildren: Boolean

  def isLeaf = !allowsChildren


  def children: java.util.Enumeration[TreeNode] = {
    new TreeEnum(childNodes).asInstanceOf[java.util.Enumeration[TreeNode]]

  }



  def getPath: Array[TreeNode] = {
    return getPathToRoot(this, 0)
  }


 
  protected def getPathToRoot(aNode: TreeNode, depth: Int): Array[TreeNode] = {
    var retNodes: Array[TreeNode] = null

    if (aNode == null) {
      if (depth == 0) return null
      else retNodes = new Array[TreeNode](depth)
    }
    else {
      var newdepth = depth + 1
      retNodes = getPathToRoot(aNode.getParent, newdepth)
      retNodes(retNodes.length - newdepth) = aNode
    }

    retNodes
  }

  override def getAllowsChildren = allowsChildren

  override def getIndex(node: TreeNode) = indexOf(node)

  override def getParent = parent.getOrElse(null)

  override def getChildCount = numChildren

  override def getChildAt(childIndex: Int) = childNodes(childIndex)


  override def setParent(newParent: MutableTreeNode) = parent = Some(newParent)

  override def removeFromParent = parent match {
    case Some(x) => x.remove(this)
    case _ => ()
  }

  override def setUserObject(`object` : Any) = error("cannot set user object")

  override def remove(node: MutableTreeNode) = node.asInstanceOf[GenericTreeNode[A]]

  override def remove(index: Int) = childNodes.remove(index)

  override def insert(child: MutableTreeNode, index: Int) = childNodes.insert(index, child.asInstanceOf[GenericTreeNode[A]])

  override def toString = value.toString()
}


class TreeEnum(val childNodes: Seq[GenericTreeNode[_]]) extends java.util.Enumeration[TreeNode] {
  var i = 0

  def hasMoreElements: Boolean = {
    i < childNodes.size
  }

  def nextElement: TreeNode = {
    val ret = childNodes(i)
    i = i + 1
    ret
  }
}


object NonRecursiveFileSelector extends FileSelector {
  def traverseDescendents(p1: FileSelectInfo) = p1.getDepth == 0

  def includeFile(p1: FileSelectInfo) = p1.getDepth > 0

}


class FileObjectTreeNode(override var parent: Option[MutableTreeNode], override val value: FileObject, val selector: FileSelector) extends GenericTreeNode[FileObject] {
  self =>
  
  var cachedNodes: Option[ArrayBuffer[GenericTreeNode[FileObject]]] = None

  def childNodes: ArrayBuffer[GenericTreeNode[FileObject]] = {
    cachedNodes match {
      case Some(x) => x
      case None => {
        val res = ArrayBuffer(value.findFiles(selector): _*)
        res.map(child => new FileObjectTreeNode(Some(self), child, self.selector))
      }

    }
  }


  override def allowsChildren = value.isFolder

 
  override def toString: String = {
    if (this == !parent.isDefined) {
      try {
        val uri: URI = new URI(value.getName.getURI)
        uri.getHost + ":" + uri.getPath
      }
      catch {
        case e: URISyntaxException => {
          return value.getName.getURI
        }
      }
    }

    else value.name
  }
}

object TreeTest extends SwingApplication {
  def startup(args: Array[String]) = {
    val jf = new JFrame()
    val rootNode: DefaultMutableTreeNode = new DefaultMutableTreeNode("File Systems")
    val treeModel = new DefaultTreeModel(rootNode)
    val fileTree: JTree = new JTree(treeModel)

    val node = new FileObjectTreeNode(None, RichFileObject("/home/brad"), NonRecursiveFileSelector)
    val root: MutableTreeNode = treeModel.getRoot.asInstanceOf[MutableTreeNode]
    treeModel.insertNodeInto(node, root, root.getChildCount)

    jf.add(fileTree, BorderLayout.CENTER)
    jf.setSize(800, 800)
    println("setting frame visible")
    jf.setVisible(true)

  }
}