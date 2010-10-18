package sc.bflow.core

import javax.swing.event. {InternalFrameEvent, InternalFrameListener}
import javax.swing._
import java.awt._
import sc.bflow.image.data.BrainVolume
import boxwood.io.SystemResource

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 10, 2010
 * Time: 8:50:36 PM
 * To change this template use File | Settings | File Templates.
 */

trait ImageCanvas {
  self: JComponent =>

  def container: Container

  def model: ImageCanvasModel

  def +=(view: ImageViewPanel) = {
    addView(view)
    model.views += view
    if (model.selectedIndex().isEmpty) model.selectedIndex := Some(model.views.size-1)
  }

  def -=(view: ImageViewPanel) = {
    removeView(view)
    model.views -= view

  }


  protected[this] def addView(view: ImageViewPanel)

  protected[this] def removeView(view: ImageViewPanel)

  def whichSelectedView(p: Point): Option[ImageViewPanel] = {
    val view = whichView(p)
    view match {
      case Some(v) => if (v == model.selectedView) view else None
      case None => None
    }
  }

  def whichView(point: Point): Option[ImageViewPanel] = {
    val component: Component = container.findComponentAt(point)

    if (component.isInstanceOf[ImageViewPanel]) {
      Some(component.asInstanceOf[ImageViewPanel])
    } else {
      val cont: Container = SwingUtilities.getAncestorOfClass(classOf[ImageViewPanel], component)
      if (cont != null && cont.isInstanceOf[ImageViewPanel]) Some(cont.asInstanceOf[ImageViewPanel]) else None
    }
  }

  def whichView(source: Component, p: Point): Option[ImageViewPanel] = {
    val relPoint = SwingUtilities.convertPoint(source, p, container)
    whichView(relPoint)
  }

  def whichComponent(p: Point): Option[Component] = {
    val ret = container.findComponentAt(p)
    if (ret == null) None else Some(ret)
  }

  def whichPlot(p: Point): Option[ImagePlot] = {
    val view = whichView(p)
    view.flatMap( (v: ImageViewPanel) => {
      v.whichPlot(SwingUtilities.convertPoint(container, p, v))
    })
  }

  def allPlots: Seq[ImagePlot] = {
    model.views().map(_.plots).flatten
  }

}




class ImageCanvasDesktop(_views: ImageViewPanel*) extends JPanel with ImageCanvas with InternalFrameListener {


  val container: JDesktopPane = new JDesktopPane

  val model = ImageCanvasModel(_views :_*)
  model.views().foreach(v => addView(v))

  setLayout(new BorderLayout())
  add(container, BorderLayout.CENTER)

  private def findNearestFrame(loc: Point): Option[JInternalFrame] = {
    val frames: Array[JInternalFrame] = container.getAllFrames
    if (frames.length == 0) None else {
      val distances = frames.map(jf => loc.distance(jf.getLocation))
      Some(frames(distances.findIndexOf(_ == distances.min)))
    }
  }



  def positionForFrame(jframe: JInternalFrame) = {
    val d: Dimension = container.getSize
    val loc: Point = container.getLocation
    var p: Point = new Point((loc.x +.25 * d.width).toInt, (loc.y +.25 * d.height).toInt)
    if (container.getAllFrames.length > 0) {
      val nearest = findNearestFrame(p)
      val dist: Double = p.distance(nearest.get.getLocation)
      if (dist < 10) {
        p = new Point(nearest.get.getLocation().x + container.getComponentCount * 5, nearest.get.getLocation().y + container.getComponentCount * 5)
      }
    }

    p

  }

  private[this] def titleFor(view: ImageViewPanel) = "View [" + (model.views().indexOf(view) + 1) + "]"


  protected[this] def removeView(view: ImageViewPanel) = {}



  protected[this] def addView(view: ImageViewPanel) = {
    view.setSize(view.getPreferredSize)

    val jframe: JInternalFrame = new JInternalFrame("view", true, true, true, true)
    jframe.setContentPane(view)
    jframe.setSize(view.getSize)
    jframe.setLocation(positionForFrame(jframe))
    jframe.setVisible(true)
    jframe.addInternalFrameListener(this)

    jframe.setTitle(titleFor(view))
    container.add(jframe)
    jframe.moveToFront
  }

  def whichFrame(view: ImageViewPanel): Option[JInternalFrame] = {
    val frames: Array[JInternalFrame] = container.getAllFrames
    val ret = frames.filter(_.getContentPane == view)
    if (ret.size > 0) Some(ret(0)) else None
  }


  def internalFrameDeactivated(e: InternalFrameEvent) = {}

  private def updateSelection(view: ImageViewPanel): Unit = {
    val frame = whichFrame(view)
    frame.foreach( f => {
      f.moveToFront
      if (!f.isSelected) f.setSelected(true)
    })
  }

  def internalFrameActivated(e: InternalFrameEvent) = {
    val frame: JInternalFrame = e.getInternalFrame
    val comp: Component = frame.getContentPane

    if (comp.isInstanceOf[ImageViewPanel]) {
      val sel: ImageViewPanel = comp.asInstanceOf[ImageViewPanel]
      if (sel != model.selectedView.getOrElse(None)) {
        model.selectedIndex := Some(model.views.indexOf(sel))
        updateSelection(sel)
      }
    } else {
      model.selectedIndex := None
    }

  }

  def internalFrameDeiconified(e: InternalFrameEvent) = {}

  def internalFrameIconified(e: InternalFrameEvent) = {}

  def internalFrameClosed(e: InternalFrameEvent) = {
    if (e.getInternalFrame.getContentPane.isInstanceOf[ImageViewPanel]) {
      val view = e.getInternalFrame.getContentPane.asInstanceOf[ImageViewPanel]
      this -= view
    }
  }

  def internalFrameClosing(e: InternalFrameEvent) = {}

  def internalFrameOpened(e: InternalFrameEvent) = {}
}


object ImageCanvasDesktop {

  def main(args: Array[String]) = {
    val im1 = BrainVolume(SystemResource("data/anat_alepi.nii").toFileObject)

    val view1: ImageViewPanel = ImageView(im1)
    val view2: ImageViewPanel = ImageView(im1)

    val canvas = new ImageCanvasDesktop(view1, view2)
    val frame = new JFrame()
    frame.add(canvas, BorderLayout.CENTER)
    frame.setSize(800,800)
    frame.setVisible(true)


  }


}