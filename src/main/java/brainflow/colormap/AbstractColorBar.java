package brainflow.colormap;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Apr 28, 2005
 * Time: 8:04:22 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractColorBar extends JPanel {

    private int orientation = SwingConstants.VERTICAL;

    protected BufferedImage cachedImage = null;

    protected BufferedImage backgroundImage = null;

    protected BufferedImage cachedBackgroundImage = null;

    private PropertyChangeListener mapListener;

    protected IColorMap colorMap;


    private boolean drawOutline;

    private Paint outlineColor = Color.WHITE;

    protected AbstractColorBar(IColorMap _colorMap, int orientation) {
        this.orientation = orientation;
        colorMap = _colorMap;
        initListener(colorMap);
        initBackground();
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }


    public boolean isDrawOutline() {
        return drawOutline;
    }

    public void setDrawOutline(boolean drawOutline) {
        this.drawOutline = drawOutline;
    }

    public BufferedImage getImage() {
        if (cachedImage == null) {
            renderOffscreen();
        }
        return cachedImage;
    }


    protected void initBackground() {
        /*URL url = getClass().getClassLoader().getResource("resources/icons/checkerboard.jpg");
        BufferedImage icon = null;
        try {
            icon = ImageIO.read(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setBackgroundImage(icon);
        */

    }


    public void paintInto(Graphics g, int x, int y, int width, int height, boolean paintBackground) {
        Graphics2D g2 = (Graphics2D) g;
        
        AffineTransform at = null;


        if (cachedImage == null) {
            renderOffscreen();
        }
        if (getOrientation() == SwingConstants.VERTICAL) {
            at = AffineTransform.getTranslateInstance(x, y);
            at.scale((double) width / 10f, (double) height / colorMap.getMapSize());

        }

        if (getOrientation() == SwingConstants.HORIZONTAL) {
            at = AffineTransform.getTranslateInstance(x, y);
            at.scale((double) width / (double) colorMap.getMapSize(), (double) height / (double) 10);

        }

        if (paintBackground) {
            drawBackground(g2);
        }


        g2.drawRenderedImage(cachedImage, at);

        if (isDrawOutline()) {
            Paint op = g2.getPaint();
            g2.setPaint(outlineColor);
            g2.drawRect(x, y, width, height);
            g2.setPaint(op);
        }


    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = this.getSize();
        Insets insets = getInsets();

        int width = (int) d.getWidth() - (insets.left + insets.right);
        int height = (int) d.getHeight() - (insets.top + insets.bottom);


        g2.clearRect(insets.left, insets.top, width, height);
        AffineTransform at = null;


        if (cachedImage == null) {
            renderOffscreen();
        }
        if (getOrientation() == SwingConstants.VERTICAL) {
            at = AffineTransform.getTranslateInstance(insets.left, insets.top);
            at.scale((double) width / 10f, (double) height / colorMap.getMapSize());

        }

        if (getOrientation() == SwingConstants.HORIZONTAL) {
            at = AffineTransform.getTranslateInstance(insets.left, insets.top);
            at.scale((double) width / (double) colorMap.getMapSize(), (double) height / (double) 10);

        }


        drawBackground(g2);
        g2.drawRenderedImage(cachedImage, at);
    }


    public void drawBackground(Graphics2D g2) {
        Dimension d = this.getSize();
        Insets insets = getInsets();

        int width = (int) d.getWidth() - (insets.left + insets.right);
        int height = (int) d.getHeight() - (insets.top + insets.bottom);


        if (backgroundImage == null) {
            GradientPaint gpaint = new GradientPaint(0, 0, Color.BLACK, 5, 0, Color.WHITE, true);
            Paint oldPaint = g2.getPaint();
            g2.setPaint(gpaint);
            g2.fillRect(insets.left, insets.top, width, height);
            g2.setPaint(oldPaint);
        } else if ((cachedBackgroundImage.getWidth() == width) && (cachedBackgroundImage.getHeight() == height)) {
            g2.drawRenderedImage(cachedBackgroundImage, AffineTransform.getTranslateInstance(insets.left, insets.top));
        } else {
            AffineTransform at = AffineTransform.getTranslateInstance(insets.left, insets.top);
            at.scale((double) width / backgroundImage.getWidth(),
                    (double) height / backgroundImage.getHeight());

            BufferedImageOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
            cachedBackgroundImage = op.filter(backgroundImage, null);
            g2.drawRenderedImage(cachedBackgroundImage, AffineTransform.getTranslateInstance(0, 0));

        }
    }


    protected void initListener(IColorMap imap) {
        // /
        //if (colorMap != null) {
        //    colorMap.removePropertyChangeListener(mapListener);
        //}

        colorMap = imap;

        mapListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {

                renderOffscreen();
                repaint();

            }
        };

        //colorMap.addPropertyChangeListener(mapListener);


    }

    public IColorMap getColorMap() {
        return colorMap;
    }

    public void setColorMap(IColorMap imap) {
        initListener(imap);
        renderOffscreen();
        repaint();

    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
        cachedBackgroundImage = backgroundImage;
        repaint();
    }

    protected abstract BufferedImage renderOffscreen();


}
