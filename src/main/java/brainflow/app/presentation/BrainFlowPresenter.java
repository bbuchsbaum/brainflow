package brainflow.app.presentation;

import brainflow.app.services.*;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.gui.AbstractPresenter;
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


public abstract class BrainFlowPresenter extends AbstractPresenter  {


    private ImageView selectedView;

    private static Logger log = Logger.getLogger(BrainFlowPresenter.class.getName());

    private EventSubscriber<ImageViewSelectionEvent> e1 = new EventSubscriber<ImageViewSelectionEvent>() {
        @Override
        public void onEvent(ImageViewSelectionEvent evt) {
            if (selectedView != null && !selectedView.getModel().isEmpty()) {
                viewDeselected(selectedView);
            }

            selectedView = evt.getSelectedImageView();
            if (selectedView != null && selectedView.getSelectedLayerIndex() >= 0) {
                viewSelected(selectedView);
            }
            else allViewsDeselected();


        }
    };

    private EventSubscriber<ImageViewLayerSelectionEvent> e2 = new EventSubscriber<ImageViewLayerSelectionEvent>() {
        @Override
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

    private EventSubscriber<ImageViewListDataEvent> e4 = new EventSubscriber<ImageViewListDataEvent>() {
        @Override
        public void onEvent(ImageViewListDataEvent event) {
            if (event.getImageView() == getSelectedView()) {
                ListDataEvent listEvent = event.getListDataEvent();

                layerChangeNotification();

                switch(listEvent.getType()) {
                    case ListDataEvent.INTERVAL_ADDED:
                        layerIntervalAdded(listEvent);
                        break;
                    case ListDataEvent.INTERVAL_REMOVED:
                        layerIntervalRemoved(listEvent);
                        break;
                    case ListDataEvent.CONTENTS_CHANGED:
                        layerContentsChanged(listEvent);
                        break;

                }

            }
        }
    };

    private EventSubscriber<ViewBoundsChangedEvent> e5 = new EventSubscriber<ViewBoundsChangedEvent>() {
        @Override
        public void onEvent(ViewBoundsChangedEvent event) {
            viewBoundsChanged(event);
        }

    };

    private EventSubscriber<BrainCanvasSelectionEvent> e6 = new EventSubscriber<BrainCanvasSelectionEvent>() {
        @Override
        public void onEvent(BrainCanvasSelectionEvent event) {
            canvasSelected(event.getSelectedCanvas());
        }
    };

    private EventSubscriber<ImagePlotSelectionEvent> e7 = new EventSubscriber<ImagePlotSelectionEvent>() {
        @Override
        public void onEvent(ImagePlotSelectionEvent event) {
            if (event.getOldSelectedPlot() != null) {
                plotDeselected(event.getOldSelectedPlot());

            }
            plotSelected(event.getSelectedPlot());
        }
    };





    public BrainFlowPresenter() {
        subscribeListeners();
    }


    private void subscribeListeners() {
        EventBus.subscribeStrongly(ImageViewSelectionEvent.class, e1);
        EventBus.subscribeStrongly(ImageViewLayerSelectionEvent.class, e2);
        EventBus.subscribeStrongly(ImageViewModelChangedEvent.class, e3);
        EventBus.subscribeStrongly(ImageViewListDataEvent.class, e4);
        EventBus.subscribeStrongly(ViewBoundsChangedEvent.class, e5);
        EventBus.subscribeStrongly(BrainCanvasSelectionEvent.class, e6);
        EventBus.subscribeStrongly(ImagePlotSelectionEvent.class, e7);

    }

    public abstract void viewSelected(ImageView view);

    public abstract void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel);

    public void viewDeselected(ImageView view) { }

    public abstract void allViewsDeselected();

    protected void layerChangeNotification() {}

    protected void layerDeselected(ImageLayer3D layer) {}

    protected void layerSelected(ImageLayer3D layer) {}

    protected void layerContentsChanged(ListDataEvent event) {}

    protected void layerIntervalAdded(ListDataEvent event) {}

    protected void layerIntervalRemoved(ListDataEvent event) {}

    protected void viewBoundsChanged(ViewBoundsChangedEvent event) {}

    protected void canvasSelected(IBrainCanvas canvas) {}

    protected void plotDeselected(IImagePlot plot) {}

    protected void plotSelected(IImagePlot plot) {}

    public ImageView getSelectedView() {
        return selectedView;
    }



    public ImageLayer3D getSelectedLayer() {
        if (selectedView == null) return null;
        return getSelectedView().getSelectedLayer();

    }


}
