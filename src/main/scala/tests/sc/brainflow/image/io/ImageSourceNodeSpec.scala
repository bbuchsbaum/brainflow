package tests.sc.brainflow.image.io

import org.apache.commons.vfs.FileObject
import sc.bflow.image.io._
import org.scalatest.FlatSpec
import boxwood.io.SystemResource
import boxwood.io.RichFileObject._
import sc.bflow.image.io.ImageSource3D
import java.lang.String
import brainflow.utils.ProgressListener

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jun 27, 2010
 * Time: 3:39:33 PM
 * To change this template use File | Settings | File Templates.
 */

class ImageSourceNodeSpec extends FlatSpec {

   val resourceDir = new SystemResource("data").toFileObject
   val fileSet: List[FileObject] = resourceDir.find("nw.*nii", filesOnly = true, recursive = true)

  "An ImageSourceNode" should "should be instantiable" in {
    val meta = NIFTI.readMetaInfo(fileSet(0))
    val source = ImageSource3D(0, meta.get)
    assert(source != None && source != null)
  }

  it should "be loadable from a FileObject" in {
    val meta = NIFTI.readMetaInfo(fileSet(0))
    val source = ImageSource3D(0, meta.get)
    //val data = source.createSource(1, None)
    val data = source.load(1)
    assert(data != None && data != null)

  }

  it should "be loadable from a FileObject, read in chunks, and be passed a ProgressListener" in {
    val meta = NIFTI.readMetaInfo(fileSet(0))
    val source = ImageSource3D(0, meta.get)

    val data = source.load(10)

    assert(data != None && data != null)

  }

  it should "be capable of being created from a sequence of ImageSource3D objects" in {
    val sources = fileSet.map( f => {
      val meta = NIFTI.readMetaInfo(f)
      ImageSource3D(0, meta.get)
    })

    val branch = ImageSourceSeq3D("group", sources)
    assert(branch != null && branch != None)
    assert(branch.numChildren == sources.size)

  }







}