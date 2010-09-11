package sc.brainflow.image.space
import sc.brainflow._

import brainflow.image.axis.{ImageAxis, CoordinateAxis}
import brainflow.image.anatomy.AnatomicalAxis
import brainflow.image.space._

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 6, 2010
 * Time: 6:46:17 PM
 * To change this template use File | Settings | File Templates.
 */

trait AnatPoint[T, U <: CoordinateAxis] {

  def numDimensions: Int

  def axes: Seq[U]

  def coords: Seq[T]

  def whichAxis(axis: AnatomicalAxis, ignoreDirection: Boolean = true) = {
    val index = if (ignoreDirection) {
      axes.findIndexOf(x => axis.sameAxis(x.getAnatomicalAxis))
    } else {
      axes.findIndexOf(_.getAnatomicalAxis == axis)
    }

    require(index >= 0)

    index
  }
}


trait AnatPoint1D[T, U <: CoordinateAxis] extends AnatPoint[T, U] {

  def x: T

  def xaxis: U

  lazy val axes = Seq[U](xaxis)

  lazy val coords = Seq[T](x)

}

trait AnatPoint2D[T, U <: CoordinateAxis] extends AnatPoint1D[T, U] {

  def y: T

  def xaxis: U

  def yaxis: U

  override lazy val axes = Seq[U](xaxis, yaxis)

  override lazy val coords = Seq[T](x,y)


}

trait AnatPoint3D[T, U <: CoordinateAxis] extends AnatPoint2D[T,U] {

  def z: T

  def xaxis: U

  def yaxis: U

  def zaxis: U

  override lazy val axes = Seq[U](xaxis, yaxis, zaxis)

  override lazy val coords = Seq[T](x,y,z)


}




case class GridPoint1D(x: Double, xaxis: ImageAxis) extends AnatPoint1D[Double, ImageAxis] {

  def numDimensions = 1

  def reverse: GridPoint1D =  new GridPoint1D(xaxis.getNumSamples - x, xaxis.flip)



}


case class GridPoint2D(x: Double, y: Double, space: IImageSpace2D) extends AnatPoint2D[Double, ImageAxis] {

  def numDimensions = 2

  def xaxis = space.getImageAxis(Axis.X_AXIS)

  def yaxis = space.getImageAxis(Axis.Y_AXIS)

  def toIndex = IndexPoint2D(xaxis.nearestSample(x), yaxis.nearestSample(y), space)

  def apply(axis: AnatomicalAxis) = {
    val axid = whichAxis(axis, true)
    val ret = axid match {
      case 0 => GridPoint1D(x, xaxis)
      case 1 => GridPoint1D(y, yaxis)
    }

    if (axis.sameDirection(axes(axid).getAnatomicalAxis)) ret else ret.reverse
  }

}


case class GridPoint3D(x: Double, y: Double, z:Double, space: IImageSpace3D) extends AnatPoint3D[Double, ImageAxis] {

  def numDimensions = 3

  def xaxis = space.getImageAxis(Axis.X_AXIS)

  def yaxis = space.getImageAxis(Axis.Y_AXIS)

  def zaxis = space.getImageAxis(Axis.Z_AXIS)

  def toIndex = IndexPoint3D(xaxis.nearestSample(x), yaxis.nearestSample(y), zaxis.nearestSample(z), space)

  def apply(axis: AnatomicalAxis) = {
    val axid = whichAxis(axis, true)
    val ret = axid match {
      case 0 => GridPoint1D(x, xaxis)
      case 1 => GridPoint1D(y, yaxis)
      case 2 => GridPoint1D(z, zaxis)
    }

    if (axis.sameDirection(axes(axid).getAnatomicalAxis)) ret else ret.reverse
  }

  //def toWorld: RealPoint3D = {
  //  val pt = space.gridToWorld(x.toFloat, y.toFloat, z.toFloat)
  //  return RealPoint3D(space.getMapping.getWorldAnatomy, pt(0), pt(1), pt(2))
 // }

}

object GridPoint3D {


  def fromWorld(x: Double, y: Double, z: Double, space: IImageSpace3D): GridPoint3D = {
    val grid = space.worldToGrid(x.toFloat, y.toFloat, z.toFloat)
    return new GridPoint3D(grid(0), grid(1), grid(2), space)
  }

  def fromReal(x: Double, y: Double, z: Double, space: IImageSpace3D)  : GridPoint3D = {
    GridPoint3D(space.x_axis.gridPosition(x), space.y_axis.gridPosition(y), space.z_axis.gridPosition(z), space)
  }

  def fromReal(x: Double, y: Double, z: Double, xaxis: ImageAxis, yaxis: ImageAxis, zaxis: ImageAxis) = {
    GridPoint3D(xaxis.gridPosition(x), yaxis.gridPosition(y), zaxis.gridPosition(z), new ImageSpace3D(xaxis, yaxis, zaxis))
  }

  implicit def realToGrid(pt: RealPoint3D)  : GridPoint3D = {
    GridPoint3D(pt.xaxis.gridPosition(pt.x), pt.yaxis.gridPosition(pt.y), pt.zaxis.gridPosition(pt.z), pt.space)
  }
}

