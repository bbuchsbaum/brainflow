package brainflow.utils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2006
 * Time: 11:39:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class HandledBorder extends AbstractBorder {


    private Paint selectedColor = Color.RED;
    private Paint preselectedColor = Color.PINK;
    private Paint handleColor = Color.ORANGE;
    private Paint handleBackground = Color.WHITE;

    private int handleDim = 5;
    private boolean selected = false;
    private boolean preSelected = false;

    private Map<IResizeableBorder.HANDLE_LOCATION, Rectangle2D> handles = new HashMap<IResizeableBorder.HANDLE_LOCATION, Rectangle2D>();
    private Rectangle previousBounds = new Rectangle(0, 0, 0, 0);

    private float alpha = .5f;


    public static Border createSelectedHandledBorder(int _outerMargin, int _innerMargin) {
        EmptyBorder innerMargin = new EmptyBorder(_innerMargin, _innerMargin, _innerMargin, _innerMargin);
        EmptyBorder outerMargin = new EmptyBorder(_outerMargin, _outerMargin, _outerMargin, _outerMargin);
        HandledBorder border = new HandledBorder();
        border.setSelected(true);
        CompoundBorder cborder = new CompoundBorder(outerMargin, new CompoundBorder(border, innerMargin));
        return cborder;

    }

    public static Border createPreSelectedHandledBorder(int _outerMargin, int _innerMargin) {
        EmptyBorder innerMargin = new EmptyBorder(_innerMargin, _innerMargin, _innerMargin, _innerMargin);
        EmptyBorder outerMargin = new EmptyBorder(_outerMargin, _outerMargin, _outerMargin, _outerMargin);
        HandledBorder border = new HandledBorder();
        border.setPreSelected(true);
        CompoundBorder cborder = new CompoundBorder(outerMargin, new CompoundBorder(border, innerMargin));
        return cborder;

    }

    public static Border createUnSelectedHandledBorder(int _outerMargin, int _innerMargin) {
        EmptyBorder innerMargin = new EmptyBorder(_innerMargin, _innerMargin, _innerMargin, _innerMargin);
        EmptyBorder outerMargin = new EmptyBorder(_outerMargin, _outerMargin, _outerMargin, _outerMargin);
        HandledBorder border = new HandledBorder();
        border.setPreSelected(true);
        CompoundBorder cborder = new CompoundBorder(outerMargin, new CompoundBorder(border, innerMargin));
        return cborder;

    }

    protected HandledBorder() {
        super();
    }


    public boolean onHandle(Point p) {
        for (Rectangle2D rect : handles.values()) {
            if (rect.contains(p)) {
                return true;
            }

        }

        return false;

    }

    public IResizeableBorder.HANDLE_LOCATION getHandle(Point p) {

        if (handles.size() == 0) {
            return IResizeableBorder.HANDLE_LOCATION.NULL;
        }

        for (IResizeableBorder.HANDLE_LOCATION loc : handles.keySet()) {
            Rectangle2D rect = handles.get(loc);
            if (rect.contains(p)) {
                return loc;
            }

        }

        return IResizeableBorder.HANDLE_LOCATION.NULL;

    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (isSelected()) {
            paintSelected(c, g, x, y, width, height);
        } else if (isPreSelected()) {
            paintPreSelected(c, g, x, y, width, height);
        }

    }

    private void paintPreSelected(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle bounds = new Rectangle(x, y, width, height);
        if (!bounds.equals(previousBounds) || handles.size() != 8)
            createHandles(x, y, width, height);

        Composite oldComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
        Paint oldPaint = g2.getPaint();
        g2.setPaint(preselectedColor);
        g2.drawRect(x, y, width - 1, height - 1);


        for (Rectangle2D rect : handles.values()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
            g2.setPaint(handleColor);
            g2.fill(rect);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            g2.setPaint(handleBackground);
            g2.draw(rect);
        }

        g2.setPaint(oldPaint);
        g2.setComposite(oldComposite);


    }

    private void paintSelected(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle bounds = new Rectangle(x, y, width, height);
        if (!bounds.equals(previousBounds) || handles.size() != 8)
            createHandles(x, y, width, height);

        Paint oldPaint = g2.getPaint();
        g2.setPaint(selectedColor);
        g.drawRect(x, y, width - 1, height - 1);


        for (Rectangle2D rect : handles.values()) {
            g2.setPaint(handleColor);
            g2.fill(rect);
            g2.setPaint(handleBackground);
            g2.draw(rect);
        }

        g2.setPaint(oldPaint);


    }


    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = handleDim;
        insets.right = handleDim;
        insets.top = handleDim;
        insets.bottom = handleDim;
        return insets;
    }

    private void createHandles(int x, int y, int width, int height) {
        handles.clear();
        double halfWidth = (handleDim - 1) / 2f;
        Rectangle2D uleft = new Rectangle2D.Double(x - halfWidth, y - halfWidth, handleDim, handleDim);
        Rectangle2D uright = new Rectangle2D.Double(x + width - halfWidth - 1, y - halfWidth, handleDim, handleDim);
        Rectangle2D umiddle = new Rectangle2D.Double(x + (width / 2) - halfWidth - 1, y - halfWidth, handleDim, handleDim);
        Rectangle2D sideleft = new Rectangle2D.Double(x - halfWidth, y - halfWidth + (height / 2), handleDim, handleDim);
        Rectangle2D sideright = new Rectangle2D.Double(x + width - halfWidth - 1, y - halfWidth + (height / 2), handleDim, handleDim);

        Rectangle2D bleft = new Rectangle2D.Double(x - halfWidth, y + height - halfWidth - 1, handleDim, handleDim);
        Rectangle2D bright = new Rectangle2D.Double(x + width - halfWidth - 1, y + height - halfWidth - 1, handleDim, handleDim);
        Rectangle2D bmiddle = new Rectangle2D.Double(x + (width / 2) - halfWidth - 1, y + height - halfWidth - 1, handleDim, handleDim);

        handles.put(IResizeableBorder.HANDLE_LOCATION.TOP_LEFT, uleft);
        handles.put(IResizeableBorder.HANDLE_LOCATION.TOP_RIGHT, uright);
        handles.put(IResizeableBorder.HANDLE_LOCATION.TOP_MIDDLE, umiddle);
        handles.put(IResizeableBorder.HANDLE_LOCATION.SIDE_LEFT, sideleft);
        handles.put(IResizeableBorder.HANDLE_LOCATION.SIDE_RIGHT, sideright);
        handles.put(IResizeableBorder.HANDLE_LOCATION.BOTTOM_LEFT, bleft);
        handles.put(IResizeableBorder.HANDLE_LOCATION.BOTTOM_RIGHT, bright);
        handles.put(IResizeableBorder.HANDLE_LOCATION.BOTTOM_MIDDLE, bmiddle);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        preSelected = false;
        if (!selected)
            handles.clear();
    }

    public Paint getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Paint selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Paint getHandleColor() {
        return handleColor;
    }

    public void setHandleColor(Paint handleColor) {
        this.handleColor = handleColor;
    }

    public int getHandleDim() {
        return handleDim;
    }

    public void setHandleDim(int handleDim) {
        this.handleDim = handleDim;
    }

    public Paint getHandleBackground() {
        return handleBackground;
    }

    public void setHandleBackground(Paint handleBackground) {
        this.handleBackground = handleBackground;
    }

    public boolean isPreSelected() {
        return preSelected;
    }

    public void setPreSelected(boolean preSelected) {
        this.preSelected = preSelected;
        selected = false;
    }


    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setSize(400, 400);
        JPanel panel = new JPanel();
        panel.setSize(256, 256);
        panel.setLocation(20, 20);

        Border margin = new EmptyBorder(5, 5, 5, 5);

        HandledBorder border = new HandledBorder();
        border.setPreSelected(true);
        panel.setBorder(new CompoundBorder(margin, border));
        //panel.setOpaque(true);
        //panel.setBackground(Color.GREEN);

        jf.setLayout(null);
        jf.add(panel);
        //jf.pack();
        jf.setVisible(true);


    }


}
