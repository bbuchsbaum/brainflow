package brainflow.core.layer;

import brainflow.image.data.*;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.interpolation.NearestNeighborInterpolator;
import brainflow.colormap.BinaryColorMap;
import brainflow.display.InterpolationType;
import brainflow.core.SliceRenderer;
import brainflow.core.rendering.BasicImageSliceRenderer;

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

    public MaskLayer3D(IMaskedData3D data, ImageLayerProperties _params) {
        super(data, _params);
        init();
    }

    private void init() {
        getImageLayerProperties().colorMap.set(new BinaryColorMap(Color.WHITE));
        getImageLayerProperties().interpolationType.set(InterpolationType.NEAREST_NEIGHBOR);

    }

    @Override
    public double getValue(AnatomicalPoint3D pt) {
        IImageSpace space = getCoordinateSpace();
        float x = (float) pt.getValue(space.getAnatomicalAxis(Axis.X_AXIS)).getValue();
        float y = (float) pt.getValue(space.getAnatomicalAxis(Axis.Y_AXIS)).getValue();
        float z = (float) pt.getValue(space.getAnatomicalAxis(Axis.Z_AXIS)).getValue();

        return getData().value(x, y, z, new NearestNeighborInterpolator());
    }


    @Override
    public IMaskedData3D getData() {
        return (IMaskedData3D) super.getData();
    }

   
    protected SliceRenderer createSliceRenderer(IImageSpace3D refspace, AnatomicalPoint3D slice, Anatomy3D displayAnatomy) {
        return new BasicImageSliceRenderer(refspace, this, slice, displayAnatomy) {
            @Override
            protected RGBAImage thresholdRGBA(RGBAImage rgba) {
                return rgba;
            }
        };

    }

    @Override
    public SliceRenderer getSliceRenderer(IImageSpace refspace, AnatomicalPoint3D slice, Anatomy3D displayAnatomy) {
        return createSliceRenderer((IImageSpace3D)refspace, slice, displayAnatomy);   
    }
}
