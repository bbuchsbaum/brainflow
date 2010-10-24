package tests.sc.brainflow.image.space

import org.scalatest.FlatSpec
import brainflow.image.axis.ImageAxis
import brainflow.image.anatomy.AnatomicalAxis
import brainflow.image.space.ImageSpace3D
import sc.bflow.image.space.IndexPoint3D

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 11, 2010
 * Time: 10:05:11 PM
 * To change this template use File | Settings | File Templates.
 */

class AnatPointSpec extends FlatSpec {

  val xaxis = new ImageAxis(0,100, AnatomicalAxis.LEFT_RIGHT, 100)
  val yaxis = new ImageAxis(0,100, AnatomicalAxis.POSTERIOR_ANTERIOR, 100)
  val zaxis = new ImageAxis(0,100, AnatomicalAxis.INFERIOR_SUPERIOR, 100)

  val space = new ImageSpace3D(xaxis, yaxis, zaxis)

  "An IndexPoint3D" should "be convertible to a GridPoint3D and back again" in {

    val index = IndexPoint3D(25,25,25,space)

    assert(index.toGrid.toIndex == index)

  }
}