package brainflow.application.presentation.controls;

import brainflow.colormap.IColorMap;
import brainflow.colormap.HistogramColorBar;
import brainflow.colormap.LinearColorMap2;
import brainflow.colormap.ColorTable;
import brainflow.image.Histogram;
import brainflow.image.io.IImageDataSource;
import brainflow.application.BrainFlowException;
import brainflow.utils.Range;
import brainflow.utils.IRange;

import javax.swing.*;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;

import test.TestUtils;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.CustomBalloonTip;

import net.java.balloontip.styles.ModernBalloonStyle;


/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 27, 2008
 * Time: 4:19:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistogramControl extends JPanel implements MouseListener {

    private Histogram histogram;

    private IColorMap colorMap;

    private HistogramColorBar colorBar;

    private JXLayer<HistogramColorBar> layer;

    private IRange overlayRange;

    private JSlider slider;

    public HistogramControl(IColorMap map, Histogram histogram, Range overlayRange) {
        this.colorMap = map;
        this.histogram = histogram;
        this.overlayRange = overlayRange;

        colorBar = new HistogramColorBar(map, histogram);
        initLayer();

        slider = new JSlider(JSlider.VERTICAL, 1, 200, 100);

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double val = (double) slider.getValue();

                double frac = val / 100.0;
                frac = Math.pow(frac, 2);
                colorBar.setYaxisFraction(frac);
            }
        });

        setLayout(new BorderLayout());
        add(layer, BorderLayout.CENTER);
        add(slider, BorderLayout.WEST);

        //addMouseListener();
    }

    private void initLayer() {
        layer = new JXLayer<HistogramColorBar>(colorBar);
        AbstractLayerUI<HistogramColorBar> layerUI = new AbstractLayerUI<HistogramColorBar>() {

            private boolean drawTip = false;

            private Point tiploc;

            private double xvalue = 0;

            private int count = 0;

            private NumberFormat formatter = NumberFormat.getNumberInstance();


            @Override
            protected void paintLayer(Graphics2D g2, JXLayer<HistogramColorBar> l) {
                formatter.setMaximumFractionDigits(2);
                // this paints layer as is
                super.paintLayer(g2, l);
                // custom painting:
                // here we paint translucent foreground
                // over the whole layer

                double xstart = colorBar.valueToLocationX(overlayRange.getMin());
                double xend = colorBar.valueToLocationX(overlayRange.getMax());

                g2.setColor(new Color(37, 37, 37));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));


                g2.fillRect((int) xstart, colorBar.getYOffset(), (int) (xend - xstart), colorBar.getDataArea().height);
                g2.setColor(new Color(127, 127, 127));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
                g2.drawRect((int) xstart, colorBar.getYOffset(), (int) (xend - xstart), colorBar.getDataArea().height);


            }

            private void paintTip(Graphics2D g2, Point p, double value, int count) {
                Composite oldcomposite = g2.getComposite();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f));
                Rectangle rect = new Rectangle(colorBar.getWidth() - 100, 4, 95, 40);
                g2.setColor(Color.BLACK);
                g2.fill(rect);
                g2.setColor(Color.WHITE);
                g2.draw(rect);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
                g2.drawString("count : " + count, colorBar.getWidth() - 96, 15);
                g2.drawString("value : " + formatter.format(value), colorBar.getWidth() - 96, 30);

                g2.setComposite(oldcomposite);

            }

            @Override
            protected void processMouseEvent(MouseEvent e, JXLayer<HistogramColorBar> l) {
                if (e.getID() == MouseEvent.MOUSE_PRESSED)
                    mousePressed(e);
            }

            protected void processMouseMotionEvent(MouseEvent e, JXLayer<HistogramColorBar> l) {
                Point p = e.getPoint();
                xvalue = colorBar.locationToValueX(p.x);
                int bin = colorBar.getBin(xvalue);
                colorBar.setSelectedBin(bin);
                double yval = colorBar.locationToValueY(p.y);
                count = (int) colorBar.getCount(xvalue);


                drawTip = true;
                tiploc = e.getPoint();
                repaint();

                //System.out.println("mouse id = " + e.getID());


            }
        };

        // set our LayerUI
        layer.setUI(layerUI);

    }

    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    //private JidePopup infoPopup;

    private BalloonTip customBalloon;


    public void mousePressed(MouseEvent e) {
        if (customBalloon != null) {
            customBalloon.setVisible(false);
        }

        Point p = e.getPoint();
        double xvalue = colorBar.locationToValueX(p.x);

        int bin = colorBar.getBin(xvalue);

        if (bin < 0) return;


        int count = (int) colorBar.getCount(xvalue);
        IRange binRange = colorBar.getBinInterval(bin);

        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);

        Rectangle2D barBounds = colorBar.getBarBounds(colorBar.getSelectedBin());

        barBounds = new Rectangle((int)(barBounds.getX() -2), (int)(barBounds.getY()-2), (int)(barBounds.getWidth() +5), (int)(barBounds.getHeight()+5));
        if (!barBounds.contains(p)) {
            return;
        }

        customBalloon = new CustomBalloonTip(colorBar,

                "<html> bin: " + "[" + format.format(binRange.getMin()) + " -> " + format.format(binRange.getMax()) + "]" +
                "<br> count: " + count + "</html>",
                barBounds.getBounds(),
                new ModernBalloonStyle(3, 3, Color.WHITE, Color.WHITE.darker(), Color.GRAY),
                BalloonTip.Orientation.LEFT_ABOVE,
                BalloonTip.AttachLocation.WEST,
                10, 10,
                false);

        
        customBalloon.setVisible(true);



    }

    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public IRange getOverlayRange() {
        return overlayRange;
    }

    public void setOverlayRange(IRange overlayRange) {
        this.overlayRange = overlayRange;
        repaint();
    }

    public IColorMap getColorMap() {
        return colorMap;
    }

    public void setColorMap(IColorMap colorMap) {
        this.colorMap = colorMap;
        colorBar.setColorMap(colorMap);
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public void setHistogram(Histogram histogram) {
        this.histogram = histogram;
        colorBar.setHistogram(histogram);
    }


    public static void main(String[] args) {
        IImageDataSource dataSource = TestUtils.quickDataSource("resources/data/global_mean+orig.HEAD");
        try {
            dataSource.load();
        } catch (BrainFlowException e) {
            e.printStackTrace();
        }
        Histogram histo = new Histogram(dataSource.getData(), 75);
        histo.computeBins();
        IColorMap map = new LinearColorMap2(histo.getMinValue(), histo.getMaxValue(), ColorTable.GRAYSCALE);
        final HistogramControl control = new HistogramControl(map, histo, new Range(histo.getMinValue(), histo.getMaxValue()));
        //bar.setPreferredSize(new Dimension(350, 100));


        JFrame jf = new JFrame();
        jf.add(control, BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }
}
