package brainflow.application.presentation;

import brainflow.application.services.ImageDisplayModelEvent;
import brainflow.application.services.ImageViewLayerSelectionEvent;
import brainflow.application.services.ImageViewSelectionEvent;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer;
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

    public ImageViewPresenter() {
        subscribeListeners();
    }


    private void subscribeListeners() {
        EventBus.subscribeStrongly(ImageViewSelectionEvent.class, new EventSubscriber() {

            public void onEvent(Object evt) {
               // System.out.println("view changed event");
              //  System.out.println("class is " + ImageViewPresenter.this.getClass());

                long start = System.currentTimeMillis();

                ImageViewSelectionEvent event = (ImageViewSelectionEvent) evt;

                // remove listeners from old selected view
                if (selectedView != null) {
                    selectedView.getModel().removeImageDisplayModelListener(ImageViewPresenter.this);
                    viewDeselected(selectedView);
                }


                selectedView = event.getSelectedImageView();


                if (selectedView != null) {
                    selectedView.getModel().addImageDisplayModelListener(ImageViewPresenter.this);                 
                    viewSelected(selectedView);

                } else {
                    allViewsDeselected();
                }

                long end = System.currentTimeMillis();
               // System.out.println("view event took: " + (end - start));

               

            }
        });

        EventBus.subscribeStrongly(ImageViewLayerSelectionEvent.class, new EventSubscriber() {

            public void onEvent(Object evt) {
                ImageViewLayerSelectionEvent event = (ImageViewLayerSelectionEvent) evt;
                if (event.getSource() == getSelectedView()) {

                    ImageLayer3D oldLayer = event.getDeselectedLayer();
                    if (oldLayer != null) {
                        layerDeselected(oldLayer);
                    }

                    ImageLayer3D layer = event.getSelectedLayer();
                    layerSelected(layer);

                }
            }
        });


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

    public  void viewDeselected(ImageView view) {}

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
