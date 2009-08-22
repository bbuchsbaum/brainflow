package brainflow.app.presentation.controls;

import brainflow.colormap.IColorMap;
import brainflow.colormap.HistogramColorBar;
import brainflow.colormap.LinearColorMap2;
import brainflow.colormap.ColorTable;
import brainflow.image.Histogram;
import brainflow.image.MaskedHistogram;
import brainflow.image.io.IImageDataSource;
import brainflow.core.BrainFlowException;
import brainflow.app.IBrainFlowClient;
import brainflow.app.toplevel.BrainFlowClientSupport;
import brainflow.utils.Range;
import brainflow.utils.IRange;
import brainflow.core.ImageView;
import brainflow.core.IClipRange;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.AbstractLayer;
import brainflow.app.chart.XAxis;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;

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
public class HistogramControl extends JPanel implements MouseListener, IBrainFlowClient {

    private Histogram histogram;

    private IColorMap colorMap;

    private HistogramColorBar colorBar;

    private HistogramWithAxis histoBar;

    private JXLayer<HistogramColorBar> layer;

    private IRange overlayRange;

    private JSlider slider;

    private JCheckBox useMaskCheckBox;

    private BrainFlowClientSupport support;

    public HistogramControl(IColorMap map, Histogram histogram, Range overlayRange) {
        this.colorMap = map;
        this.histogram = histogram;
        this.overlayRange = overlayRange;

        colorBar = new HistogramColorBar(map, histogram);

        initLayer();
        histoBar = new HistogramWithAxis();


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

        useMaskCheckBox = new JCheckBox("use mask");
        add(useMaskCheckBox, BorderLayout.NORTH);

        add(histoBar, BorderLayout.CENTER);
        add(slider, BorderLayout.WEST);

        support = new BrainFlowClientSupport(this);


    }

    private void initCheckBox() {
        useMaskCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (useMaskCheckBox.isSelected()) {
                    //MaskedHistogram mhist = new MaskedHistogram(support.getSelectedView().getModel().getSelectedLayer().getData(),
                    //      support.getSelectedView().getModel().getSelectedLayer().getMaskProperty().buildMask(), histogram.getNumBins());  
                }
            }
        });

    }

    private void initLayer() {
        layer = new JXLayer<HistogramColorBar>(colorBar);
        AbstractLayerUI<HistogramColorBar> layerUI = new AbstractLayerUI<HistogramColorBar>() {


            private NumberFormat formatter = NumberFormat.getNumberInstance();


            boolean resizeMin = false;

            boolean resizeMax = false;


            @Override
            protected void paintLayer(Graphics2D g2, JXLayer<HistogramColorBar> l) {
                formatter.setMaximumFractionDigits(2);
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


            @Override
            protected void processMouseEvent(MouseEvent e, JXLayer<HistogramColorBar> l) {
                if (e.getID() == MouseEvent.MOUSE_PRESSED)
                    mousePressed(e);
            }


            protected void processMouseMotionEvent(MouseEvent e, JXLayer<HistogramColorBar> l) {
                if (e.getID() == MouseEvent.MOUSE_MOVED) {
                    mouseMoved(e);
                } else if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
                    mouseDragged(e);
                } else if (e.getID() == MouseEvent.MOUSE_EXITED) {
                    resizeMin = false;
                    resizeMax = false;
                }


            }

            public void mouseDragged(MouseEvent e) {
                Point p = e.getPoint();
                if (resizeMin) {
                    double xmin = colorBar.locationToValueX(p.x);
                    AbstractLayer layer = getSelectedLayer();
                    IClipRange clip = layer.getLayerProps().thresholdRange.get();

                    if (xmin > clip.getHighClip()) return;
                    //AbstractLayer layer = getSelectedLayer();
                    layer.getLayerProps().thresholdRange.set(
                            clip.newClipRange(clip.getMin(), clip.getMax(), xmin, clip.getHighClip()));
                } else if (resizeMax) {
                    double xmax = colorBar.locationToValueX(p.x);
                    AbstractLayer layer = getSelectedLayer();
                    IClipRange clip = layer.getLayerProps().thresholdRange.get();
                    if (xmax < clip.getLowClip()) return;
                    //AbstractLayer layer = getSelectedLayer();
                    layer.getLayerProps().thresholdRange.set(
                            clip.newClipRange(clip.getMin(), clip.getMax(), clip.getLowClip(), xmax));

                }


            }


            private void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                double xvalue = colorBar.locationToValueX(p.x);

                int bin = colorBar.getBin(xvalue);
                colorBar.setSelectedBin(bin);

                double xstart = colorBar.valueToLocationX(overlayRange.getMin());
                double xend = colorBar.valueToLocationX(overlayRange.getMax());

                if (Math.abs(xend - p.x) < 3) {
                    HistogramControl.this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    resizeMax = true;
                    resizeMin = false;
                } else if (Math.abs(xstart - p.x) < 3) {
                    HistogramControl.this.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                    resizeMin = true;
                    resizeMax = false;

                } else {
                    HistogramControl.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    resizeMin = false;
                    resizeMax = false;
                }


                repaint();

            }
        };

        // set our LayerUI
        layer.setUI(layerUI);

    }

    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


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

        barBounds = new Rectangle((int) (barBounds.getX() - 2), (int) (barBounds.getY() - 2), (int) (barBounds.getWidth() + 5), (int) (barBounds.getHeight() + 5));
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
        if (!this.overlayRange.equals(overlayRange)) {
            this.overlayRange = overlayRange;
            repaint();
        }
    }

    public IColorMap getColorMap() {
        return colorMap;
    }

    public void setColorMap(IColorMap colorMap) {
        this.colorMap = colorMap;
        colorBar.setColorMap(colorMap);
        histoBar.updateAxis(colorMap.getMinimumValue(), colorMap.getMaximumValue());
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public void setHistogram(Histogram histogram) {
        this.histogram = histogram;
        colorBar.setHistogram(histogram);
    }

    public void viewSelected(ImageView view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void viewModelChanged(ImageView view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerChangeNotification() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerSelected(ImageLayer layer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerAdded(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerRemoved(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerChanged(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerIntervalAdded(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerIntervalRemoved(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public ImageView getSelectedView() {
        return support.getSelectedView();
    }

    public AbstractLayer getSelectedLayer() {
        return support.getSelectedLayer();
    }


    class HistogramWithAxis extends JPanel {
        XAxis axis = new XAxis(0, 255);


        JPanel axispanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //axis.setYoffset(0);

                Rectangle bounds = getBounds();
                bounds = new Rectangle(bounds.x, bounds.y, bounds.width - colorBar.RIGHT_CUSHION, bounds.height);
                axis.draw((Graphics2D) g, bounds);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(256, 20);
            }
        };


        public HistogramWithAxis() {
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setLayout(new BorderLayout());
            axis.setXoffset(0);
            axis.setYoffset(0);
            add(layer, BorderLayout.CENTER);
            add(axispanel, BorderLayout.SOUTH);
        }

        public void updateAxis(double min, double max) {
            axis.setMin(min);
            axis.setMax(max);
            repaint();


        }


    }

    public static void main(String[] args) {
        IImageDataSource dataSource = null; //BF.quickDataSource("resources/data/global_mean+orig.HEAD");
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
