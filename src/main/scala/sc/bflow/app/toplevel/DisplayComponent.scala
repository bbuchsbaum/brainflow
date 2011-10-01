package sc.bflow.app.toplevel

import sc.bflow.image.io.{ImageSource3D, ImageSourceNode}
import sc.bflow.core.ImageView

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 9/26/11
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */

trait DisplayComponent {
  self: BrainFlowContext =>

  def displayService : DisplayService

  class DisplayService {

    def openVolume(source: ImageSource3D) = {
      val image = source.load(5)
      val imageView = ImageView(image)
      self.selectedCanvas() += imageView

    }
  }

}