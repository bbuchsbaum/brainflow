package sc.bflow.app.toplevel

import javax.swing.SwingWorker
import boxwood.binding.ObservableBuffer
import java.beans.{PropertyChangeEvent, PropertyChangeListener}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 11/28/10
 * Time: 9:25 PM
 * To change this template use File | Settings | File Templates.
 */

trait TaskManagerComponent {

  val manager: TaskManager

  class TaskManager {

   /* val tasks: ObservableBuffer[NamedTask[_, _]] = ObservableBuffer(List.empty)

    def makeSwingWorker[T](name: String, f: => T) = new NamedTask[T, T](name) {
      def doInBackground = f

      override def isWatched = false
    }

    def submit[T](name: String, f: => T) = {
      val worker = makeSwingWorker(name, f)
      tasks += worker

      worker.addPropertyChangeListener(new PropertyChangeListener() {

      })
      worker.execute
      worker
    }

    def submit[T, U](name: String, sworker: SwingWorker[T, U]) = {
      require(sworker.getState != SwingWorker.StateValue.STARTED)
      val worker = NamedTask(name, sworker)
      tasks += worker

      worker.execute
      worker
    }


*/
  }

}

object NamedTask {

 /* def apply[T, U](name: String, sworker: SwingWorker[T, U]) = {
   val ret = new NamedTask(name) {
      self =>

      def doInBackground = try {
        sworker.doInBackground
      } catch {
        case t: Throwable => {
          exception = Some(t)
          firePropertyChange("exception", None, t)
        }

      }
    }

    ret


  }

  def apply[T](name: String, f: => T) = new NamedTask[T, T](name) {
    def doInBackground = try {
      f
    } catch {
      case t: Throwable => {
        exception = Some(t)
       // firePropertyChange("exception", None, t)
      }

    }
  }

  override def isWatched = false

*/
}

abstract class NamedTask[T, U](name: String) extends SwingWorker[T, U] {

  protected var exception: Option[Throwable] = None

  def isWatched: Boolean = true



}

