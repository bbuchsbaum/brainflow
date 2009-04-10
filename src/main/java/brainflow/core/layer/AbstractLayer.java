package brainflow.core.layer;

import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.space.ICoordinateSpace;
import brainflow.image.space.IImageSpace;
import brainflow.core.layer.ImageLayerProperties;
import brainflow.core.SliceRenderer;
import brainflow.core.IClipRange;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 15, 2007
 * Time: 9:51:11 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLayer {
    
    //private PropertyChangeSupport support = new PropertyChangeSupport(this);

    // instantiate to a NullMaskList or something to that effect.
    //private IMaskProperty maskProperty;

    private ImageLayerProperties properties;

    private String name;

    protected AbstractLayer(String name, ImageLayerProperties properties) {
        this.properties = properties;
        this.name = name;

    }

    public ImageLayerProperties getImageLayerProperties() {
        return properties;
    }


    public String getName() {
        return name;
    }

    public abstract double getValue(AnatomicalPoint3D pt);

    public abstract SliceRenderer getSliceRenderer(IImageSpace refspace, AnatomicalPoint3D slice, Anatomy3D displayAnatomy);

    //todo deprecate
    //public boolean isVisible() {
    //    return properties.isVisible();
    //}

    public double getOpacity() {
        return properties.opacity.get();
    }

    public IClipRange getThreshold() {
        return properties.getThresholdRange();
    }

    public IClipRange getClipRange() {
        return properties.getClipRange();
    }

    public abstract IMaskProperty getMaskProperty();

    public abstract Object getDataSource();

    public abstract ICoordinateSpace getCoordinateSpace();

    public abstract double getMinValue();

    public abstract double getMaxValue();

    public abstract String getLabel();


}
