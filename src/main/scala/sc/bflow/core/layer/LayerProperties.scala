package sc.bflow.core.layer

import brainflow.core.IClipRange
import brainflow.colormap.IColorMap
import boxwood.binding.{Observing, ObservableVar}
import collection.mutable.Publisher
import brainflow.display.InterpolationType

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 7, 2010
 * Time: 10:20:01 PM
 * To change this template use File | Settings | File Templates.
 */

trait LayerProperties  {

  def clipRange: Clip

  def thresholdRange: Threshold

  def opacity: Opacity

  def colorMap: ColorMap

  def interpolation: Interpolation

  def properties = Seq[LayerProperty[_]](clipRange, thresholdRange, opacity, colorMap)
  


}

sealed trait LayerProperty[T] extends ObservableVar[T]

abstract class AbstractLayerProperty[T](prop: T) extends LayerProperty[T]  {
  _value = prop
}
 
case class Clip(prop: IClipRange) extends AbstractLayerProperty[IClipRange](prop)
case class Threshold(prop: IClipRange) extends AbstractLayerProperty[IClipRange](prop)
case class Opacity(prop: Double) extends AbstractLayerProperty[Double](prop)
case class ColorMap(prop: IColorMap) extends AbstractLayerProperty[IColorMap](prop)
case class Interpolation(prop: InterpolationType) extends AbstractLayerProperty[InterpolationType](prop)