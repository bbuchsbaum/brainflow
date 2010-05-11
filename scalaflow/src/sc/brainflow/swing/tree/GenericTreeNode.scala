package sc.brainflow.swing.tree

import collection.mutable.ArrayBuffer
import boxwood.io.RichFileObject._
import swing.SwingApplication
import javax.swing.{JTree, JFrame}
import javax.swing.tree._
import boxwood.io.RichFileObject
import java.awt.BorderLayout
import java.net.{URI, URISyntaxException}
import org.apache.commons.vfs.{FileType, FileSelectInfo, FileSelector, FileObject}

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

  def allowsChildren: Boolean

  def isLeaf: Boolean

  def numChildren: Int

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

  override def setUserObject(`object` : Any) = error("cannot set user object")

  override def getAllowsChildren = allowsChildren

  override def getParent = parent.getOrElse(null)

  override def setParent(newParent: MutableTreeNode) = parent = Some(newParent)

  override def removeFromParent = parent match {
    case Some(x) => x.remove(this)
    case _ => ()
  }

}

trait GenericLeafNode[A] extends GenericTreeNode[A] {

  def isLeaf = true

  def allowsChildren = false;

  def numChildren: Int = 0

  override def getChildCount = 0

  def remove(node: MutableTreeNode) = error("cannot remove elemtnet from leaf node")

  def remove(index: Int) = error("cannot remove elemtnet from leaf node")

  def insert(child: MutableTreeNode, index: Int) = error("cannot insert element in to a leaf node")

  def children: java.util.Enumeration[TreeNode] = error("cannot insert element in to a leaf node")

  def getIndex(node: TreeNode) = error("cannot remove elemtnet from leaf node")

  def getChildAt(childIndex: Int) = error("cannot remove elemtnet from leaf node")
}

trait GenericBranchNode[A, B] extends GenericTreeNode[A] {
  def childNodes: ArrayBuffer[GenericTreeNode[B]]

  def indexOf(node: TreeNode) = childNodes.indexOf(node)

  def allowsChildren: Boolean

  def isLeaf = !allowsChildren

  def numChildren: Int = childNodes.size

  def children: java.util.Enumeration[TreeNode] = {
    new TreeEnum(childNodes).asInstanceOf[java.util.Enumeration[TreeNode]]

  }



  override def getAllowsChildren = allowsChildren

  override def getIndex(node: TreeNode) = indexOf(node)

  override def getParent = parent.getOrElse(null)

  override def getChildCount = numChildren

  override def getChildAt(childIndex: Int) = childNodes(childIndex)

  override def remove(node: MutableTreeNode) = childNodes - node.asInstanceOf[GenericTreeNode[B]]

  override def remove(index: Int) = childNodes.remove(index)

  override def insert(child: MutableTreeNode, index: Int) = childNodes.insert(index, child.asInstanceOf[GenericTreeNode[B]])

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

class FolderTreeNode(override var parent: Option[MutableTreeNode], override val value: FileObject, val selector: FileSelector) extends GenericBranchNode[FileObject, FileObject] {
  self =>
  var cachedNodes: Option[ArrayBuffer[GenericTreeNode[FileObject]]] = None

  def childNodes: ArrayBuffer[GenericTreeNode[FileObject]] = {
    cachedNodes match {
      case Some(x) => x
      case None => {
        val res = ArrayBuffer(value.findFiles(selector): _*)
        res.map(child => {
          child.getType match {
            case FileType.FOLDER => new FolderTreeNode(Some(self), child, self.selector)
            case FileType.FILE => new FileTreeNode(Some(self), child)
          }
        })

      }
    }
  }

  def allowsChildren = true

  override def toString: String = {

    /*try {
      val uri: URI = new URI(value.getName.getURI)
      uri.getHost + ":" + uri.getPath
    }
    catch {
      case e: URISyntaxException => {
        return value.getName.getURI
      }
    } */

    value.name
  }
}




class FileTreeNode(override var parent: Option[MutableTreeNode], override val value: FileObject) extends GenericLeafNode[FileObject] {
  self =>


  override def toString: String = {
    /*try {
      val uri: URI = new URI(value.getName.getURI)
      uri.getHost + ":" + uri.getPath
    }
    catch {
      case e: URISyntaxException => {
        return value.getName.getURI
      }
    }
  }*/
    value.name
  }

}

object TreeTest extends SwingApplication {
  def startup(args: Array[String]) = {
    val jf = new JFrame()
    val rootNode: DefaultMutableTreeNode = new DefaultMutableTreeNode("File Systems")
    val treeModel = new DefaultTreeModel(rootNode)
    val fileTree: JTree = new JTree(treeModel)

    val node = new FolderTreeNode(None, RichFileObject("c:/javacode"), NonRecursiveFileSelector)
    val root: MutableTreeNode = treeModel.getRoot.asInstanceOf[MutableTreeNode]
    treeModel.insertNodeInto(node, root, root.getChildCount)

    jf.add(fileTree, BorderLayout.CENTER)
    jf.setSize(800, 800)
    println("setting frame visible")
    jf.setVisible(true)

  }
}
