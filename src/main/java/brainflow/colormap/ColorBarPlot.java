/*
 * ColorBarPlot.java
 *
 * Created on July 13, 2006, 10:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.colormap;

import brainflow.app.presentation.controls.ColorBarForm;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author buchs
 */
public class ColorBarPlot extends ChartPanel {

    private ColorMapDataset dataset;

    private JFreeChart chart;

    private PlotOrientation orientation = PlotOrientation.VERTICAL;

    private AbstractColorBar colorBar;

    //todo what on earther was this for?
    //private ChangeHandler handler = new ChangeHandler();

    /**
     * Creates a new instance of ColorBarPlot
     */

    public ColorBarPlot(IColorMap cmap) {
        super(null);
        dataset = new ColorMapDataset(cmap);
        colorBar = dataset.getColorMap().createColorBar();
        colorBar.setOrientation(SwingUtilities.HORIZONTAL);
        //dataset.addChangeListener(handler);
        init();

    }

    public ColorBarPlot() {
        super(null);

        dataset = new ColorMapDataset(new LinearColorMap2(0, 255, ColorTable.SPECTRUM));
        //dataset.addChangeListener(handler);
        colorBar = dataset.getColorMap().createColorBar();
        colorBar.setOrientation(SwingUtilities.HORIZONTAL);
        init();
    }

    //todo somewhat expensive method
    public void setColorMap(IColorMap map) {

        dataset.setColorMap(map);

        chart.getXYPlot().getDomainAxis().setRange(map.getMinimumValue(), map.getMaximumValue());

        colorBar = map.createColorBar();
        
        colorBar.setOrientation(SwingUtilities.HORIZONTAL);
        chart.getXYPlot().setBackgroundImage(colorBar.getImage());


    }

    public IColorMap getColorMap() {
        return dataset.getColorMap();
    }

    public void setPlotOrientation(PlotOrientation _orientation) {
        orientation = _orientation;
        //initView();
    }


    private void init() {


        setBackground(Color.BLACK);

        chart = ChartFactory.createXYBarChart("", "", false, "", dataset, orientation
                , true, true, false);

        //chart.setPadding(new RectangleInsets(0,0,0,0));
        ColorIntervalBarRenderer renderer = new ColorIntervalBarRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setDoNothing(true);


        setChart(chart);

        chart.getXYPlot().setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        chart.getXYPlot().setBackgroundImage(colorBar.getImage());

        setMinimumDrawHeight(25);
        setMaximumDrawHeight(300);

        setMinimumDrawWidth(25);
        setMaximumDrawWidth(300);
        //chartPanel.setPreferredSize(new Dimension(256, 50));


        NumberAxis hAxis = new NumberAxis("");
        hAxis.setLowerMargin(0);
        hAxis.setLabel("");
        //hAxis.setLowerBound(dataset.getColorMap().getMinimumValue());
        //hAxis.setUpperBound(dataset.getColorMap().getMaximumValue());
        hAxis.setAutoRange(true);

        NumberAxis vAxis = new NumberAxis("");
        vAxis.setLowerMargin(0);
        vAxis.setLabel("");
        vAxis.setLowerBound(0);
        vAxis.setUpperBound(10);

        vAxis.setTickLabelsVisible(false);
        vAxis.setTickMarksVisible(false);


        chart.getXYPlot().setDomainAxis(hAxis);
        chart.getXYPlot().setRangeAxis(vAxis);

        chart.getXYPlot().setRenderer(renderer);
        chart.removeLegend();
        chart.getXYPlot().setDomainGridlinesVisible(false);
        chart.getXYPlot().setRangeGridlinesVisible(false);


    }


    class ChangeHandler implements DatasetChangeListener {


        public void datasetChanged(DatasetChangeEvent event) {

            final Rectangle2D dataArea = ColorBarPlot.this.getChartRenderingInfo().getPlotInfo().getDataArea();

            if (dataArea.getWidth() > 0)  {

                // the reason for this is because the ColorMapChangeEvent is dispatched
                // to this class and to the ColorBar class. If this class gets the event first then
                // the we will repaint the old color bar. Therefore I add a 200 ms delay with the Timer
                // class. It is a hack but it seems to work.
                
                Timer timer = new javax.swing.Timer(200, new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        BufferedImage img = new BufferedImage((int) dataArea.getWidth(), (int) dataArea.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                        Graphics2D g2 = img.createGraphics();
                        colorBar.setSize((int) dataArea.getWidth(), (int) dataArea.getHeight());
                        colorBar.paint(g2);

                        g2.dispose();
                        chart.getXYPlot().setBackgroundImage(img);


                    }
                });

                timer.setCoalesce(true);
                timer.setRepeats(false);
                timer.start();

            }

        }
    }

    public Dimension getPreferredSize() {
        if (orientation == PlotOrientation.HORIZONTAL) {
            return new Dimension(100, 300);
        } else {
            return new Dimension(300, 100);
        }
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.add(new ColorBarForm(), BorderLayout.CENTER);
        jf.setVisible(true);
        jf.pack();

    }


}
