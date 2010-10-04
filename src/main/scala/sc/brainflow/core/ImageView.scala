package sc.brainflow.core

import sc.brainflow._
import app.commands.Selectable
import image.space.{GridPoint1D, IndexPoint1D, GridPoint3D}
import layer.ImageLayer3D
import sc.brainflow.image.data.BrainVolume
import boxwood.io.SystemResource
import java.awt.{Dimension, BorderLayout}
import boxwood.binding._
import swing.ObservableSlider
import boxwood.binding.swing._
import javax.swing._
import brainflow.image.anatomy.{SpatialLoc1D, GridLoc1D, Anatomy3D}
import brainflow.image.axis.ImageAxis

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

  def sliceController : SliceController

}



trait ImageViewLayout[T <: ImagePlot] {
  self: ImageView[T] =>

  def layoutPlots(panel: JPanel, plots: Seq[T]): Unit

}



abstract class AbstractImageView(val model: ImageViewModel) extends JPanel with ImageView[ImagePlotPanel] with Observing with BindManager {

  lazy val plots = makePlots

  protected[this] def layoutPlots(plots: Seq[ImagePlotPanel]): Unit

  protected[this] def makePlots: Seq[ImagePlotPanel]

  val crosshair = {
    val centroid = model.space.getCentroid
    val gridCenter = GridPoint3D.fromReal(centroid.getX, centroid.getY, centroid.getZ, model.space)
    new ObservableVar(gridCenter) with SwingBindable[GridPoint3D]

  }

  setLayout(new BorderLayout())

  layoutPlots(plots)

  val selectedPlotIndex = Observable[Int](0)

  val selectedPlot: Observable[ImagePlotPanel] = Observable(plots(selectedPlotIndex.value))

  crosshair <|> new ObservablePath(selectedPlot, (x: Observable[ImagePlotPanel]) => x().plotSlice)



}

class SimpleImageView(model: ImageViewModel, val displayAnatomy: Anatomy3D = Anatomy3D.AXIAL_LAI) extends AbstractImageView(model) with SliceController {

  def makePlots = {
    Seq(ImagePlot(model, displayAnatomy))
  }

  protected[this] def layoutPlots(plots: scala.Seq[ImagePlotPanel]) = {
    add(plots(0), BorderLayout.CENTER)
  }

  def sliceController = this
}

object TestSimpleImageView {
  def main(args: Array[String]) = {
    val bvol = BrainVolume(new SystemResource("data/anat_alepi.nii").toFileObject)
    val layer = new ImageLayer3D(bvol)
    val model = ImageViewModel(layer)

    val view = new SimpleImageView(model) with ViewSlider with SimpleToolbar

    val frame = new JFrame()
    frame.add(view, BorderLayout.CENTER)
    frame.setSize(500, 500)
    frame.setVisible(true)

  }
}


trait ViewSlider extends Observing {
  self: AbstractImageView =>


  val slider = new JSlider(SwingConstants.HORIZONTAL, 0, self.model.space.zdim - 1, (self.model.space.zdim - 1) / 2)

  implicit val pointConverter = new Converter[GridPoint3D, Int] {
    def invert(b: Int) = {
      val iaxis = crosshair().findAxis(selectedPlot().displayAnatomy.ZAXIS)
      crosshair().replace(IndexPoint1D(b, iaxis).toGrid)

    }

    def apply(a: GridPoint3D) = {
      a.toIndex(selectedPlot().displayAnatomy.ZAXIS).x
    }
  }

  self.crosshair <*> slider.sliderValue

  self.add(slider, BorderLayout.SOUTH)


}



trait ViewToolbar extends AbstractImageView {


  val toolbar = new JToolBar()

  add(toolbar, BorderLayout.NORTH)

}

trait SimpleToolbar extends ViewToolbar {

  val toggleAxial = new Selectable(selectedPlot().plotAnatomy().isAxial)
  val toggleSagittal = new Selectable(selectedPlot().plotAnatomy().isCoronal)
  val toggleCoronal = new Selectable(selectedPlot().plotAnatomy().isSagittal)
  toggleAxial.getDefaultFace(true).setText("Axial")
  toggleSagittal.getDefaultFace(true).setText("Sagittal")
  toggleCoronal.getDefaultFace(true).setText("Coronal")

  println("binding selectable")

  toggleAxial <|> new ObservablePath(selectedPlot, (x: Observable[ImagePlotPanel]) => x().plotAnatomy).map(_.isAxial)
  toggleSagittal <|> new ObservablePath(selectedPlot, (x: Observable[ImagePlotPanel]) => x().plotAnatomy).map(_.isCoronal)
  toggleCoronal <|> new ObservablePath(selectedPlot, (x: Observable[ImagePlotPanel]) => x().plotAnatomy).map(_.isSagittal)

  toolbar.add(toggleAxial.createButton())
  toolbar.add(toggleSagittal.createButton())
  toolbar.add(toggleCoronal.createButton())


}





