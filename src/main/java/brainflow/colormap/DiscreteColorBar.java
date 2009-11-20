package brainflow.colormap;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 13, 2006
 * Time: 11:51:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscreteColorBar extends AbstractColorBar {

    public static final int DEFAULT_WIDTH = 250;
    public static final int DEFAULT_HEIGHT = 300;


    public DiscreteColorBar(IColorMap _colorMap, int orientation) {
        super(_colorMap, orientation);
        initListener(_colorMap);
        
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = this.getSize();
        int width = (int) d.getWidth();
        int height = (int) d.getHeight();

        g2.clearRect(0, 0, width, height);
        AffineTransform at = null;


        if (cachedImage == null) {
            renderOffscreen();
        }
        if (getOrientation() == SwingConstants.VERTICAL) {
            at = AffineTransform.getScaleInstance((double) width / 10f, (double) height / DEFAULT_HEIGHT);
        }

        if (getOrientation() == SwingConstants.HORIZONTAL) {
            at = AffineTransform.getScaleInstance((double) width / DEFAULT_WIDTH, (double) height / (double) 10);
        }


        drawBackground(g2);
        g2.drawRenderedImage(cachedImage, at);
    }


    @Override
    protected BufferedImage renderOffscreen() {
        BufferedImage bimage = null;


        if (getOrientation() == SwingConstants.HORIZONTAL) {

            bimage = new BufferedImage(DEFAULT_WIDTH, 10, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = bimage.createGraphics();
            int ncolors = colorMap.getMapSize();
            double distance = colorMap.getMaximumValue() - colorMap.getMinimumValue();
            
            double start = colorMap.getMinimumValue();

            for (int i = 0; i < ncolors; i++) {
                ColorInterval ci = colorMap.getInterval(i);
                double size = ((ci.getMaximum() - ci.getMinimum()) / distance);
                double xpos = (ci.getMinimum() - start) / distance;


                Rectangle2D rect = new Rectangle2D.Double(xpos * DEFAULT_WIDTH, 0, size * DEFAULT_WIDTH, 10);
                float trans = (float) ci.getAlpha();

                AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, trans / 255f);

                g2d.setComposite(comp);
                g2d.setPaint(ci.getColor());
                g2d.fill(rect);
            }
        } else {
            bimage = new BufferedImage(10, DEFAULT_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
            int ncolors = colorMap.getMapSize();
            double distance = colorMap.getMaximumValue() - colorMap.getMinimumValue();
            double start = colorMap.getMinimumValue();

            Graphics2D g2d = bimage.createGraphics();
            for (int i = 0; i < ncolors; i++) {
                ColorInterval ci = colorMap.getInterval(i);
                double size = ((ci.getMaximum() - ci.getMinimum()) / distance);
                double ypos = (ci.getMinimum() - start) / distance;


                Rectangle2D rect = new Rectangle2D.Double(0, ypos * DEFAULT_HEIGHT, 10, size * DEFAULT_HEIGHT);
                float trans = (float) ci.getAlpha();

                AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, trans / 255f);

                g2d.setComposite(comp);
                g2d.setPaint(ci.getColor());
                g2d.fill(rect);

            }
        }

        cachedImage = bimage;
        return cachedImage;
    }

    public Dimension getPreferredSize() {
        if (getOrientation() == SwingConstants.VERTICAL) {
            return new Dimension(75, 256);
        }

        return new Dimension(DEFAULT_WIDTH, 50);
    }

    public static void main(String[] args) {
        DiscreteColorMap cmap = new DiscreteColorMap(new LinearColorMap2(0, 255, ColorTable.SPECTRUM));


        AbstractColorBar jp = cmap.createColorBar();
        //jp.setOrientation(SwingConstants.VERTICAL);

        JFrame jf = new JFrame();
        jf.add(jp);
        jf.pack();
        jf.setVisible(true);


    }
}
