package sc.bflow.core

import boxwood.binding._

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 11, 2010
 * Time: 2:00:18 PM
 * To change this template use File | Settings | File Templates.
 */

trait ImageCanvasModel extends Onlooker {

  val selectedIndex: ObservableVar[Option[Int]]

  val views: ObservableBuffer[ImageViewPanel]

  observe(views) {
    case ev @ Remove(_, ElemRemove(x:ImageViewPanel,i)) => {
      val ind = selectedIndex()
      if (ind.isDefined && views().size == 0) {
        selectedIndex := None
      } else if (ind.isDefined && ind.get >= views().size) {
        selectedIndex := Some(views().size - 1)
      }
    }

    case _ =>

  }

  def selectedView : Option[ImageViewPanel] = {
    selectedIndex() match {
      case Some(index) => Some(views()(index))
      case None => None
    }
  }

  def size = views().size

}

object ImageCanvasModel {

  def apply(_views: ImageViewPanel*) = {
    new ImageCanvasModel {
      lazy val selectedIndex: ObservableVar[Option[Int]] = Observable(Some(0))
      lazy val views = ObservableBuffer(_views)
    }
  }
}