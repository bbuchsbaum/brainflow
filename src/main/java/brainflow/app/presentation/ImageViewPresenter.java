package brainflow.app.presentation;

import brainflow.app.services.ImageDisplayModelEvent;
import brainflow.app.services.ImageViewLayerSelectionEvent;
import brainflow.app.services.ImageViewSelectionEvent;
import brainflow.app.services.ImageViewModelChangedEvent;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.gui.AbstractPresenter;
import brainflow.image.space.IImageSpace;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import javax.swing.event.ListDataEvent;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 18, 2006
 * Time: 12:32:20 PM
 * To change this template use File | Settings | File Templates.
 */


public abstract class ImageViewPresenter extends AbstractPresenter implements ImageDisplayModelListener {


    private ImageView selectedView;

    private static Logger log = Logger.getLogger(ImageViewPresenter.class.getName());

    private EventSubscriber<ImageViewSelectionEvent> e1 = new EventSubscriber<ImageViewSelectionEvent>() {

        public void onEvent(ImageViewSelectionEvent evt) {
            if (selectedView != null) {
                viewDeselected(selectedView);
            }

            selectedView = evt.getSelectedImageView();
            if (selectedView != null) viewSelected(selectedView);
            else allViewsDeselected();


        }
    };

    private EventSubscriber<ImageViewLayerSelectionEvent> e2 = new EventSubscriber<ImageViewLayerSelectionEvent>() {

        public void onEvent(ImageViewLayerSelectionEvent evt) {
            if (evt.getSource() == getSelectedView()) {

                ImageLayer3D oldLayer = evt.getDeselectedLayer();
                if (oldLayer != null) {
                    layerDeselected(oldLayer);
                }

                ImageLayer3D layer = evt.getSelectedLayer();
                layerSelected(layer);

            }
        }
    };

    private EventSubscriber<ImageViewModelChangedEvent> e3 = new EventSubscriber<ImageViewModelChangedEvent>() {
        @Override
        public void onEvent(ImageViewModelChangedEvent event) {
            if (event.getImageView() == getSelectedView()) {
                viewModelChanged(getSelectedView(), event.getOldModel(), event.getNewModel());

            }
        }
    };


    public ImageViewPresenter() {
        subscribeListeners();
    }


    private void subscribeListeners() {
        EventBus.subscribe(ImageViewSelectionEvent.class, e1);
        EventBus.subscribe(ImageViewLayerSelectionEvent.class, e2);
        EventBus.subscribe(ImageViewModelChangedEvent.class, e3);


        EventBus.subscribeStrongly(ImageDisplayModelEvent.class, new EventSubscriber() {

            public void onEvent(Object evt) {
                ImageDisplayModelEvent event = (ImageDisplayModelEvent) evt;

                ImageView view = getSelectedView();

                if (view == null) return;

                if (view.getModel() == event.getModel()) {
                    layerChangeNotification();
                    switch (event.getType()) {
                        case LAYER_ADDED:
                            layerAdded(event.getListDataEvent());
                            break;
                        case LAYER_CHANGED:
                            layerChanged(event.getListDataEvent());
                            break;
                        case LAYER_INTERVAL_ADDED:
                            layerIntervalAdded(event.getListDataEvent());
                            break;
                        case LAYER_INTERVAL_REMOVED:
                            layerIntervalRemoved(event.getListDataEvent());
                            break;
                        case LAYER_REMOVED:
                            layerRemoved(event.getListDataEvent());
                            break;
                    }
                }
            }
        });


    }

    public abstract void viewSelected(ImageView view);

    public abstract void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel);

    public void viewDeselected(ImageView view) {
    }

    public abstract void allViewsDeselected();

    protected void layerChangeNotification() {
    }

    protected void layerDeselected(ImageLayer3D layer) {

    }

    protected void layerSelected(ImageLayer3D layer) {

    }

    protected void layerAdded(ListDataEvent event) {
    }

    protected void layerRemoved(ListDataEvent event) {
    }

    protected void layerChanged(ListDataEvent event) {
    }

    protected void layerIntervalAdded(ListDataEvent event) {
    }

    protected void layerIntervalRemoved(ListDataEvent event) {

    }

    public ImageView getSelectedView() {
        return selectedView;
    }

    public ImageLayer3D getSelectedLayer() {
        if (selectedView == null) return null;
        return selectedView.getSelectedLayer();

    }

    public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {

    }

    public void intervalAdded(ListDataEvent e) {

    }

    public void intervalRemoved(ListDataEvent e) {

    }

    public void contentsChanged(ListDataEvent e) {

    }
}
