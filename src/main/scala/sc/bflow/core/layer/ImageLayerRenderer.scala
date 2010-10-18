package sc.bflow.core.layer

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D
import brainflow.image.space.IImageSpace3D
import brainflow.image.data.IImageData2D
import sc.bflow.image.space.GridPoint3D

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 9, 2010
 * Time: 7:21:58 PM
 * To change this template use File | Settings | File Templates.
 */

trait ImageLayerRenderer3D {

  def layer: ImageLayer3D

  def refSpace: IImageSpace3D

  def cutPoint: GridPoint3D
   
  def render: BufferedImage

  def data: IImageData2D

  def renderUnto(frame: Rectangle2D, g2: Graphics2D): Unit
}