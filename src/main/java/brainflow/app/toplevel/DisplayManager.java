/*
 * DisplayManager.java
 *
 * Created on April 30, 2003, 10:45 AM
 */

package brainflow.app.toplevel;

import brainflow.app.services.*;
import brainflow.app.dnd.ImageViewTransferHandler;
import brainflow.app.presentation.BrainFlowPresenter;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.modes.ImageViewInteractor;
import brainflow.image.io.IImageDataSource;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.events.IndexedPropertyListener;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.IndexedProperty;

import org.bushe.swing.event.EventBus;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Bradley
 */


public class DisplayManager {


    private static final Logger log = Logger.getLogger(DisplayManager.class.getName());

    private List<IBrainCanvas> canvasList = new ArrayList<IBrainCanvas>();

    private IBrainCanvas selectedCanvas = null;
   
    private ImageViewSelectionListener imageViewSelectionListener;
    private ImageViewListListener imageViewListListener;


    private ImageViewMouseMotionListener cursorListener;

    private CrossHairListener crossHairListener;


    protected DisplayManager() {

        
    }


    public static DisplayManager get() {
        return (DisplayManager) SingletonRegistry.REGISTRY.getInstance("brainflow.app.toplevel.DisplayManager");
    }

    private void listenToCanvas(IBrainCanvas canvas) {
        if (imageViewSelectionListener == null) imageViewSelectionListener = new ImageViewSelectionListener();
        if (imageViewListListener == null) imageViewListListener = new ImageViewListListener();
        if (cursorListener == null) cursorListener = new ImageViewMouseMotionListener();
        if (crossHairListener == null) crossHairListener = new CrossHairListener();

        //canvas.getImageCanvasModel().addPropertyChangeListener(imageViewSelectionListener);
        //canvas.getImageCanvasModel().listSelection.

        BeanContainer.get().addListener(canvas.getImageCanvasModel().listSelection, imageViewSelectionListener);
        BeanContainer.get().addListener(canvas.getImageCanvasModel().imageViewList, imageViewListListener);

        canvas.addInteractor(cursorListener);


    }





    public void addImageCanvas(IBrainCanvas _canvas) {
        canvasList.add(_canvas);
        if (selectedCanvas == null)
            selectedCanvas = _canvas;

        listenToCanvas(_canvas);


    }

    //public void addPropertyChangeListener(PropertyChangeListener listener) {
    //    support.addPropertyChangeListener(listener);
    //}

    //public void removePropertyChangeListener(PropertyChangeListener listener) {
    //    support.removePropertyChangeListener(listener);
   // }

    protected IBrainCanvas getSelectedCanvas() {
        return selectedCanvas;
    }

    protected List<ImageView> getImageViews(ImageViewModel model) {
        return selectedCanvas.getViews(model);
    }

    protected void removeView(ImageView view) {
        for (IBrainCanvas canvas : canvasList) {
            canvas.removeImageView(view);
        }
    }

    protected void updateViews(ImageViewModel oldModel, ImageViewModel newModel) {
        for (ImageView view : getImageViews(oldModel)) {
            view.setModel(newModel);
        }
    }

    protected boolean isShowing(IImageDataSource dsource) {
        for (IBrainCanvas canvas : canvasList) {
            List<ImageView> views = canvas.getViews();
            for (ImageView v : views) {
                ImageViewModel model = v.getModel();
                for (int i=0; i<model.size(); i++) {
                    if (model.get(i).getDataSource().equals(dsource)) {
                        return true;
                    }
                }
            }

        }

        return false;


    }

    protected void display(final ImageView view) {
        view.setTransferHandler(new ImageViewTransferHandler());

        // adding the listener here is a bit dicey as its behavior depends on using the "display" method.
        // it seems rather that this behavior should be in a singleton method.

        BeanContainer.get().addListener(view.viewModel, new PropertyListener() {
            @Override
            public void propertyChanged(BaseProperty baseProperty, Object o, Object o1, int i) {
                EventBus.publish(new ImageViewModelChangedEvent(view, (ImageViewModel)o, (ImageViewModel)o1));    
            }
        });



        view.addViewBoundsChangedListener(new ViewBoundsChangedListener() {
            @Override
            public void viewBoundsChanged(IImagePlot source, ViewBounds oldViewBounds, ViewBounds newViewBounds) {
                EventBus.publish(new ViewBoundsChangedEvent(view, source, oldViewBounds, newViewBounds));
            }
        });

        getSelectedCanvas().addImageView(view);
    }

