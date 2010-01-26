/*
 * ColorBandChart.java
 *
 * Created on July 20, 2006, 12:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.app.chart;

import brainflow.colormap.ColorTable;
import brainflow.colormap.IColorMap;
import brainflow.colormap.LinearColorMap;
import brainflow.math.ArrayUtils;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;


/**
 * @author buchs
 */
public class ColorBandChart implements MouseMotionListener, MouseListener {

    public enum ColorBand {
        RED,
        GREEN,
        BLUE,
        ALPHA
    }

    public enum CHART_TYPE {
        CONNECT_POINTS,
        CONTROL_POINTS
    }


    public static final int BAND_SAMPLES = 256;

    public static final int DEFAULT_INTERCEPT = 127;

    private EventListenerList listeners = new EventListenerList();


    private JFreeChart chart;

    private ChartPanel chartPanel;

    private XYPlot plot;

    private DynamicXYDataset controlPoints;

    private DynamicSplineXYDataset fittedLine;

    private IColorMap colorMap;

    private ColorBand colorBand = ColorBandChart.ColorBand.RED;

    private boolean dragging = false;

    private int lockedOnItem = -1;


    public ColorBandChart(ColorBand cband, IColorMap _colorMap) {
        if (_colorMap.getMapSize() < 3) {
            throw new IllegalArgumentException("ColorBandChart requires at least 3 values");
        }

        colorMap = _colorMap;
        colorBand = cband;

        double[][] values = getControlPoints(colorMap);

        controlPoints = new DynamicXYDataset();

        controlPoints.addXYSeries(values[0], values[1]);


        fittedLine = new DynamicSplineXYDataset(controlPoints, BAND_SAMPLES);

        initPlot();
        chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        initPanel();

    }

    private double[][] getControlPoints(IColorMap colorMap) {
        int ncontrol = 4;
        if (colorMap.getMapSize() < 4) {
            ncontrol = 3;
        }

        double range = colorMap.getMaximumValue() - colorMap.getMinimumValue();
        double increment = range / (ncontrol - 1);
        double[][] controlPoints = new double[2][ncontrol];

        double cur = colorMap.getMinimumValue();
        for (int i = 0; i < ncontrol; i++) {
            controlPoints[0][i] = cur;
            if (colorBand == ColorBand.RED) {
                controlPoints[1][i] = colorMap.getColor(cur).getRed();
            } else if (colorBand == ColorBand.GREEN) {
                controlPoints[1][i] = colorMap.getColor(cur).getGreen();
            } else if (colorBand == ColorBand.BLUE) {
                controlPoints[1][i] = colorMap.getColor(cur).getBlue();
            } else if (colorBand == ColorBand.ALPHA) {
                controlPoints[1][i] = colorMap.getColor(cur).getAlpha();
            }
            cur = cur + increment;
        }

        return controlPoints;

    }

    private byte[] getBandValues(ColorBand band, IColorMap colorMap) {
        byte[] bandVals = new byte[colorMap.getMapSize()];

        switch (band) {
            case RED:
                for (int i = 0; i < bandVals.length; i++) {
                    bandVals[i] = (byte) colorMap.getInterval(i).getRed();
                }
                break;
            case GREEN:
                for (int i = 0; i < bandVals.length; i++) {
                    bandVals[i] = (byte) colorMap.getInterval(i).getGreen();
                }
                break;
            case BLUE:
                for (int i = 0; i < bandVals.length; i++) {
                    bandVals[i] = (byte) colorMap.getInterval(i).getBlue();
                }
                break;
            case ALPHA:
                for (int i = 0; i < bandVals.length; i++) {
                    bandVals[i] = (byte) colorMap.getInterval(i).getAlpha();
                }
                break;
            default:
                assert false : "impossible";

        }

        return bandVals;

    }

    /**
     * Method initPanel initialiazes the ChartPanel which contains the plot.
     */
    private void initPanel() {
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.BLACK);

