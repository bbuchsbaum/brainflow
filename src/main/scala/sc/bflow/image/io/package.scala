package sc.bflow.image

import io.{ImageSourceNode, ImageSource3D, ImageMetaInfo, ImageFileDescriptors}
import org.apache.commons.vfs.{FileObject}
import boxwood.io.RichFileObject._
import java.io.{IOException}
import brainflow.utils.{ProgressListener}
import boxwood.io.{RichFileObject, VFSUtils}

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

  implicit def optionalListener(listener: ProgressListener) : Option[ProgressListener] = {
    Some(listener)
  }

  def readMetaInfo(fileObject: FileObject) : Either[Throwable, ImageMetaInfo] = {
    //require(ImageFileDescriptors.supportedFileType(fileObject.path))
    val metaInfo = ImageFileDescriptors.readMetaInfo(fileObject)
    if (metaInfo.isDefined) Right(metaInfo.get) else Left(new IOException("could not read header for file " + fileObject.getName.getPath))

  }



  def makeImageSource(info: ImageMetaInfo) : ImageSourceNode = {
    if (info.numVolumes == 1)
      ImageSource3D(0, info)
    else if (info.numVolumes > 1)
      ImageSourceSeq3D(info.label, (0 until info.numVolumes).map(i => ImageSource3D(i, info)))
    else sys.error("number of volumes must be >= 0 " + "found " + info.numVolumes + " in " + info.headerFile.path)
  }







}

