package tests.sc.brainflow.image.io

import org.scalatest.FlatSpec
import sc.bflow.image.io._
import org.apache.commons.vfs.FileObject
import boxwood.io.RichFileObject._
import boxwood.io.{RichFileObject, SystemResource}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 11/28/10
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */

class ReadMetaInfoSpec extends FlatSpec {

   val resourceDir = new SystemResource("data").toFileObject
   val fileSet: List[FileObject] = resourceDir.find("nw.*nii", filesOnly = true, recursive = true)


  "A NiftiMetaInfo" should "should be readable" in {
    val meta = readMetaInfo(fileSet(0))
    assert(meta.isRight)
    assert(meta.right.get != null)
  }

  "A list of NiftiMetaInfos" should "be readable" in {
    fileSet.map(readMetaInfo(_)).foreach(x => {
      assert(x.isRight)
    })
  }

  "A list of NiftiMetaInfos with a missing file thrown in" should "be handled correctly" in {
    val fileList = RichFileObject("c:/garbage").file :: fileSet
    val results = fileList.map(readMetaInfo(_))
    assert(results != null)
    assert(results(0).isLeft)

    val errs = results.filter(_.isLeft)
    assert(errs.size == 1)
  }

}