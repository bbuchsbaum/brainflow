package sc.bflow.app.toplevel

import _root_.brainflow.app.toplevel.BrainFlow
import boxwood.io.VFSUtils
import joptsimple.{OptionSet, OptionParser}
import scalaj.collection.Imports._
import org.apache.commons.vfs.FileObject
import sc.bflow.image.io.ImageFileDescriptors



/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 2, 2010
 * Time: 7:20:57 PM
 * To change this template use File | Settings | File Templates.
 */

case class BrainFlowLauncher(val args: Array[String]) {

  val parser = new OptionParser()

  parser.accepts("mount").withRequiredArg()
  parser.accepts("show").withRequiredArg().withValuesSeparatedBy(',')

  def launch() {

    val options : OptionSet = parser.parse(args : _*)

    val config = new _root_.brainflow.app.toplevel.BrainFlow.LaunchConfiguration()

    if (options.has("mount")) {
      val mountPoint = options.valueOf("mount").toString
      VFSUtils.resolveFileObject(mountPoint) match {
        case Right(mp) => config.addMountPoint(mp)
        case Left(e) => None
      }


    }

    if (options.has("show")) {
      val ret = options.valuesOf("show").asScala
      val files = ret.map(x => VFSUtils.resolveFileObject(x.toString(), mustExist=true))

      files.foreach(_ match {
        case Left(e) => fail(e.getMessage())
        case _ =>
      })



      val validFiles = files.flatMap(_.right.toSeq)
      val supportedFiles = validFiles.filter(x => isSupportedFile(x.getName.getPath))
      println("number of files " + supportedFiles.length)
      config.addDataset(supportedFiles.asJava)

    }

    val bflow =  _root_.brainflow.app.toplevel.BrainFlow.get()
    bflow.launch(config)

  }

  def isSupportedFile(fileName: String) = ImageFileDescriptors.supportedFileType(fileName)

  def fail(msg: String) = {
    System.err.println("error: " + msg)
    System.exit(1)

  }



}

object BrainFlowRunner {

  def main(args: Array[String]) = BrainFlowLauncher(args).launch
}