/*
 * DisplayManager.java
 *
 * Created on April 30, 2003, 10:45 AM
 */

package brainflow.app.toplevel;

import brainflow.app.services.ImageViewMousePointerEvent;
import brainflow.app.services.ImageViewSelectionEvent;
import brainflow.app.services.ImageViewCursorEvent;
import brainflow.app.services.ImageViewModelChangedEvent;
import brainflow.app.dnd.ImageViewTransferHandler;
import brainflow.app.presentation.ImageViewPresenter;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.LayerList;
import brainflow.modes.ImageViewInteractor;
import brainflow.image.io.IImageDataSource;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;

import org.bushe.swing.event.EventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Bradley
 */


public class DisplayManager {

    public static final String SELECTED_CANVAS_PROPERTY = "selectedCanvas";

    private static final Logger log = Logger.getLogger(DisplayManager.class.getName());

    private List<IBrainCanvas> canvasList = new ArrayList<IBrainCanvas>();

    private IBrainCanvas selectedCanvas = null;
   
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private CanvasSelectionListener canvasListener;

    private ImageViewMouseMotionListener cursorListener;

    private CrossHairListener crossHairListener;


    //private WeakHashMap<ImageView, IImageDisplayModel> registeredViews = new WeakHashMap<ImageView, IImageDisplayModel>();

    

    protected DisplayManager() {

        
    }


    public static DisplayManager get() {
        return (DisplayManager) SingletonRegistry.REGISTRY.getInstance("brainflow.app.toplevel.DisplayManager");
    }

    private void listenToCanvas(IBrainCanvas canvas) {
        if (canvasListener == null) canvasListener = new CanvasSelectionListener();
        if (cursorListener == null) cursorListener = new ImageViewMouseMotionListener();
        if (crossHairListener == null) crossHairListener = new CrossHairListener();

        //canvas.getImageCanvasModel().addPropertyChangeListener(canvasListener);
        //canvas.getImageCanvasModel().listSelection.

        BeanContainer.get().addListener(canvas.getImageCanvasModel().listSelection, canvasListener);

        canvas.addInteractor(cursorListener);


    }


    /*public String register(ImageView view) {
        if (registeredViews.containsKey(view)) {
            log.warning("view " + view + " is already registered, no action taken");
            return view.getId();

        }

        registeredViews.put(view, view.getModel());

        if (registeredViews.size() >= ids.length) {
            return String.valueOf(registeredViews.size());
        }


        return ids[registeredViews.size() - 1];
    }*/


    public void addImageCanvas(IBrainCanvas _canvas) {
        canvasList.add(_canvas);
        if (selectedCanvas == null)
            selectedCanvas = _canvas;

        listenToCanvas(_canvas);


    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

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

    protected void displayView(final ImageView view) {
        view.setTransferHandler(new ImageViewTransferHandler());

        BeanContainer.get().addListener(view.viewModel, new PropertyListener() {
            @Override
            public void propertyChanged(BaseProperty baseProperty, Object o, Object o1, int i) {
                EventBus.publish(new ImageViewModelChangedEvent(view, (ImageViewModel)o, (ImageViewModel)o1));    
            }
        });

        getSelectedCanvas().addImageView(view);
    }

    protected void replaceLayer(ImageLayer3D oldlayer, ImageLayer3D newlayer, ImageView view) {
        int i = view.getModel().indexOf(oldlayer);

        if (i < 0) {
            throw new IllegalArgumentException("layer " + oldlayer + " not found in model " + view.getModel());
        }


        log.info("replacing layer : " + oldlayer + " with " + newlayer);

        List<ImageLayer3D> list = view.getModel().cloneList();
        list.set(i, newlayer);
        view.setModel(new ImageViewModel(view.getName(), list));
    }

    protected void setSelectedCanvas(IBrainCanvas canvas) {
        if (canvasList.contains(canvas)) {
            IBrainCanvas oldCanvas = this.selectedCanvas;
            selectedCanvas = canvas;
            support.firePropertyChange(SELECTED_CANVAS_PROPERTY, oldCanvas, selectedCanvas);
        } else {
            throw new RuntimeException("ImageCanvas " + canvas + " is not currently managed my DisplayManager.");
        }
    }


    public List<IBrainCanvas> getImageCanvases() {
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

            BeanContainer.get().removeListener(canvas.getImageCanvasModel().listSelection, canvasListener);
            
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

        //todo is this correct to return null?
        //todo perhaps throw exception?
        return null;

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

    class CrossHairListener extends ImageViewPresenter {

        class Listener implements PropertyListener {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                EventBus.publish(new ImageViewCursorEvent(getSelectedImageView()));
            }
        }

        Listener listener = new Listener();

        public void viewSelected(ImageView view) {
            EventBus.publish(new ImageViewCursorEvent(getSelectedView()));
            BeanContainer.get().addListener(view.cursorPos, listener);
        }


        @Override
        public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
            
        }

        @Override
        public void viewDeselected(ImageView view) {
            BeanContainer.get().removeListener(view.cursorPos, listener);
        }

        public void allViewsDeselected() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public JComponent getComponent() {
            throw new UnsupportedOperationException();
        }
    }


    class CanvasSelectionListener implements PropertyListener {

        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            BrainCanvasModel model = (BrainCanvasModel)prop.getParent();

            EventBus.publish(new ImageViewSelectionEvent(model.getSelectedView()));


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
