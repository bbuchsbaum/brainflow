package sc.brainflow.core

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 9, 2010
 * Time: 7:21:58 PM
 * To change this template use File | Settings | File Templates.
 */

trait ImageSliceRenderer {
   
  def render: BufferedImage

  def renderUnto(frame: Rectangle2D, g2: Graphics2D): Unit
}