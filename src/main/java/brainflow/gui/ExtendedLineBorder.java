package brainflow.gui;

import javax.swing.border.AbstractBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 27, 2004
 * Time: 2:35:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedLineBorder extends AbstractBorder {

    private Paint linePaint = new Color(22,22,22, 57);

    protected int thickness = 5;

    protected boolean roundedCorners = true;

    public ExtendedLineBorder() {
    }

    public ExtendedLineBorder(Paint _linePaint) {
        linePaint = _linePaint;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        Paint oldPaint = g2d.getPaint();
        
        g2d.setPaint(linePaint);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        BasicStroke stroke = new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2d.setStroke(stroke);
        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 2, 2);
        g2d.draw(rect);

        g2d.setPaint(oldPaint);
    }


    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }


    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = thickness;
        return insets;
    }


    public Paint getLinePaint() {
        return linePaint;
    }

    public void setThickness(int _thickness) {
        thickness = _thickness;

    }


    public int getThickness() {
        return thickness;
    }


    public boolean getRoundedCorners() {
        return roundedCorners;
    }


    public boolean isBorderOpaque() {
        return !roundedCorners;
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        JPanel jp = new JPanel();

        jf.getContentPane().setBackground(Color.RED);
        jp.setOpaque(false);
        jp.setPreferredSize(new Dimension(200,200));
        jp.setBorder(new ExtendedLineBorder());

        jf.setSize(500,500);
        jf.add(jp, BorderLayout.CENTER);
        jf.setVisible(true);



    }


}
