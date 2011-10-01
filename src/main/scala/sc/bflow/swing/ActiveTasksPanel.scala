package sc.bflow.swing

import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import com.jidesoft.pane.{CollapsiblePane, CollapsiblePanes}
import swing.{SimpleSwingApplication}
import javax.swing._
import java.awt.BorderLayout

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 11/28/10
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */

class ActiveTasksPanel extends JPanel {

  val taskPane = new CollapsiblePanes()

  var workerMap = Map.empty[String, ProgressWorker[_]]

  add(taskPane)

  def uniqueKey(name: String) = {
    var key = name
    while (workerMap.contains(name)) {
      key = key + "*"
    }
    key

  }

  def removePane(key: String) = {
    SwingUtilities.invokeLater(new Runnable() {
      def run = {
        workerMap.get(key).foreach(x => taskPane.remove(x.panel))
        revalidate
      }
    })

  }

  def addTask[T](name: String, worker: SwingWorker[T, _]) = {
    //workerList = workerList :+ worker

    val key = uniqueKey(name)

    worker.addPropertyChangeListener(new PropertyChangeListener {
      def propertyChange(evt: PropertyChangeEvent) = {
        evt.getPropertyName match {
          case "state" => {
            worker.getState match {
              case SwingWorker.StateValue.DONE => println(key + " is done"); removePane(key)
              case _ =>
            }
          }
          case _ =>


        }
      }
    })

    println("state value is " + worker.getState )

    if (worker.getState != SwingWorker.StateValue.DONE) {
      val progressWorker = new ProgressWorker[T](key, worker)
      println("adding key: " + key)
      println("progress is : " + worker.getProgress)
      println("state value is " + worker.getState )
      workerMap = workerMap + (key -> progressWorker)
      println("worker map size " + workerMap.size)
      if (worker.getState != SwingWorker.StateValue.DONE) {
        taskPane.add(progressWorker.panel)
      }

    }
  }


}

class ProgressWorker[T](val name: String, val worker: SwingWorker[T, _]) {

  val panel = new CollapsiblePane()

  val progressBar = new JProgressBar()

  progressBar.setStringPainted(true)

  updateProgress(worker.getProgress)

  panel.setContentPane(progressBar)
  panel.setTitle("Task: " + name)
  panel.setEmphasized(true)
  panel.setOpaque(false)

  worker.addPropertyChangeListener(new PropertyChangeListener {
    def propertyChange(evt: PropertyChangeEvent) = {
      evt.getPropertyName match {
        case "progress" => println("progress = " + evt.getNewValue.asInstanceOf[Int]); updateProgress(evt.getNewValue.asInstanceOf[Int])
        case "message" => progressBar.setString(evt.getNewValue.asInstanceOf[String])
        case "state" => println(evt.getNewValue)
      }
    }
  })


  def updateProgress(prog: Int) = {
    SwingUtilities.invokeLater(new Runnable {
      def run = progressBar.setValue(prog)
    })
  }

  def updateMessage(message: String) = {
    SwingUtilities.invokeLater(new Runnable {
      def run = progressBar.setString(message)
    })
  }


}

object Test {

  def main(args: Array[String]) = {
    val frame = new JFrame()
    val manager = new ActiveTasksPanel()

    val count = 1000000


    def makeTask(count: Int) = {
      val ret = new SwingWorker[Object, Object] {
        def doInBackground = {
          for (i <- 0 until count) {
            i * Math.random
            setProgress((i.toDouble / count.toDouble * 100).toInt)
          }

          setProgress(100)

          null.asInstanceOf[Object]
        }
      }

      ret.execute
      ret
    }

    manager.addTask("task1", makeTask(100))
    manager.addTask("task2", makeTask(1000))
    manager.addTask("task3", makeTask(10000))
    manager.addTask("task4", makeTask(100000))
    manager.addTask("task5", makeTask(10000000))
    manager.addTask("task6", makeTask(100000000))



    frame.add(manager, BorderLayout.CENTER)
    frame.pack()
    frame.setVisible(true)
  }


}