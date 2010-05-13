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

trait ImageSource[T <: IImageData] {

  def dataFile: FileObject

  def headerFile: FileObject

  def fileFormat: String

  def load(listener: Option[ProgressListener] = None)

}

trait ImageSource3D extends ImageSource[IImageData3D] {

}