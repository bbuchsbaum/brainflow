package brainflow.core;

import brainflow.core.layer.ImageLayer;

import javax.swing.event.ListDataEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 29, 2007
 * Time: 10:27:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageViewClient {


        
    public void selectedLayerChanged(ImageLayer layer);

    public void modelChanged(IImageDisplayModel model);

    public void layerContentsChanged(ListDataEvent event);

}
