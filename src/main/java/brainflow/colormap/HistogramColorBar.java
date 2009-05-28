package brainflow.colormap;

import brainflow.image.Histogram;
import brainflow.image.io.IImageDataSource;
import brainflow.core.BrainFlowException;
import brainflow.utils.Range;
import brainflow.utils.IRange;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import cern.colt.list.DoubleArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 13, 2008
 * Time: 6:37:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistogramColorBar extends JComponent {

    private IColorMap colorMap;

    private Histogram histogram;

    private double yaxisFraction = 1;

    public int RIGHT_CUSHION = 12;

    public int BOTTOM_CUSHION = 12;

    public int TOP_CUSHION = 4;


    private int selectedBin = -1;


    public HistogramColorBar(IColorMap map, Histogram histogram) {
        this.colorMap = map;
        this.histogram = histogram;

    }

    public double getYaxisFraction() {
        return yaxisFraction;
    }

    public void setYaxisFraction(double yaxisFraction) {
        if (yaxisFraction <= 0) throw new IllegalArgumentException("yaxisFraction must be > 0 and <= 1");
        this.yaxisFraction = yaxisFraction;
        repaint();
    }

    public IColorMap getColorMap() {
        return colorMap;
    }

    public void setColorMap(IColorMap colorMap) {
        this.colorMap = colorMap;
        repaint();
    }

    public int getSelectedBin() {
        return selectedBin;
    }

    public void setSelectedBin(int selectedBin) {
        this.selectedBin = selectedBin;
        repaint();
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public int getYOffset() {
        return getInsets().top + TOP_CUSHION;
    }

    public int getXOffset() {
        return getInsets().left;
    }

    public double getMinValue() {
        return histogram.getMinValue();
    }

    public double getMaxValue() {
        return histogram.getMaxValue();
    }

    public int valueToLocationX(double value) {
        double perc = (value - histogram.getMinValue()) / (histogram.getMaxValue() - histogram.getMinValue());
        return (int) Math.round(perc * getDataArea().width);
    }

    public double locationToValueX(int loc) {
        double perc = ((double) loc - getXOffset()) / getDataArea().getWidth();
        return perc * (histogram.getMaxValue() - histogram.getMinValue()) + histogram.getMinValue();


    }

    public double locationToValueY(int loc) {
        double perc = ((double) loc - getYOffset()) / getDataArea().getHeight();
        if (perc < 0 || perc > 1) return -1;

        DoubleArrayList binHeights = histogram.computeBins();
        double ymax = binHeights.get(histogram.getHighestBin()) * yaxisFraction;

        return (1 - perc) * ymax;


    }

    public IRange getBinInterval(int bin) {
        DoubleArrayList binIntervals = histogram.getBinIntervals();

        double xstart = binIntervals.get(bin);
        double xend = binIntervals.get(bin + 1);

        return new Range(xstart, xend);

    }


    public int getBin(double value) {
        return histogram.whichBin(value);
    }

    public double getCount(double value) {
        int bin = histogram.whichBin(value);
        if (bin < 0) return 0;
        return histogram.getCount(bin);
    }

    public void setHistogram(Histogram histogram) {
        this.histogram = histogram;
        repaint();
    }

    public Rectangle getDataArea() {

        Insets insets = getInsets();
        return new Rectangle(insets.left, insets.top + TOP_CUSHION,
                getWidth() - insets.left - insets.right - RIGHT_CUSHION,
                getHeight() - insets.top - insets.bottom - BOTTOM_CUSHION - TOP_CUSHION);
    }

    public Rectangle2D getBarBounds(int bin) {
        DoubleArrayList binIntervals = histogram.getBinIntervals();
        DoubleArrayList binHeights = histogram.computeBins();
        Rectangle dataArea = getDataArea();

        double minx = binIntervals.get(0);
        double xextent = binIntervals.get(binIntervals.size() - 1) - minx;

        double ymax = binHeights.get(histogram.getHighestBin()) * yaxisFraction;

        double xstart = binIntervals.get(bin);
        double xend = binIntervals.get(bin + 1);
        
        double nxstart = (xstart - minx) / xextent;
        double nxend = (xend - minx) / xextent;

        //double y = Math.log(binHeights.get(i));
        double y = binHeights.get(selectedBin);
        double ny = Math.min((y / ymax), 1) * (double) dataArea.height;


        return new Rectangle2D.Double(dataArea.x + nxstart * dataArea.width - 2, dataArea.height - ny + dataArea.y, (nxend - nxstart) * dataArea.width + 2, ny);


    }


    private void paintSelectedBin(Graphics2D g2, Rectangle dataArea) {

        DoubleArrayList binIntervals = histogram.getBinIntervals();
        DoubleArrayList binHeights = histogram.computeBins();
        double minx = binIntervals.get(0);
        double xextent = binIntervals.get(binIntervals.size() - 1) - minx;

        double ymax = binHeights.get(histogram.getHighestBin()) * yaxisFraction;

        double xstart = binIntervals.get(selectedBin);
        double xend = binIntervals.get(selectedBin + 1);

        Color startColor = colorMap.getColor(xstart);
        Color endColor = colorMap.getColor(xend);


        double nxstart = (xstart - minx) / xextent;
        double nxend = (xend - minx) / xextent;

        //double y = Math.log(binHeights.get(i));
        double y = binHeights.get(selectedBin);
        double ny = Math.min((y / ymax), 1) * (double) dataArea.height;


        Rectangle2D rect = new Rectangle2D.Double(dataArea.x + nxstart * dataArea.width - 2, dataArea.height - ny + dataArea.y, (nxend - nxstart) * dataArea.width + 2, ny);
        Paint p = new GradientPaint((float) (nxstart * dataArea.width), 0, startColor, (float) (nxend * dataArea.width), 0, endColor);

        g2.setPaint(p);

        g2.fill(rect);
        g2.setPaint(startColor);
        g2.draw(rect);
        g2.setComposite(AlphaComposite.SrcOver.derive(.4f));
        g2.setPaint(Color.WHITE);
        g2.draw(rect);


    }


    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle dataArea = getDataArea();

        int width = dataArea.width;
        int height = dataArea.height;

        histogram.computeBins();

        DoubleArrayList binHeights = histogram.computeBins();
        DoubleArrayList binIntervals = histogram.getBinIntervals();

        double minx = binIntervals.get(0);
        double xextent = binIntervals.get(binIntervals.size() - 1) - minx;

        double ymax = binHeights.get(histogram.getHighestBin()) * yaxisFraction;

        for (int i = 0; i < binHeights.size(); i++) {
            if (i == getSelectedBin()) {
                continue;
            }


            double xstart = binIntervals.get(i);
            double xend = binIntervals.get(i + 1);

            Color startColor = colorMap.getColor(xstart);
            Color endColor = colorMap.getColor(xend);


            double nxstart = (xstart - minx) / xextent;
            double nxend = (xend - minx) / xextent;

            //double y = Math.log(binHeights.get(i));
            double y = binHeights.get(i);
            double ny = Math.min((y / ymax), 1) * (double) height;


            //rect = new Rectangle2D.Double(dataArea.x + nxstart * width-.5, height - ny + dataArea.y, (nxend - nxstart) * width+.5, ny);
            //p = new GradientPaint((float) (nxstart * width), 0, startColor, (float) (nxend * width), 0, endColor);

            Rectangle2D rect = new Rectangle2D.Double(dataArea.x + nxstart * width, height - ny + dataArea.y, (nxend - nxstart) * width, ny);
            Paint p = new GradientPaint((float) (nxstart * width), 0, startColor, (float) (nxend * width), 0, endColor);


            g2.setPaint(p);

            g2.fill(rect);


            p = new GradientPaint((float) (nxstart * width), 0, Color.BLACK,
                    (float) (nxstart * width), height,
                    new Color((int) (startColor.getRed() * .85), (int) (startColor.getGreen() * .85), (int) (startColor.getBlue() * .85)));


            g2.setPaint(p);

            g2.draw(rect);

        }

        if (selectedBin >= 0)
            paintSelectedBin(g2, dataArea);

    }

    public static void main(String[] args) {
        IImageDataSource dataSource = null;//TestUtils.quickDataSource("resources/data/global_mean+orig.HEAD");
        try {
            dataSource.load();
        } catch (BrainFlowException e) {
            e.printStackTrace();
        }
        Histogram histo = new Histogram(dataSource.getData(), 75);
        histo.ignoreRange(new Range(0, 10));
        histo.computeBins();
        IColorMap map = new LinearColorMap2(histo.getMinValue(), histo.getMaxValue(), ColorTable.GRAYSCALE);
        final HistogramColorBar bar = new HistogramColorBar(map, histo);
        bar.setPreferredSize(new Dimension(350, 100));

        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());
        jp.add(bar, BorderLayout.CENTER);
        final JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 100);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double frac = (double) slider.getValue() / 100.0;
                bar.setYaxisFraction(frac);
            }
        });

        jp.add(slider, BorderLayout.WEST);
        JFrame jf = new JFrame();
        jf.add(jp, BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }
}
