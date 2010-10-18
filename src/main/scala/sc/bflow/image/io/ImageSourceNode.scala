package sc.bflow.image.io

import _root_.brainflow.image.data.{IImageData3D, IImageData}
import _root_.org.apache.commons.vfs.FileObject
import BasicImageFactory._
import brainflow.image.anatomy.AnatomicalAxis
import brainflow.image.axis.ImageAxis
import brainflow.image.io.ImageInfo
import brainflow.utils.{IDimension, ProgressListener}
import brainflow.image.space.{ImageSpace3D, IImageSpace3D}

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Apr 6, 2010
 * Time: 9:41:06 PM
 * To change this template use File | Settings | File Templates.
 */


sealed trait ImageSourceNode {


  //def metaInfo : ImageMetaInfo

  def label: String

  //def numChildren: Int

}

trait Branch[T <: ImageSourceNode] {

  def children: Seq[T]

  def numChildren: Int = children.size

}

trait Leaf


case class ImageSource3D(index: Int = 0, metaInfo: ImageMetaInfo) extends ImageSourceNode with Leaf  {

  private var image: Option[IImageData3D] = None

  private def make[T: ImageMaker3D](t: Array[T], space: IImageSpace3D) = {
    implicitly[ImageMaker3D[T]].make(t, space)
  }

  def numChildren = 0

  def makeImageSpace = {

    val aaxes: Array[AnatomicalAxis] = metaInfo.anatomy.getAnatomicalAxes

    def createImageAxes(volumeDim: Seq[Int], spacing: Seq[Double]) = {
      for{
        i <- 0 until volumeDim.size
        extent = volumeDim(i).intValue * spacing(i).doubleValue
      } yield (new ImageAxis(-extent / 2, extent / 2, aaxes(i), volumeDim(i)))

    }

    val iaxes: Seq[ImageAxis] = createImageAxes(metaInfo.dimensions.slice(0, 3), metaInfo.spacing)
    new ImageSpace3D(iaxes(0), iaxes(1), iaxes(2), metaInfo.coordinateMapping)

  }


  def load(numChunks: Int = 10, listener: Option[ProgressListener] = None): IImageData3D = {

    def _load: IImageData3D = {
      val dfile = metaInfo.dataFile
      val ispace = makeImageSpace
      val dataReader = metaInfo.createDataReader(index)

      val ret = dataReader match {
        case r: ByteReader => make(r.read(numChunks, listener), ispace)
        case r: ShortReader => make(r.read(numChunks, listener), ispace)
        case r: IntReader => make(r.read(numChunks, listener), ispace)
        case r: FloatReader => make(r.read(numChunks, listener), ispace)
        case r: DoubleReader => make(r.read(numChunks, listener), ispace)
        case _ => error("cannot read image of type " + metaInfo.dataType(index))
      }

      image = Some(ret)
      ret


    }

    image match {
      case Some(data) => data
      case None => _load
    }

  }

  def label = if (metaInfo.numVolumes == 1) metaInfo.label else metaInfo.label + "#" + index
}

case class ImageSourceSeq3D(val label: String, override val children: Seq[ImageSource3D]) extends ImageSourceNode with Branch[ImageSource3D] {


  def load(numChunks: Int = 10, listener: Option[ProgressListener] = None): Seq[IImageData3D] = {
    children.map(_.load(numChunks, listener))
  }

  override def numChildren = children.size

  def subLabels = children.map(_.label)

}

case class ImageSourceGroup(val label: String, override val children: Seq[ImageSourceNode]) extends ImageSourceNode with Branch[ImageSourceNode] {
  override def numChildren = children.size
}






