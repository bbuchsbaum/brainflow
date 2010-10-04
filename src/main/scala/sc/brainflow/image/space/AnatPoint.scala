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

  def findAxis(axis: AnatomicalAxis, ignoreDirection: Boolean = false) = {
    val id = whichAxis(axis, ignoreDirection)
    require(id >= 0)
    axes(id)
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

trait ImageAxisPoint1D[T] extends AnatPoint1D[T,ImageAxis] {


  def numDimensions = 1

}

trait ImageAxisPoint2D[T] extends AnatPoint2D[T,ImageAxis] {

  def space: IImageSpace2D

  def numDimensions = 2

  lazy val xaxis = space.getImageAxis(Axis.X_AXIS)

  lazy val yaxis = space.getImageAxis(Axis.Y_AXIS)



}

trait ImageAxisPoint3D[T] extends AnatPoint3D[T,ImageAxis] {

  def space: IImageSpace3D

  def numDimensions = 3

  lazy val xaxis = space.getImageAxis(Axis.X_AXIS)

  lazy val yaxis = space.getImageAxis(Axis.Y_AXIS)

  lazy val zaxis = space.getImageAxis(Axis.Z_AXIS)


}




case class GridPoint1D(x: Double, xaxis: ImageAxis) extends ImageAxisPoint1D[Double] {


  def reverse: GridPoint1D =  new GridPoint1D(xaxis.getNumSamples - x, xaxis.flip)

}


case class GridPoint2D(x: Double, y: Double, space: IImageSpace2D) extends ImageAxisPoint2D[Double] {

  def toIndex = IndexPoint2D(x.toInt, y.toInt, space)

  def replace(gp: GridPoint1D) = {
    val axid = whichAxis(gp.xaxis.getAnatomicalAxis, true)
    require(axid >= 0)

    val gp1 = if (axes(axid).getAnatomicalAxis.sameDirection(gp.xaxis.getAnatomicalAxis)) {
      gp.reverse
    } else {
      gp
    }

    axid match {
      case 0 => GridPoint2D(gp1.x, y, space)
      case 1 => GridPoint2D(x, gp1.x, space)
    }

  }

  def apply(axis: AnatomicalAxis) = {
    val axid = whichAxis(axis, true)
    require(axid >= 0)
    val ret = axid match {
      case 0 => GridPoint1D(x, xaxis)
      case 1 => GridPoint1D(y, yaxis)
    }

    if (axis.sameDirection(axes(axid).getAnatomicalAxis)) ret else ret.reverse
  }

}


case class GridPoint3D(x: Double, y: Double, z:Double, space: IImageSpace3D) extends ImageAxisPoint3D[Double] {

  def toIndex = IndexPoint3D(x.toInt, y.toInt, z.toInt, space)

  def replace(gp: GridPoint1D) = {
    val axid = whichAxis(gp.xaxis.getAnatomicalAxis, true)

    val gp1 = if (!axes(axid).getAnatomicalAxis.sameDirection(gp.xaxis.getAnatomicalAxis)) {
      gp.reverse
    } else {
      gp
    }

    axid match {
      case 0 => GridPoint3D(gp1.x, y, z, space)
      case 1 => GridPoint3D(x, gp1.x, z, space)
      case 2 => GridPoint3D(x, y, gp1.x, space)
    }

  }


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

case class RealPoint1D(x: Double, xaxis: ImageAxis) extends ImageAxisPoint1D[Double] {


  def reverse: RealPoint1D =  new RealPoint1D(xaxis.getMaximum - x, xaxis.flip)


}


case class RealPoint2D(x: Double, y: Double, space: IImageSpace2D) extends ImageAxisPoint2D[Double] {


  def apply(axis: AnatomicalAxis) = {
    val axid = whichAxis(axis, true)
    val ret = axid match {
      case 0 => RealPoint1D(x, xaxis)
      case 1 => RealPoint1D(y, yaxis)
    }

    if (axis.sameDirection(axes(axid).getAnatomicalAxis)) ret else ret.reverse

  }


}

case class RealPoint3D(x: Double, y: Double, z:Double, space: IImageSpace3D) extends ImageAxisPoint3D[Double] {


  def apply(axis: AnatomicalAxis) = {
    val axid = whichAxis(axis, true)
    val ret = axid match {
      case 0 => RealPoint1D(x, xaxis)
      case 1 => RealPoint1D(y, yaxis)
      case 2 => RealPoint1D(z, zaxis)
    }

    if (axis.sameDirection(axes(axid).getAnatomicalAxis)) ret else ret.reverse
  }


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


case class IndexPoint1D(x: Int, xaxis: ImageAxis) extends ImageAxisPoint1D[Int] {


  def reverse: IndexPoint1D =  new IndexPoint1D((xaxis.getNumSamples-1) - x, xaxis.flip)

  def toReal = RealPoint1D(x*xaxis.getSpacing + xaxis.getSpacing/2 + xaxis.getMinimum, xaxis)

  def toGrid = GridPoint1D(x + xaxis.getSpacing/2, xaxis)

}

case class IndexPoint2D(x: Int, y: Int, space: IImageSpace2D) extends ImageAxisPoint2D[Int] {



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


case class IndexPoint3D(x: Int, y: Int, z: Int, space: IImageSpace3D) extends ImageAxisPoint3D[Int] {


  def toReal = RealPoint3D(x*xaxis.getSpacing + xaxis.getSpacing/2 + xaxis.getMinimum,
                           y*yaxis.getSpacing + yaxis.getSpacing/2 + yaxis.getMinimum,
                           z*zaxis.getSpacing + zaxis.getSpacing/2 + zaxis.getMinimum, space)

  def toGrid = GridPoint3D(x + xaxis.getSpacing/2, y + yaxis.getSpacing/2,  z + zaxis.getSpacing/2, space)

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














