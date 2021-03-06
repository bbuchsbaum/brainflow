package sc.bflow.core.layer

import brainflow.image.anatomy.{Anatomy3D}
import java.awt.{RenderingHints, AlphaComposite, Composite, Graphics2D}
import java.awt.geom.{Rectangle2D, AffineTransform}
import brainflow.display.InterpolationType
import java.awt.image.AffineTransformOp
import brainflow.image.space.{Axis, IImageSpace3D}
import brainflow.image.operations.ImageSlicer
import brainflow.colormap.IColorMap
import scala.math._
import sc.bflow.image.space.GridPoint3D
import sc.bflow._
import brainflow.core.ClipRange

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 15, 2010
 * Time: 7:30:00 AM
 * To change this template use File | Settings | File Templates.
 */

class DefaultLayerRenderer3D(val layer: ImageLayer3D, val refSpace: IImageSpace3D, val cutPoint: GridPoint3D, val displayAnatomy: Anatomy3D, colorMap: IColorMap) extends ImageLayerRenderer3D {


  lazy val zslice = {
    val zdisp = cutPoint(displayAnatomy.ZAXIS)
    val index: Int = max((zdisp.x - .5f).toInt, 0)
     min(index, refSpace.getDimension(displayAnatomy.ZAXIS) - 1)
  }

  lazy val data = {
    ImageSlicer.createSlicer(refSpace, layer.data).getSlice(displayAnatomy, zslice)
  }

  lazy val rgbImage = {
    // need to sync clip range and color map range...
    val cmap = layer.colorMap()
    val colorRange = new ClipRange(cmap.getMinimumValue, cmap.getMaximumValue, cmap.getLowClip, cmap.getHighClip)
    val clipRange = layer.clipRange()
    if (clipRange != colorRange) {
      layer.colorMap.updateSilently(colorMap.newClipRange(clipRange.getLowClip, clipRange.getHighClip, clipRange.getMin, clipRange.getMax))
      layer.colorMap().getRGBAImage(data)
    } else {
      colorMap.getRGBAImage(data)
    }
  }

  lazy val bufferedImage = {
    rgbImage.getAsBufferedImage
  }

  lazy val resampledImage = {
    val interp = layer.interpolation()
    val ispace = data.getImageSpace
    val sx = ispace.x_axis.getRange.getInterval / ispace.xdim
    val sy = ispace.y_axis.getRange.getInterval / ispace.ydim
    val at = AffineTransform.getTranslateInstance(0, 0)

    at.scale(sx, sy)
    val aop = new AffineTransformOp(at, interp.getID)
    aop.filter(bufferedImage, null)

  }

  def renderUnto(frame: Rectangle2D, g2: Graphics2D) = {
    val space = data.getImageSpace
    val minx = space.x_axis.getMinimum
    val miny = space.y_axis.getMinimum
    val transx = (minx - frame.getMinX)
    val transy = (miny - frame.getMinY)

    val oldComposite: Composite = g2.getComposite

    val composite: AlphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer.opacity().toFloat)
    g2.setComposite(composite)

    g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
    g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
    g2.drawRenderedImage(render, AffineTransform.getTranslateInstance(transx, transy))
    g2.setComposite(oldComposite)
  }

  def render = resampledImage


}
