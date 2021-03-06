package brainflow.core;

import brainflow.core.annotations.IAnnotation;
import brainflow.display.InterpolationType;
import brainflow.image.anatomy.*;
import brainflow.image.axis.AxisRange;
import brainflow.image.axis.ImageAxis;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 2, 2006
 * Time: 3:10:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ComponentImagePlot extends JPanel implements IImagePlot {

    private GridPoint3D slice;

    private ViewBounds viewBounds;

    private LinkedHashMap<String, IAnnotation> annotationMap = new LinkedHashMap<String, IAnnotation>();

    private IImageProducer producer;

    private ImageViewModel model;

    private Insets plotInsets = new Insets(18, 18, 18, 18);

    private Insets plotSlack = new Insets(0, 0, 0, 0);

    private Rectangle plotArea = new Rectangle(300, 300);

    private boolean preserveAspectRatio = true;

    private String name;

    private Rectangle oldArea = null;

    private PropertyChangeListener annotationListener;

    private InterpolationType screenInterpolation = InterpolationType.LINEAR;

    private EventListenerList listeners = new EventListenerList();


    private ComponentImagePlot(ImageViewModel model, IImageProducer _producer, ViewBounds viewBounds) {
        setBackground(Color.BLACK);
        setOpaque(true);

        this.viewBounds = viewBounds;
        this.model = model;
        producer = _producer;

        initAnnotationListener();

    }

    public ComponentImagePlot(ImageViewModel model, ViewBounds viewBounds) {
        setBackground(Color.BLACK);
        setOpaque(true);
        this.viewBounds = viewBounds;
        this.model = model;

        slice = GridPoint3D.fromReal(model.getImageSpace().getCentroid(), model.getImageSpace());
        producer = new CompositeImageProducer(this,  slice);

        initAnnotationListener();

    }

    private void initAnnotationListener() {
        annotationListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                repaint();
            }
        };
    }

    @Override
    public void addViewBoundsChangedListener(ViewBoundsChangedListener listener) {
        listeners.add(ViewBoundsChangedListener.class, listener);
    }

    @Override
    public void removeViewBoundsChangedListener(ViewBoundsChangedListener listener) {
        listeners.remove(ViewBoundsChangedListener.class, listener);
    }

    public InterpolationType getScreenInterpolation() {
        return screenInterpolation;
    }

    public void setScreenInterpolation(InterpolationType type) {
        screenInterpolation = type;
        producer.setScreenInterpolation(type);
        repaint();
    }

    public boolean isPreserveAspectRatio() {
        return preserveAspectRatio;
    }

    public void setPreserveAspectRatio(boolean b) {
        preserveAspectRatio = b;
        repaint();
    }


    public void setSlice(GridPoint3D slice) {
        if (getSlice() == null || !getSlice().equals(slice)) {
            this.slice = slice;
            producer.setSlice(slice);
            repaint();

        }

    }

    public GridPoint3D getSlice() {
        return slice;
    }

    public ImageViewModel getModel() {
        return model;
    }

    public JComponent getComponent() {
        return this;
    }

    public void setViewBounds(ViewBounds vbounds) {
        ViewBounds oldBounds = getViewBounds();

        if (!oldBounds.equals(vbounds)) {
            viewBounds = vbounds;
            producer.reset();
            repaint();
            fireViewBoundsListenerEvent(oldBounds, viewBounds);
        }
    }

    private void fireViewBoundsListenerEvent(ViewBounds oldBounds, ViewBounds newBounds) {
        ViewBoundsChangedListener[] list = listeners.getListeners(ViewBoundsChangedListener.class);
        for (ViewBoundsChangedListener vbc : list) {
            vbc.viewBoundsChanged(this, oldBounds, newBounds);
        }
    }

    public ViewBounds getViewBounds() {
        return viewBounds;
    }

    private Insets getPlotSlack() {
        return plotSlack;
    }

    public Insets getPlotMargins() {
        Insets plotSlack = getPlotSlack();
        Insets plotInsets = getPlotInsets();
        return new Insets(plotInsets.top + plotSlack.top, plotInsets.left + plotSlack.left,
                plotInsets.bottom + plotSlack.bottom, plotInsets.right + plotSlack.right);


    }

    public void setImageProducer(IImageProducer producer) {
        this.producer = producer;
        producer.setPlot(this);
    }

    public IImageProducer getImageProducer() {
        return producer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        //Dimension size = getSize();

        Rectangle plotArea = getPlotArea();


        if (!plotArea.equals(oldArea)) {
            oldArea = plotArea;
            producer.setScreenSize(plotArea);
        }


        //Insets insets = getInsets();
        //Color oldColor = g2.getColor();
        //g2.setColor(Color.BLACK);

        //g2.fillRect(insets.left, insets.top,
        //        (int) (size.getWidth() - insets.left - insets.right),
        //        (int) (size.getHeight() - insets.top - insets.bottom));


        //g2.setColor(oldColor);

        if (plotArea.getWidth() < 5 || plotArea.getHeight() < 5) {
            return;
        }


        g2.drawRenderedImage(producer.getImage(), AffineTransform.getTranslateInstance((int) plotArea.getX(), (int) plotArea.getY()));
    
        for (String annot : annotationMap.keySet()) {
            IAnnotation ia = annotationMap.get(annot);
            if (ia.isVisible()) {
                ia.draw(g2, plotArea, this);
            }
        }
    }

    public Insets getPlotInsets() {
        return plotInsets;
    }


    public Rectangle getPlotArea() {
        Insets insets = getInsets();
        Dimension size = getSize();

        Rectangle2D available = new Rectangle2D.Double(
                insets.left + plotInsets.left, insets.top + plotInsets.top,
                size.getWidth() - insets.left - insets.right - plotInsets.left - plotInsets.right,
                size.getHeight() - insets.top - insets.bottom - plotInsets.top - plotInsets.bottom
        );


        int maxDrawWidth = (int) available.getWidth();
        int maxDrawHeight = (int) available.getHeight();

        int drawWidth = maxDrawWidth;
        int drawHeight = maxDrawHeight;

        Anatomy3D anatomy = getDisplayAnatomy();

        double xspace = getModel().getImageSpace().getImageAxis(anatomy.XAXIS, true).getRange().getInterval();
        double yspace = getModel().getImageSpace().getImageAxis(anatomy.YAXIS, true).getRange().getInterval();

        double sx = maxDrawWidth / xspace;
        double sy = maxDrawHeight / yspace;

        if (isPreserveAspectRatio()) {
            double sxy = Math.min(sx, sy);

            drawWidth = (int) (sxy * xspace);
            drawHeight = (int) (sxy * yspace);

            plotSlack.left = (int) ((maxDrawWidth - drawWidth) / 2.0);
            plotSlack.right = (int) Math.round((maxDrawWidth - drawWidth) / 2.0);
            plotSlack.top = (int) ((maxDrawHeight - drawHeight) / 2.0);
            plotSlack.bottom = (int) Math.round((maxDrawHeight - drawHeight) / 2.0);

        } else {
            plotSlack.left = 0;
            plotSlack.right = 0;
            plotSlack.top = 0;
            plotSlack.bottom = 0;
        }


        plotArea = new Rectangle(
                insets.left + plotInsets.left + plotSlack.left, insets.top + plotInsets.top + plotSlack.top, drawWidth, drawHeight
        );


        return plotArea;
    }

    public double getScaleX() {
        return plotArea.getWidth() / getXAxisRange().getInterval();

    }

    public double getScaleY() {
        return plotArea.getHeight() / getYAxisRange().getInterval();
    }

    public void setPlotInsets(Insets insets) {
        plotInsets = insets;
    }

    public Anatomy3D getDisplayAnatomy() {
        return viewBounds.getDisplayAnatomy();

    }

    /*public void setDisplayAnatomy(Anatomy3D anatomy) {
        displayAnatomy = anatomy;
        xAxis = model.getImageAxis(displayAnatomy.XAXIS).getBounds();
        yAxis = model.getImageAxis(displayAnatomy.YAXIS).getBounds();
        repaint();
    }*/

    /*public void updateAxis(AxisRange range) {
        if (range.getAnatomicalAxis().sameAxis(xAxis.getAnatomicalAxis())) {
            if (!range.getAnatomicalAxis().sameDirection(xAxis.getAnatomicalAxis())) {
                setXAxisRange(range.flip());
            } else {
                setXAxisRange(range);
            }
        } else if (range.getAnatomicalAxis().sameAxis(yAxis.getAnatomicalAxis())) {
            if (!range.getAnatomicalAxis().sameDirection(yAxis.getAnatomicalAxis())) {
                setYAxisRange(range.flip());
            } else {
                setYAxisRange(range);
            }
        }
    } */

    public double getXExtent() {
        return viewBounds.getXExtent();
    }

    public double getYExtent() {
        return viewBounds.getYExtent();
    }

    /*public void updateAxes(AxisRange xrange, AxisRange yrange) {
        xAxis = xrange;
        yAxis = yrange;
        producer.reset();
        repaint();
    } */


    /*public void setXAxisRange(AxisRange xrange) {
        xAxis = xrange;

        producer.setXAxis(xrange);
        repaint();
    }

    public void setYAxisRange(AxisRange yrange) {
        yAxis = yrange;
        producer.setYAxis(yrange);
        repaint();
    }*/

    public Point translateAnatToScreen(SpatialLoc2D pt) {
        if (pt.getAnatomy().XAXIS != getXAxisRange().getAnatomicalAxis()) {
            throw new ImageAxis.IncompatibleAxisException("supplied point does not match image plot axes: " +
                    "X: " + pt.getAnatomy().XAXIS + " Y: " + pt.getAnatomy().YAXIS +
                    " Plot X: " + getXAxisRange().getAnatomicalAxis() +
                    " Plot Y: " + getYAxisRange().getAnatomicalAxis());
        }

        //Insets insets = getInsets();
        //Insets plotMargins = getPlotMargins();

        //int x = (int) (pt.getX().getValue() * getScaleX() + plotMargins.left + insets.left);
        //int y = (int) (pt.getY().getValue() * getScaleY() + plotMargins.top + insets.top);

        int x = (int) (pt.getX().getValue() * getScaleX() + plotArea.x);
        int y = (int) (pt.getY().getValue() * getScaleY() + plotArea.y);
        return new Point(x, y);
    }

    public void repaint() {
        super.repaint();
    }


    public SpatialLoc2D translateScreenToAnat(Point screenPoint) {


        double x = (screenPoint.getX() - plotArea.x) / getScaleX();
        double y = (screenPoint.getY() - plotArea.y) / getScaleY();



        return new SpatialLoc2D(Anatomy2D.matchAnatomy(
                getXAxisRange().getAnatomicalAxis(),
                getYAxisRange().getAnatomicalAxis()),
                x + getXAxisRange().getMinimum(),
                y + getYAxisRange().getMinimum());


    }


    public AxisRange getXAxisRange() {
        return viewBounds.getXRange();
    }

    public AxisRange getYAxisRange() {
        return viewBounds.getYRange();
    }

    public void setAnnotation(String name, IAnnotation annotation) {
        annotationMap.put(name, annotation);
        annotation.addPropertyChangeListener(annotationListener);
        repaint();
    }

    public IAnnotation getAnnotation(String name) {
        return annotationMap.get(name);
    }

    public void removeAnnotation(String name) {
        IAnnotation annotation = annotationMap.remove(name);
        if (annotation != null) {
            annotation.removePropertyChangeListener(annotationListener);
        }
    }

    public void clearAnnotations() {
        annotationMap.clear();
    }

    public Map<String, IAnnotation> getAnnotations() {
        return Collections.unmodifiableMap(annotationMap);
    }

    public void setName(String _name) {
        name = _name;
    }

    public String toString() {
        if (name != null) return name;
        else {
            return super.toString();
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension((int) getXExtent(), (int) getYExtent());
    }

    public static ComponentImagePlot createComponentImagePlot(ImageViewModel model, IImageProducer _producer, Anatomy3D displayAnatomy, AxisRange xAxis, AxisRange yAxis) {
        ComponentImagePlot plot = new ComponentImagePlot(model, _producer, new ViewBounds(model.getImageSpace(), displayAnatomy, xAxis, yAxis));
        plot.producer.setPlot(plot);
        return plot;
    }
}
