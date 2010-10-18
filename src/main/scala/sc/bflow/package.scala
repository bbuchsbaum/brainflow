package sc

import bflow.core.ImageViewModel
import bflow.image.space.{RichImageSpace2D, RichImageSpace3D}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jun 21, 2010
 * Time: 9:54:08 AM
 * To change this template use File | Settings | File Templates.
 */

package object bflow {

  type Tuple3F = Tuple3[Float,Float,Float]
  type Tuple4F = Tuple4[Float,Float,Float,Float]

  type Tuple4X4F = Tuple4[Tuple4F, Tuple4F, Tuple4F, Tuple4F]
  type Tuple3X3F = Tuple3[Tuple3F, Tuple3F, Tuple3F]

  implicit def wrapImageSpace3D = RichImageSpace3D.wrap _
  implicit def wrapImageSpace2D = RichImageSpace2D.wrap _


  
}