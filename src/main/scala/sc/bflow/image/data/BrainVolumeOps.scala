package sc.bflow.image.data

import brainflow.image.data.{BasicImageData3D, IImageData3D}
import brainflow.image.space.IImageSpace3D

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 15, 2010
 * Time: 12:26:57 PM
 * To change this template use File | Settings | File Templates.
 */

trait BrainVolumeOps extends IImageData3D {
  self: IImageData3D =>

  private def build(space: IImageSpace3D, ret: Array[Double]) = new BasicImageData3D.Double(self.getImageSpace, ret)

  def +(x: Double) = {
    val ret = new Array[Double](self.getImageSpace.getNumSamples)
    val iter = self.valueIterator
    while (iter.hasNext) {
      val tmp = iter.next + x
      ret(iter.index) = tmp
    }

    build(self.getImageSpace, ret)

  }

  def +(x: IImageData3D) = {
    require(x.getImageSpace == self.getImageSpace)
    val ret = new Array[Double](self.getImageSpace.getNumSamples)

    for (i <- 0 until ret.length) {
      ret(i) = self.value(i) + x.value(i)
    }

    build(self.getImageSpace, ret)
  }

  def -(x: Double) = {
    val ret = new Array[Double](self.getImageSpace.getNumSamples)
    val iter = self.valueIterator
    while (iter.hasNext) {
      val tmp = iter.next - x
      ret(iter.index) = tmp
    }

    build(self.getImageSpace, ret)

  }

  def -(x: IImageData3D) = {
    require(x.getImageSpace == self.getImageSpace)
    val ret = new Array[Double](self.getImageSpace.getNumSamples)

    for (i <- 0 until ret.length) {
      ret(i) = self.value(i) - x.value(i)
    }

    build(self.getImageSpace, ret)
  }

  def /(x: Double) = {
    val ret = new Array[Double](self.getImageSpace.getNumSamples)
    val iter = self.valueIterator
    while (iter.hasNext) {
      val tmp = iter.next / x
      ret(iter.index) = tmp
    }

    build(self.getImageSpace, ret)

  }

  def /(x: IImageData3D) = {
    require(x.getImageSpace == self.getImageSpace)

    val ret = new Array[Double](self.getImageSpace.getNumSamples)

    for (i <- 0 until ret.length) {
      ret(i) = self.value(i) / x.value(i)
    }

    build(self.getImageSpace, ret)
  }


  def *(x: Double) = {
    val ret = new Array[Double](self.getImageSpace.getNumSamples)
    val iter = self.valueIterator
    while (iter.hasNext) {
      val tmp = iter.next * x
      ret(iter.index) = tmp
    }

    build(self.getImageSpace, ret)

  }

  def *(x: IImageData3D) = {
    require(x.getImageSpace == self.getImageSpace)

    val ret = new Array[Double](self.getImageSpace.getNumSamples)

    for (i <- 0 until ret.length) {
      ret(i) = self.value(i) * x.value(i)
    }

    build(self.getImageSpace, ret)
  }


}
