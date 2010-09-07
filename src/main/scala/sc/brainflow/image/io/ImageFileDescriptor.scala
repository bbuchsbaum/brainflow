package sc.brainflow.image.io

import java.io.File
import boxwood.io.RichFileObject._
import org.apache.commons.vfs.{FileSystemException, VFS, FileObject}
import brainflow.image.io._
import boxwood.io.VFSUtils

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 7, 2010
 * Time: 11:36:20 AM
 * To change this template use File | Settings | File Templates.
 */


object ImageFileDescriptor {
}

trait ImageFileDescriptor {
  import ImageFileDescriptor._

  def headerExtension: String

  def dataExtension: String

  val fileFormat: String

  val headerEncoding: FileEncoding

  val dataEncoding: FileEncoding

  def isMatch(headerFileName: String, dataFileName: String): Boolean = isHeaderMatch(headerFileName) && isDataMatch(dataFileName)

  def isHeaderMatch(headerFileName: String): Boolean = headerFileName.endsWith("." + headerExtension)

  def isDataMatch(dataFileName: String): Boolean = dataFileName.endsWith("." + dataExtension)


  def stripExtension(fullName: String): String = {
    if (fullName.endsWith("." + headerExtension)) {
      fullName.substring(0, fullName.length - (dottedHeaderExtension.length))
    }
    else if (fullName.endsWith("." + dataExtension)) {
      fullName.substring(0, fullName.length - (dottedDataExtension.length))
    } else {
      error("invalid filename " + fullName + " file name should end with: " + headerExtension + " or " + dataExtension)
    }

  }

  def dottedHeaderExtension = {
    if (headerEncoding.extension.equals("")) {
      "." + headerExtension
    }
    else {
      "." + headerExtension + "." + headerEncoding.extension
    }
  }

  def dottedDataExtension = {
    if (dataEncoding.extension.equals("")) {
      "." + dataExtension
    }
    else {
      "." + dataExtension + "." + dataEncoding.extension
    }
  }

  def getDataName(file: FileObject): String = {
    require(isHeaderMatch(file.name) || isDataMatch(file.name), "file " + file.name + " does not match expected naming convention for file type: " + fileFormat)
    if (isDataMatch(file.name)) {
      file.name
    } else {
      stripExtension(file.name) + "." + dataExtension
    }
  }


  def getHeaderName(file: FileObject): String = {
    require(isHeaderMatch(file.name) || isDataMatch(file.name), "file " + file.name + " does not match expected naming convention for file type: " + fileFormat)
    if (isHeaderMatch(file.name)) {
      file.name
    } else {
      stripExtension(file.name) + "." + dataExtension
    }

  }


  def resolveDataFileObject(headerFile: FileObject): Option[FileObject] = {
    VFSUtils.resolveFileObject(headerFile.getParent, getDataName(headerFile), true)
  }


  def resolveHeaderFileObject(dataFile: FileObject): Option[FileObject] = {
    VFSUtils.resolveFileObject(dataFile.getParent, getHeaderName(dataFile), true)

  }


  def dataFileFilter: FileObject => Boolean = (fileObject => this.isDataMatch(fileObject.path))


  def headerFileFilter: FileObject => Boolean = (fileObject => this.isHeaderMatch(fileObject.path))


  def createDataSource(headerFile: FileObject, dataFile: FileObject): Option[IImageSource[_]]


  def createDataSource(headerFile: FileObject): Option[IImageSource[_]] = {
    require(isHeaderMatch(headerFile.name))
    require(headerFile.exists)

    val dataFile = VFSUtils.resolveFileObject(headerFile.parent, getDataName(headerFile), true)

    dataFile match {
      case Some(data) => createDataSource(headerFile, data)
      case None => None
    }

  }


  def createInfoReader(headerFile: FileObject, dataFile: FileObject): ImageInfoReader

  def readMetaInfo(headerFile: FileObject): Option[ImageMetaInfo]


}

trait FileEncoding {
  def extension: String
}

case object GZIPEncoding extends FileEncoding {
  def extension = "gz"
}

case object RawBinaryEncoding extends FileEncoding {
  def extension = ""
}

object ImageFileDescriptors {
  abstract class GenericFileDescriptor(val headerExtension: String, val dataExtension: String, val fileFormat: String, val headerEncoding: FileEncoding, val dataEncoding: FileEncoding) extends ImageFileDescriptor

  val descList = List(NIFTI, NIFTI_GZ, NIFTI_PAIR, AFNI, AFNI_GZ)

  private def readNiftiMetaInfo(headerFile: FileObject, dataFileOption: Option[FileObject], desc: GenericFileDescriptor) = {
    val readerOption = dataFileOption match {
      case Some(dataFile) => Some(desc.createInfoReader(headerFile, dataFile))
      case None => None
    }

    readerOption match {
      case Some(r) => Some(NiftiMetaInfo.convert(r.asInstanceOf[NiftiInfoReader].readInfo))
      case None => None
    }

  }

