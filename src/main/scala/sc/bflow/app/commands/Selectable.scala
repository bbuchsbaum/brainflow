package sc.bflow.app.commands

import com.pietschy.command.toggle.ToggleCommand
import javax.swing.Icon
import boxwood.binding._

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 18, 2010
 * Time: 6:14:31 PM
 * To change this template use File | Settings | File Templates.
 */

class Selectable(init: Boolean = false) extends ToggleCommand with Observable[Boolean, Boolean] {
  super.setSelected(true)


  def transform(input: Boolean) = input

  def apply() = _selected

  private[this] var _selected: Boolean = this.isSelected

  override def addObserver(observer: Observer[Boolean])(implicit observing: Observing) = {
    println("somebody is observing me!")
    super.addObserver(observer)
  }

  def handleSelection(p1: Boolean) = {
    println("handling selection " + p1 + " and notifying " + this.observers.size + " observers")
    if (p1 != _selected) {
      _selected = p1
       notifyObservers(UpdateEvent(p1, Some(!p1), this), None)
    }

  }

  def update(newval: Boolean, dontNotify: Option[List[Observer[_]]]) = {
    if (!(newval == apply())) {
      setSelected(newval)
      notifyObservers(UpdateEvent(newval, Some(!newval), this), dontNotify)
    }
  }
}