package brainflow.core.annotations;

import brainflow.core.IImagePlot;
import brainflow.image.anatomy.BrainPoint2D;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.container.BeanContainer;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 22, 2007
 * Time: 3:02:29 PM
 */
public class BoxAnnotation extends AbstractAnnotation {

    public static final String ID = "box";

    public static final String XMIN_PROPERTY = "xmin";
    public static final String YMIN_PROPERTY = "ymin";
    public static final String WIDTH_PROPERTY = "width";
    public static final String HEIGHT_PROPERTY = "height";

    public static final String FILLPAINT_PROPERTY = "fillPaint";
    public static final String LINEPAINT_PROPERTY = "linePaint";

    public final Property<Double> xmin = ObservableProperty.create(0.0);

    public final Property<Double> ymin = ObservableProperty.create(0.0);

    public final Property<Double> width = ObservableProperty.create(0.0);

    public final Property<Double> height = ObservableProperty.create(0.0);

    public final Property<Paint> fillPaint = ObservableProperty.create((Paint)new Color(0, 255, 0, 87));

    public final Property<Paint> linePaint = ObservableProperty.create((Paint)Color.GREEN.brighter());




    public BoxAnnotation() {
        BeanContainer.bind(this);
    }

    public BoxAnnotation(Rectangle2D rect, Paint _fillPaint, Paint _linePaint) {
        BeanContainer.bind(this);
        xmin.set(rect.getMinX());
        ymin.set(rect.getMinY());
        fillPaint.set(_fillPaint);
        linePaint.set(_linePaint);
    }

    public IAnnotation safeCopy() {
        return new BoxAnnotation(new Rectangle2D.Double(xmin.get(), ymin.get(), width.get(), height.get()), fillPaint.get(), linePaint.get());
    }


    public boolean containsPoint(IImagePlot plot, Point plotPoint) {
        BrainPoint2D pt = plot.translateScreenToAnat(plotPoint);

        if (pt.getX().getValue() < xmin.get()) return false;
        if (pt.getY().getValue() < ymin.get()) return false;
        if (pt.getX().getValue() > (xmin.get() + width.get()) ) return false;
        if (pt.getY().getValue() > (ymin.get() + height.get()) ) return false;

        return true;

    }

    public BrainPoint2D translateFromJava2D(IImagePlot plot, Point plotPoint) {
        return plot.translateScreenToAnat(plotPoint);

    }

    public double getNormalizedX(IImagePlot plot, double val) {
        return (val - plot.getXAxisRange().getBeginning().getValue()) / plot.getXAxisRange().getInterval();

    }

    public double getNormalizedY(IImagePlot plot, double val) {
        return (val - plot.getYAxisRange().getBeginning().getValue()) / plot.getYAxisRange().getInterval();
    }

    public double getNormalizedWidth(IImagePlot plot, double val) {
        return val / plot.getXAxisRange().getInterval();
    }

    public double getNormalizedHeight(IImagePlot plot, double val) {
        return val / plot.getYAxisRange().getInterval();
    }


    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (!isVisible()) return;


        double nX = getNormalizedX(plot, getXmin());
        double nY = getNormalizedY(plot, getYmin());
        double nW = getNormalizedWidth(plot, getWidth());
        double nH = getNormalizedHeight(plot, getHeight());


        double screenX = (nX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (nY * plotArea.getHeight()) + plotArea.getY();
        double screenWidth = nW * plotArea.getWidth();
        double screenHeight = nH * plotArea.getHeight();


        Paint oldPaint = g2d.getPaint();
        g2d.setPaint(linePaint.get());
        g2d.drawRect((int) screenX, (int) screenY, (int) screenWidth, (int) screenHeight);
        g2d.setPaint(fillPaint.get());
        g2d.fillRect((int) (screenX + 1), (int) (screenY + 1), (int) (screenWidth - 1), (int) (screenHeight - 1));

        g2d.setPaint(oldPaint);



    }

    public String getIdentifier() {
        return ID;
    }


    public double getXmin() {
        return xmin.get();
    }

    public void setXmin(double _xmin) {
       xmin.set(_xmin);
    }

    public double getYmin() {
        return ymin.get();
    }

    public void setYmin(double _ymin) {
        ymin.set(_ymin);
    }

    public double getWidth() {
        return width.get();
    }

    public void setWidth(double _width) {
        width.set(_width);
    }

    public double getHeight() {
       return height.get();
    }

    public void setHeight(double _height) {
        height.set(_height);

    }


    public Paint getFillPaint() {
        return fillPaint.get();
    }

    public void setFillPaint(Paint _fillPaint) {
        fillPaint.set(_fillPaint);

    }

    public Paint getLinePaint() {
        return linePaint.get();
    }

    public void setLinePaint(Paint _linePaint) {
        linePaint.set(_linePaint);

    }
}
