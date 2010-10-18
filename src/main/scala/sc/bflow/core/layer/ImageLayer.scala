package sc.bflow.core.layer

import brainflow.image.data.IImageData
import brainflow.image.space.{ICoordinateSpace, IImageSpace}
import brainflow.core.{SliceRenderer, IClipRange}
import brainflow.image.anatomy.{Anatomy, Anatomy3D}
import sc.bflow.image.space.GridPoint3D

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 7, 2010
 * Time: 10:09:26 PM
 * To change this template use File | Settings | File Templates.
 */

trait ImageLayer {

  type D <: IImageData
  type S <: ICoordinateSpace
  type A <: Anatomy
  type P <: LayerProperties


  val data: D
  
  def createSliceRenderer(refspace: S, slice: GridPoint3D, displayAnatomy: A): ImageLayerRenderer3D

  def minValue: Double  = data.minValue

  def maxValue: Double  = data.maxValue

  def layerProperties: P



}