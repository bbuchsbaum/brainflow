package sc.bflow.image.io

import brainflow.utils.ProgressListener
import brainflow.image.space.IImageSpace3D
import brainflow.image.data.{BasicImageData3D, IImageData3D}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jun 25, 2010
 * Time: 2:20:30 PM
 * To change this template use File | Settings | File Templates.
 */

trait ImageMaker3D[T]  {

  def make(data: Array[T], space: IImageSpace3D) : IImageData3D
}



object BasicImageFactory {

  implicit object BasicImageByteFactory3D extends ImageMaker3D[Byte] {
    def make(data: Array[Byte], space: IImageSpace3D) = new BasicImageData3D.Byte(space, data)
  }

  implicit object BasicImageShortFactory3D extends ImageMaker3D[Short] {
    def make(data: Array[Short], space: IImageSpace3D) = new BasicImageData3D.Short(space, data)
  }
  implicit object BasicImageFloatFactory3D extends ImageMaker3D[Float] {
    def make(data: Array[Float], space: IImageSpace3D) = new BasicImageData3D.Float(space, data)
  }
  implicit object BasicImageIntFactory3D extends ImageMaker3D[Int] {
    def make(data: Array[Int], space: IImageSpace3D) = new BasicImageData3D.Int(space, data)
  }
  
  implicit object BasicImageDoubleFactory3D extends ImageMaker3D[Double] {
    def make(data: Array[Double], space: IImageSpace3D) = new BasicImageData3D.Double(space, data)
  }

  
  //implicit object BasicImageLongFactory3D extends ImageFactory3D[Long] {
  //  def make(data: Array[Long], space: IImageSpace3D) = new BasicImageData3D.Long(space, data)
  //}

}
