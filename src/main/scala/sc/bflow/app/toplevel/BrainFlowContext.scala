package sc.bflow.app.toplevel

import sc.bflow.app.commands.CommandComponent

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/17/10
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */

trait BrainFlowContext extends FileSystemComponent with BrainFlowApplication with CommandComponent with DisplayComponent {

  val fileSystemService = new FileSystemService

  val displayService = new DisplayService


}