package brainflow.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 15, 2004
 * Time: 3:54:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class GradientJPanel extends JPanel {

    private Color colorTwo = Color.BLUE;

    private Color colorOne = Color.WHITE;

    private GradientPaint gp;

    private int cachedHeight = 0;

    private int cachedWidth = 0;

    private float alpha = .5f;

    private int gradientOrientation;


    public GradientJPanel(Color c1, Color c2, int _gradientOrientation) {
        colorOne = c1;
        colorTwo = c2;

        gradientOrientation = _gradientOrientation;


    }

    private GradientPaint makeGradientPaint() {
        if (gradientOrientation == SwingConstants.VERTICAL) {
            gp = new GradientPaint(0, 0, colorOne, 0, getHeight(), colorTwo);
        } else if (gradientOrientation == SwingConstants.HORIZONTAL) {
            gp = new GradientPaint(0, 0, colorOne, getWidth(), 0, colorTwo);
        }

        return gp;

    }

    public void setAlpha(float _alpha) {
        assert _alpha >= 0 && _alpha <= 1;
        alpha = _alpha;
        repaint();
    }

    public void setColors(Color c1, Color c2) {
        colorOne = c1;
        colorTwo = c2;
        gp = makeGradientPaint();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cachedHeight != getHeight() | cachedWidth != getWidth()) {
            cachedHeight = getHeight();
            cachedWidth = getWidth();
            gp = makeGradientPaint();
        }
        if (isOpaque()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(gp);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.fillRect(0, 0, cachedWidth, cachedHeight);
        }
    }

}
