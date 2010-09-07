package sc.brainflow.core.layer

import boxwood.binding.Observable
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
import sc.brainflow.image.space.GridPoint3D
import brainflow.image.anatomy.Anatomy3D

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 8, 2010
 * Time: 8:37:34 AM
 * To change this template use File | Settings | File Templates.
 */

class ImageLayer3D(val data: IImageData3D) extends ImageLayer with LayerProperties {
  self =>

  type D = IImageData3D
  type S = IImageSpace3D
  type A = Anatomy3D
  type P = LayerProperties


  val clipRange = Observable[IClipRange](new ClipRange(data.minValue, data.maxValue, data.minValue, data.maxValue))

  val thresholdRange = Observable[IClipRange](new ClipRange(data.minValue, data.maxValue, data.minValue, data.maxValue))

  val opacity = Observable[Double](1)

  val colorMap = Observable[IColorMap](new LinearColorMap(data.minValue, data.maxValue, ColorTable.GRAYSCALE))

  val interpolation = Observable[InterpolationType](InterpolationType.LINEAR)

  var maskSeq : List[IMaskedData3D] = List(makePrimaryMask) 

  def createSliceRenderer(refspace: IImageSpace3D, slice: GridPoint3D, displayAnatomy: Anatomy3D=data.getAnatomy) = new DefaultLayerRenderer3D(this, refspace, slice, displayAnatomy, colorMap.value)

  def layerProperties = this

  private def makePrimaryMask = {

    var pred: MaskPredicate = new MaskPredicate {
      val range = thresholdRange.value
      def mask(value: Double): Boolean = {
        return !range.contains(value)
      }
    }

    new MaskedData3D(data, pred)

  }


}