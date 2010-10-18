package sc.bflow.core.layer

import brainflow.core.IClipRange
import brainflow.colormap.IColorMap
import boxwood.binding.{ObservableVar}

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 7, 2010
 * Time: 10:20:01 PM
 * To change this template use File | Settings | File Templates.
 */

trait LayerProperties {

  val clipRange: ObservableVar[IClipRange]

  val thresholdRange: ObservableVar[IClipRange]

  val opacity: ObservableVar[Double]

  val colorMap: ObservableVar[IColorMap]

}