package sc.bflow.core

import java.awt.{Rectangle, Graphics2D}
import sc.bflow.core.layer.ImageLayerRenderer3D
import sc.bflow.image.space.RichImageSpace3D._
import sc.bflow.image.space.RichImageSpace2D._
import scala.math._
import brainflow.image.rendering.RenderUtils
import brainflow.display.InterpolationType
import java.awt.geom.{AffineTransform, Rectangle2D}
import java.awt.image.{RenderedImage, AffineTransformOp, BufferedImage}
import scala.actors.Futures._
import scala.math._
import sc.bflow.image.space.GridPoint3D
import brainflow.image.anatomy.Anatomy3D

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 30, 2010
 * Time: 8:18:10 AM
 * To change this template use File | Settings | File Templates.
 */

trait ImageViewRenderer[T] {

  def renderers(slice: GridPoint3D, displayAnatomy: Anatomy3D): Seq[ImageLayerRenderer3D] = modelState.layers.map(_.createSliceRenderer(modelState.space, slice, displayAnatomy))

  def modelState: ImageViewModel

  def render(slice: GridPoint3D, displayAnatomy: Anatomy3D, region: Rectangle2D, outputFrame: Rectangle, interp: InterpolationType = InterpolationType.LINEAR) : T

  def render(slice: GridPoint3D, displayAnatomy: Anatomy3D, region: Rectangle2D) : T

  //def allTransparent = renderers.map(_.layer.opacity.value == 0 || _.l)

  def bounds(renderers:Seq[ImageLayerRenderer3D]) : Rectangle2D = {
    val minX = renderers.map(_.data.getImageSpace.x_axis.getMinimum).reduceLeft((x, y) => min(x,y))
    val minY = renderers.map(_.data.getImageSpace.y_axis.getMinimum).reduceLeft((x, y) => min(x,y))
    val maxX = renderers.map(_.data.getImageSpace.x_axis.getMaximum).reduceLeft((x, y) => max(x,y))
    val maxY = renderers.map(_.data.getImageSpace.y_axis.getMaximum).reduceLeft((x, y) => max(x,y))
    new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY)

  }

  def scale(bimg: BufferedImage, ox: Float, oy: Float, sx: Float, sy: Float, interp: InterpolationType): BufferedImage = {
    val at: AffineTransform = AffineTransform.getTranslateInstance(ox, oy)
    at.scale(sx,sy)
    val aop: AffineTransformOp = new AffineTransformOp(at, interp.getID)
    aop.filter(bimg, null)
  }

  def crop(bounds: Rectangle2D, region: Rectangle2D, image: BufferedImage): BufferedImage = {
    if (bounds.equals(region)) {
      image
    } else {

      val xmin: Int = max(round(region.getX - bounds.getX), 0).toInt
      val ymin: Int = max(round(region.getY - bounds.getY), 0).toInt
      var width: Int = min(bounds.getWidth - xmin, region.getWidth).toInt
      var height: Int = min(bounds.getHeight - ymin, region.getHeight).toInt
      width = min(width, image.getWidth)
      height = min(height, image.getHeight)
      image.getSubimage(xmin, ymin, width, height)
    }
  }

}


class BasicImageViewRenderer(val modelState: ImageViewModel) extends ImageViewRenderer[BufferedImage] {



  def renderImage(rendSeq: Seq[ImageLayerRenderer3D], slice: GridPoint3D, sliceBounds: Rectangle2D) = {
    val sourceImage: BufferedImage = RenderUtils.createCompatibleImage(sliceBounds.getWidth.toInt, sliceBounds.getHeight.toInt)
    val g2: Graphics2D = sourceImage.getGraphics.asInstanceOf[Graphics2D]
    rendSeq.filter(x => modelState.isVisible(x.layer)).foreach(_.renderUnto(sliceBounds, g2))
    g2.dispose()
    sourceImage

  }


  def render(slice: GridPoint3D, displayAnatomy: Anatomy3D, region: Rectangle2D) = {
    val rends = renderers(slice, displayAnatomy)
    val sliceBounds = bounds(rends)
    crop(sliceBounds, region, renderImage(rends, slice, sliceBounds))
  }

  def render(slice: GridPoint3D, displayAnatomy: Anatomy3D, region: Rectangle2D, outputFrame: Rectangle, interp: InterpolationType = InterpolationType.LINEAR) = {
    val rends = renderers(slice, displayAnatomy)
    val sliceBounds = bounds(rends)
    val image = crop(sliceBounds, region, renderImage(rends, slice, sliceBounds))
    var sx = outputFrame.getWidth / image.getWidth
    var sy = outputFrame.getHeight / image.getHeight
    scale(image, 0,0, sx.toFloat,sy.toFloat, interp)
  }

}


class ParallelImageViewRenderer(modelState: ImageViewModel) extends BasicImageViewRenderer(modelState) {



   override def renderImage(rendSeq: Seq[ImageLayerRenderer3D], slice: GridPoint3D, sliceBounds: Rectangle2D) = {
      val sourceImage: BufferedImage = RenderUtils.createCompatibleImage(sliceBounds.getWidth.asInstanceOf[Int], sliceBounds.getHeight.asInstanceOf[Int])
      val g2: Graphics2D = sourceImage.getGraphics.asInstanceOf[Graphics2D]

      rendSeq.filter(x => modelState.isVisible(x.layer)).map(x => future { x.render }).foreach { _() }


      g2.dispose()
      sourceImage

  }









}