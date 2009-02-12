package brainflow.display;

import brainflow.application.BrainFlowException;
import brainflow.colormap.ColorTable;
import brainflow.colormap.IColorMap;
import brainflow.colormap.LinearColorMapDeprecated;
import brainflow.image.Histogram;
import brainflow.image.data.IImageData;
import brainflow.image.io.BrainIO;
import brainflow.chart.HistogramDatasetX;
import brainflow.chart.LogHistogramDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.IndexColorModel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ColoredHistogram extends JPanel {

    private Histogram histogram;

    private HistogramDatasetX dataset;

    private Point lastPressed;

    private Rectangle2D dataArea;


    private JFreeChart chart;

    private ChartPanel chartPanel;

    private NumberAxis hAxis;

    private NumberAxis yAxis;

    private IColorMap colorModel;

    private double xmedian;

    private JPanel bottom = new JPanel();




    double ybound;


    public ColoredHistogram(Histogram _histogram) {
        setLayout(new BorderLayout());
        histogram = _histogram;

        hAxis = new NumberAxis("");
        hAxis.setAutoRange(false);
        yAxis = new NumberAxis("");
        yAxis.setAutoRange(false);

        setHistogram(histogram);

        chart = ChartFactory.createXYBarChart("", "domain", false, "range",
                dataset, org.jfree.chart.plot.PlotOrientation.VERTICAL, false, false, false);

        hAxis.setUpperMargin(2);
        hAxis.setLowerMargin(2);
        yAxis.setUpperMargin(2);
        yAxis.setLowerMargin(2);


        chart.getXYPlot().setDomainAxis(hAxis);
        chart.getXYPlot().setRangeAxis(yAxis);
        chart.getXYPlot().setDomainGridlinesVisible(false);
        chart.getXYPlot().setRangeGridlinesVisible(false);

        chart.getPlot().setBackgroundPaint(Color.black);


        class MyBarRenderer extends org.jfree.chart.renderer.xy.XYBarRenderer {


            public MyBarRenderer() {
                super();

            }



            @Override
            public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info,
                                 XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis,
                                 XYDataset data, int series, int item, CrosshairState crosshairState, int pass) {


              

                //int numBins = histogram.getNumBins();
                double xval= dataset.getXValue(series, item);
                Color rgb = colorModel.getColor(xval);

                super.setSeriesPaint(series, rgb, false);

                super.drawItem(g2, state, dataArea, info, plot, domainAxis, rangeAxis, data, series, item, crosshairState, pass);

            }
        }


        chartPanel = new ChartPanel(chart);
        chartPanel.setMinimumDrawHeight(200);
        chartPanel.setMinimumDrawWidth(200);
        chartPanel.setMaximumDrawHeight(800);
        chartPanel.setMaximumDrawWidth(800);
        

        //chartPanel.addMouseListener(this);
        //chartPanel.addMouseMotionListener(this);


        //xaxisPointer = new AxisPointer(this, 10, 10);
        //xaxisPointer.setAxisValue((double) hAxis.getLowerBound() + 1);

        //chart.getXYPlot().setDomainCrosshairValue(xaxisPointer.getAxisValue());
        //chart.getXYPlot().setDomainCrosshairPaint(Color.GREEN);
        MyBarRenderer bren = new MyBarRenderer();
        bren.setDrawBarOutline(false);
        chart.getXYPlot().setRenderer(bren);
        //chart.getXYPlot().setDomainCrosshairVisible(true);


        add(chartPanel, "Center");
        //add(bottom, "South");
        //add(yBoundsSlider, "East");
    }

    


    public void setHistogram(Histogram histogram) {

        xmedian = histogram.intervalMedian();
        dataset = new LogHistogramDataset(histogram);

        hAxis.setLowerBound(histogram.getMinValue());
        hAxis.setUpperBound(histogram.getMaxValue());

        //ybound = histogram.binMedian() + (2 * histogram.binStandardDeviation());
        ybound = dataset.getYRange().getMax();
        yAxis.setUpperBound(ybound);
        yAxis.setLowerBound(0);

        if (chart != null) {
            chart.getXYPlot().setDataset(dataset);
        }

    }

    public double getYBound() {
        return ybound;
    }

    public XYPlot getXYPlot() {
        return chart.getXYPlot();
    }

    public HistogramDatasetX getDataset() {
        return dataset;
    }

    public ChartPanel getChartPanel() {
        return chartPanel;

    }






    public void setIndexColorModel(IndexColorModel icm) {
        colorModel = new LinearColorMapDeprecated(colorModel.getMaximumValue(), colorModel.getMaximumValue(), icm);
        chartPanel.repaint();
    }

    public void setColorModel(IColorMap iColorMap) {
        colorModel = iColorMap;
        chartPanel.repaint();
    }





    public static void main(String[] args) {
        try {
            IImageData data = BrainIO.readAnalyzeImage("C:/DTI/slopes/bAge.Norm");
            Histogram hist = new Histogram(data, 100);
            ColoredHistogram chist = new ColoredHistogram(hist);
            chist.setColorModel(new LinearColorMapDeprecated(0, 255, ColorTable.SPECTRUM));
            JFrame frame = new JFrame();
            frame.getContentPane().add(chist);
            frame.pack();
            frame.setVisible(true);
        } catch (BrainFlowException ex) {
           
        }
    }


}
