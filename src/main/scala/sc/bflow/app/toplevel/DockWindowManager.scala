package sc.bflow.app.toplevel

import com.jidesoft.swing.JideMenu
import com.jidesoft.docking.DockableFrame
import javax.swing.ImageIcon
import com.pietschy.command.ActionCommand
import com.pietschy.command.face.Face
import brainflow.app.actions.ActivateDockableFrameCommand
import com.jidesoft.docking.event.DockableFrameEvent

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 10, 2010
 * Time: 4:32:49 PM
 * To change this template use File | Settings | File Templates.
 */

class DockWindowManager {


  var windowMap = Map.empty[String, DockableFrame]

  val dockMenu = new JideMenu("Window")

  dockMenu.setMnemonic('W')


  def createDockableFrame(title: String, state: Int, side: Int): DockableFrame = {
    val dframe: DockableFrame = new DockableFrame(title)
    dframe.getContext.setInitMode(state)
    dframe.getContext.setInitSide(side)
    dframe.getContext.setInitIndex(0)
    dframe.getContext.setHidable(true)
    dockMenu.add(createCommand(dframe, title).createMenuItem())
    windowMap += (title -> dframe)
    dframe
  }


  def createDockableFrame(title: String, iconLocation: String, state: Int, side: Int): DockableFrame = {
    val dframe: DockableFrame = new DockableFrame(title, new ImageIcon(getClass.getClassLoader.getResource(iconLocation)))
    dframe.getContext.setInitMode(state)
    dframe.getContext.setInitSide(side)
    dframe.getContext.setInitIndex(0)
    dframe.getContext.setHidable(true)
    dockMenu.add(createCommand(dframe, title).createMenuItem())
    windowMap += (title -> dframe)
    dframe
  }


  private def createCommand(dframe: DockableFrame, title: String): ActionCommand = {
    val command: ActionCommand = new ActivateDockableFrameCommand(dframe)
    val menuFace: Face = command.getFace(Face.MENU, true)
    menuFace.setExtendsContext(Face.DEFAULT)
    menuFace.setText(title)
    command
  }


  def createDockableFrame(title: String, iconLocation: String, state: Int, side: Int, index: Int): DockableFrame = {
    val dframe: DockableFrame = new DockableFrame(title, new ImageIcon(getClass.getClassLoader.getResource(iconLocation)))
    dframe.getContext.setInitMode(state)
    dframe.getContext.setInitSide(side)
    dframe.getContext.setInitIndex(index)
   // dframe.addDockableFrameListener(this)
    windowMap += (title -> dframe)
    dockMenu.add(createCommand(dframe, title).createMenuItem())
    dframe
  }







}