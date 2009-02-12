/*
 * ImageLayerSelectionEvent.java
 *
 * Created on July 5, 2006, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.application.services;

import brainflow.core.ImageView;
import brainflow.core.layer.ImageLayer;

/**
 * @author buchs
 */
public class ImageViewLayerSelectionEvent extends ImageViewEvent {


    private ImageLayer deselectedLayer;

    private ImageLayer selectedLayer;

    /**
     * Creates a new instance of ImageLayerSelectionEvent
     */

    public ImageViewLayerSelectionEvent(ImageView view, ImageLayer deselectedLayer, ImageLayer selectedLayer) {
        super(view);
        this.selectedLayer = selectedLayer;
        this.deselectedLayer = deselectedLayer;

    }

    public ImageLayer getSelectedLayer() {
        return selectedLayer;
    }

    public ImageLayer getDeselectedLayer() {
        return deselectedLayer;
    }

    public String toString() {
        return getImageView().getName() + " Selected Layer : " + selectedLayer;
    }

}
