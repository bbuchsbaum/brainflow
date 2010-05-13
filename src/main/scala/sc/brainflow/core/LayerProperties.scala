package sc.brainflow.core

import brainflow.core.IClipRange
import boxwood.binding.Observable
import brainflow.colormap.IColorMap

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 7, 2010
 * Time: 10:20:01 PM
 * To change this template use File | Settings | File Templates.
 */

trait LayerProperties {

  val clipRange: Observable[IClipRange]

  val thresholdRange: Observable[IClipRange]

  val opacity: Observable[Double]

  val colorMap: Observable[IColorMap]

}