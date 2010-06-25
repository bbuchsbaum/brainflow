package sc

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jun 21, 2010
 * Time: 9:54:08 AM
 * To change this template use File | Settings | File Templates.
 */

package object brainflow {

  type Tuple3F = Tuple3[Float,Float,Float]
  type Tuple4F = Tuple4[Float,Float,Float,Float]

  type Tuple4X4F = Tuple4[Tuple4F, Tuple4F, Tuple4F, Tuple4F]
  type Tuple3X3F = Tuple3[Tuple3F, Tuple3F, Tuple3F]  
  
}