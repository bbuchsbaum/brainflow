package brainflow.core.annotations;

import brainflow.colormap.AbstractColorBar;
import brainflow.core.layer.AbstractLayer;
import brainflow.core.IImagePlot;
import brainflow.core.ImageViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 6, 2007
 * Time: 2:01:03 PM
 */
public class ColorBarAnnotation extends AbstractAnnotation {

    public static final String ID = "color bar";

    public static final String ORIENTATION_PROPERTY = "orientation";

    public static final String POSITION_PROPERTY = "position";

    public static final String BAR_SIZE_PROPERTY = "barSize";

    public static final String BAR_LENGTH_PROPERTY = "barLength";

    public static final String MARGIN_PROPERTY = "margin";


    private AbstractLayer selectedLayer;

    private ImageViewModel model;

    private int orientation = SwingUtilities.VERTICAL;


    private int barSize = 30;

    private double barLength = 1f;

    private int margin = 20;


    private AbstractColorBar colorBar = null;


    public ColorBarAnnotation(ImageViewModel _model) {
        model = _model;

        BeanContainer.get().addListener(model.layerSelection, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                Integer sel = (Integer)newValue;
                if (isVisible()) {
                    selectedLayer = ColorBarAnnotation.this.model.get(sel);

                }
            }
        });
       
    }


    public final String getIdentifier() {
        return ColorBarAnnotation.ID;
    }

    public int getBarSize() {
        return barSize;
    }

    public void setBarSize(int barSize) {
        int old = this.barSize;
        this.barSize = barSize;
        support.firePropertyChange(ColorBarAnnotation.BAR_SIZE_PROPERTY, old, getBarSize());
    }

    public double getBarLength() {
        return barLength;
    }

    public void setBarLength(double barLength) {
        double old = getBarLength();
        this.barLength = barLength;

        support.firePropertyChange(ColorBarAnnotation.BAR_LENGTH_PROPERTY, old, getBarLength());
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        int old = getMargin();
        this.margin = margin;
        support.firePropertyChange(ColorBarAnnotation.MARGIN_PROPERTY, old, getMargin());
    }

    public IAnnotation safeCopy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (!isVisible() || model.size() == 0) return;


        AbstractLayer layer = model.get(model.getSelectedIndex());

        if (colorBar == null || layer != selectedLayer) {
            selectedLayer = layer;
            colorBar = selectedLayer.getImageLayerProperties().getColorMap().createColorBar();
            colorBar.setOrientation(orientation);
            colorBar.setDrawOutline(true);
        } else if (colorBar.getColorMap() != selectedLayer.getImageLayerProperties().getColorMap()) {
            colorBar = selectedLayer.getImageLayerProperties().getColorMap().createColorBar();
            colorBar.setOrientation(orientation);
        }

        if (orientation == SwingUtilities.VERTICAL) {
            drawVertical(g2d, plot.getComponent().getBounds());
        } else {
            drawHorizontal(g2d);
        }

        selectedLayer = layer;

    }

    private void drawVertical(Graphics2D g2d, Rectangle bounds) {

        int roomx = (int) bounds.getWidth() - margin;
        int roomy = (int) bounds.getHeight() - (margin * 2);

        int width = Math.min(roomx, barSize);
        int height = (int) (barLength * roomy);


        int xmin = (int) bounds.getWidth() - width - margin;
        int ymin = margin;

        /*AffineTransform xform = AffineTransform.getTranslateInstance(xmin, ymin);
        xform.scale((float) width / (float) colorBarImage.getWidth(), (float) height / (float) colorBarImage.getHeight());
        g2d.drawRenderedImage(colorBarImage, xform);  */

        colorBar.paintInto(g2d, xmin, ymin, width, height, false);

    }

    private void drawHorizontal(Graphics2D g2d) {
        Rectangle2D cbounds = g2d.getClipBounds();

        int roomx = (int) cbounds.getWidth() - margin * 2;
        int roomy = (int) cbounds.getHeight() - (margin);

        int width = (int) (barLength * roomx);
        int height = Math.min(roomy, barSize);


        int xmin = margin;
        int ymin = (int) cbounds.getHeight() - height - margin;

        /*AffineTransform xform = AffineTransform.getTranslateInstance(xmin, ymin);
        xform.scale((float) width / (float) colorBarImage.getWidth(), (float) height / (float) colorBarImage.getHeight());
        g2d.drawRenderedImage(colorBarImage, xform);  */

        colorBar.paintInto(g2d, xmin, ymin, width, height, false);

    }


    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        int old = getOrientation();
        this.orientation = orientation;

        support.firePropertyChange(ColorBarAnnotation.ORIENTATION_PROPERTY, old, getOrientation());
    }
}
