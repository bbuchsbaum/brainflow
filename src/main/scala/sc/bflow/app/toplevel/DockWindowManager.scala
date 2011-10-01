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
    val dockableFrame: DockableFrame = new DockableFrame(title)
    dockableFrame.getContext.setInitMode(state)
    dockableFrame.getContext.setInitSide(side)
    dockableFrame.getContext.setInitIndex(0)
    dockableFrame.getContext.setHidable(true)
    dockMenu.add(createCommand(dockableFrame, title).createMenuItem())
    windowMap += (title -> dockableFrame)
    dockableFrame
  }


  def createDockableFrame(title: String, iconLocation: String, state: Int, side: Int): DockableFrame = {
    val dockableFrame: DockableFrame = new DockableFrame(title, new ImageIcon(getClass.getClassLoader.getResource(iconLocation)))
    dockableFrame.getContext.setInitMode(state)
    dockableFrame.getContext.setInitSide(side)
    dockableFrame.getContext.setInitIndex(0)
    dockableFrame.getContext.setHidable(true)
    dockMenu.add(createCommand(dockableFrame, title).createMenuItem())
    windowMap += (title -> dockableFrame)
    dockableFrame
  }


  private def createCommand(dframe: DockableFrame, title: String): ActionCommand = {
    val command: ActionCommand = new ActivateDockableFrameCommand(dframe)
    val menuFace: Face = command.getFace(Face.MENU, true)
    menuFace.setExtendsContext(Face.DEFAULT)
    menuFace.setText(title)
    command
  }


  def createDockableFrame(title: String, iconLocation: String, state: Int, side: Int, index: Int): DockableFrame = {
    val dockableFrame: DockableFrame = new DockableFrame(title, new ImageIcon(getClass.getClassLoader.getResource(iconLocation)))
    dockableFrame.getContext.setInitMode(state)
    dockableFrame.getContext.setInitSide(side)
    dockableFrame.getContext.setInitIndex(index)
   // dframe.addDockableFrameListener(this)
    windowMap += (title -> dockableFrame)
    dockMenu.add(createCommand(dockableFrame, title).createMenuItem())
    dockableFrame
  }







}