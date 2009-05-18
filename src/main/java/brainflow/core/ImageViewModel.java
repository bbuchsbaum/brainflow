package brainflow.core;

import brainflow.core.layer.*;
import brainflow.colormap.IColorMap;
import brainflow.utils.WeakEventListenerList;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.axis.ImageAxis;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.data.IImageData;
import brainflow.image.data.IImageData3D;

import java.util.*;

import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.Property;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 8, 2009
 * Time: 8:17:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewModel implements Iterable {


    private final LinkedHashMap<ImageLayer3D, Boolean> layers;

    private List<ImageLayer3D> layerList = new ArrayList<ImageLayer3D>();

    //todo potentially problematic
    private WeakEventListenerList eventListeners = new WeakEventListenerList();

    public final Property<Integer> layerSelection = ObservableProperty.create(0);

    private String name = "viewmodel";

    private IImageSpace3D space;

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
        layers = new LinkedHashMap<ImageLayer3D, Boolean>();

        for (ImageLayer3D layer : _layers) {
            layers.put(layer, true);
            listenToLayer(layer);
        }

        initList();
        layerSelection.set(selectedIndex);
        this.name = name;
        space = get(0).getCoordinateSpace();
    }


    private void initList() {
        ImageLayer3D[] tmp = new ImageLayer3D[layers.size()];
        layers.keySet().toArray(tmp);
        layerList = Collections.unmodifiableList(Arrays.asList(tmp));

    }


    public ImageViewModel() {
        BeanContainer.bind(this);
        name = "empty";
        layers = new LinkedHashMap<ImageLayer3D, Boolean>();
        layerSelection.set(-1);
        space = Viewport3D.EMPTY_SPACE;
        initList();
    }


    public List<ImageLayer3D> cloneList() {
        List<ImageLayer3D> ret = new ArrayList<ImageLayer3D>();
        for (ImageLayer3D layer : layerList) {
            ret.add(layer);
        }
        return ret;
    }

    public String getName() {
        return name;
    }

    public ImageViewModel add(ImageLayer3D layer) {
        List<ImageLayer3D> l = cloneList();
        l.add(layer);
        return new ImageViewModel(name, new ArrayList<ImageLayer3D>(l), layerSelection.get());

    }

    public ImageViewModel remove(int i) {
        if (i < 0 || i >= size()) {
            throw new IllegalArgumentException("removal index is out of range " + i);
        }

        if (size() == 1) {
            return new ImageViewModel("empty");
        }


        List<ImageLayer3D> l = cloneList();
        l.remove(i);

        if (i == layerSelection.get()) {
            return new ImageViewModel(name, new ArrayList<ImageLayer3D>(l), layerSelection.get() - 1);
        } else {
            return new ImageViewModel(name, new ArrayList<ImageLayer3D>(l), layerSelection.get());
        }
    }

    public ImageViewModel insert(int i, ImageLayer3D layer) {
        ImageLayer3D selLayer = getSelectedLayer();
        List<ImageLayer3D> l = cloneList();
        l.add(i, layer);

        int selIndex = l.indexOf(selLayer);
        return new ImageViewModel(name, new ArrayList<ImageLayer3D>(l), selIndex);
    }

    public boolean contains(ImageLayer3D layer) {
        return layers.containsKey(layer);
    }

    public Iterator<ImageLayer3D> iterator() {
        return layerList.iterator();
    }

    public ImageLayer3D get(int index) {
        return layerList.get(index);

    }

    public int indexOf(ImageLayer3D layer) {
        return layerList.indexOf(layer);


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
            ImageLayer3D layer = get(i);
            if (layers.get(layer)) {
                ret.add(i);
            }
        }

        return ret;

    }

    public void setVisible(int i, boolean vis) {
        ImageLayer3D layer = layerList.get(i);
        boolean v = layers.get(layer);
        if (v != vis) {
            layers.put(layer, vis);
            fireVisiblityChanged(layer);
        }
    }

    public boolean isVisible(int i) {
        return layers.get(layerList.get(i));
    }

    private void fireVisiblityChanged(ImageLayer3D layer) {
        ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
        for (ImageLayerListener listener : listeners) {
            listener.visibilityChanged(new ImageLayerEvent(ImageViewModel.this, layer));
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
        if (eventListeners.contains(ImageLayerListener.class, listener)) {
            throw new IllegalArgumentException("listener list already contains listener : " + listener);
        }

        eventListeners.add(ImageLayerListener.class, listener);

    }

    public void removeImageLayerListener(ImageLayerListener listener) {
        eventListeners.remove(ImageLayerListener.class, listener);
    }

    public ImageLayer3D getSelectedLayer() {
        return get(layerSelection.get());
    }

    public int getSelectedIndex() {
        return layerSelection.get();
    }


    private void listenToLayer(final ImageLayer3D layer) {

        BeanContainer.get().addListener(layer.getImageLayerProperties().colorMap, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.colorMapChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                }
            }
        });


        BeanContainer.get().addListener(layer.getImageLayerProperties().interpolationType, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.interpolationMethodChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                }
            }
        });

        

        BeanContainer.get().addListener(layer.getImageLayerProperties().opacity, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.opacityChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                }
            }
        });

        BeanContainer.get().addListener(layer.getImageLayerProperties().smoothingRadius, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.smoothingChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                }
            }
        });


        BeanContainer.get().addListener(layer.getImageLayerProperties().thresholdRange, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {

                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.thresholdChanged(new ImageLayerEvent(ImageViewModel.this, layer));
                }

            }
        });

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


        BeanContainer.get().addListener(layer.maskProperty, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {

                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.maskChanged(new ImageLayerEvent(ImageViewModel.this, layer));

                }

            }
        });


    }

}
