package brainflow.core.layer;

import brainflow.image.anatomy.GridLoc3D;
import brainflow.image.io.IImageDataSource;
import brainflow.image.io.MemoryImageDataSource;

import brainflow.core.rendering.DefaultImageSliceRenderer;
import brainflow.core.SliceRenderer;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.data.*;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.interpolation.NearestNeighborInterpolator;
import brainflow.utils.Range;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.container.BeanContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 6:05:46 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageLayer3D extends ImageLayer<IImageSpace3D> {


    public final Property<MaskProperty3D> maskProperty = ObservableProperty.create();


    public ImageLayer3D(ImageLayer3D layer) {
        super(layer.getDataSource(), new LayerProps(layer.getLayerProps()));
        init();
    }

    public ImageLayer3D(IImageData data) {
        super(new MemoryImageDataSource(data), new Range(data.minValue(), data.maxValue()));
        init();
    }

    public ImageLayer3D(IImageData data, LayerProps _params) {
        super(new MemoryImageDataSource(data), _params);
        init();
    }


    public ImageLayer3D(IImageDataSource dataSource) {
        super(dataSource);
        init();
    }

    public ImageLayer3D(IImageDataSource dataSource, LayerProps _params) {
        super(dataSource, _params);
        init();
    }

    private void init() {
        BeanContainer.bind(this);
        maskProperty.set(new MaskProperty3D(this));

    }


    public IImageData3D getData() {
        return (IImageData3D) super.getData();
    }

    public double getValue(GridLoc3D pt) {
       SpatialLoc3D apt = pt.toWorld();
        return getData().worldValue((float) apt.getX(), (float) apt.getY(), (float) apt.getZ(), new NearestNeighborInterpolator());
    }

    public MaskProperty3D getMaskProperty() {
        return maskProperty.get();
    }

    public void setMaskProperty(MaskProperty3D mask) {
        maskProperty.set(mask);
    }

    //private Map<Anatomy3D, ImageSliceRenderer> rendererMap = new ConcurrentHashMap<Anatomy3D, ImageSliceRenderer>();


    private SliceRenderer getSliceRenderer(IImageSpace3D refspace, GridLoc3D slice, Anatomy3D displayAnatomy) {
        return new DefaultImageSliceRenderer(refspace, this, slice, displayAnatomy);
    }

    //todo what on earth?
    public SliceRenderer getSliceRenderer(IImageSpace refspace, GridLoc3D slice, Anatomy3D displayAnatomy) {
        return getSliceRenderer((IImageSpace3D)refspace, slice, displayAnatomy);
    }
    //todo what on earth?


}
