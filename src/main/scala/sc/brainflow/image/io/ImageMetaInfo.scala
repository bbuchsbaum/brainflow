package sc.brainflow.image.io


import _root_.java.nio.ByteOrder
import _root_.org.apache.commons.vfs.FileObject

import brainflow.utils.DataType
import brainflow.image.anatomy.Anatomy3D
import brainflow.image.space.ImageMapping3D

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Apr 7, 2010
 * Time: 10:53:13 AM
 * To change this template use File | Settings | File Templates.
 */



trait ImageMetaInfo {

  val attributes: Map[String,Any]
  
  def dataFile: FileObject

  def headerFile: FileObject

  def anatomy: Anatomy3D

  def dataType: Seq[DataType]

  def apply(key: String) : Option[Any] = attributes.get(key)

  def dimensions: Seq[Int]

  def spacing: Seq[Double]

  def label: String

  def volumeLabels: Seq[String]

  def intercept: Seq[Double]

  def scaleFactor: Seq[Double]

  def numVolumes: Int

  def byteOffset: Int

  def endian: ByteOrder

  def coordinateMapping: ImageMapping3D

  def createDataReader(index: Int): DataReader[_]

  def createDataReader(index: Seq[Int]) : Seq[DataReader[_]] = {
    for (i <- index) yield createDataReader(i)
  }

  
}



/*class ImageMetaInfo3D(val dataFile: Option[FileObject], val headerFile: Option[FileObject], val dim: Array[Int], val spacing: Dim3[Double],
                                             val index: Int = 0, val byteOffset: Int = 0, val scaleFactor: Double = 1, val intercept: Double=0,
                                             val endian: ByteOrder = ByteOrder.nativeOrder, val label: String = "untitled") extends ImageFileMetaInfo[Dim3] {
  


}

class ImageMetaInfo4D(val dataFile: Option[FileObject], val headerFile: Option[FileObject], val dim: Dim4[Int], val spacing: Dim4[Double],
                                             val index: Int = 0, val byteOffset: Int = 0, val scaleFactor: Double = 1, val intercept: Double=0,
                                             val endian: ByteOrder = ByteOrder.nativeOrder, val label: String = "untitled") extends ImageFileMetaInfo[Dim4] {



}  */





