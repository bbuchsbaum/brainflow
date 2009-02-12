package brainflow.core.annotations;

import brainflow.core.IImagePlot;
import brainflow.image.anatomy.AnatomicalPoint1D;
import brainflow.image.anatomy.AnatomicalPoint3D;
import net.java.dev.properties.Property;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 15, 2006
 * Time: 2:05:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairAnnotation extends AbstractAnnotation {

    public static final String ID = "crosshair";

    public static final String LINE_PAINT_PROPERTY = "linePaint";
    public static final String LINE_LENGTH_PROPERTY = "lineLength";
    public static final String LINE_WIDTH_PROPERTY = "lineWidth";
    public static final String GAP_PROPERTY = "gap";


    public static final Paint DEFAULT_LINE_PAINT = Color.GREEN;
    public static final Float DEFAULT_LINE_LENGTH = 1.0f;
    public static final Float DEFAULT_LINE_WIDTH = 1.8f;
    public static final Integer DEFAULT_GAP = 0;

    private Paint linePaint = DEFAULT_LINE_PAINT;
    private double lineLength = DEFAULT_LINE_LENGTH;
    private double lineWidth = DEFAULT_LINE_WIDTH;

    private Integer gap = DEFAULT_GAP;

    private Stroke stroke;

    private Point location;


    private Property<AnatomicalPoint3D> crosshair;


    public CrosshairAnnotation(Property<AnatomicalPoint3D> _crosshair) {
        crosshair = _crosshair;
        linePaint = DEFAULT_LINE_PAINT;
        lineLength = DEFAULT_LINE_LENGTH.doubleValue();

        resetStroke();

    }

    private void resetStroke() {
        stroke = new BasicStroke((float) lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    }

    public String getIdentifier() {
        return ID;
    }

    /*public Point getLocation() {
        return location;
    }*/

    public IAnnotation safeCopy() {
        CrosshairAnnotation annot = new CrosshairAnnotation(crosshair);
        annot.setVisible(super.isVisible());
        annot.location = new Point(location);
        annot.linePaint = linePaint;
        annot.stroke = stroke;
        annot.lineLength = lineLength;
        annot.lineWidth = lineWidth;
        annot.gap = gap;

        return annot;

    }

    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (!isVisible()) return;


        // todo is it fixed? -- (old note: potential bug because may not match plot axes????)
        AnatomicalPoint1D xpt = crosshair.get().getValue(plot.getXAxisRange().getAnatomicalAxis(), plot.getXAxisRange().getBeginning().getValue(), plot.getXAxisRange().getEnd().getValue());
        AnatomicalPoint1D ypt = crosshair.get().getValue(plot.getYAxisRange().getAnatomicalAxis(), plot.getYAxisRange().getBeginning().getValue(), plot.getYAxisRange().getEnd().getValue());


        double percentX = (xpt.getValue() - plot.getXAxisRange().getBeginning().getValue()) / plot.getXAxisRange().getInterval();
        double percentY = (ypt.getValue() - plot.getYAxisRange().getBeginning().getValue()) / plot.getYAxisRange().getInterval();

        double screenX = (percentX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (percentY * plotArea.getHeight()) + plotArea.getY();

        location = new Point((int) Math.round(screenX), (int) Math.round(screenY));

        //double width = Math.max(lineLength * plotArea.getWidth(), lineLength * plotArea.getHeight());
        g2d.setPaint(linePaint);
        g2d.setStroke(stroke);

        double span = Math.max(plotArea.getWidth(), plotArea.getHeight());
        double halfSpan = (span / 2.0) * lineLength;


        if (gap == 0) {
            Line2D lineX = new Line2D.Double(screenX - halfSpan, screenY, screenX + halfSpan, screenY);
            Line2D lineY = new Line2D.Double(screenX, screenY - halfSpan, screenX, screenY + halfSpan);
            g2d.draw(lineX);
            g2d.draw(lineY);
        } else {
            Line2D lineXLeft = new Line2D.Double(plotArea.getMinX(), screenY, screenX - gap, screenY);
            Line2D lineXRight = new Line2D.Double(screenX + gap, screenY, plotArea.getMaxX(), screenY);
            Line2D lineYTop = new Line2D.Double(screenX, plotArea.getMinY(), screenX, screenY - gap);
            Line2D lineYBottom = new Line2D.Double(screenX, screenY + gap, screenX, plotArea.getMaxY());

            g2d.draw(lineXLeft);
            g2d.draw(lineXRight);
            g2d.draw(lineYTop);
            g2d.draw(lineYBottom);

        }

    }


    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        Paint oldPaint = this.linePaint;
        this.linePaint = linePaint;

        support.firePropertyChange(CrosshairAnnotation.LINE_PAINT_PROPERTY, oldPaint, getLinePaint());
    }

    public double getLineLength() {
        return lineLength;
    }

    public void setLineLength(double lineLength) {
        double old = getLineLength();
        this.lineLength = lineLength;

        support.firePropertyChange(CrosshairAnnotation.LINE_LENGTH_PROPERTY, old, getLineLength());
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        double old = getGap();
        this.gap = gap;
        support.firePropertyChange(CrosshairAnnotation.GAP_PROPERTY, old, getGap());
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }


    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth) {
        double old = this.lineWidth;
        this.lineWidth = lineWidth;

        resetStroke();
        support.firePropertyChange(CrosshairAnnotation.LINE_WIDTH_PROPERTY, old, getLineWidth());
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
