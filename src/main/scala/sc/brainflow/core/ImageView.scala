package sc.brainflow.core

import sc.brainflow._
import layer.ImageLayer3D
import brainflow.image.anatomy.{Anatomy3D}
import sc.brainflow.image.data.BrainVolume
import boxwood.io.SystemResource
import java.awt.{Dimension, BorderLayout}
import sc.brainflow.image.space.GridPoint3D
import boxwood.binding._
import javax.swing.{SwingConstants, JSlider, JFrame, JPanel}
import swing.ObservableSlider
import boxwood.binding.swing._

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 5, 2010
 * Time: 3:57:24 PM
 * To change this template use File | Settings | File Templates.
 */

trait ImageView[T <: ImagePlot] {
  def model: ImageViewModel

  def plots: Seq[T]

  def selectedPlot: Observable[T]

  def selectedPlotIndex: Observable[Int]

  def selectedLayer: ImageLayer3D = model.selectedLayer

  def crosshair: Observable[GridPoint3D]

}



trait ImageViewLayout[T <: ImagePlot] {
  self: ImageView[T] =>

  def layoutPlots(panel: JPanel, plots: Seq[T]): Unit

}



abstract class AbstractImageView(val model: ImageViewModel) extends JPanel with ImageView[ImagePlotPanel] with Observing {
  lazy val plots = makePlots

  protected[this] def layoutPlots(plots: Seq[ImagePlotPanel]): Unit

  protected[this] def makePlots: Seq[ImagePlotPanel]

  val crosshair = {
    val centroid = model.space.getCentroid
    val gridCenter = GridPoint3D.fromReal(centroid.getX, centroid.getY, centroid.getZ, model.space)
    new BasicObservable(gridCenter) with SwingBindable[GridPoint3D]
    //new ObservablePath[ImagePlotPanel, GridPoint3D](selectedPlot, _().plotSlice)
  }




  setLayout(new BorderLayout())

  layoutPlots(plots)

  val selectedPlotIndex = Observable[Int](0)

  val selectedPlot: Observable[ImagePlotPanel] = Observable(plots(selectedPlotIndex.value))

  crosshair <|> new ObservablePath[ImagePlotPanel, GridPoint3D](selectedPlot, _().plotSlice)
  println("crosshair value is: " + crosshair.value)

  val sliceIndex = crosshair.transform(gp => gp.toIndex(selectedPlot.value.displayAnatomy.ZAXIS).x)

  Binder.swingConnect(crosshair, sliceIndex)(new Converter[GridPoint3D, Int] {
    def reverse(b: Int) = crosshair.value.

    def forward(a: GridPoint3D) = a.toIndex()
  }

  sliceIndex.addObserver( x => println("new slice index: " + x))




}

class SimpleImageView(model: ImageViewModel, val displayAnatomy: Anatomy3D = Anatomy3D.AXIAL_LAI) extends AbstractImageView(model) {
  def makePlots = {
    Seq(ImagePlot(model, displayAnatomy))
  }

  protected[this] def layoutPlots(plots: scala.Seq[ImagePlotPanel]) = {
    add(plots(0), BorderLayout.CENTER)
  }

}

object TestSimpleImageView {
  def main(args: Array[String]) = {
    val bvol = BrainVolume(new SystemResource("data/anat_alepi.nii").toFileObject)
    val layer = new ImageLayer3D(bvol)
    val model = ImageViewModel(layer)

    val view = new SimpleImageView(model) with ViewSlider

    val frame = new JFrame()
    frame.add(view, BorderLayout.CENTER)
    frame.setSize(500, 500)
    frame.setVisible(true)

  }
}


trait ViewSlider extends Observing {
  self: AbstractImageView =>


  val slider = new JSlider(SwingConstants.HORIZONTAL, 0, self.model.space.zdim - 1, (self.model.space.zdim - 1) / 2)

  self.sliceIndex <|> slider.sliderValue



  self.add(slider, BorderLayout.SOUTH)






}