package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.AxisRange;
import brainflow.image.axis.ImageAxis;
import brainflow.image.space.IImageSpace3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Feb 5, 2009
 * Time: 12:19:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class ViewBounds {

    private IImageSpace3D referenceSpace;

    private Anatomy3D displayAnatomy;

    private AxisRange xrange;

    private AxisRange yrange;

    public ViewBounds(IImageSpace3D referenceSpace, Anatomy3D displayAnatomy, AxisRange xrange, AxisRange yrange) {
        if (xrange.getAnatomicalAxis() != displayAnatomy.XAXIS || yrange.getAnatomicalAxis() != displayAnatomy.YAXIS) {
            throw new IllegalArgumentException("AxisRange axes must match x and y axes of displayAnatomy");
        }
        
        this.displayAnatomy = displayAnatomy;
        this.xrange = xrange;
        this.yrange = yrange;
        this.referenceSpace = referenceSpace;
    }

    public IImageSpace3D getReferenceSpace() {
        return referenceSpace;
    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    public AxisRange getXRange() {
        return xrange;
    }

    public AxisRange getYRange() {
        return yrange;
    }

    public double getXExtent() {
        return xrange.getInterval();
    }

    public double getYExtent() {
        return yrange.getInterval();
    }

    public double getXProportion() {
        return xrange.getInterval()/referenceSpace.getExtent(referenceSpace.getImageAxis(xrange.getAnatomicalAxis(), true).getAnatomicalAxis());
    }

    public double getYProportion() {
        return yrange.getInterval()/referenceSpace.getExtent(referenceSpace.getImageAxis(yrange.getAnatomicalAxis(), true).getAnatomicalAxis());
    }

    public ViewBounds snapToGrid() {

        double xcenter = xrange.getCenter().getValue();
        double ycenter = yrange.getCenter().getValue();

        ImageAxis xaxis = referenceSpace.getImageAxis(xrange.getAnatomicalAxis(), true).matchAxis(xrange.getAnatomicalAxis());
        int x0 = xaxis.nearestSample(xrange.getBeginning());
        int x1 = xaxis.nearestSample(xcenter + (xcenter-xaxis.valueOf(x0).getValue()));

        ImageAxis yaxis = referenceSpace.getImageAxis(yrange.getAnatomicalAxis(), true).matchAxis(yrange.getAnatomicalAxis());
        int y0 = yaxis.nearestSample(yrange.getBeginning());
        int y1 = yaxis.nearestSample(ycenter + (ycenter-yaxis.valueOf(y0).getValue()));

        AxisRange newxrange = new AxisRange(xrange.getAnatomicalAxis(), xaxis.valueOf(x0).getValue(), xaxis.valueOf(x1).getValue());
        AxisRange newyrange = new AxisRange(yrange.getAnatomicalAxis(), yaxis.valueOf(y0).getValue(), yaxis.valueOf(y1).getValue());



        return new ViewBounds(referenceSpace, displayAnatomy, newxrange,
                newyrange);



    }


    public ViewBounds newXRange(double start, double end) {
        if (start > end) throw new IllegalArgumentException("start cannot be greater than end");
        return new ViewBounds(referenceSpace, displayAnatomy, new AxisRange(xrange.getAnatomicalAxis(), start, end),
                yrange);

    }

    public ViewBounds newYRange(double start, double end) {
        if (start > end) throw new IllegalArgumentException("start cannot be greater than end");
        return new ViewBounds(referenceSpace, displayAnatomy, xrange, new AxisRange(yrange.getAnatomicalAxis(), start, end));

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewBounds that = (ViewBounds) o;

        if (!displayAnatomy.equals(that.displayAnatomy)) return false;
        if (!referenceSpace.equals(that.referenceSpace)) return false;
        if (!xrange.equals(that.xrange)) return false;
        if (!yrange.equals(that.yrange)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = referenceSpace.hashCode();
        result = 31 * result + displayAnatomy.hashCode();
        result = 31 * result + xrange.hashCode();
        result = 31 * result + yrange.hashCode();
        return result;
    }
}
