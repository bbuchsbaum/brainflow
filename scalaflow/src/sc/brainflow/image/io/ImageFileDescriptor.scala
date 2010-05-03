package sc.brainflow.image.io

import java.io.File
import boxwood.io.RichFileObject._
import org.apache.commons.vfs.{FileSystemException, VFS, FileObject}
import brainflow.image.io.{ImageDataSource,ImageInfoReader, IImageDataSource}

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 7, 2010
 * Time: 11:36:20 AM
 * To change this template use File | Settings | File Templates.
 */



object ImageFileDescriptor {

  def resolveFile(path: String, name: String): Option[FileObject] = {
    try {
      Some(VFS.getManager.resolveFile(path + File.separatorChar + name))
    } catch {
      case e: FileSystemException => None
      case _ => None
    }

  }

  def resolveFile(parent: FileObject, name: String): Option[FileObject] = resolveFile(parent.parentPath, name)

  def resolveFile(parent: File, name: String): Option[FileObject] = resolveFile(parent.getAbsolutePath, name)

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
    }

    error("invalid filename " + fullName)
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

  def getDataName(headerFile: FileObject): String = {
    require(isHeaderMatch(headerFile.name), "headerFile " + headerFile.name + " does not match expected naming convention for file type: " + fileFormat)

    stripExtension(headerFile.name) + "." + dataExtension
  }


  def getHeaderName(dataFile: FileObject): String = {
    require(isDataMatch(dataFile.name), "headerFile " + dataFile.name + " does not match expected naming convention for file type: " + fileFormat)

    stripExtension(dataFile.name) + "." + dataExtension

  }


  def resolveDataFileObject(headerFile: FileObject): Option[FileObject] = {
    ImageFileDescriptor.resolveFile(headerFile.getParent, getDataName(headerFile))
  }


  def resolveHeaderFileObject(dataFile: FileObject): Option[FileObject] = {
    ImageFileDescriptor.resolveFile(dataFile.getParent, getHeaderName(dataFile))

  }


  def dataFileFilter: FileObject => Boolean = (fileObject => this.isDataMatch(fileObject.path))


  def headerFileFilter: FileObject => Boolean = (fileObject => this.isHeaderMatch(fileObject.path))


  def createDataSource(headerFile: FileObject, dataFile: FileObject): Option[IImageDataSource]


  def createDataSource(headerFile: FileObject): Option[IImageDataSource] = {
    require(isHeaderMatch(headerFile.name))
    require(headerFile.exists)

    val dataFile = resolveFile(headerFile.parent, getDataName(headerFile))

    dataFile match {
      case Some(data) => createDataSource(headerFile, data)
      case None => None
    }

  }


  def createInfoReader(headerFile: FileObject, dataFile: FileObject): Option[ImageInfoReader]


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

  case object NIFTI extends GenericFileDescriptor("nii", "nii", "NIFTI", RawBinaryEncoding, RawBinaryEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = None

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".nii")

  }

  case object NIFTI_GZ extends GenericFileDescriptor("nii", "nii", "NIFTI", GZIPEncoding, GZIPEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = None

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".nii.gz")

  }

  case object NIFTI_PAIR extends GenericFileDescriptor("hdr", "img", "NIFTI", RawBinaryEncoding, RawBinaryEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = None

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".nii.gz")

  }

  case object AFNI extends GenericFileDescriptor("HEAD", "BRIK", "AFNI", RawBinaryEncoding, RawBinaryEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = None

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".HEAD") || filename.endsWith(".BRIK")

  }

  case object AFNI_GZ extends GenericFileDescriptor("HEAD", "BRIK", "AFNI", GZIPEncoding, GZIPEncoding) {
    def createInfoReader(headerFile: FileObject, dataFile: FileObject) = None

    def createDataSource(headerFile: FileObject, dataFile: FileObject) = None

    def unapply(filename: String) = filename.endsWith(".HEAD.gz") || filename.endsWith(".BRIK.gz")

  }

  def supportedFileType(fileName : String) = {
    fileName match {
      case NIFTI() => true
      case NIFTI_GZ() => true
      case NIFTI_PAIR() => true
      case AFNI() => true
      case AFNI_GZ() => true
      case _ => false
    }
  }



  
}

object Test {
  import ImageFileDescriptors._

  def main(args:Array[String]) {
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
    println("is jojo.nii0 a supported file type? " + supportedFileType("jojo.nii0"))


  }
}




