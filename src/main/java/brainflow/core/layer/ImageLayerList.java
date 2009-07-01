package brainflow.core.layer;

import brainflow.core.layer.*;
import brainflow.core.Viewport3D;
import brainflow.core.ClipRange;
import brainflow.core.ImageViewModel;
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
public class ImageLayerList implements Iterable<ImageLayer3D> {



    private List<ImageLayer3D> layerList = new ArrayList<ImageLayer3D>();

    private IImageSpace3D space;

    public ImageLayerList(ImageLayer3D... _layers) {
        this(Arrays.asList(_layers));
    }

    public ImageLayerList(List<ImageLayer3D> _layers) {

        if (_layers.isEmpty()) {
            throw new IllegalArgumentException("cannot instantiate an empty model");
        }

        layerList = _layers;
        space = get(0).getCoordinateSpace();
    }





    public ImageLayerList() {
        layerList = new ArrayList<ImageLayer3D>();
        space = Viewport3D.EMPTY_SPACE;

    }


    public List<ImageLayer3D> cloneList() {
        List<ImageLayer3D> ret = new ArrayList<ImageLayer3D>();
        for (ImageLayer3D layer : layerList) {
            ret.add(layer);
        }
        return ret;
    }

    public ImageLayerList add(ImageLayer3D layer) {
        List<ImageLayer3D> l = cloneList();
        l.add(layer);
        return new ImageLayerList(new ArrayList<ImageLayer3D>(l));
    }

    public ImageLayerList remove(int i) {
        if (i < 0 || i >= size()) {
            throw new IllegalArgumentException("removal index is out of range " + i);
        }

        if (size() == 1) {
            return new ImageLayerList();
        }

        List<ImageLayer3D> l = cloneList();
        l.remove(i);
        return new ImageLayerList(l);


    }

    public ImageLayerList insert(int i, ImageLayer3D layer) {
        List<ImageLayer3D> l = cloneList();
        l.add(i, layer);

        return new ImageLayerList(new ArrayList<ImageLayer3D>(l));
    }

    public ImageLayerList set(int i, ImageLayer3D layer) {
        List<ImageLayer3D> l = cloneList();
        l.set(i, layer);
        return new ImageLayerList(l);


    }

    public ImageLayerList rotate() {
        List<ImageLayer3D> list = cloneList();
        Collections.rotate(list,1);
        return new ImageLayerList(list);


    }

    public boolean contains(ImageLayer3D layer) {
        return layerList.contains(layer);
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


    public int size() {
        return layerList.size();
    }

    public boolean isEmpty() {
        return layerList.size() == 0;
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





}