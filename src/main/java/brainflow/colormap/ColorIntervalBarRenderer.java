package brainflow.colormap;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 7, 2006
 * Time: 7:12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorIntervalBarRenderer extends XYBarRenderer {

    private boolean doNothing = false;


    public boolean isDoNothing() {
        return doNothing;
    }

    public void setDoNothing(boolean doNothing) {
        this.doNothing = doNothing;
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis,
                         XYDataset dataset, int series, int item,
                         CrosshairState crosshairState, int pass) {

        if (doNothing) return;

        ColorMapDataset intervalDataset = (ColorMapDataset) dataset;
        ColorInterval interval = intervalDataset.getColorInterval(series, item);

        double value0;
        double value1;
        if (getUseYInterval()) {
            value0 = intervalDataset.getStartYValue(series, item);
            value1 = intervalDataset.getEndYValue(series, item);
        } else {
            value0 = getBase();
            value1 = intervalDataset.getYValue(series, item);
        }
        if (Double.isNaN(value0) || Double.isNaN(value1)) {
            return;
        }

        double translatedValue0 = rangeAxis.valueToJava2D(
                value0, dataArea, plot.getRangeAxisEdge()
        );
        double translatedValue1 = rangeAxis.valueToJava2D(
                value1, dataArea, plot.getRangeAxisEdge()
        );


        RectangleEdge location = plot.getDomainAxisEdge();


        Number startXNumber = intervalDataset.getStartX(series, item);
        if (startXNumber == null) {
            return;
        }
        double translatedStartX = domainAxis.valueToJava2D(
                startXNumber.doubleValue(), dataArea, location
        );

        Number endXNumber = intervalDataset.getEndX(series, item);
        if (endXNumber == null) {
            return;
        }
        double translatedEndX = domainAxis.valueToJava2D(
                endXNumber.doubleValue(), dataArea, location
        );


        double translatedWidth = Math.max(
                1, Math.abs(translatedEndX - translatedStartX)
        );
        double translatedHeight = Math.abs(translatedValue1 - translatedValue0);


        Rectangle2D bar = null;
        PlotOrientation orientation = plot.getOrientation();
        if (orientation == PlotOrientation.HORIZONTAL) {
            bar = new Rectangle2D.Double(
                    Math.min(translatedValue0, translatedValue1),
                    Math.min(translatedStartX, translatedEndX),
                    translatedHeight, translatedWidth);
        } else if (orientation == PlotOrientation.VERTICAL) {
            bar = new Rectangle2D.Double(
                    Math.min(translatedStartX, translatedEndX),
                    Math.min(translatedValue0, translatedValue1),
                    translatedWidth, translatedHeight);
        }

        Color clr = intervalDataset.getColor(startXNumber.doubleValue());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) interval.getAlpha() / 255f));
        g2.setPaint(clr);
        //g2.fill(bar);
        g2.draw(bar);


    }


}
