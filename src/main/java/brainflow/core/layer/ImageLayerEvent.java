package brainflow.core.layer;

import brainflow.core.ImageViewModel;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 8:00:59 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageLayerEvent extends EventObject {

    private ImageLayer3D affectedLayer;

    private ImageViewModel model;

    public ImageLayerEvent(ImageViewModel _model, ImageLayer3D layer) {
        super(_model);
        model = _model;
        affectedLayer = layer;

    }

    public int getLayerIndex() {
        return model.indexOf(affectedLayer);
    }


    public ImageLayer3D getAffectedLayer() {
        return affectedLayer;
    }

    public ImageViewModel getModel() {
        return model;
    }


}
