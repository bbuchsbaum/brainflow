package sc.brainflow.image.data

import brainflow.image.data.IImageData3D
import brainflow.image.space.Axis
import brainflow.image.interpolation.InterpolationFunction3D

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 15, 2010
 * Time: 1:17:46 PM
 * To change this template use File | Settings | File Templates.
 */




object BrainVolume {

  implicit def wrap(x: IImageData3D) = new BrainVolumeWrapper(x)

   
}

class BrainVolumeWrapper(val wrapped: IImageData3D) extends IImageData3D with BrainVolumeOps {
  
  final def getImageSpace = wrapped.getImageSpace

  final def getImageLabel = wrapped.getImageLabel

  final def getImageInfo = wrapped.getImageInfo

  final def minValue = wrapped.minValue

  final def maxValue = wrapped.maxValue

  final def getDimension(axisNum: Axis) = wrapped.getDimension(axisNum)

  final def getAnatomy = wrapped.getAnatomy

  final def getDataType = wrapped.getDataType

  final def createBuffer(clear: Boolean) = wrapped.createBuffer(clear)

  final def indexToGrid(idx: Int) = wrapped.indexToGrid(idx)

  final def worldValue(wx: Float, wy: Float, wz: Float, interp: InterpolationFunction3D) = wrapped.worldValue(wx,wy,wz,interp)

  final def dim = wrapped.dim

  final def length = wrapped.length

  final def value(i: Int) = wrapped.value(i)

  final def valueIterator = wrapped.valueIterator

  final def indexOf(i: Int, j: Int, k: Int) = wrapped.indexOf(i,j,k)

  final def value(i: Int, j: Int, k: Int) = wrapped.value(i,j,k)

  final def value(x: Float, y: Float, z: Float, interp: InterpolationFunction3D) = wrapped.value(x,y,z, interp)
}