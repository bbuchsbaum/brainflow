package sc.brainflow.app.commands

import com.pietschy.command.toggle.ToggleCommand
import javax.swing.Icon
import boxwood.binding.{Observing, Observer, ObservableVar, Observable}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 18, 2010
 * Time: 6:14:31 PM
 * To change this template use File | Settings | File Templates.
 */

class Selectable(init: Boolean = false) extends ToggleCommand with Observable[Boolean] {
  super.setSelected(true)

  private[this] var _selected: Boolean = this.isSelected

  override def addObserver(observer: Observer[Boolean])(implicit observing: Observing) = {
    println("somebody is observing me!")
    super.addObserver(observer)
  }

  def handleSelection(p1: Boolean) = {
    println("handling selection " + p1 + " and notifying " + this.observers.size + " observers")
    if (p1 != _selected) {
      _selected = p1
       notifyObservers(p1, Some(!p1), None)
    }

  }

  def value = _selected


  def update(newval: Boolean, dontNotify: Option[Observer[Boolean]]) = {
    if (!(newval == value)) {
      setSelected(newval)
      notifyObservers(newval, Some(!newval), dontNotify)
    }
  }
}