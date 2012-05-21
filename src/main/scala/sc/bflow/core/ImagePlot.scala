package sc.bflow.core

import sc.bflow._
import brainflow.display.InterpolationType
import brainflow.image.axis.AxisRange
import java.awt.image.BufferedImage
import java.awt.geom.{AffineTransform, Rectangle2D}
import javax.swing.{JPanel, JComponent}
import scala.math._
import java.awt._
import brainflow.image.space.Axis
import brainflow.image.anatomy.Anatomy3D
import sc.bflow.image.space.GridPoint3D
import boxwood.binding.{Onlooker, Observable}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 1, 2010
 * Time: 9:15:14 AM
 * To change this template use File | Settings | File Templates.
 */



object ImagePlot {

  def apply(model: ImageViewModel, displayAnatomy: Anatomy3D = Anatomy3D.AXIAL_LPI) : ImagePlotPanel = {
    val renderer = new BasicImageViewRenderer(model)
    ImagePlot(model, renderer, displayAnatomy)
  }

  def apply(model: ImageViewModel, renderer: ImageViewRenderer[BufferedImage], displayAnatomy: Anatomy3D) : ImagePlotPanel = {
    val xrange: AxisRange = model.space.x_axis.getRange
    val yrange: AxisRange = model.space.y_axis.getRange

    val centroid = model.space.getCentroid
    val slice = GridPoint3D.fromReal(centroid.getX, centroid.getY, centroid.getZ, model.space)
    new ImagePlotPanel(renderer, displayAnatomy, slice, ImagePlotBounds(xrange, yrange))
  }


}


trait ImagePlot {

  def modelRenderer: ImageViewRenderer[BufferedImage]

  def xaxis: AxisRange

  def yaxis: AxisRange

  def displayAnatomy: Anatomy3D

  def scale: (Float, Float)

  def screenArea: Rectangle

  def slice: GridPoint3D

  def preserveAspectRatio: Boolean


}


case class ImagePlotBounds(xrange: AxisRange, yrange: AxisRange, plotInsets: Insets = new Insets(10, 10, 10, 10), plotSlack: Insets = new Insets(0, 0, 0, 0), preserveAspect: Boolean = true) {
  lazy val plotMargins = {
    new Insets(plotInsets.top + plotSlack.top, plotInsets.left + plotSlack.left, plotInsets.bottom + plotSlack.bottom, plotInsets.right + plotSlack.right)
  }

  lazy val region = new Rectangle2D.Double(xrange.getMinimum, yrange.getMinimum, xrange.getInterval, yrange.getInterval)


}


class ImagePlotPanel(val modelRenderer: ImageViewRenderer[BufferedImage], displayAnatomy0: Anatomy3D, slice0: GridPoint3D, private[this] var plotBounds0: ImagePlotBounds) extends JPanel with ImagePlot with Onlooker {
  setBackground(Color.BLACK)
  setOpaque(true)


  private[this] var plotArea: Rectangle = new Rectangle(0, 0, (plotBounds0.xrange.getInterval*1.5).toInt, (plotBounds0.yrange.getInterval*1.5).toInt)

  private[this] var cachedImage: Option[BufferedImage] = None

  val plotSlice = Observable[GridPoint3D](slice0)

  val plotAnatomy = Observable[Anatomy3D](displayAnatomy0)

  observe(plotSlice) { e =>
    println("plot slice changed to" + e.newValue.z)
    cachedImage = None
    repaint()
  }

  observe(plotAnatomy) { e =>
    //println("anatomy is now " + plotAnatomy)
    cachedImage = None
    repaint()

  }
  
  def clearAndRepaint() { 
    cachedImage = None 
    repaint()
  }

  def plotBounds = plotBounds0


  def preserveAspectRatio = true

  def plotState_(state: ImagePlotBounds) {
    plotBounds0 = state
    repaint()
  }

  def plotInsets = plotBounds.plotInsets

  def displayAnatomy = plotAnatomy()

  //def displayAnatomy_(anat: Anatomy3D) = plotAnatomy := anat

  def screenArea = plotArea

  def xaxis = plotBounds.xrange

  def yaxis = plotBounds.yrange

  def scale = ( (plotArea.getWidth / xaxis.getInterval).toFloat, (plotArea.getHeight / yaxis.getInterval).toFloat)

  def slice = plotSlice()


  override def getPreferredSize = new Dimension((xaxis.getInterval*1.5).toInt, (yaxis.getInterval*1.5).toInt)

  def computePlotArea: Rectangle = {
    val insets: Insets = getInsets
    val size: Dimension = getSize

    val available: Rectangle2D = new Rectangle2D.Double(insets.left + plotInsets.left, insets.top + plotInsets.top, size.getWidth - insets.left - insets.right - plotInsets.left - plotInsets.right, size.getHeight - insets.top - insets.bottom - plotInsets.top - plotInsets.bottom)

    val maxDrawWidth: Int = available.getWidth.toInt
    val maxDrawHeight: Int = available.getHeight.toInt

    val xspace = xaxis.getInterval
    val yspace = yaxis.getInterval

    val sx: Double = maxDrawWidth / xspace
    val sy: Double = maxDrawHeight / yspace

    var drawWidth: Int = maxDrawWidth
    var drawHeight: Int = maxDrawHeight
    var plotSlack = new Insets(0, 0, 0, 0)

    if (preserveAspectRatio) {
      var sxy: Double = min(sx, sy)
      drawWidth = (sxy * xspace).toInt
      drawHeight = (sxy * yspace).toInt
      plotSlack.left = ((maxDrawWidth - drawWidth) / 2.0).toInt
      plotSlack.right = round((maxDrawWidth - drawWidth) / 2.0).toInt
      plotSlack.top = ((maxDrawHeight - drawHeight) / 2.0).toInt
      plotSlack.bottom = round((maxDrawHeight - drawHeight) / 2.0).toInt
    }

    new Rectangle(insets.left + plotInsets.left + plotSlack.left, insets.top + plotInsets.top + plotSlack.top, drawWidth, drawHeight)

  }

  protected override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    val g2 = g.asInstanceOf[Graphics2D]

    val newPlotArea = computePlotArea
    if (newPlotArea != plotArea) {
      println("clearing cached image")
      cachedImage = None
      plotArea = newPlotArea
    }



    if (plotArea.getWidth > 5 && plotArea.getHeight > 5) {
      val image = cachedImage.getOrElse(modelRenderer.render(slice, displayAnatomy, plotBounds.region, plotArea, InterpolationType.LINEAR))
      g2.drawRenderedImage(image, AffineTransform.getTranslateInstance(plotArea.x, plotArea.y))
      cachedImage = Some(image)
    }

  }


}