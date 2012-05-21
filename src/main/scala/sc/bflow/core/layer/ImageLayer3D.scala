package sc.bflow.core.layer

import brainflow.core.{IClipRange, ClipRange}
import brainflow.colormap.{LinearColorMap, ColorTable, IColorMap}
import brainflow.image.operations.ImageSlicer
import brainflow.display.InterpolationType
import java.awt.geom.{AffineTransform, Rectangle2D}
import java.awt.image.{AffineTransformOp}
import brainflow.image.space.{Axis, IImageSpace3D}
import java.awt.{RenderingHints, AlphaComposite, Composite, Graphics2D}
import brainflow.image.data._
import scala.math._
import sc.bflow.image.space.GridPoint3D
import brainflow.image.anatomy.Anatomy3D
import collection.mutable.Publisher
import boxwood.binding.{Onlooker, Observable}

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 8, 2010
 * Time: 8:37:34 AM
 * To change this template use File | Settings | File Templates.
 */

class ImageLayer3D(val data: IImageData3D) extends ImageLayer with Publisher[LayerProperty[_]] with Onlooker with LayerProperties {
  self =>

  type D = IImageData3D
  type S = IImageSpace3D
  type A = Anatomy3D
  type P = LayerProperties

  type Pub <: ImageLayer3D


  override val clipRange = Clip(new ClipRange(data.minValue, data.maxValue, data.minValue, data.maxValue))

  override val thresholdRange = Threshold(new ClipRange(data.minValue, data.maxValue, data.minValue, data.maxValue))

  override val opacity = Opacity(1)

  override val colorMap = ColorMap(new LinearColorMap(data.minValue, data.maxValue, ColorTable.GRAYSCALE))

  override val interpolation = Interpolation(InterpolationType.LINEAR)

  var maskSeq: List[IMaskedData3D] = List(makePrimaryMask)

  def createSliceRenderer(refspace: IImageSpace3D, slice: GridPoint3D, displayAnatomy: Anatomy3D = data.getAnatomy) = new DefaultLayerRenderer3D(this, refspace, slice, displayAnatomy, colorMap())

  def layerProperties = this

  properties.foreach {
    prop =>
      observe(prop) {
        e =>
          publish(prop)
      }
  }


  private def makePrimaryMask = {

    var pred: MaskPredicate = new MaskPredicate {
      val range = thresholdRange()

      def mask(value: Double): Boolean = {
        return !range.contains(value)
      }
    }

    new MaskedData3D(data, pred)

  }


}