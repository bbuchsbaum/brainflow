package brainflow.core;

import brainflow.core.layer.*;
import brainflow.colormap.IColorMap;
import brainflow.utils.WeakEventListenerList;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.axis.ImageAxis;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.data.IImageData3D;

import java.util.*;

import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.Property;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.EventListenerList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 8, 2009
 * Time: 8:17:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewModel implements Iterable {

    private List<Boolean> visibleList;

    private ImageLayerList layers;

    //private final LinkedHashMap<ImageLayer3D, Boolean> layers;

    private EventListenerList eventListeners = new EventListenerList();

    public final Property<Integer> layerSelection = ObservableProperty.create(-1);

    private String name = "view_model";

    private IImageSpace3D space;

    private Map<ImageLayer3D, List<PropListener<ImageLayerListener>>> listenerRefs = new HashMap<ImageLayer3D, List<PropListener<ImageLayerListener>>>();

    public ImageViewModel(String name, ImageLayer3D... _layers) {
        this(name, Arrays.asList(_layers));

    }

    public ImageViewModel(String name, List<ImageLayer3D> _layers) {
        this(name, _layers, 0);
    }

    public ImageViewModel(String name, List<ImageLayer3D> _layers, int selectedIndex) {
        BeanContainer.bind(this);

        if (_layers.isEmpty()) {
            throw new IllegalArgumentException("cannot instantiate an empty model");
        }
        if (selectedIndex < 0 || selectedIndex >= _layers.size()) {
            throw new IllegalArgumentException("selected index is out of range " + selectedIndex);

        }

        layers = new ImageLayerList(_layers);
        visibleList = new ArrayList<Boolean>(_layers.size());

        for (ImageLayer3D layer : _layers) {
            visibleList.add(true);
            listenToLayer(layer);
        }


        layerSelection.set(selectedIndex);
        this.name = name;

        space = get(0).getCoordinateSpace();
    }


    public ImageViewModel() {
        BeanContainer.bind(this);
        name = "empty";
        layers = new ImageLayerList();
        layerSelection.set(-1);
        space = Viewport3D.EMPTY_SPACE;

    }


    public List<ImageLayer3D> getLayers() {
        return layers.cloneList();
    }

    public String getName() {
        return name;
    }

    public void add(ImageLayer3D layer) {
        layers = layers.add(layer);
       // List<ImageLayer3D> l = getLayers();
        //l.add(layer);
        visibleList.add(true);

        fireListDataEvent(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, layers.size() - 1, layers.size() - 1));
        listenToLayer(layer);

    }

    public void remove(ImageLayer3D layer) {
        if (!layers.contains(layer)) {
            throw new IllegalArgumentException("model does not contain layer " + layer + ", cannot  remove.");
        }

        int i = indexOf(layer);
        remove(i);
        visibleList.remove(i);
        fireListDataEvent(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, i, i));

    }

    public void set(int i, ImageLayer3D layer) {
        ImageLayer3D selLayer = getSelectedLayer();
        layers.set(i, layer);
        layerSelection.set(layers.indexOf(selLayer));
        fireListDataEvent(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, i, i));


    }

    public void rotate() {
        ImageLayer3D selLayer = getSelectedLayer();
        layers = layers.rotate();
        Collections.rotate(visibleList, 1);
        layerSelection.set(layers.indexOf(selLayer));
        fireListDataEvent(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, layers.size()-1));
    }


    

    public ImageLayer3D remove(int i) {
        if (i < 0 || i >= size()) {
            throw new IllegalArgumentException("removal index is out of range " + i);
        }

        ImageLayer3D selLayer = getSelectedLayer();
        int selIndex = getSelectedIndex();

        List<ImageLayer3D> l = getLayers();
        ImageLayer3D oldLayer = l.remove(i);
        visibleList.remove(i);


        if (l.isEmpty()) {
            layers = new ImageLayerList();
            layerSelection.set(-1);
        } else {
            layers = new ImageLayerList(l);
            if (i == selIndex) {
                layerSelection.set(Math.max(0, selIndex - 1));
            } else {
                layerSelection.set(indexOf(selLayer));
            }
        }

        deafToLayer(oldLayer);

        fireListDataEvent(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, i, i));
        return oldLayer;
    }

    public void insert(int i, ImageLayer3D layer) {
        ImageLayer3D selLayer = getSelectedLayer();
        List<ImageLayer3D> l = getLayers();
        l.add(i, layer);
        visibleList.add(i, true);
        int selIndex = l.indexOf(selLayer);
        layers = new ImageLayerList(l);
        listenToLayer(layer);

        if (i == selIndex) {
            layerSelection.set(indexOf(selLayer));
        }

        fireListDataEvent(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, i, i));


    }

    public boolean contains(ImageLayer3D layer) {
        return layers.contains(layer);
    }

    public Iterator<ImageLayer3D> iterator() {
        return layers.iterator();
    }

    public ImageLayer3D get(int index) {
        return layers.get(index);

    }

    public int indexOf(ImageLayer3D layer) {
        return layers.indexOf(layer);
    }

    public List<Integer> indexOf(IImageData3D data) {
        List<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < size(); i++) {
            ImageLayer layer = get(i);
            if (layer.getDataSource().getData() == data) {
                ret.add(i);
            }
        }

        return ret;
    }

    public List<Integer> visibleLayers() {
        List<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < size(); i++) {
            if (visibleList.get(i)) {
                ret.add(i);
            }

        }
        return ret;

    }

    public void setVisible(int i, boolean vis) {

        boolean v = visibleList.get(i);
        if (v != vis) {
            visibleList.set(i, vis);
            fireVisiblityChanged(get(i));
        }
    }

    public boolean isVisible(int i) {
        return visibleList.get(i);
    }

    private void fireVisiblityChanged(ImageLayer3D layer) {
        ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
        for (ImageLayerListener listener : listeners) {
            listener.visibilityChanged(new ImageLayerEvent(ImageViewModel.this, layer));
        }

    }


    private void fireListDataEvent(ListDataEvent event) {
        ListDataListener[] listeners = eventListeners.getListeners(ListDataListener.class);
        for (ListDataListener listener : listeners) {
            switch (event.getType()) {
                case ListDataEvent.CONTENTS_CHANGED:
                    listener.contentsChanged(event);
                    break;
                case ListDataEvent.INTERVAL_ADDED:
                    listener.intervalAdded(event);
                    break;
                case ListDataEvent.INTERVAL_REMOVED:
                    listener.intervalRemoved(event);
                    break;

            }
        }

    }


    public int size() {
        return layers.size();
    }

    public boolean isEmpty() {
        return layers.size() == 0;
    }

    public IImageSpace3D getImageSpace() {
        return space;
    }

    public ImageAxis getImageAxis(Axis axis) {
        return getImageSpace().getImageAxis(axis);
    }

    public ImageAxis getImageAxis(AnatomicalAxis axis) {
        ImageAxis iaxis = getImageSpace().getImageAxis(axis, true);
        return iaxis.matchAxis(axis);
    }


    public void addImageLayerListener(ImageLayerListener listener) {
        //if (eventListeners.contains(ImageLayerListener.class, listener)) {
        //    throw new IllegalArgumentException("listener list already contains listener : " + listener);
        //}

        eventListeners.add(ImageLayerListener.class, listener);

    }

    public void removeImageLayerListener(ImageLayerListener listener) {
        eventListeners.remove(ImageLayerListener.class, listener);
    }

    public void addListDataListener(ListDataListener listener) {
        //if (eventListeners.contains(ListDataListener.class, listener)) {
        //    throw new IllegalArgumentException("listener list already contains listener : " + listener);
        //}

        eventListeners.add(ListDataListener.class, listener);

    }

    public void removeListDataListener(ListDataListener listener) {
        eventListeners.remove(ListDataListener.class, listener);
    }

    public ImageLayer3D getSelectedLayer() {
        return get(layerSelection.get());
    }

    public int getSelectedIndex() {
        return layerSelection.get();
    }

    private interface EventForwarder<T extends EventListener> {
        public void forwardEvent(T listener, ImageLayer3D layer, Object oldValue, Object newValue);

    }


    
    private class PropListener<T extends EventListener> implements PropertyListener {

        private ImageLayer3D layer;
        private Class<T> listenerClass;
        private EventForwarder<T> forwarder;

        private PropListener(ImageLayer3D layer, Class<T> listenerClass, EventForwarder<T> forwarder) {
            this.layer = layer;
            this.listenerClass = listenerClass;
            this.forwarder = forwarder;
        }

        @Override
        public void propertyChanged(BaseProperty baseProperty, Object o, Object o1, int i) {
            T[] listeners = eventListeners.getListeners(listenerClass);
            for (T listener : listeners) {
                forwarder.forwardEvent(listener, layer, o, o1);

            }

        }
    }

    private void deafToLayer(ImageLayer3D layer) {
        List<PropListener<ImageLayerListener>> refs = listenerRefs.get(layer);
        for (PropListener<ImageLayerListener> proplistener : refs) {
            BeanContainer.get().removeListener(layer, proplistener);
        }
    }


    private List<PropListener<ImageLayerListener>> listenToLayer(final ImageLayer3D layer) {


        PropListener<ImageLayerListener> colorMapListener = new PropListener<ImageLayerListener>(layer, ImageLayerListener.class,
                new EventForwarder<ImageLayerListener>() {
                    @Override
                    public void forwardEvent(ImageLayerListener listener, ImageLayer3D layer, Object o, Object o1) {
                        listener.colorMapChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                    }
                });


        PropListener<ImageLayerListener> interpolationListener = new PropListener<ImageLayerListener>(layer, ImageLayerListener.class,
                new EventForwarder<ImageLayerListener>() {
                    @Override
                    public void forwardEvent(ImageLayerListener listener, ImageLayer3D layer, Object o, Object o1) {
                        listener.interpolationMethodChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                    }
                });

        PropListener<ImageLayerListener> opacityListener = new PropListener<ImageLayerListener>(layer, ImageLayerListener.class,
                new EventForwarder<ImageLayerListener>() {
                    @Override
                    public void forwardEvent(ImageLayerListener listener, ImageLayer3D layer, Object o, Object o1) {
                        listener.opacityChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                    }
                });

        PropListener<ImageLayerListener> smoothingListener = new PropListener<ImageLayerListener>(layer, ImageLayerListener.class,
                new EventForwarder<ImageLayerListener>() {
                    @Override
                    public void forwardEvent(ImageLayerListener listener, ImageLayer3D layer, Object o, Object o1) {
                        listener.smoothingChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                    }
                });

        PropListener<ImageLayerListener> thresholdListener = new PropListener<ImageLayerListener>(layer, ImageLayerListener.class,
                new EventForwarder<ImageLayerListener>() {
                    @Override
                    public void forwardEvent(ImageLayerListener listener, ImageLayer3D layer, Object o, Object o1) {
                        listener.thresholdChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                    }
                });

        PropListener<ImageLayerListener> maskListener = new PropListener<ImageLayerListener>(layer, ImageLayerListener.class,
                new EventForwarder<ImageLayerListener>() {
                    @Override
                    public void forwardEvent(ImageLayerListener listener, ImageLayer3D layer, Object o, Object o1) {
                        listener.maskChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                    }
                });




        BeanContainer.get().addListener(layer.getImageLayerProperties().colorMap, colorMapListener);
        BeanContainer.get().addListener(layer.getImageLayerProperties().interpolationType, interpolationListener);
        BeanContainer.get().addListener(layer.getImageLayerProperties().opacity, opacityListener);
        BeanContainer.get().addListener(layer.getImageLayerProperties().smoothingRadius, smoothingListener);
        BeanContainer.get().addListener(layer.getImageLayerProperties().thresholdRange, thresholdListener);
        BeanContainer.get().addListener(layer.maskProperty, maskListener);

        List<PropListener<ImageLayerListener>> listeners = Arrays.asList(colorMapListener, interpolationListener, opacityListener,
                smoothingListener, thresholdListener, maskListener);





        BeanContainer.get().addListener(layer.getImageLayerProperties().clipRange, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ClipRange clip = (ClipRange) newValue;
                Number lowClip = clip.getLowClip();
                Number highClip = clip.getHighClip();

                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);

                IColorMap oldMap = layer.getImageLayerProperties().getColorMap();

                if (oldMap.getLowClip() == lowClip.doubleValue() && oldMap.getHighClip() == highClip.doubleValue()) {
                    return;
                }

                IColorMap newMap = oldMap.newClipRange(lowClip.doubleValue(), highClip.doubleValue(), clip.getMin(), clip.getMax());
                layer.getImageLayerProperties().colorMap.set(newMap);

                for (ImageLayerListener listener : listeners) {
                    listener.clipRangeChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                }

            }
        });

        return listeners;





    }

    @Override
    public String toString() {
        return name;
    }
}
