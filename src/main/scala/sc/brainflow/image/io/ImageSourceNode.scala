package sc.brainflow.image.io

import _root_.brainflow.image.data.{IImageData3D, IImageData}
import _root_.brainflow.utils.ProgressListener
import _root_.org.apache.commons.vfs.FileObject

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Apr 6, 2010
 * Time: 9:41:06 PM
 * To change this template use File | Settings | File Templates.
 */


sealed trait ImageSourceNode[T <: IImageData] {

  def metaInfo : ImageMetaInfo

  def label : String

}
case class ImageSource3D(val index: Int = 0, val metaInfo: ImageMetaInfo) extends ImageSourceNode[IImageData3D] {
  def load(listener: Option[ProgressListener] = None) : IImageData3D = {
    
  }
}

case class ImageSourceList[T](val children : Seq[ImageSource3D]) extends ImageSourceNode[IImageData3D] {
  def load(listener: Option[ProgressListener] = None) : List[IImageData3D]

  def subLabels = children.map(_.label)
}




