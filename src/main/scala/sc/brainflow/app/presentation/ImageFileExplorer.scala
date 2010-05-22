package sc.brainflow.app.presentation

import collection.mutable.ArrayBuffer
import boxwood.io.RichFileObject._
import sc.brainflow.controls.FileExplorer
import org.apache.commons.vfs.{FileType, FileSelectInfo, FileSelector, FileObject}
import sc.brainflow.image.io.ImageFileDescriptors
import sc.brainflow.swing.tree.{GenericLeafNode, GenericTreeNode}
import javax.swing.tree.MutableTreeNode
import brainflow.image.io.{BrainIO, IImageSource, ImageSource3D, ImageInfo}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 4, 2010
 * Time: 6:45:03 PM
 * To change this template use File | Settings | File Templates.
 */

class ImageFileExplorer(root: FileObject) extends FileExplorer(root) {

  override val selector = new FilteredFileSelector((fileInfo: FileSelectInfo) => ImageFileDescriptors.supportedHeaderFile(fileInfo.getFile.name))


  override def makeNode(fileObject: FileObject) = {
    val reader = ImageFileDescriptors.createInfoReader(fileObject)

    reader match {
      case Some(x) => x.readInfo
    }

  //}


  def makeSource(infoList: List[ImageInfo]) = {

  }
}

}      ////
case class DataSourceNode3D(override var parent: Option[MutableTreeNode] = None, override val value: ImageSource3D) extends GenericLeafNode[ImageSource3D] {

}

class FilteredFileSelector(val filter: (FileSelectInfo => Boolean)) extends FileSelector {
  def traverseDescendents(fileInfo: FileSelectInfo) = fileInfo.getDepth == 0

  def includeFile(fileInfo: FileSelectInfo) = {
    if (fileInfo.getDepth == 0) {
      false
    } else if (fileInfo.getFile.getType == FileType.FOLDER) {
      true
    } else if (filter(fileInfo)) {
      true
    } else {
      false
    }
  }
}



