package sc.bflow.swing

import javax.swing.JComponent
import reactive.EventSource
import java.awt.event.{MouseAdapter, MouseMotionAdapter, MouseMotionListener, MouseEvent}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/1/11
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */


sealed trait SourceType

case object MouseMotion

case object MouseClicked

case object MousePressed

case object MouseReleased

case object MouseDragged

case object MouseEntered

case object MouseExited


object SwingEventSource {

  def apply(component: JComponent, eventType: SourceType) = {
    eventType match {
      case MouseMotion => new EventSource[MouseEvent] {
        component.addMouseMotionListener(new MouseMotionAdapter() {
          override def mouseMoved(e: MouseEvent) {
            fire(e)
          }

        })
      }
      case MouseDragged => new EventSource[MouseEvent] {
        component.addMouseMotionListener(new MouseMotionAdapter() {
          override def mouseDragged(e: MouseEvent) {
            fire(e)
          }

        })
      }
      case MouseClicked => new EventSource[MouseEvent] {
        component.addMouseListener(new MouseAdapter() {
          override def mouseClicked(e: MouseEvent) {
            fire(e)
          }
        })
      }
      case MousePressed => new EventSource[MouseEvent] {
        component.addMouseListener(new MouseAdapter() {
          override def mousePressed(e: MouseEvent) {
            fire(e)
          }

        })
      }

      case MouseReleased => new EventSource[MouseEvent] {
        component.addMouseListener(new MouseAdapter() {
          override def mouseReleased(e: MouseEvent) {
            fire(e)
          }

        })
      }

      case MouseEntered => new EventSource[MouseEvent] {
        component.addMouseListener(new MouseAdapter() {
          override def mouseEntered(e: MouseEvent) {
            fire(e)
          }
        })
      }

      case MouseExited => new EventSource[MouseEvent] {
        component.addMouseListener(new MouseAdapter() {
          override def mouseExited(e: MouseEvent) { fire(e) }
        })
      }


    }
  }

}