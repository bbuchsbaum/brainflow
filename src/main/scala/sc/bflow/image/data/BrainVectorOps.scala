package sc.bflow.image.data

import brainflow.image.data.{BasicImageData3D, BasicImageDataVector3D, IImageDataVector3D, IImageData3D}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 12, 2010
 * Time: 8:25:57 PM
 * To change this template use File | Settings | File Templates.
 */

trait  BrainVectorOps {
  self: IImageDataVector3D =>


  def collapse(func: (Seq[Double] => Double)) : IImageData3D = {
    val ret = new Array[Double](self.getImageSpace.getNumSamples)
    for (i <- 0 until self.getImageSpace.getNumSamples) {
      ret(i) = func(extractVector(i))
    }

    new BasicImageData3D.Double(self.getImageSpace, ret)

  }

  def extractVector(spatialIndex: Int) : Array[Double] = {
    val ret = new Array[Double](self.getNumVolumes)
    for (i <- 0 until self.getNumVolumes) {
      ret(i) = self.value(spatialIndex, i)
    }

    ret
  }
}

