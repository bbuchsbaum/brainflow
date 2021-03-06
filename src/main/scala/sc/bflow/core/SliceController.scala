package sc.bflow.core

import sc.bflow.image.space.{GridPoint1D, GridPoint3D}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 12, 2010
 * Time: 8:22:43 PM
 * To change this template use File | Settings | File Templates.
 */

trait SliceController {
 self: ImageViewPanel =>

  def pageStep = .12

  def apply: GridPoint3D = self.selectedPlot().plotSlice()

  def update(slice: GridPoint3D) { self.selectedPlot().plotSlice := slice  }

  def nextSlice() { update(incrementSlice(zaxis.getSpacing)) }

  def previousSlice() { update(incrementSlice(-zaxis.getSpacing)) }

  def pageBack() { update(incrementSlice(-(zaxis.getExtent * pageStep))) }

  def pageForward() { update(incrementSlice(zaxis.getExtent * pageStep))  }

  def zaxis = apply.findAxis(self.selectedPlot().displayAnatomy.ZAXIS, true)

  protected def incrementSlice(incr: Double): GridPoint3D = {
    val curSlice = apply
    val iaxis = zaxis
    curSlice.replace(GridPoint1D(curSlice(iaxis.getAnatomicalAxis).x + incr, iaxis))
  }

}