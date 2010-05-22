package sc.brainflow.image.io


import _root_.java.nio.ByteOrder
import _root_.org.apache.commons.vfs.FileObject
import _root_.sc.brainflow.utils.{Dim4, Dim3, Dim}
import brainflow.image.io.ImageReader

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Apr 7, 2010
 * Time: 10:53:13 AM
 * To change this template use File | Settings | File Templates.
 */



trait ImageMetaInfo {

  
  
  def dataFile: Option[FileObject]

  

  def headerFile: Option[FileObject]

  def apply(key: String) : Option[Any] = attributeMap.get(key)

  def dimensions: Seq[Int]

  def spacing: Seq[Double]

  def label: String

  def byteOffset: Int

  def endian: ByteOrder

  def createImageReader: ImageReader

  

}



/*class ImageMetaInfo3D(val dataFile: Option[FileObject], val headerFile: Option[FileObject], val dim: Array[Int], val spacing: Dim3[Double],
                                             val index: Int = 0, val byteOffset: Int = 0, val scaleFactor: Double = 1, val intercept: Double=0,
                                             val endian: ByteOrder = ByteOrder.nativeOrder, val label: String = "untitled") extends ImageFileMetaInfo[Dim3] {
  


}

class ImageMetaInfo4D(val dataFile: Option[FileObject], val headerFile: Option[FileObject], val dim: Dim4[Int], val spacing: Dim4[Double],
                                             val index: Int = 0, val byteOffset: Int = 0, val scaleFactor: Double = 1, val intercept: Double=0,
                                             val endian: ByteOrder = ByteOrder.nativeOrder, val label: String = "untitled") extends ImageFileMetaInfo[Dim4] {



}  */





