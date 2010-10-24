package tests.sc.brainflow.app.presentation

import org.scalatest.FlatSpec
import boxwood.io.SystemResource
import sc.bflow.app.presentation.ImageFileExplorer

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jun 27, 2010
 * Time: 6:41:40 PM
 * To change this template use File | Settings | File Templates.
 */

class ImageFileExplorerSpec extends FlatSpec {

  val resourceDir = new SystemResource("data").toFileObject

  "An ImageFileExplorer" should "should be instantiable" in {
    val explorer = new ImageFileExplorer(resourceDir)
    assert(explorer != null)
  }



}