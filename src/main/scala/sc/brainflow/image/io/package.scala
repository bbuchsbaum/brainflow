package sc.brainflow.image

import io.{ImageMetaInfo, ImageFileDescriptors}
import boxwood.io.VFSUtils

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jun 22, 2010
 * Time: 7:03:25 AM
 * To change this template use File | Settings | File Templates.
 */

package object io {

  val NIFTI = ImageFileDescriptors.NIFTI

  val NIFTI_GZ = ImageFileDescriptors.NIFTI_GZ

  val NIFTI_PAIR = ImageFileDescriptors.NIFTI_PAIR

  val NIFTI_PAIR_GZ = ImageFileDescriptors.NIFTI_PAIR_GZ

  val AFNI = ImageFileDescriptors.AFNI

  val AFNI_GZ = ImageFileDescriptors.AFNI_GZ

  def readMetaInfo[T <: ImageMetaInfo](fileName: String) : Option[T] = {
    require(ImageFileDescriptors.supportedFileType(fileName))
    val fileObject = VFSUtils.resolveFileObject(fileName)

    val res = fileObject match {
      case Left(e) => throw e
      case Right(fobj) => fobj
    }

    val reader = ImageFileDescriptors.createInfoReader(res)
    None

  }




}

