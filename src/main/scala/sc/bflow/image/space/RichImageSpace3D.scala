package sc.bflow.image.space

import brainflow.image.space.{IImageSpace2D, Axis, IImageSpace3D}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 30, 2010
 * Time: 10:30:58 PM
 * To change this template use File | Settings | File Templates.
 */

object RichImageSpace3D {
  implicit def wrap(x: IImageSpace3D) = new RichImageSpace3D(x)

}

object RichImageSpace2D {
  implicit def wrap(x: IImageSpace2D) = new RichImageSpace2D(x)

}


class RichImageSpace3D(x: IImageSpace3D) extends Proxy {

  def self: Any = x

  val x_axis = x.getImageAxis(Axis.X_AXIS)
  val y_axis = x.getImageAxis(Axis.Y_AXIS)
  val z_axis = x.getImageAxis(Axis.Z_AXIS)

  val xdim = x.getDimension(Axis.X_AXIS)
  val ydim = x.getDimension(Axis.Y_AXIS)
  val zdim = x.getDimension(Axis.Z_AXIS)

}

class RichImageSpace2D(x: IImageSpace2D) extends Proxy {

  def self: Any = x

  lazy val x_axis = x.getImageAxis(Axis.X_AXIS)
  lazy val y_axis = x.getImageAxis(Axis.Y_AXIS)

  lazy val xdim = x.getDimension(Axis.X_AXIS)
  lazy val ydim = x.getDimension(Axis.Y_AXIS)

}