package sc.brainflow.image.space
import sc.brainflow._

import brainflow.image.axis.{ImageAxis, CoordinateAxis}
import brainflow.image.space.{Axis, ICoordinateSpace3D, IImageSpace3D}
import brainflow.image.anatomy.AnatomicalAxis

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

  def value(axis: AnatomicalAxis, flip: Boolean) = {
    val index = axes.findIndexOf(x => axis.sameAxis(x.getAnatomicalAxis))
    require(index >= 0)
    coords(index)
  }

}




case class GridPoint1D(x: Double, xaxis: ImageAxis) extends AnatPoint1D[Double, ImageAxis] {

  def numDimensions = 1

  def reverse: GridPoint1D =  new GridPoint1D(xaxis.getNumSamples - x, xaxis.flip)



}


case class GridPoint2D(x: Double, y: Double, xaxis: ImageAxis, yaxis: ImageAxis) extends AnatPoint2D[Double, ImageAxis] {

  def numDimensions = 2


}


case class GridPoint3D(x: Double, y: Double, z:Double,
                  xaxis: ImageAxis, yaxis: ImageAxis, zaxis: ImageAxis) extends AnatPoint3D[Double, ImageAxis] {

  def numDimensions = 3





}

object GridPoint3D {

  def apply(x: Double, y: Double, z: Double, space: IImageSpace3D) : GridPoint3D = {
    GridPoint3D(x, y, z, space.x_axis, space.y_axis, space.z_axis)

  }

  def fromReal(x: Double, y: Double, z: Double, space: IImageSpace3D)  : GridPoint3D = {
    GridPoint3D(space.x_axis.gridPosition(x), space.y_axis.gridPosition(y), space.z_axis.gridPosition(z), space.x_axis, space.y_axis, space.z_axis)
  }

  def fromReal(x: Double, y: Double, z: Double, xaxis: ImageAxis, yaxis: ImageAxis, zaxis: ImageAxis) = {
    GridPoint3D(xaxis.gridPosition(x), yaxis.gridPosition(y), zaxis.gridPosition(z), xaxis, yaxis, zaxis)
  }

  implicit def realToGrid(pt: RealPoint3D)  : GridPoint3D = {
    GridPoint3D(pt.xaxis.gridPosition(pt.x), pt.yaxis.gridPosition(pt.y), pt.zaxis.gridPosition(pt.z), pt.xaxis, pt.yaxis, pt.zaxis)
  }
}

case class RealPoint1D(x: Double, xaxis: ImageAxis) extends AnatPoint1D[Double, ImageAxis] {

  def numDimensions = 1

  def reverse: RealPoint1D =  new RealPoint1D(xaxis.getMaximum - x, xaxis.flip)


}


case class RealPoint2D(x: Double, y: Double,
                  xaxis: ImageAxis, yaxis: ImageAxis) extends AnatPoint2D[Double, ImageAxis] {

  def numDimensions = 2


}

case class RealPoint3D(x: Double, y: Double, z:Double,
                  xaxis: ImageAxis, yaxis: ImageAxis, zaxis: ImageAxis) extends AnatPoint3D[Double, ImageAxis] {

  def numDimensions = 3


}

object RealPoint3D {

  def fromGrid(x: Double, y: Double, z: Double, space: IImageSpace3D) : RealPoint3D = {
    fromGrid(x,y,z,space.x_axis, space.y_axis, space.z_axis)
  }

  def fromGrid(x: Double, y: Double, z: Double, xaxis: ImageAxis, yaxis: ImageAxis, zaxis: ImageAxis) : RealPoint3D = {
    RealPoint3D(x*xaxis.getSpacing + xaxis.getMinimum,
      y*yaxis.getSpacing + yaxis.getMinimum,
      z*zaxis.getSpacing + zaxis.getMinimum, xaxis, yaxis, zaxis)
  }

  implicit def gridToReal(pt: GridPoint3D) = {
    fromGrid(pt.x, pt.y, pt.z, pt.xaxis, pt.yaxis, pt.zaxis)
  }
}

object IndexPoint3D {

  def apply(x: Int, y: Int, z: Int, space: IImageSpace3D) : IndexPoint3D = {
    IndexPoint3D(x,y,z, space.x_axis, space.y_axis, space.z_axis)
  }
}


case class IndexPoint1D(x: Int, xaxis: ImageAxis) extends AnatPoint1D[Int, ImageAxis] {

  def numDimensions = 1

  def reverse: IndexPoint1D =  new IndexPoint1D((xaxis.getNumSamples-1) - x, xaxis.flip)



}

case class IndexPoint2D(x: Int, y: Int, xaxis: ImageAxis, yaxis: ImageAxis) extends AnatPoint2D[Int, ImageAxis] {

  def numDimensions = 2


}


case class IndexPoint3D(x: Int, y: Int, z: Int,
                  xaxis: ImageAxis, yaxis: ImageAxis, zaxis: ImageAxis) extends AnatPoint3D[Int, ImageAxis] {

  def numDimensions = 3


}














