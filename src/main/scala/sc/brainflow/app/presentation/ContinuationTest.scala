package sc.brainflow.app.presentation

import java.awt.event.{ActionEvent, ActionListener}
import scala.util.continuations._
import javax.swing.{JFrame, JButton}
import java.awt.BorderLayout
import java.util.{Timer, TimerTask}
import actors.Actor._

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 29, 2010
 * Time: 1:07:43 PM
 * To change this template use File | Settings | File Templates.
 */

object ContinuationTest extends Application {


  //def main(args: Array[String]) = {
  new ButtonTest()

  // }




}


class ButtonTest {
  val button: JButton = new JButton("hit me")

  button.addActionListener(new ActionListener() {
    val timer = new Timer()


    def sleep(delay: Int) = shift {
      k: (Unit => Unit) =>
        //timer.schedule(new TimerTask {
        //  def run() = k() // in a real program, we'd execute k on a thread pool
       // }, delay)
        actor {
          Thread.sleep(delay)
          k()
        }

    }

    def actionPerformed(e: ActionEvent) = {
      reset {
        println("look, Ma ...")
        sleep(10000)
        println(" no threads!")
      }

      println("outside continuation")
    }
  })

  val frame: JFrame = new JFrame()
  frame.add(button, BorderLayout.CENTER)
  frame.pack()
  frame.setVisible(true)

}