  case object NIFTI extends GenericFileDescriptor("nii", "nii", "NIFTI", RawBinaryEncoding, RawBinaryEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = new NiftiInfoReader(headerFile, dataFile)

    def readMetaInfo(headerFile: FileObject) = {
      val dataFileOption = VFSUtils.resolveFileObject(headerFile.parent, getDataName(headerFile), true)
      readNiftiMetaInfo(headerFile, dataFileOption, this)
    }

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".nii")

  }

  case object NIFTI_GZ extends GenericFileDescriptor("nii", "nii", "NIFTI", GZIPEncoding, GZIPEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = new NiftiInfoReader(headerFile, dataFile)

    def readMetaInfo(headerFile: FileObject) = {
      val dataFileOption = VFSUtils.resolveFileObject(headerFile.parent, getDataName(headerFile), true)
      readNiftiMetaInfo(headerFile, dataFileOption, this)
    }

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".nii.gz")

  }

  case object NIFTI_PAIR extends GenericFileDescriptor("hdr", "img", "NIFTI", RawBinaryEncoding, RawBinaryEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = new NiftiInfoReader(headerFile, dataFile)


    def readMetaInfo(headerFile: FileObject) = {
      val dataFileOption = VFSUtils.resolveFileObject(headerFile.parent, getDataName(headerFile), true)
      readNiftiMetaInfo(headerFile, dataFileOption, this)
    }

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".img") || filename.endsWith(".hdr")

  }

  // both files have to be gzipped but will match if either file is gzipped, could be an issue
  case object NIFTI_PAIR_GZ extends GenericFileDescriptor("hdr", "img", "NIFTI", GZIPEncoding, GZIPEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = new NiftiInfoReader(headerFile, dataFile)

    def readMetaInfo(headerFile: FileObject) = {
      val dataFileOption = VFSUtils.resolveFileObject(headerFile.parent, getDataName(headerFile), true)
      readNiftiMetaInfo(headerFile, dataFileOption, this)
    }

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".img.gz") || filename.endsWith(".hdr.gz")

  }

  case object AFNI extends GenericFileDescriptor("HEAD", "BRIK", "AFNI", RawBinaryEncoding, RawBinaryEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = new AFNIInfoReader(headerFile, dataFile)


    def readMetaInfo(headerFile: FileObject) = None

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".HEAD") || filename.endsWith(".BRIK")

  }

  // both files have to be gzipped but will match if either file is gzipped, could be an issue
  case object AFNI_GZ extends GenericFileDescriptor("HEAD", "BRIK", "AFNI", GZIPEncoding, GZIPEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = new AFNIInfoReader(headerFile, dataFile)


    def readMetaInfo(headerFile: FileObject) = None

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".HEAD.gz") || filename.endsWith(".BRIK.gz")

  }


  def readMetaInfo(header: FileObject): Option[ImageMetaInfo] = {
    header.name match {
      case NIFTI() => NIFTI.readMetaInfo(header)
      case NIFTI_GZ() => NIFTI_GZ.readMetaInfo(header)
      case NIFTI_PAIR() => NIFTI_PAIR.readMetaInfo(header)
      case AFNI() => AFNI.readMetaInfo(header)
      case AFNI_GZ() => AFNI_GZ.readMetaInfo(header)
      case _ => None
    }

  }

  def createInfoReader(header: FileObject): Option[ImageInfoReader] = {
    header.name match {
      case NIFTI() => Some(NIFTI.createInfoReader(header, NIFTI.resolveDataFileObject(header).getOrElse(header)))
      case NIFTI_GZ() => Some(NIFTI_GZ.createInfoReader(header, NIFTI_GZ.resolveDataFileObject(header).getOrElse(header)))
      case NIFTI_PAIR() => Some(NIFTI_PAIR.createInfoReader(header, NIFTI_PAIR.resolveDataFileObject(header).getOrElse(header)))
      case AFNI() => Some(AFNI.createInfoReader(header, AFNI.resolveDataFileObject(header).getOrElse(header)))
      case AFNI_GZ() => Some(AFNI_GZ.createInfoReader(header, AFNI_GZ.resolveDataFileObject(header).getOrElse(header)))
      case _ => None
    }

  }

  def supportedFileType(fileName: String) = {
    fileName match {
      case NIFTI() => true
      case NIFTI_GZ() => true
      case NIFTI_PAIR() => true
      case AFNI() => true
      case AFNI_GZ() => true
      case _ => false
    }
  }

  def supportedHeaderFile(file: String) = {
    descList.exists(_.isHeaderMatch(file))
  }

  def supportedDataFile(file: String) = {
    descList.exists(_.isDataMatch(file))
  }


}

object Test {
  import ImageFileDescriptors._

  def main(args: Array[String]) {
    val x = "x.HEAD"
    x match {
      case ImageFileDescriptors.AFNI() => println(x + " is an afni header")
      case _ => println("not an afni header")
    }

    val y = "hello/hello/goodbye/junk.BRIK.gz"
    y match {
      case ImageFileDescriptors.AFNI_GZ() => println(y + " is an afni gz header")
      case _ => println("not an afni gz header")
    }

    val z = "hello/hello/goodbye/junk.nii"
    z match {
      case ImageFileDescriptors.NIFTI() => println(z + " is a nifti header")
      case _ => println("not a nifti header")
    }

    println("is jojo.nii a supported file type? " + supportedFileType("jojo.nii"))
    println("is jojo.nii a supported header? " + supportedHeaderFile("jojo.nii"))
    println("is jojo.nii0 a supported file type? " + supportedFileType("jojo.nii0"))


  }
}




