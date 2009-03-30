package brainflow.core;


import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableIndexed;
import net.java.dev.properties.container.ObservableProperty;

import java.util.*;
import java.util.logging.Logger;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import brainflow.app.YokeHandler;
import brainflow.display.ICrosshair;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 27, 2005
 * Time: 10:35:14 AM
 * To change this template use File | Settings | File Templates.
 */


public class BrainCanvasModel {

    private static final Logger log = Logger.getLogger(BrainCanvasModel.class.getCanonicalName());


    public final Property<Integer> listSelection = new ObservableProperty<Integer>(-1) {
        public void set(Integer integer) {
            if (integer.intValue() >= imageViewList.size()) {
                throw new IllegalArgumentException("selection index exceeds size of list");
            }
            super.set(integer);
        }
    };
    
    public final IndexedProperty<ImageView> imageViewList = ObservableIndexed.create();

    private Map<ImageView, YokeHandler> viewLinkMap = new WeakHashMap<ImageView, YokeHandler>();



    public BrainCanvasModel() {
        BeanContainer.bind(this);
        


    }


    private List<ImageView> views() {
        return  imageViewList.get();


    }

    public void setSelectedView(ImageView view) {
        if (views().contains(view)) {
            int i = views().indexOf(view);
            listSelection.set(i);
        } else {
            throw new IllegalArgumentException("Supplied argument " + view + " is not contained by canvas model ");
        }
    }

    public ImageView getSelectedView() {
        if (listSelection.get().intValue() < 0) {
            return null;
        }

        return imageViewList.get(listSelection.get());
    }

    public List<ImageView> getImageViews() {
        return views();
    }

    public int getNumViews() {
        return views().size();
    }

    public int indexOf(ImageView view) {
        return views().indexOf(view);
    }

    public void addImageView(ImageView view) {
        imageViewList.add(view);
        //if (listSelection.get() == -1) {
        //    listSelection.set(0);
        //}
    }

    public void removeImageView(ImageView view) {       
        imageViewList.remove(view);
        if (listSelection.get() == (getNumViews())) {
            listSelection.set(listSelection.get()-1);
        }

        cutLinks(view);
        view.clearListeners();
        System.gc();

    }

    private void cutLinks(ImageView view) {
        Set<ImageView> viewSet = viewLinkMap.keySet();
        Iterator<ImageView> iter = viewSet.iterator();

        while (iter.hasNext()) {
            ImageView cur = iter.next();
            if (viewLinkMap.get(cur).containsSource(view)) {

                YokeHandler handler = viewLinkMap.get(cur);
                handler.removeSource(view);
            }
        }

        viewLinkMap.remove(view);
      
    }

    public Set<ImageView> getYokedViews(ImageView view) {
        YokeHandler handler = viewLinkMap.get(view);

        Set<ImageView> ret;
        if (handler == null) {
            ret = new HashSet<ImageView>();
        } else {
            ret = handler.getSources();
        }

        return ret;

    }

    public void unyoke(ImageView target1, ImageView target2) {
        YokeHandler handler = viewLinkMap.get(target1);
        if (handler != null) {
            handler.removeSource(target2);
        }

        handler = viewLinkMap.get(target2);
        if (handler != null) {
            handler.removeSource(target1);
        }

    }


    public void yoke(ImageView target1, ImageView target2) {
        if (!imageViewList.get().contains(target1) || !imageViewList.get().contains(target2) ) {
            throw new IllegalArgumentException("both arguments must be contained in BrainCanvasModel");
        }



        YokeHandler handler = viewLinkMap.get(target1);
        if (handler == null) {
            handler = new YokeHandler(target1);
            viewLinkMap.put(target1, handler);
        }

        handler.addSource(target2);

        handler = viewLinkMap.get(target2);
        if (handler == null) {
            handler = new YokeHandler(target2);
            viewLinkMap.put(target2, handler);
        }

        handler.addSource(target1);


    }

    class CrosshairListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {

            ICrosshair cross = (ICrosshair) evt.getSource();
            //(ImageView)evt.getSource();


        }
    }


}