    protected void replaceLayer(ImageLayer3D oldlayer, ImageLayer3D newlayer, ImageView view) {
        int i = view.getModel().indexOf(oldlayer);

        if (i < 0) {
            throw new IllegalArgumentException("layer " + oldlayer + " not found in model " + view.getModel());
        }


        view.getModel().set(i, newlayer);

    }

    protected void setSelectedCanvas(IBrainCanvas canvas) {
        if (canvasList.contains(canvas)) {
           selectedCanvas = canvas;
            selectedCanvas.getComponent().requestFocus();
            EventBus.publish(new BrainCanvasSelectionEvent(canvas));
            
            EventBus.publish(new ImageViewSelectionEvent(canvas.getSelectedView()));

        } else {
            throw new IllegalArgumentException("ImageCanvas " + canvas + " is not currently managed my DisplayManager.");
        }
    }


    public List<IBrainCanvas> getCanvasList() {
        return Collections.unmodifiableList(canvasList);
    }

    protected IBrainCanvas newCanvas() {
        IBrainCanvas canvas = new BrainCanvas();
        
        addImageCanvas(canvas);
        return canvas;
    }

    protected IBrainCanvas getImageCanvas(int idx) {
        if (canvasList.size() > idx && idx >= 0)
            return canvasList.get(idx);
        else {
            throw new IllegalArgumentException("No canvas exists at index" + idx);
        }
    }

    protected void removeImageCanvas(IBrainCanvas canvas) {
        if (canvasList.contains(canvas)) {
            canvasList.remove(canvas);

            BeanContainer.get().removeListener(canvas.getImageCanvasModel().listSelection, imageViewSelectionListener);
            
        }

    }

    /*public void addImageView(ImageView view) {
        if (!registeredViews.containsKey(view)) {
            register(view);
            BrainCanvas canvas = getSelectedCanvas();
            if (canvas != null) {
                canvas.add(view);
            }

        }
    }*/

    public ImageView getSelectedImageView() {
        if (getSelectedCanvas() != null)
            return selectedCanvas.getSelectedView();

        throw new RuntimeException("No selected canvas, therefore no selected view.");

    }

    public Set<ImageView> getYokedViews(ImageView view) {
        return getSelectedCanvas().getImageCanvasModel().getYokedViews(view);
    }

    
    public void unyoke(ImageView target1, ImageView target2) {
        getSelectedCanvas().getImageCanvasModel().unyoke(target1, target2);

    }


    public void yoke(ImageView target1, ImageView target2) {
        getSelectedCanvas().getImageCanvasModel().yoke(target1, target2);

    }

    class CrossHairListener extends BrainFlowPresenter {

        class CursorEventPublisher implements PropertyListener {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                EventBus.publish(new ImageViewCursorEvent(getSelectedImageView()));
            }
        }

        CursorEventPublisher cursorEventPublisher = new CursorEventPublisher();

        public void viewSelected(ImageView view) {
            EventBus.publish(new ImageViewCursorEvent(getSelectedView()));
            BeanContainer.get().addListener(view.cursorPos, cursorEventPublisher);
        }


        @Override
        public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
            
        }

        @Override
        public void viewDeselected(ImageView view) {
            BeanContainer.get().removeListener(view.cursorPos, cursorEventPublisher);
        }

        @Override
        public void allViewsDeselected() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public JComponent getComponent() {
            throw new UnsupportedOperationException();
        }
    }


    class ImageViewSelectionListener implements PropertyListener {

        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            BrainCanvasModel model = (BrainCanvasModel)prop.getParent();
            EventBus.publish(new ImageViewSelectionEvent(model.getSelectedView()));
        }


    }

    class ImageViewListListener implements IndexedPropertyListener {
        @Override
        public void propertyInserted(IndexedProperty indexedProperty, Object o, int i) {
            ImageView view = (ImageView)o;
            EventBus.publish(new BrainCanvasListDataEvent(getSelectedCanvas(), new ListDataEvent(view, ListDataEvent.INTERVAL_ADDED, i, i)));
        }

        @Override
        public void propertyRemoved(IndexedProperty indexedProperty, Object o, int i) {
            ImageView view = (ImageView)o;
            EventBus.publish(new BrainCanvasListDataEvent(getSelectedCanvas(), new ListDataEvent(view, ListDataEvent.INTERVAL_REMOVED, i, i)));
        }

        @Override
        public void propertyChanged(BaseProperty baseProperty, Object o, Object o1, int i) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }



    class ImageViewMouseMotionListener extends ImageViewInteractor {


        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            ImageView iview = selectedCanvas.whichView((Component) e.getSource(), p);

            if (iview != selectedCanvas.getSelectedView()) {
                return;
            }

            EventBus.publish(new ImageViewMousePointerEvent(iview, e));

        }
    }


}
