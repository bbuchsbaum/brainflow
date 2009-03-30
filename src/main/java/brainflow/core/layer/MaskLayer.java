package brainflow.core.layer;

import brainflow.app.MemoryImageDataSource;
import brainflow.colormap.BinaryColorMap;
import brainflow.colormap.DiscreteColorMap;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayerProperties;
import brainflow.core.layer.IMaskItem;
import brainflow.core.SliceRenderer;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.data.IImageData3D;
import brainflow.image.data.MaskedData3D;
import brainflow.image.interpolation.NearestNeighborInterpolator;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 9, 2007
 * Time: 10:16:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskLayer extends ImageLayer {


    private IMaskItem item;


    public MaskLayer(IMaskItem item, ImageLayerProperties properties) {
        super(new MemoryImageDataSource(new MaskedData3D((IImageData3D) item.getSource().getData(), item.getPredicate())), properties);
        this.item = item;

        java.util.List<Color> clrs = new ArrayList<Color>();
        clrs.add(Color.BLACK);
        clrs.add(Color.WHITE);


        java.util.List<Double> bounds = new ArrayList<Double>();
        bounds.add(0.0);
        bounds.add(.5);
        bounds.add(1.0);

        DiscreteColorMap dmap = new DiscreteColorMap(clrs, bounds);

        //properties = new ImageLayerProperties(new Range(0,1), item.getPredicate());
        properties.colorMap.set(dmap);


    }

    public MaskLayer(IMaskItem item, ImageLayerProperties properties, Color maskColor) {
        super(new MemoryImageDataSource(new MaskedData3D((IImageData3D) item.getSource().getData(), item.getPredicate())), properties);
        this.item = item;

        BinaryColorMap bmap = new BinaryColorMap(maskColor);

        //properties = new ImageLayerProperties(new Range(0,1), item.getPredicate());
        properties.colorMap.set(bmap);


    }

    public IMaskProperty getMaskProperty() {
        //todo implement please
        throw new UnsupportedOperationException();
    }

    public double getValue(AnatomicalPoint3D pt) {
        IImageSpace space = getCoordinateSpace();
        float x = (float) pt.getValue(space.getAnatomicalAxis(Axis.X_AXIS)).getValue();
        float y = (float) pt.getValue(space.getAnatomicalAxis(Axis.Y_AXIS)).getValue();
        float z = (float) pt.getValue(space.getAnatomicalAxis(Axis.Z_AXIS)).getValue();

        return getData().value(x, y, z, new NearestNeighborInterpolator());
    }

    public SliceRenderer getSliceRenderer(IImageSpace refspace, AnatomicalPoint3D slice, Anatomy3D displayAnatomy) {
        //todo fix me
        /*return new BasicImageSliceRenderer((ImageSpace3D) refspace, this, slice, displayAnatomy) {
            protected RGBAImage thresholdRGBA(RGBAImage rgba) {
                return rgba;
            }
        };    */

        return null;

    }


    @Override
    public MaskedData3D getData() {
        return (MaskedData3D) getDataSource().getData();
    }

    public IImageSpace getCoordinateSpace() {
        return getData().getImageSpace();
    }

    public double getMinValue() {
        return getData().minValue();
    }

    public double getMaxValue() {
        return getData().maxValue();
    }

    public String getLabel() {
        return getData().getImageLabel();
    }
}
