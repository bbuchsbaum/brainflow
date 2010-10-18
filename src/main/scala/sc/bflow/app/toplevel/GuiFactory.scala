package sc.bflow.app.toplevel

import com.jidesoft.action.CommandMenuBar
import javax.swing._
import com.pietschy.command.factory. {MenuFactory, ButtonFactory}
import com.jidesoft.swing. {ButtonStyle, JideButton, JideToggleButton, JideMenu}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/17/10
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */

object GuiFactory extends MenuFactory with ButtonFactory {

  def createMenuItem = new JMenuItem

  def createCheckBoxMenuItem = new JCheckBoxMenuItem

  def createRadioButtonMenuItem = new JRadioButtonMenuItem

  def createPopupMenu = new JPopupMenu

  def createMenuBar = new CommandMenuBar

  def createButton = {
    val button: JideButton = new JideButton
    button.setButtonStyle(ButtonStyle.TOOLBAR_STYLE)
    button.setText("")
    button
  }

  def createToggleButton = new JideToggleButton

  def createCheckBox = new JCheckBox

  def createRadioButton = new JRadioButton

  def createMenu = new JideMenu
}