        chartPanel.addMouseListener(this);
        chartPanel.addMouseMotionListener(this);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setMinimumDrawWidth(50);
        chartPanel.setMinimumDrawHeight(50);
        chartPanel.setMaximumDrawWidth(800);
        chartPanel.setMaximumDrawHeight(800);

    }


    /**
     * Method addChangeListener
     *
     * @param listener
     */

    public void addChangeListener(ChangeListener listener) {
        listeners.add(ChangeListener.class, listener);
    }


    /**
     * Method removeChangeListener
     *
     * @param listener
     */

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(ChangeListener.class, listener);
    }

    /**
     * Method fireChangeEvent
     *
     * @param e
     */

    protected void fireChangeEvent(ChangeEvent e) {
        ChangeListener[] cl = listeners.getListeners(ChangeListener.class);
        for (ChangeListener c : cl) {
            c.stateChanged(e);
        }
    }


    public byte[] getBandData() {
        double[] splineVals = fittedLine.getSpline(0, DynamicXYDataset.RANGE).elements();

        byte[] vals = ArrayUtils.castToUnsignedBytes(splineVals);

        return vals;
    }

    private void initPlot() {
        ValueAxis hAxis = new NumberAxis("");
        ValueAxis vAxis = new NumberAxis("");


        DefaultXYItemRenderer controlRenderer = new ControlPointRenderer();
        controlRenderer.setSeriesStroke(0, new BasicStroke(2));


        StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
        renderer.setDrawSeriesLineAsPath(true);
        //renderer.setShapesVisible(false);
        renderer.setSeriesStroke(0, new BasicStroke(2f));

        plot = new XYPlot(fittedLine, hAxis, vAxis, renderer);
        plot.setDataset(0, fittedLine);

        plot.setDomainAxis(0, hAxis);
        plot.setRangeAxis(0, vAxis);
        plot.setRenderer(0, renderer);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDataset(1, controlPoints);
        plot.setRenderer(1, controlRenderer);


        hAxis.setAutoRange(false);
        vAxis.setAutoRange(false);
        hAxis.setLowerBound(colorMap.getMinimumValue() - .05 * (colorMap.getMaximumValue() - colorMap.getMinimumValue()));
        hAxis.setUpperBound(colorMap.getMaximumValue() + .05 * (colorMap.getMaximumValue() - colorMap.getMinimumValue()));
        vAxis.setLowerBound(-25);
        vAxis.setUpperBound(280);


        switch (colorBand) {
            case ALPHA:
                renderer.setSeriesPaint(0, Color.WHITE);
                controlRenderer.setSeriesPaint(0, Color.WHITE);
                break;
            case RED:
                renderer.setSeriesPaint(0, Color.RED);
                controlRenderer.setSeriesPaint(0, Color.RED);
                break;
            case GREEN:
                renderer.setSeriesPaint(0, Color.GREEN);
                controlRenderer.setSeriesPaint(0, Color.GREEN);
                break;
            case BLUE:
                renderer.setSeriesPaint(0, Color.BLUE);
                controlRenderer.setSeriesPaint(0, Color.BLUE);
                break;

            default:

        }

        plot.setDomainAxis(hAxis);
        plot.setRangeAxis(vAxis);
        plot.setBackgroundPaint(Color.BLACK);

        plot.getDomainAxis().setVisible(true);
        plot.getRangeAxis().setVisible(false);


    }


    private void setNewXValue(int series, int item, int value) {

        /*if (value >= colorMap.getMapSize()) {
            value = colorMap.getMapSize() - 1;
        } else if (value < 0) {
            value = 0;
        } */

        fittedLine.setXValue(series, item, value);
        fireChangeEvent(new ChangeEvent(this));

    }

    private void setNewYValue(int series, int item, double value) {
        if (value > 255) value = 255;
        else if (value < 0) value = 0;
        fittedLine.setYValue(series, item, value);

        fireChangeEvent(new ChangeEvent(this));


    }

    private void addNewXYValue(int series, double x, double y) {
        fittedLine.addXYValue(series, x, y);
        fireChangeEvent(new ChangeEvent(this));

    }

    public void mouseClicked(MouseEvent e) {

        if (e.getClickCount() == 2) {
            ValueAxis hAxis = plot.getDomainAxis();
            ValueAxis vAxis = plot.getRangeAxis();
            double y = vAxis.java2DToValue((double) e.getY(), chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea(), plot.getRangeAxisEdge());
            double x = hAxis.java2DToValue((double) e.getX(), chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea(), plot.getDomainAxisEdge());

            int series = fittedLine.onLine(x, y);
            if (series != -1) {
                addNewXYValue(series, x, y);
            }
        }

    }

    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void mouseReleased(MouseEvent e) {
        if (dragging) {
            dragging = false;
            lockedOnItem = -1;
        }
    }

    public void mouseEntered(MouseEvent e) {
        chartPanel.requestFocus();
    }

    public void mouseExited(MouseEvent e) {

    }


    private void horizontalDrag(Point p) {
        Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();

        ValueAxis hAxis = plot.getDomainAxis();
        ValueAxis vAxis = plot.getRangeAxis();

        double y = vAxis.java2DToValue(p.getY(), dataArea, plot.getRangeAxisEdge());
        double x = hAxis.java2DToValue(p.getX(), dataArea, plot.getDomainAxisEdge());

        if (lockedOnItem != -1) {
            double hdist = controlPoints.horizontalDistance(0, x, y, lockedOnItem);

            double perc = hdist / hAxis.getRange().getLength();

            if (perc < .1) {
                lockedOnItem = -1;
            } else {
                setNewXValue(0, lockedOnItem, (int) x);
            }

            return;
        }

        int item = controlPoints.nearestItem(0, x, y);

        if (item != -1) {
            lockedOnItem = item;
            setNewXValue(0, item, (int) x);
        }

    }


    public void mouseDragged(MouseEvent e) {


        if (e.isControlDown()) {
            horizontalDrag(e.getPoint());
            return;
        }

        Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();

        ValueAxis hAxis = plot.getDomainAxis();
        ValueAxis vAxis = plot.getRangeAxis();

        double y = vAxis.java2DToValue(e.getY(), dataArea, plot.getRangeAxisEdge());
        double x = hAxis.java2DToValue(e.getX(), dataArea, plot.getDomainAxisEdge());

        dragging = true;


        if (lockedOnItem != -1) {
            double hdist = controlPoints.horizontalDistance(0, x, y, lockedOnItem);

            double perc = hdist / hAxis.getRange().getLength();
            if (perc < .1) {
                lockedOnItem = -1;
            } else {
                setNewYValue(0, lockedOnItem, y);
            }

            return;
        }

        int item = controlPoints.nearestItem(0, x, y);

        if (item != -1) {
            lockedOnItem = item;
            setNewYValue(0, item, y);
        }


    }


    public void mouseMoved(MouseEvent e) {
        Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();

        ValueAxis hAxis = plot.getDomainAxis();
        ValueAxis vAxis = plot.getRangeAxis();

        double y = vAxis.java2DToValue(e.getY(), dataArea, plot.getRangeAxisEdge());
        double x = hAxis.java2DToValue(e.getX(), dataArea, plot.getDomainAxisEdge());


        int item = controlPoints.nearestItem(0, x, y);

        if (item != -1) {
            double hdist = controlPoints.horizontalDistance(0, x, y, item);

            double perc = hdist / hAxis.getRange().getLength();
           
        }

    }


    public JComponent getComponent() {
        return chartPanel;

    }


    public static void main(String[] args) {
        JFrame jf = new JFrame();

        byte[] rvals = new byte[256];
        ColorTable.SPECTRUM.getReds(rvals);

        LinearColorMap lmap = new LinearColorMap(0, 1000, ColorTable.SPECTRUM);
        ColorBandChart chart1 = new ColorBandChart(ColorBand.RED, lmap);
        ColorBandChart chart2 = new ColorBandChart(ColorBand.GREEN, lmap);
        ColorBandChart chart3 = new ColorBandChart(ColorBand.BLUE, lmap);

        JPanel panel = new JPanel();

        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.add(chart1.getComponent());
        panel.add(chart2.getComponent());
        panel.add(chart3.getComponent());
        jf.add(panel);
        jf.pack();
        jf.setVisible(true);

    }


    class ControlPointRenderer extends DefaultXYItemRenderer {
        public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info,
                             XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
                             int series, int item, CrosshairState crosshairState, int pass) {

            double x = dataset.getX(series, item).doubleValue();
            double y = dataset.getY(series, item).doubleValue();

            double xtrans = domainAxis.valueToJava2D(
                    x, dataArea, plot.getDomainAxisEdge());

            double ytrans = rangeAxis.valueToJava2D(
                    y, dataArea, plot.getRangeAxisEdge());

            //Line2D line = new Line2D.Double(xtrans, ytrans, xtrans, ytrans);
            Ellipse2D ellipse = new Ellipse2D.Double(xtrans - 4, ytrans - 4, 8, 8);

            g2.setStroke(getSeriesStroke(0));
            //g2.setPaint(getSeriesPaint(0));
            //g2.draw(line);

            //g2.setPaint(Color.YELLOW);
            //g2.fill(ellipse);
            g2.setPaint(getSeriesPaint(0));
            g2.fill(ellipse);


        }


    }


}
