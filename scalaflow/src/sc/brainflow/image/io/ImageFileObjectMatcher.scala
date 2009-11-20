package sc.brainflow.image.io


import org.apache.commons.vfs.FileObject

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 31, 2009
 * Time: 11:06:58 PM
 * To change this template use File | Settings | File Templates.
 */

object ImageFileObjectMatcher {

  trait BaseMatcher {
    def apply(fobj: FileObject) = {
      fobj.getName.getPath
    }
  }

  object AFNIHeaderMatcher extends BaseMatcher {
    def unapply(headerPath: String): Boolean= {
      headerPath.endsWith(".HEAD") || headerPath.endsWith(".HEAD.gz")
    }
  }


}

object Test {
  import ImageFileObjectMatcher._
  
  def main(args:Array[String]) {
    val x = "x.HEAD.gz"
    x match {
      case AFNIHeaderMatcher() => println("afni header")
      case _ => println("not an afni header")
    }
  }
}