case class RealPoint1D(x: Double, xaxis: ImageAxis) extends AnatPoint1D[Double, ImageAxis] {

  def numDimensions = 1

  def reverse: RealPoint1D =  new RealPoint1D(xaxis.getMaximum - x, xaxis.flip)


}


case class RealPoint2D(x: Double, y: Double, space: IImageSpace2D) extends AnatPoint2D[Double, ImageAxis] {

  def numDimensions = 2

  def xaxis = space.getImageAxis(Axis.X_AXIS)

  def yaxis = space.getImageAxis(Axis.Y_AXIS)

  def apply(axis: AnatomicalAxis) = {
    val axid = whichAxis(axis, true)
    val ret = axid match {
      case 0 => RealPoint1D(x, xaxis)
      case 1 => RealPoint1D(y, yaxis)
    }

    if (axis.sameDirection(axes(axid).getAnatomicalAxis)) ret else ret.reverse

  }


}

case class RealPoint3D(x: Double, y: Double, z:Double, space: IImageSpace3D) extends AnatPoint3D[Double, ImageAxis] {

  def numDimensions = 3

  def xaxis = space.getImageAxis(Axis.X_AXIS)

  def yaxis = space.getImageAxis(Axis.Y_AXIS)

  def zaxis = space.getImageAxis(Axis.Z_AXIS)

  def apply(axis: AnatomicalAxis) = {
    val axid = whichAxis(axis, true)
    val ret = axid match {
      case 0 => RealPoint1D(x, xaxis)
      case 1 => RealPoint1D(y, yaxis)
      case 2 => RealPoint1D(z, zaxis)
    }

    if (axis.sameDirection(axes(axid).getAnatomicalAxis)) ret else ret.reverse
  }

  //def replace(pt: IndexPoint1D) = {
  //  val axid = whichAxis(pt.xaxis.getAnatomicalAxis, true)
  //  pt.xaxis.getAnatomicalAxis match {
  //    case 0 => RealPoint3D(pt.app)
  //
  //  }
  //}


}

object RealPoint3D {

  def fromGrid(x: Double, y: Double, z: Double, space: IImageSpace3D) : RealPoint3D = {
    RealPoint3D(x*space.x_axis.getSpacing + space.x_axis.getMinimum,
      y*space.y_axis.getSpacing + space.y_axis.getMinimum,
      z*space.z_axis.getSpacing + space.z_axis.getMinimum, space)

  }

  def fromGrid(x: Double, y: Double, z: Double, xaxis: ImageAxis, yaxis: ImageAxis, zaxis: ImageAxis) : RealPoint3D = {
      fromGrid(x,y,z,new ImageSpace3D(xaxis, yaxis, zaxis))
  }

  implicit def gridToReal(pt: GridPoint3D) = {
    fromGrid(pt.x, pt.y, pt.z, pt.space)
  }
}

object IndexPoint3D {

  def apply(x: Int, y: Int, z: Int,  xaxis: ImageAxis, yaxis: ImageAxis, zaxis: ImageAxis) : IndexPoint3D = {
    IndexPoint3D(x,y,z, new ImageSpace3D(xaxis, yaxis, zaxis))
  }
}


case class IndexPoint1D(x: Int, xaxis: ImageAxis) extends AnatPoint1D[Int, ImageAxis] {

  def numDimensions = 1

  def reverse: IndexPoint1D =  new IndexPoint1D((xaxis.getNumSamples-1) - x, xaxis.flip)

  def toGrid = GridPoint1D(x + xaxis.getSpacing/2, xaxis)

}

case class IndexPoint2D(x: Int, y: Int, space: IImageSpace2D) extends AnatPoint2D[Int, ImageAxis] {

  def numDimensions = 2

  def xaxis = space.getImageAxis(Axis.X_AXIS)

  def yaxis = space.getImageAxis(Axis.Y_AXIS)

  def toGrid = GridPoint2D(x + xaxis.getSpacing/2, y + yaxis.getSpacing/2, space)

  def apply(axis: AnatomicalAxis) = {
    val axid = whichAxis(axis, true)
    val ret = axid match {
      case 0 => IndexPoint1D(x, xaxis)
      case 1 => IndexPoint1D(y, yaxis)
    }
    if (axis.sameDirection(axes(axid).getAnatomicalAxis)) {
      ret
    } else {
      ret.reverse
    }
  }


}


case class IndexPoint3D(x: Int, y: Int, z: Int, space: IImageSpace3D) extends AnatPoint3D[Int, ImageAxis] {

  def numDimensions = 3

  def xaxis = space.getImageAxis(Axis.X_AXIS)

  def yaxis = space.getImageAxis(Axis.Y_AXIS)

  def zaxis = space.getImageAxis(Axis.Z_AXIS)

  def toGrid = GridPoint3D(x + xaxis.getSpacing/2, y + yaxis.getSpacing/2, z + zaxis.getSpacing/2, space)

  def apply(axis: AnatomicalAxis) = {
    val axid = whichAxis(axis, true)
    val ret = axid match {
      case 0 => IndexPoint1D(x, xaxis)
      case 1 => IndexPoint1D(y, yaxis)
      case 2 => IndexPoint1D(z, zaxis)
    }
    if (axis.sameDirection(axes(axid).getAnatomicalAxis)) {
      ret
    } else {
      ret.reverse
    }
  }


}














