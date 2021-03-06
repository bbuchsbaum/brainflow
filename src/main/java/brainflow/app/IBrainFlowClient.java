package brainflow.app;

import brainflow.core.layer.AbstractLayer;
import brainflow.core.IImageDisplayModel;
import brainflow.core.ImageView;
import brainflow.core.layer.ImageLayer;
import brainflow.image.space.IImageSpace;

import javax.swing.event.ListDataEvent;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 13, 2007
 * Time: 10:24:49 PM
 */
public interface IBrainFlowClient {

    public void viewSelected(ImageView view);

    public void viewModelChanged(ImageView view);

    public void allViewsDeselected();

    public void layerChangeNotification();

    public void layerSelected(ImageLayer layer);

    public void layerAdded(ListDataEvent event);

    public void layerRemoved(ListDataEvent event);

    public void layerChanged(ListDataEvent event);

    public void layerIntervalAdded(ListDataEvent event);

    public void layerIntervalRemoved(ListDataEvent event);

    public ImageView getSelectedView();

    public AbstractLayer getSelectedLayer();



}
