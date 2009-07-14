package brainflow.core.layer;

import brainflow.core.layer.LayerProps;
import brainflow.image.data.IImageData2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 6:10:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class LayerSlice {

    private IImageData2D data;

    private LayerProps props;

    public LayerSlice(IImageData2D _data, LayerProps _params) {
        data = _data;
        props = _params;
    }

    public IImageData2D getImageData() {
        return data;
    }

    public LayerProps getLayerProps() {
        return props;
    }




}
