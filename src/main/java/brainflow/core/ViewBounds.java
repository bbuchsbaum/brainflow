package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.AxisRange;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Feb 5, 2009
 * Time: 12:19:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class ViewBounds {


    private Anatomy3D displayAnatomy;

    private AxisRange xrange;

    private AxisRange yrange;

    public ViewBounds(Anatomy3D displayAnatomy, AxisRange xrange, AxisRange yrange) {
        this.displayAnatomy = displayAnatomy;
        this.xrange = xrange;
        this.yrange = yrange;
    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    public AxisRange getXrange() {
        return xrange;
    }

    public AxisRange getYrange() {
        return yrange;
    }

    public double getXExtent() {
        return xrange.getInterval();
    }

    public double getYExtent() {
        return yrange.getInterval();
    }


    public ViewBounds newXRange(double start, double end) {
        if (start > end) throw new IllegalArgumentException("start value cannot exceed end value");
        return new ViewBounds(displayAnatomy, new AxisRange(xrange.getAnatomicalAxis(), start, end),
                yrange);

    }

    public ViewBounds newYRange(double start, double end) {
        if (start > end) throw new IllegalArgumentException("start value cannot exceed end value");
        return new ViewBounds(displayAnatomy, xrange, new AxisRange(yrange.getAnatomicalAxis(), start, end));

    }
}
