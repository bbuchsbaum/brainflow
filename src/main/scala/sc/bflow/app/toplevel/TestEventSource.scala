package sc.bflow.app.toplevel

import javax.swing.{JComponent, JPanel, JFrame}
import java.awt.{Point, BorderLayout}
import reactive.EventSource
import swing.event.MouseMotionEvent
import java.awt.event.{MouseMotionListener, MouseEvent}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/1/11
 * Time: 10:48 AM
 * To change this template use File | Settings | File Templates.
 */

class TestEventSource extends  reactive.Observing {

  val frame = new JFrame("mouse motion test")
  val panel = new JPanel()

  frame.add(panel, BorderLayout.CENTER)

  val estream = new MouseMotionEventSource(panel).map(_.getPoint).filter(_.getX < 200).foreach(x => println(x))

  frame.setSize(500,500)
  frame.setVisible(true)
  
  
  def main(args: Array[String]) = {
    val x = new TestEventSource()
  }

}


class MouseMotionEventSource(val component: JComponent) extends EventSource[MouseEvent]  {
  
  component.addMouseMotionListener(new MouseMotionListener() {
    def mouseDragged(e: MouseEvent) {}

    def mouseMoved(e: MouseEvent) {
      fire(e)
    }
  })
  

}