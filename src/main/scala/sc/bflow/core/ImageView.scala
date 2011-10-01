package sc.bflow.core

import sc.bflow._
import app.commands.Selectable
import image.space.{GridPoint1D, IndexPoint1D, GridPoint3D}
import layer.ImageLayer3D
import sc.bflow.image.data.BrainVolume
import boxwood.io.SystemResource
import boxwood.binding._
import boxwood.binding.swing._
import javax.swing._
import brainflow.image.anatomy.{Anatomy3D}
import java.awt.{Point, BorderLayout}
import brainflow.image.data.IImageData3D
import collection.mutable.Subscriber
import net.miginfocom.swing.MigLayout
import collection.Seq

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 5, 2010
 * Time: 3:57:24 PM
 * To change this template use File | Settings | File Templates.
 */

trait ImageView[T <: ImagePlot] extends Subscriber[LayerEvent, ImageViewModel] {
  def model: ImageViewModel

  def plots: Seq[T]

  def selectedPlot: ObservableVar[T]

  def selectedPlotIndex: ObservableVar[Int]

  def selectedLayer: ImageLayer3D = model.selectedLayer

  def crosshair: ObservableVar[GridPoint3D]

  def sliceController: SliceController

  def whichPlot(point: Point): Option[T]

  
}

object ImageView {

  def apply(volumes: IImageData3D*) = {
    
    val layers = ImageLayerModel(volumes.map(vol => new ImageLayer3D(vol)): _*)
    val model = ImageViewModel(layers)

    new SimpleImageView(model)

  }

  def apply(model: ImageViewModel, displayAnatomy: Anatomy3D = Anatomy3D.getCanonicalAxial, slider: Boolean = true) = {
    if (slider) new SimpleImageView(model, displayAnatomy) with ViewSlider else new SimpleImageView(model, displayAnatomy)
  }
}


trait ImageViewLayout[T <: ImagePlot] {
  self: ImageView[T] =>

  def layoutPlots(panel: JPanel, plots: Seq[T])

}


abstract class ImageViewPanel(val model: ImageViewModel) extends JPanel with ImageView[ImagePlotPanel] with Observing with BindManager {

  lazy val plots = makePlots

  protected[this] def layoutPlots(plots: Seq[ImagePlotPanel])

  protected[this] def makePlots: Seq[ImagePlotPanel]

  val crosshair = {
    val centroid = model.space.getCentroid
    val gridCenter = GridPoint3D.fromReal(centroid.getX, centroid.getY, centroid.getZ, model.space)
    Observable(gridCenter)

  }

  setLayout(new BorderLayout())

  layoutPlots(plots)

  model.subscribe(this)

  def whichPlot(point: Point) = {
    val res = plots.filter(x => x.contains(SwingUtilities.convertPoint(this, point, x)))
    if (res.isEmpty) None else Some(res(0))
  }


  def notify(pub: ImageViewModel, event: LayerEvent) {
    println("layer changed event!!!!!")
    plots.foreach { p => p.clearAndRepaint() }
    
  }

  val selectedPlotIndex = Observable[Int](0)

  val selectedPlot = Observable(plots(selectedPlotIndex()))

  val sliceBinding = crosshair <|> new ObservablePath(selectedPlot, (x: ObservableVar[ImagePlotPanel]) => x().plotSlice)


}

class SimpleImageView(model: ImageViewModel, val displayAnatomy: Anatomy3D = Anatomy3D.AXIAL_LAI) extends ImageViewPanel(model) with SliceController {

  protected[this] def makePlots = {
    Seq(ImagePlot(model, displayAnatomy))
  }

  protected[this] def layoutPlots(plots: scala.Seq[ImagePlotPanel]) {
    add(plots(0), BorderLayout.CENTER)
  }

  def sliceController = this
}

object OrthoImageView {
  sealed trait Orientation
  case object Horizontal extends Orientation
  case object Vertical extends Orientation
  case object Triangular extends Orientation
}

class OrthoImageView(model: ImageViewModel, val displayAnatomy: Anatomy3D = Anatomy3D.AXIAL_LAI, val orientation: OrthoImageView.Orientation = OrthoImageView.Horizontal) extends ImageViewPanel(model) with SliceController {

  protected[this] def layoutPlots(plots: Seq[ImagePlotPanel]) {
    orientation match {
      case OrthoImageView.Horizontal => {
        val layout: BoxLayout = new BoxLayout(this, BoxLayout.X_AXIS)
        setLayout(layout)
        plots.foreach(p => add(p))
      }
      case OrthoImageView.Vertical => {
        val layout: BoxLayout = new BoxLayout(this, BoxLayout.Y_AXIS)
        setLayout(layout)
        plots.foreach(p => add(p))
      }
      case OrthoImageView.Triangular => {
        val layout: MigLayout = new MigLayout
        setLayout(layout)
        val zipped: Seq[(ImagePlotPanel, String)] = plots.zip(Seq("grow, width 80:400:1000, height 80:400:1000, span 2 2",
                      "grow, width 80:400:1000, height 80:400:1000, wrap",
                      "grow, width 80:400:1000, height 80:400:1000"))
        
        zipped.foreach(Z => add(Z._1, Z._2))
        
      }
        
     
    }
    

  }

  protected[this] def makePlots = {
    val anat = displayAnatomy.getCanonicalOrthogonal
    anat.map(x => {
      ImagePlot(model, x)
    })

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


trait ViewSlider {
  self: ImageViewPanel =>


  val slider = new JSlider(SwingConstants.HORIZONTAL, 0, self.model.space.zdim - 1, (self.model.space.zdim - 1) / 2)

  implicit val pointConverter = new Converter[GridPoint3D, Int]  {
    def invert(b: Int) = {
      //println("inverting point: " + b)
      val iaxis = crosshair().findAxis(selectedPlot().displayAnatomy.ZAXIS, true)
      crosshair().replace(IndexPoint1D(b, iaxis).toGrid)

    }

    def apply(a: GridPoint3D) = {
      //println("applying point: " + a)
      a.toIndex(selectedPlot().displayAnatomy.ZAXIS).x
    }
  }



  val crosshairBinding = self.crosshair <|> slider.sliderValue

  self.add(slider, BorderLayout.SOUTH)

  observe(crosshair) { e =>
    println("cross hair changed: ")
  }

}


trait ViewToolbar extends ImageViewPanel {


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

  // toggleAxial <|> new ObservablePath(selectedPlot, (x: Observable[ImagePlotPanel]) => x().plotAnatomy).map(_.isAxial)
  // toggleSagittal <|> new ObservablePath(selectedPlot, (x: Observable[ImagePlotPanel]) => x().plotAnatomy).map(_.isCoronal)
  //  toggleCoronal <|> new ObservablePath(selectedPlot, (x: Observable[ImagePlotPanel]) => x().plotAnatomy).map(_.isSagittal)

  toolbar.add(toggleAxial.createButton())
  toolbar.add(toggleSagittal.createButton())
  toolbar.add(toggleCoronal.createButton())


}





