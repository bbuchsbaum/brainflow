package brainflow.core.layer;

import brainflow.image.anatomy.GridLoc3D;
import brainflow.image.data.*;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.interpolation.NearestNeighborInterpolator;
import brainflow.colormap.BinaryColorMap;
import brainflow.display.InterpolationType;
import brainflow.core.SliceRenderer;
import brainflow.core.rendering.DefaultImageSliceRenderer;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 18, 2008
 * Time: 11:08:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskLayer3D extends ImageLayer3D {


    public MaskLayer3D(IMaskedData3D data) {
        super(data);
        init();

    }

    public MaskLayer3D(IMaskedData3D data, LayerProps _params) {
        super(data, _params);
        init();
    }

    private void init() {
        getLayerProps().colorMap.set(new BinaryColorMap(Color.WHITE));
        getLayerProps().interpolationType.set(InterpolationType.NEAREST_NEIGHBOR);

    }

    @Override
    public double getValue(GridLoc3D pt) {
        IImageSpace space = getCoordinateSpace();
        assert space.getAnatomy() == pt.getAnatomy();
        float x = (float) pt.getValue(space.getAnatomicalAxis(Axis.X_AXIS), false).getValue();
        float y = (float) pt.getValue(space.getAnatomicalAxis(Axis.Y_AXIS), false).getValue();
        float z = (float) pt.getValue(space.getAnatomicalAxis(Axis.Z_AXIS), false).getValue();

        return getData().value(x, y, z, new NearestNeighborInterpolator());
    }


    @Override
    public IMaskedData3D getData() {
        return (IMaskedData3D) super.getData();
    }

   
    protected SliceRenderer createSliceRenderer(IImageSpace3D refspace, GridLoc3D slice, Anatomy3D displayAnatomy) {
        return new DefaultImageSliceRenderer(refspace, this, slice, displayAnatomy) {
            @Override
            protected RGBAImage thresholdRGBA(RGBAImage rgba) {
                return rgba;
            }
        };

    }

    @Override
    public SliceRenderer getSliceRenderer(IImageSpace refspace, GridLoc3D slice, Anatomy3D displayAnatomy) {
        return createSliceRenderer((IImageSpace3D)refspace, slice, displayAnatomy);   
    }


}
