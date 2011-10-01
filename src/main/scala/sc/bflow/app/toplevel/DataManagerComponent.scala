package sc.bflow.app.toplevel

import org.apache.commons.vfs.FileObject
import sc.bflow.image.io._
import javax.swing.SwingWorker
import brainflow.image.data.{IImageData3D, IImageData}
import brainflow.utils.ProgressListener
import java.lang.String
import boxwood.swing.utils._


/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/24/10
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */

trait DataManagerComponent {
  //self: BrainFlowContext

  var sourceSet = Map.empty[String,ImageSourceNode]

  def createSource(files: FileObject*) = {
    val metaInfoSeq = files.map(readMetaInfo(_)).collect{
      case Right(info) => info
    }

    metaInfoSeq.map(makeImageSource(_))

  }


  def register(nodes: ImageSourceNode*) {
    for (src <- nodes) {
       sourceSet = sourceSet + (src.label -> src)
    }
  }
  


  /*def makeImageSourceSeq3DLoader(node: ImageSourceSeq3D, numChunks: Int = 10) = {
    new SwingWorker[Seq[IImageData3D], IImageData3D] {
      self =>
      //x.reactions += {
      //  case Finished => self.firePropertyChange("state", None, SwingWorker.StateValue.DONE)
      //  case Progress(x) => self.firePropertyChange("progress", None, x)
      //  case ErrorMessage(e) => self.firePropertyChange("error", None, e)
      //  case ProgressMessage(m) => self.firePropertyChange("message", None, m)
     // }
      //
      //def doInBackground = x.createSource(numChunks)

    }
  } */

  /*def makeImageSource3DLoader(node: ImageSource3D, numChunks: Int = 10) = {
    new SwingWorker[IImageData3D, IImageData3D] {
      self =>
     // x.reactions += {
      //  case Finished => self.firePropertyChange("state", None, SwingWorker.StateValue.DONE)
      //  case Progress(x) => self.firePropertyChange("progress", None, x)
     //   case ErrorMessage(e) => self.firePropertyChange("error", None, e)
     //   case ProgressMessage(m) => self.firePropertyChange("message", None, m)
     // }

     // def doInBackground = x.createSource(numChunks)

    }
  }    */


}