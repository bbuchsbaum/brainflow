package brainflow.core.layer;

import brainflow.image.anatomy.GridLoc3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.space.IImageSpace;
import brainflow.core.SliceRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 4, 2008
 * Time: 7:12:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ILayer {


    //use event bus for properties?
    // fix 4d advancement issue first though
    public String getName();

    public LayerProps getLayerProps();

    public abstract IMaskProperty getMaskProperty();

    public abstract double getValue(GridLoc3D pt);

    public abstract SliceRenderer getSliceRenderer(IImageSpace refspace, GridLoc3D slice, Anatomy3D displayAnatomy);

    
}



