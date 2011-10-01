package sc.bflow.core

import brainflow.image.space.IImageSpace3D
import boxwood.binding.{ObservableVar, Observable}
import layer.{LayerProperty, ImageLayer3D}
import collection.mutable.{Subscriber, Publisher}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 30, 2010
 * Time: 6:58:46 AM
 * To change this template use File | Settings | File Templates.
 */


case class LayerEvent(prop: LayerProperty[_], layer: ImageLayer3D)

trait ImageLayerModel extends Publisher[LayerEvent] with Subscriber[LayerProperty[_], ImageLayer3D] {
  type Pub <: ImageLayerModel
  
  def name: String

  def space: IImageSpace3D

  def layers: Seq[ImageLayer3D]

  layers.foreach {
    layer =>
      layer.subscribe(this)
  }

  def notify(pub: ImageLayer3D, event: LayerProperty[_]) {
    publish(LayerEvent(event, pub))
  }


}


object ImageLayerModel {

  def apply(layers: ImageLayer3D*): ImageLayerModel = {
    new ImageLayerModelImpl("anonymous", layers.toSeq)
  }

  def apply(name: String, layers: ImageLayer3D*): ImageLayerModel = {
    new ImageLayerModelImpl(name, layers.toSeq)
  }

  //implicit def toModelState(model: ImageLayerModel) : ImageViewModelState = {
  //  new ImageViewModelState(model)
  //}

  class ImageLayerModelImpl(val name: String, val layers: Seq[ImageLayer3D]) extends ImageLayerModel {

    def space = layers(0).data.getImageSpace

  }

}


object ImageViewModel {

  def apply(layers: ImageLayer3D*): ImageViewModel = {
    new ImageViewModel(ImageLayerModel(layers: _*))
  }

  def apply(layerModel: ImageLayerModel) = {
    new ImageViewModel(layerModel)
  }

  //implicit def toModel(mstate: ImageViewModelState) = mstate.model

}

class ImageViewModel(val layerModel: ImageLayerModel) extends Publisher[LayerEvent] with Subscriber[LayerEvent,  ImageLayerModel] {
  
  type Pub <: ImageViewModel
  
  layerModel.subscribe(this)

  val visibility = (0 until layerModel.layers.size).map(x => Observable[Boolean](true))

  val selectedIndex = Observable(0)

  def selectedLayer = layerModel.layers(selectedIndex())

  def layers = layerModel.layers

  def space = layerModel.space

  def isVisible(layer: ImageLayer3D) = {
    require(layerModel.layers.contains(layer))
    visibility(layerModel.layers.indexOf(layer)).apply
  }

  def isVisible(index: Int) = visibility(index).apply

  def isSelected(layer: ImageLayer3D) = {
    require(layerModel.layers.contains(layer))
    layerModel.layers.indexOf(layer) == selectedIndex()
  }

  def notify(pub: ImageLayerModel, event: LayerEvent) {
    publish(event)
  }
}




