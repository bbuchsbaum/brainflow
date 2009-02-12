package brainflow.chart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Paint;
import java.awt.Stroke;

import java.awt.geom.*;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.PlotRenderingInfo;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class CircleRenderer extends org.jfree.chart.renderer.xy.StandardXYItemRenderer {
    
  private Line2D line = new Line2D.Double(0.0,0.0,0.0,0.0);

  public CircleRenderer() {
    super(SHAPES);
  }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, org.jfree.data.xy.XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {


        Paint seriesPaint = getSeriesPaint(series);
        Stroke seriesStroke = getSeriesStroke(series);
        g2.setPaint(seriesPaint);
        g2.setStroke(seriesStroke);

        // get the data point...
        Number x1 = dataset.getXValue(series, item);
        Number y1 = dataset.getYValue(series, item);
        if (y1!=null) {
            double transX1 = domainAxis.valueToJava2D(x1.doubleValue(), dataArea, plot.getDomainAxisEdge());
            double transY1 = rangeAxis.valueToJava2D(y1.doubleValue(), dataArea, plot.getRangeAxisEdge());
            
            
            
            if (getBaseShapesVisible() ){
                //double shapeScale = this.getDefaultShapeScale();
                double shapeScale = 5;
                Shape shape = new Ellipse2D.Double(transX1-0.5*shapeScale, transY1-0.5*shapeScale, shapeScale*1.5, shapeScale*1.5);
                g2.setPaint(java.awt.Color.white);
                g2.setPaint(seriesPaint);
                //g2.fill(shape);
                g2.setPaint(seriesPaint);
                g2.draw(shape);
                

            }

            if (getPlotLines()) {

                if (item>0) {
                    // get the previous data point...
                    Number x0 = dataset.getXValue(series, item-1);
                    Number y0 = dataset.getYValue(series, item-1);
                    if (y0!=null) {
                        double transX0 = domainAxis.valueToJava2D(x0.doubleValue(), dataArea, plot.getDomainAxisEdge());
                        double transY0 = domainAxis.valueToJava2D(y0.doubleValue(), dataArea, plot.getRangeAxisEdge());
                        line.setLine(transX0, transY0, transX1, transY1);
                        g2.draw(line);
                    }
                }
            }


             
        }
        

    }

}