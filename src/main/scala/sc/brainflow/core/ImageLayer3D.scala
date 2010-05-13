package sc.brainflow.core

import brainflow.image.data.IImageData3D
import brainflow.image.space.IImageSpace3D
import boxwood.binding.Observable
import brainflow.core.{IClipRange, ClipRange}
import brainflow.colormap.{LinearColorMap, ColorTable, IColorMap}
import brainflow.core.rendering.DefaultImageSliceRenderer
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import brainflow.image.anatomy.{GridLoc1D, GridLoc3D, Anatomy3D}
import brainflow.image.operations.ImageSlicer

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 8, 2010
 * Time: 8:37:34 AM
 * To change this template use File | Settings | File Templates.
 */

class ImageLayer3D(val name: String, val data: IImageData3D) extends ImageLayer with LayerProperties { self =>
  
  type D = IImageData3D
  type S = IImageSpace3D
  type A = Anatomy3D
  type P = LayerProperties


  val clipRange = Observable[IClipRange](new ClipRange(data.minValue, data.maxValue, data.minValue, data.maxValue))

  val thresholdRange = Observable[IClipRange](new ClipRange(data.minValue, data.maxValue, data.minValue, data.maxValue))

  val opacity = Observable[Double](1)

  val colorMap = Observable[IColorMap](new LinearColorMap(data.minValue, data.maxValue, ColorTable.GRAYSCALE))


  def createSliceRenderer(refspace: IImageSpace3D, slice: GridLoc3D, displayAnatomy: Anatomy3D) = null

  def layerProperties = this


  class DefaultRenderer(val refSpace: S, val slice: GridLoc3D, val displayAnatomy: Anatomy3D) extends ImageSliceRenderer {

    lazy val zslice = {
      val zdisp = slice.getValue(displayAnatomy.ZAXIS, false)
      val index: Int = Math.max((zdisp.getValue - .5f).asInstanceOf[Int],0)
      Math.min(index, refSpace.getDimension(displayAnatomy.ZAXIS) -1)
    }

    lazy val data = {
      var slice: Int = zslice
      ImageSlicer.createSlicer(refSpace, self.data).getSlice(displayAnatomy, zslice)
    }

    lazy val rgbImage = {
      self.colorMap.value.getRGBAImage(data)
    }

    lazy val thresholdedImage = {
      
    }

    def renderUnto(frame: Rectangle2D, g2: Graphics2D) = null

    def render = null


  }

  


}




