package brainflow.app;

import brainflow.core.ImageView;
import brainflow.core.IImageDisplayModel;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.AbstractLayer;
import brainflow.image.space.IImageSpace;

import javax.swing.event.ListDataEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Feb 28, 2009
 * Time: 10:51:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrainFlowClientAdapter implements IBrainFlowClient {
    public void viewSelected(ImageView view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerChangeNotification() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerSelected(ImageLayer layer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerAdded(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerRemoved(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerChanged(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerIntervalAdded(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerIntervalRemoved(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public ImageView getSelectedView() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AbstractLayer getSelectedLayer() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
