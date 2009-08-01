/*
 * ImageLayerSelectionEvent.java
 *
 * Created on July 5, 2006, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.app.services;

import brainflow.core.ImageView;
import brainflow.core.layer.ImageLayer3D;

/**
 * @author buchs
 */
public class ImageViewLayerSelectionEvent extends ImageViewEvent {


    private ImageLayer3D deselectedLayer;

    private ImageLayer3D selectedLayer;

    /**
     * Creates a new instance of ImageLayerSelectionEvent
     */

    public ImageViewLayerSelectionEvent(ImageView view, ImageLayer3D deselectedLayer, ImageLayer3D selectedLayer) {
        super(view);
        this.selectedLayer = selectedLayer;
        this.deselectedLayer = deselectedLayer;

    }

    public ImageLayer3D getSelectedLayer() {
        return selectedLayer;
    }

    public ImageLayer3D getDeselectedLayer() {
        return deselectedLayer;
    }

    public String toString() {
        return getImageView().getName() + " Selected Layer : " + selectedLayer;
    }

}
