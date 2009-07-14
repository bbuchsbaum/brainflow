package brainflow.core;


import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.axis.ImageAxis;
import brainflow.image.data.IImageData;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.core.layer.ImageLayerListener;
import brainflow.core.layer.LayerProps;
import brainflow.core.layer.ImageLayer3D;

import java.util.List;

import net.java.dev.properties.Property;
import net.java.dev.properties.IndexedProperty;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 16, 2004
 * Time: 10:11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageDisplayModel extends Iterable<ImageLayer3D> {


    public Property<Integer> getListSelection();

    public IndexedProperty<ImageLayer3D> getListModel();

    
    public String getName();

    public void setSelectedIndex(int index);

    public int getSelectedIndex();

    public ImageLayer3D getSelectedLayer();

    public void addImageDisplayModelListener(ImageDisplayModelListener listener);

    public void removeImageDisplayModelListener(ImageDisplayModelListener listener);

    public void addImageLayerListener(ImageLayerListener listener);

    public void removeImageLayerListener(ImageLayerListener listener);

    public String getLayerName(int idx);

    public List<Integer> indexOf(IImageData data);

    public int indexOf(ImageLayer3D layer);

    public void addLayer(ImageLayer3D layer);

    public void setLayer(int idx, ImageLayer3D layer);

    public void insertLayer(int index, ImageLayer3D layer);

    public void swapLayers(int index0, int index1);

    public void rotateLayers();

    public void removeLayer(int layer);

    public void removeLayer(ImageLayer3D layer);

    public LayerProps getLayerParameters(int layer);

    public ImageLayer3D getLayer(int layer);

    public boolean containsLayer(ImageLayer3D layer);

    public int getNumLayers();

    public IImageSpace3D getImageSpace();

    public ImageAxis getImageAxis(Axis axis);

    public ImageAxis getImageAxis(AnatomicalAxis axis);


}
