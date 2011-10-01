package sc.bflow.app.commands

import java.util.prefs.Preferences
import java.util.Date
import java.text.{ParseException, DateFormat}
import com.pietschy.command.group.{GroupBuilder, CommandGroup}
import org.apache.commons.vfs.VFS
import sc.bflow.app.toplevel.BrainFlowContext
import boxwood.binding.Observing

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/23/10
 * Time: 10:48 PM
 * To change this template use File | Settings | File Templates.
 */

class FavoritesList(implicit context: BrainFlowContext) extends Observing {

  val userPrefs = Preferences.userNodeForPackage(classOf[FavoritesList])

  val NFAVORITES = 10

  val favMap = {
    val userDir = System.getProperty("user.dir")

    val favorites = (0 until NFAVORITES).map(i => {
      val favStr = userPrefs.get("favorite-" + (i + 1), userDir)
      if (favStr == null) None else Some(Favorite(favStr))
    }).flatten[Favorite].map(x => (x.uri -> x))

    favorites.toMap
    //favorites.foldLeft(Map[String, Favorite]()) { (m, tup) => m(tup._1) = tup._2 }

  }

  val commandGroup = initCommandGroup

  def initCommandGroup = {
    val group = new CommandGroup
    group.getDefaultFace(true).setText("Favorites")
    buildCommands(group)
    group
  }



  def buildCommands(group: CommandGroup) = {
    val builder: GroupBuilder = group.getBuilder
    builder.clear
    val commands = favMap.values.toList.sortWith((x0, x1) => x0.lastAccessed.before(x1.lastAccessed)).map {
      fav =>
        QuickCommand(println("loading favorite " + fav), fav.uri)
    }

    commands.foreach(builder.add(_))
    builder.applyChanges
    commands
  }

  def updateMenu() = buildCommands(commandGroup)

  def updatePrefs() = {
    favMap.values.zip(0 until favMap.size).foreach { case (fav: Favorite, index: Int)  =>
      userPrefs.put("favorite-" + (index + 1), fav.toString)
    }

    userPrefs.flush

  }



}

object Favorite {

  def apply(str: String) : Favorite = {
    val ret = str.split("::")
    if (ret.length != 3) {
      throw new ParseException(String.format("could not parse entry %s", str), 0)
    }

    val uri: String = ret(0).trim
    val date: Date = DateFormat.getDateInstance.parse(ret(1).trim)
    val times: Int = Integer.parseInt(ret(2).trim)
    Favorite(uri, date, times)
  }
}

case class Favorite(uri: String, lastAccessed: Date = new Date(), timesAccessed: Int = 1) extends Ordered[Favorite] {
  def compare(that: Favorite) = lastAccessed.compareTo(that.lastAccessed)

  override def toString: String = {
    return uri + " :: " + DateFormat.getDateInstance.format(lastAccessed) + " :: " + timesAccessed
  }
}