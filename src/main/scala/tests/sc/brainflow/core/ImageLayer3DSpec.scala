package tests.sc.brainflow.core

import org.scalatest.FlatSpec
import org.apache.commons.vfs.FileObject
import boxwood.io.SystemResource
import sc.brainflow.image.data.BrainVolume
import sc.brainflow.core.layer.ImageLayer3D
import boxwood.io.RichFileObject._
import brainflow.image.anatomy.{Anatomy3D}
import javax.imageio.ImageIO
import java.io.File
import sc.brainflow.image.space.GridPoint3D

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 13, 2010
 * Time: 9:46:16 PM
 * To change this template use File | Settings | File Templates.
 */

class ImageLayer3DSpec extends FlatSpec {
 
  val bvol = BrainVolume(new SystemResource("data/anat_alepi.nii").toFileObject)

  "An ImageLayer3D" should "should be instantiable" in {
    val layer = new ImageLayer3D(bvol)
    assert(layer != null)
  }

  "An ImageLayer3D" should "should produce an image" in {
    val layer = new ImageLayer3D(bvol)
    val renderer = layer.createSliceRenderer(bvol.getImageSpace, GridPoint3D(2,32,88, bvol.getImageSpace), Anatomy3D.AXIAL_RPI)
    val image = renderer.render
    assert(image != null)
  }

}