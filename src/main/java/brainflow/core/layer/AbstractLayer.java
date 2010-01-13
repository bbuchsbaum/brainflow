package brainflow.core.layer;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.GridLoc3D;
import brainflow.image.space.ICoordinateSpace;
import brainflow.image.space.IImageSpace;
import brainflow.core.layer.LayerProps;
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
   

    private LayerProps props;

    private String name;

    protected AbstractLayer(String name, LayerProps props) {
        this.props = props;
        this.name = name;

    }

    public LayerProps getLayerProps() {
        return props;
    }


    public String getName() {
        return name;
    }

    public abstract double getValue(GridLoc3D pt);

    public abstract SliceRenderer getSliceRenderer(IImageSpace refspace, GridLoc3D slice, Anatomy3D displayAnatomy);


    public double getOpacity() {
        return props.opacity.get();
    }

    public IClipRange getThreshold() {
        return props.getThresholdRange();
    }

    public IClipRange getClipRange() {
        return props.getClipRange();
    }

    public abstract IMaskProperty getMaskProperty();

    public abstract Object getDataSource();

    public abstract ICoordinateSpace getCoordinateSpace();

    public abstract double getMinValue();

    public abstract double getMaxValue();

    


}
