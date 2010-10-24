package sc.bflow.app.toplevel

import org.apache.commons.vfs.FileObject
import java.io.File
import boxwood.io.RichFileObject
import boxwood.binding.{ObservableBuffer, Observable}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 10, 2010
 * Time: 10:32:43 AM
 * To change this template use File | Settings | File Templates.
 */

trait FileSystemComponent {

  val fileSystemService : FileSystemService

  class FileSystemService {

    val fileRoots = ObservableBuffer(List.empty[FileObject])

    lazy val currentDirectory = Observable(RichFileObject(System.getProperty("user.dir")))

    def mount(fileObj: FileObject) = {
      println("mounting file to " + System.identityHashCode(fileRoots))
      println("mounting file to " + fileRoots)


      if (!fileRoots().contains(fileObj)) {
        fileRoots += fileObj
      } else {
        //log.warning("DirectoryManager already contains file root: " + fobj)
      }

    }
  }
}