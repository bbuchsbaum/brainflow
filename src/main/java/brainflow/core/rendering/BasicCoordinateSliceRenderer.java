package brainflow.core.rendering;

import brainflow.colormap.IColorMap;
import brainflow.image.anatomy.GridLoc3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.axis.AxisRange;
import brainflow.image.axis.CoordinateAxis;
import brainflow.image.data.CoordinateSet3D;
import brainflow.image.space.Axis;
import brainflow.image.space.CoordinateSpace2D;
import brainflow.image.space.ICoordinateSpace;
import brainflow.core.SliceRenderer;
import brainflow.core.layer.CoordinateLayer;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 10:11:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicCoordinateSliceRenderer implements SliceRenderer {


    private GridLoc3D slice;

    private CoordinateLayer layer;

    private Anatomy3D displayAnatomy = Anatomy3D.getCanonicalAxial();

    private BufferedImage image;

    private ICoordinateSpace space;

    public BasicCoordinateSliceRenderer(CoordinateLayer layer, GridLoc3D slice, Anatomy3D displayAnatomy) {
        this.slice = slice;
        this.layer = layer;
        this.displayAnatomy = displayAnatomy;

    }

    public ICoordinateSpace getImageSpace() {
        if (space == null) {

            CoordinateAxis axis1 = layer.getCoordinateSpace().getImageAxis(displayAnatomy.XAXIS, true);
            CoordinateAxis axis2 = layer.getCoordinateSpace().getImageAxis(displayAnatomy.YAXIS, true);

            AxisRange a1 = new AxisRange(displayAnatomy.XAXIS, axis1.getRange().getMinimum(), axis1.getRange().getMaximum());
            AxisRange a2 = new AxisRange(displayAnatomy.YAXIS, axis2.getRange().getMinimum(), axis2.getRange().getMaximum());


            space = new CoordinateSpace2D(new CoordinateAxis(a1), new CoordinateAxis(a2));
        }


        return space;


    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    

    public void setSlice(GridLoc3D slice) {
        if (!getSlice().equals(slice)) {
            this.slice = slice;
            flush();
        }
    }

    public GridLoc3D getSlice() {
        return slice;
    }

    public BufferedImage render() {
        //CoordinateSet3D set = layer.getDataSource();
        //AnatomicalAxis zaxis = getSlice().getAnatomy().ZAXIS;
        //List<BrainPoint3D> pts = set.pointsWithinPlane(getSlice().evaluate(zaxis));



        throw new UnsupportedOperationException();
    }

    public void renderUnto(Rectangle2D frame, Graphics2D g2) {
        CoordinateSet3D set = layer.getDataSource();
        AnatomicalAxis zaxis = getSlice().getAnatomy().ZAXIS;
        List<Integer> indices = set.indicesWithinPlane(getSlice().getValue(zaxis, false));

        if (indices.size() == 0) {
            return;
        }


        double minx = getImageSpace().getImageAxis(Axis.X_AXIS).getRange().getMinimum();
        double miny = getImageSpace().getImageAxis(Axis.Y_AXIS).getRange().getMinimum();


        double transx = (minx - frame.getMinX()); //+ (-frameBounds.getMinX());
        double transy = (miny - frame.getMinY()); //+ (-frameBounds.getMinY());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) getLayer().getLayerProps().opacity.get().doubleValue());
        g2.setComposite(composite);




        IColorMap map = getLayer().getLayerProps().colorMap.get();
        for (int i : indices) {
            SpatialLoc3D pt = set.getAnatomicalPoint(i);
            double value = set.getValue(i);

            double radius = set.getRadius(i);

            double x = pt.getValue(displayAnatomy.XAXIS).getValue();
            double y = pt.getValue(displayAnatomy.YAXIS).getValue();

    
            Color c = map.getColor(value);
            g2.setColor(c);

            Shape shape = new Ellipse2D.Float((int) ((x - minx) - (radius / 2.0)), (int) ((y - miny) - (radius / 2.0)), (float) radius, (float) radius);
            g2.fill(shape);
        }

    }

    public void flush() {
        image = null;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    public CoordinateLayer getLayer() {
        return layer;
    }
}
