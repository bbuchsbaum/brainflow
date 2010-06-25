package tests.sc.brainflow.image.io

import org.scalatest.FlatSpec
import boxwood.io.SystemResource
import org.apache.commons.vfs.FileObject
import sc.brainflow.image.io._
import boxwood.io.RichFileObject._

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jun 22, 2010
 * Time: 6:56:01 AM
 * To change this template use File | Settings | File Templates.
 */

class NiftiMetaInfoSpec extends FlatSpec {

   val resourceDir = new SystemResource("data").toFileObject
   val fileSet: List[FileObject] = resourceDir.find("nw.*nii", filesOnly = true, recursive = true)

  "A NiftiMetaInfo" should "should be instantiable" in {
    val meta = NIFTI.readMetaInfo(fileSet(0))
    assert(meta != null)
    assert(meta != None)
  }

  it should "have the right dimensions" in {
    val meta = NIFTI.readMetaInfo(fileSet(0))
    println(meta.get.dimensions)
    assert(meta.get.dimensions == List(61,73,61))

  }
  
}