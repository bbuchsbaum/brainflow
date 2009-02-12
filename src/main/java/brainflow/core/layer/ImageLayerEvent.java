package brainflow.core.layer;

import brainflow.core.IImageDisplayModel;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 8:00:59 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageLayerEvent extends EventObject {

    private ImageLayer affectedLayer;

    private IImageDisplayModel model;

    public ImageLayerEvent(IImageDisplayModel _model, ImageLayer layer) {
        super(_model);
        model = _model;
        affectedLayer = layer;

    }

    public int getLayerIndex() {
        //hack cast
        return model.indexOf((ImageLayer3D)affectedLayer);
    }


    public ImageLayer getAffectedLayer() {
        return affectedLayer;
    }

    public IImageDisplayModel getModel() {
        return model;
    }


}
