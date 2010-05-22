package tests.sc.brainflow.image.data

import collection.mutable.Stack
import org.scalatest.FlatSpec
import boxwood.io.SystemResource
import boxwood.io.RichFileObject
import boxwood.io.RichFileObject._
import boxwood.stats.StatFun._
import org.apache.commons.vfs.FileObject
import brainflow.image.data.BasicImageDataVector3D
import sc.brainflow.image.data.{BrainVector, BrainVectorOps}
import sc.brainflow.image.data.BrainVolume._

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 13, 2010
 * Time: 8:20:05 AM
 * To change this template use File | Settings | File Templates.
 */

class BrainVectorOpsSpec extends FlatSpec {
  val resourceDir = new SystemResource("data").toFileObject
  val fileSet: List[FileObject] = resourceDir.find("nw.*nii", filesOnly = true, recursive = true)

  val imvec = BrainVector(fileSet)


  "A BrainVectorOpsSpec" should "should be instantiable" in {

    assert(imvec != null)
    assert(imvec.getNumVolumes == fileSet.size)

  }

  it should "have a max value that equals the maximum value of each of its volumes" in {
    val maxArray = (0 until imvec.getNumVolumes).map(i => imvec.getVolume(i).maxValue)
    assert(imvec.maxValue == maxArray.max)
  }

  it should "have a mean volume that is equal to the sum divided by length" in {
    assert(imvec.collapse(mean).maxValue == (imvec.collapse(sum) / imvec.getNumVolumes).maxValue)
  }

  it should "have a maximum standard deviation that is non-zero" in {
    val stdev = imvec.collapse(sd).maxValue
    println(stdev)
    assert(imvec.collapse(sd).maxValue != 0)
  }

  it should "have a max standard deviation equal to square root of max variance" in {
    val stdev = imvec.collapse(sd).maxValue
    val vce = imvec.collapse(variance).maxValue
    println(stdev)
    assert(Math.sqrt(vce) == stdev)
  }


}