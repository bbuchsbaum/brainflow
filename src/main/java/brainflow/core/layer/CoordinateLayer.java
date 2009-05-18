package brainflow.core.layer;

import brainflow.image.anatomy.BrainPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.GridPoint3D;
import brainflow.image.space.ICoordinateSpace;
import brainflow.image.space.IImageSpace;
import brainflow.image.data.CoordinateSet3D;
import brainflow.core.rendering.BasicCoordinateSliceRenderer;
import brainflow.core.layer.AbstractLayer;
import brainflow.core.layer.ImageLayerProperties;
import brainflow.core.SliceRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 17, 2007
 * Time: 12:44:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateLayer extends AbstractLayer {

    private CoordinateSet3D coordinates;

    public CoordinateLayer(ImageLayerProperties properties, CoordinateSet3D coords) {
        super("coords", properties);
        coordinates = coords;
    }

    public double getValue(GridPoint3D pt) {
        return 0;
    }

    public CoordinateSet3D getDataSource() {
        return coordinates;
    }

    public SliceRenderer getSliceRenderer(IImageSpace refspace, GridPoint3D slice, Anatomy3D displayAnatomy) {
       return new BasicCoordinateSliceRenderer(this, slice, displayAnatomy);
    }

    public IMaskProperty getMaskProperty() {
        //todo implement please
        throw new UnsupportedOperationException();
       
    }

    public ICoordinateSpace getCoordinateSpace() {
        return coordinates.getSpace();
    }

    public double getMinValue() {
        return coordinates.getMinValue();
    }

    public double getMaxValue() {
        return coordinates.getMaxValue();
    }

    public String getLabel() {
        return "coordinates";
    }


}
