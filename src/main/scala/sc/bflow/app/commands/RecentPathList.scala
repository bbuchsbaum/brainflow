package sc.bflow.app.commands

import sc.bflow.app.toplevel.BrainFlowContext
import com.pietschy.command.group.{GroupBuilder, CommandGroup}
import com.pietschy.command.ActionCommand
import brainflow.app.actions.MountDirectoryCommand
import org.apache.commons.vfs.{VFS, FileObject}
import com.pietschy.command.face.Face
import java.util.prefs.{BackingStoreException, Preferences}
import boxwood.binding.{Add, ElemAdd, Onlooker}
import boxwood.io._
import boxwood.io.RichFileObject._
import VFSUtils._

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/21/10
 * Time: 10:42 PM
 * To change this template use File | Settings | File Templates.
 */

class RecentPathList(implicit context: BrainFlowContext) extends Onlooker {

  val NDIRS = 6

  val userPrefs: Preferences = Preferences.userNodeForPackage(classOf[RecentPathList])

  var recentDirectories: List[String] = {
    val userDir = System.getProperty("user.dir")
    val dirNames = (0 until NDIRS).map(i => userPrefs.get("recent-dir-" + (i + 1), userDir))
    val validNames = dirNames.filter(x => resolveFileObject(x, mustExist=true).isRight)

    validNames.distinct.toList
  }


  val commandGroup = initCommandGroup

  observe(context.fileSystemService.fileRoots) {
    case ev@Add(_, ElemAdd(x: FileObject, i)) => {
      if (!recentDirectories.contains(x.getName.getURI)) {
        addPath(x.getName.getURI)
        updateMenu()
        updatePrefs()
      }
    }
    case _ =>

  }


  def addPath(path: String) = {
    recentDirectories = (path +: recentDirectories).take(NDIRS)
  }

  def buildCommands(group: CommandGroup) = {
    val builder: GroupBuilder = group.getBuilder
    builder.clear
    val commands = recentDirectories.map{
      dirname =>
        val command = QuickCommand(context.fileSystemService.mount(VFS.getManager.resolveFile(dirname)))
        command.getFace(Face.MENU, true).setText(dirname)
        command
    }

    commands.foreach(builder.add(_))
    builder.applyChanges
    commands

  }

  def initCommandGroup = {
    val group = new CommandGroup
    group.getDefaultFace(true).setText("Recently Mounted")
    buildCommands(group)
    group

  }

  def updateMenu() = buildCommands(commandGroup)

  def updatePrefs(): Unit = {
    (0 until recentDirectories.size).foreach {
      i =>
        userPrefs.put("recent-dir-" + (i + 1), recentDirectories(i))
    }

    userPrefs.flush

  }


}