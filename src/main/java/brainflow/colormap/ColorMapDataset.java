/*
 * ColorMapAdapter.java
 *
 * Created on July 7, 2006, 1:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.colormap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.AbstractIntervalXYDataset;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author buchs
 */
public class ColorMapDataset extends AbstractIntervalXYDataset {


    private IColorMap map;
    private int height = 10;

    private ChangeHandler handler = new ChangeHandler();

    /**
     * Creates a new instance of ColorMapAdapter
     */
    public ColorMapDataset(IColorMap _map) {
        map = _map;
        //map.addPropertyChangeListener(handler);
    }

    public void setColorMap(IColorMap _map) {
        //map.removePropertyChangeListener(handler);
        map = _map;
        //map.addPropertyChangeListener(handler);
        notifyListeners(new DatasetChangeEvent(this, this));

    }


    public IColorMap getColorMap() {
        return map;
    }

    public int getSeriesCount() {
        return 1;
    }

    public Comparable getSeriesKey(int i) {
        return "key";
    }

    public int getItemCount(int i) {
        return map.getMapSize();
    }

    public Number getX(int i, int i0) {
        ColorInterval interval = map.getInterval(i0);
        return (interval.getMaximum() + interval.getMinimum()) / 2;
    }

    public Number getY(int i, int i0) {
        return height;
    }

    public ColorInterval getColorInterval(int i, int i0) {
        return map.getInterval(i0);
    }

    public Color getColor(double val) {
        return map.getColor(val);
    }

    public Number getStartX(int i, int i0) {
        ColorInterval interval = map.getInterval(i0);
        return Math.max(interval.getMinimum(), Double.NEGATIVE_INFINITY);
    }

    public Number getEndX(int i, int i0) {
        ColorInterval interval = map.getInterval(i0);
        return Math.min(interval.getMaximum(), Double.POSITIVE_INFINITY);
    }

    public Number getStartY(int i, int i0) {
        return 0;
    }

    public Number getEndY(int i, int i0) {
        return height;
    }


    public static void main(String[] args) {

        LinearColorMap2 cmap = new LinearColorMap2(0, 300, ColorTable.SPECTRUM);

        ColorMapDataset adapter = new ColorMapDataset(cmap);
        JFreeChart chart = ChartFactory.createXYBarChart("title", "zero axis", false, "zero axis", adapter, PlotOrientation.VERTICAL
                , true, true, false);

        ColorIntervalBarRenderer renderer = new ColorIntervalBarRenderer();
        renderer.setDrawBarOutline(false);
        chart.getXYPlot().setRenderer(renderer);

        chart.setTitle("");
        chart.removeLegend();
        //chart.setPadding(new RectangleInsets(0,0,0,0));
        ChartPanel panel = new ChartPanel(chart);
        chart.getXYPlot().setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        panel.setMinimumDrawHeight(50);
        panel.setMaximumDrawHeight(300);

        panel.setMinimumDrawWidth(50);
        panel.setMaximumDrawWidth(300);
        panel.setPreferredSize(new Dimension(256, 50));
        NumberAxis hAxis = new NumberAxis("");
        hAxis.setLowerMargin(0);
        hAxis.setLabel("");
        hAxis.setLowerBound(0);
        hAxis.setUpperBound(300);
        hAxis.setAutoRange(false);
        Marker marker = new IntervalMarker(34, 68);
        marker.setPaint(new Color(0, 0, 0, 128));

        marker.setAlpha(.5f);
        chart.getXYPlot().addDomainMarker(marker);
        NumberAxis vAxis = new NumberAxis("");
        vAxis.setLowerMargin(0);
        vAxis.setLabel("");
        vAxis.setLowerBound(0);
        vAxis.setUpperBound(10);
        vAxis.setAutoRange(false);
        vAxis.setTickLabelsVisible(false);
        vAxis.setTickMarksVisible(false);


        chart.getXYPlot().setDomainAxis(hAxis);
        chart.getXYPlot().setRangeAxis(vAxis);


        JFrame frame = new JFrame();
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);


    }

    private class ChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent e) {

            notifyListeners(new DatasetChangeEvent(ColorMapDataset.this, ColorMapDataset.this));
        }
    }


}
