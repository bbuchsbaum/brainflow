package brainflow.gui;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import brainflow.utils.NumberUtils;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.io.Serializable;
import java.text.NumberFormat;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 12, 2008
 * Time: 8:10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class BiSlider extends JPanel implements MouseMotionListener, MouseListener {

    enum GRIPPER_STATE {
        LEFT_SELECTED,
        RIGHT_SELECTED,
        BOTH_SELECTED,
        NEITHER
    }

    private NumberRangeModel model;

    private Shape leftGripper;

    private Shape rightGripper;

    private float leftGripperLoc = 0;

    private float rightGripperLoc = 0;

    private GRIPPER_STATE gripperState = GRIPPER_STATE.NEITHER;

    protected transient ChangeEvent changeEvent = null;

    protected ChangeListener changeListener = createChangeListener();

    private int TOP_CUSHION = 20;

    private int BOTTOM_CUSHION = 2;

    private int LEFT_CUSHION = 8;

    private int RIGHT_CUSHION = 8;


    private NumberFormat labelFormatter = NumberFormat.getNumberInstance();

    private Map desktopHints;


    public BiSlider(NumberRangeModel model) {
        this.model = model;
        addMouseMotionListener(this);
        addMouseListener(this);

        leftGripperLoc = (float) ((model.getLowValue() - model.getMin()) / (model.getMax() - model.getMin()));
        rightGripperLoc = (float) ((model.getHighValue() - model.getMin()) / (model.getMax() - model.getMin()));

        model.addChangeListener(changeListener);

        labelFormatter.setMaximumFractionDigits(2);
        labelFormatter.setMinimumFractionDigits(0);

        Toolkit tk = Toolkit.getDefaultToolkit();
        desktopHints = (Map) tk.getDesktopProperty("awt.font.desktophints");


    }


    private class ModelListener implements ChangeListener, Serializable {
        public void stateChanged(ChangeEvent e) {

            updateGripperPositions();
            fireStateChanged();
        }
    }


    protected ChangeListener createChangeListener() {
        return new ModelListener();
    }


    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }


    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }


    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(
                ChangeListener.class);
    }


    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {

            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    /* public void setHighValue(double n) {
      NumberRangeModel m = getModel();
      double oldValue = m.getHighValue();
      if (NumberUtils.equals(oldValue, n, .0001)) {
          return;
      }
      // m.setHighValue(n);

      if (accessibleContext != null) {
          accessibleContext.firePropertyChange(
                  AccessibleContext.ACCESSIBLE_VALUE_PROPERTY,
                  new Integer(oldValue),
                  new Integer(m.evaluate()));
      }
  }  */


    public NumberRangeModel getModel() {
        return model;
    }

    public void setModel(NumberRangeModel newModel) {
        NumberRangeModel oldModel = getModel();

        if (oldModel != null) {
            oldModel.removeChangeListener(changeListener);
        }

        model = newModel;

        if (newModel != null) {
            newModel.addChangeListener(changeListener);


        }

        firePropertyChange("model", oldModel, model);
    }


    private int getGripperRadius() {

        int height = getAdjustedHeight();
        double xpart = 12;
        double ypart = .1 * (Math.pow(height, 1.3));


        int radius = (int) (xpart + ypart);
        if (radius > 100) radius = 100;

        return radius;

    }

    private int getAdjustedHeight() {
        Insets insets = getInsets();

        int height = getHeight() - (insets.top + insets.bottom + TOP_CUSHION + BOTTOM_CUSHION);
        return height;

    }

    private int getAdjustedWidth() {
        Insets insets = getInsets();

        int width = getWidth() - (insets.left + insets.right + LEFT_CUSHION + RIGHT_CUSHION);
        return width;

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        int width = getAdjustedWidth();
        int height = getAdjustedHeight();

        if (height < 0) return;
        if (width < 0) return;

        int x0 = getInsets().left + LEFT_CUSHION;
        int y0 = getInsets().top + TOP_CUSHION;


        int hubRadius = getGripperRadius();

        // Create a translucent intermediate image in which we can perform
        // the soft clipping
        GraphicsConfiguration gc = g2.getDeviceConfiguration();
        BufferedImage img = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        Graphics2D og = img.createGraphics();

        // Clear the image so all pixels have zero alpha
        og.setComposite(AlphaComposite.Clear);
        og.fillRect(0, 0, width, height);


        Rectangle2D innerRect = new Rectangle(hubRadius, 0, width - (hubRadius * 2), height);
        Shape leftHub = new Ellipse2D.Double(0, 0, hubRadius * 2, height);
        Shape rightHub = new Ellipse2D.Double(width - (hubRadius * 2), 0, hubRadius * 2, height);

        Area area = new Area(leftHub);
        area.add(new Area(rightHub));
        area.add(new Area(innerRect));

        // Render our clip shape into the image.  Note that we enable
        // antialiasing to achieve the soft clipping effect.  Try
        // commenting out the line that enables antialiasing, and
        // you will see that you end up with the usual hard clipping.
        og.setComposite(AlphaComposite.Src);
        og.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        og.setColor(Color.WHITE);
        og.fill(area);

        // Here's the trick... We use SrcAtop, which effectively uses the
        // alpha value as a coverage value for each pixel stored in the
        // destination.  For the areas outside our clip shape, the destination
        // alpha will be zero, so nothing is rendered in those areas.  For
        // the areas inside our clip shape, the destination alpha will be fully
        // opaque, so the full color is rendered.  At the edges, the original
        // antialiasing is carried over to give us the desired soft clipping
        // effect.

        Color lg = new Color(200, 200, 200);
        Color bg = new Color(111, 111, 111);

        og.setComposite(AlphaComposite.SrcAtop);
        GradientPaint gpaint = new GradientPaint(0, height, lg, 0, 0, bg);
        og.setPaint(gpaint);
        og.fill(area);


        leftGripper  = createLeftArc(leftGripperLoc * getTrackWidth(), 0, hubRadius * 2);
        rightGripper = createRightArc(rightGripperLoc * getTrackWidth(), 0, hubRadius * 2);


        paintGripper(leftGripper, og);
        paintGripper(rightGripper, og);

        og.dispose();

        g2.drawImage(img, x0, y0, null);

        paintValueLabels(g2);


    }

    private void paintLeftLabel(Graphics2D g2) {
        if (gripperState == GRIPPER_STATE.LEFT_SELECTED || gripperState == GRIPPER_STATE.BOTH_SELECTED) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD));
        } else {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN));
        }

        if (desktopHints != null) {
            g2.addRenderingHints(desktopHints);
        }
        g2.setColor(Color.BLACK);
        Rectangle bounds = leftGripper.getBounds();

        FontMetrics fmetric = g2.getFontMetrics(getFont());
        String valueLabel = labelFormatter.format(model.getLowValue());
        Rectangle2D labelBounds = fmetric.getStringBounds(valueLabel, g2);
        g2.drawString(valueLabel, (int) (LEFT_CUSHION + getInsets().left + bounds.x + bounds.width / 2 - labelBounds.getWidth()), (int) (getInsets().top + TOP_CUSHION - labelBounds.getHeight() / 2));


    }

    private void paintRightLabel(Graphics2D g2) {
        if (gripperState == GRIPPER_STATE.RIGHT_SELECTED || gripperState == GRIPPER_STATE.BOTH_SELECTED) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD));
        } else {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN));
        }

        if (desktopHints != null) {
            g2.addRenderingHints(desktopHints);
        }
        g2.setColor(Color.BLACK);
        Rectangle bounds = rightGripper.getBounds();

        FontMetrics fmetric = g2.getFontMetrics(getFont());
        String valueLabel = labelFormatter.format(model.getHighValue());
        Rectangle2D labelBounds = fmetric.getStringBounds(valueLabel, g2);
        g2.drawString(valueLabel, LEFT_CUSHION + getInsets().left + bounds.x + bounds.width / 2, (int) (getInsets().top + TOP_CUSHION - labelBounds.getHeight() / 2));


    }

    private void paintValueLabels(Graphics2D g2) {
        paintLeftLabel(g2);
        if (!NumberUtils.equals(model.getHighValue(), model.getLowValue(), .0001))
            paintRightLabel(g2);


    }


    private Shape createLeftArc(double x0, double y0, double radius) {
        Arc2D arc = new Arc2D.Double(x0, y0, radius, getAdjustedHeight(), 90, 180, Arc2D.CHORD);
        return arc;
    }

    private Shape createRightArc(double x0, double y0, double radius) {
        Arc2D arc = new Arc2D.Double(x0, y0, radius, getAdjustedHeight(), 270, 180, Arc2D.CHORD);
        return arc;
    }

    private void paintGripper(Shape gripper, Graphics2D og) {
        Rectangle bounds = gripper.getBounds();
                og.setPaint(new GradientPaint(bounds.x, bounds.y, Color.BLACK, bounds.x, bounds.y + bounds.height, Color.DARK_GRAY.brighter().brighter()));
                og.fill(gripper);
                og.setPaint(Color.WHITE.darker());
                og.drawLine(bounds.x + (int) (bounds.getWidth() / 2.0), bounds.y, bounds.x + (int) (bounds.getWidth() / 2.0), bounds.y + bounds.height);


    }

   
    private double getTrackWidth() {
        Insets insets = getInsets();
        return getWidth() - (insets.left + insets.right + LEFT_CUSHION + RIGHT_CUSHION) - getGripperRadius() * 2;

    }

    private double computeLowValue() {
        return leftGripperLoc * (model.getMax() - model.getMin()) + model.getMin();
    }

    private double computeHighValue() {
        return rightGripperLoc * (model.getMax() - model.getMin()) + model.getMin();
    }

    private void updateGripperPositions() {
        float leftLoc = (float) ((model.getLowValue() - model.getMin()) / (model.getMax() - model.getMin()));
        float rightLoc = (float) ((model.getHighValue() - model.getMin()) / (model.getMax() - model.getMin()));

        if (!NumberUtils.equals(leftLoc, leftGripperLoc, .00001) || !NumberUtils.equals(rightLoc, rightGripperLoc, .00001)) {
            leftGripperLoc = leftLoc;
            rightGripperLoc = rightLoc;
            repaint();
        }
    }

    private void translateSelectedGripper(double tx) {
        double perc = tx / getTrackWidth();
        switch (gripperState) {

            case BOTH_SELECTED:
                leftGripperLoc = Math.max(leftGripperLoc + (float) perc, 0);
                leftGripperLoc = Math.min(leftGripperLoc, 1);
                rightGripperLoc = Math.max(rightGripperLoc + (float) perc, 0);
                rightGripperLoc = Math.min(rightGripperLoc, 1);
                repaint();
                model.setRangeProperties(computeLowValue(), computeHighValue(), model.getMin(), model.getMax(), false);
                break;

            case LEFT_SELECTED:

                leftGripperLoc = Math.max(leftGripperLoc + (float) perc, 0);
                leftGripperLoc = Math.min(leftGripperLoc, 1);
                leftGripperLoc = Math.min(leftGripperLoc, rightGripperLoc);
                repaint();
                model.setRangeProperties(computeLowValue(), computeHighValue(), model.getMin(), model.getMax(), false);
                break;
            case RIGHT_SELECTED:
                rightGripperLoc = Math.max(rightGripperLoc + (float) perc, 0);
                rightGripperLoc = Math.min(rightGripperLoc, 1);
                rightGripperLoc = Math.max(rightGripperLoc, leftGripperLoc);
                repaint();
                model.setRangeProperties(computeLowValue(), computeHighValue(), model.getMin(), model.getMax(), false);
                break;
            case NEITHER:
                break;
        }

    }

    private Point lastPoint;

    public void mouseDragged(MouseEvent e) {
        if (gripperState != GRIPPER_STATE.NEITHER) {
            Point newPoint = e.getPoint();
            double transx = newPoint.getX() - lastPoint.getX();
            lastPoint = newPoint;
            translateSelectedGripper(transx);


        }


    }

    private JidePopup clickPopup;

    private JFormattedTextField clickField;

    private JidePopup getClickPopup() {
        if (clickPopup == null) {
            clickPopup = new JidePopup();
            final NumberFormat format = NumberFormat.getNumberInstance();
            format.setMaximumFractionDigits(2);
            format.setRoundingMode(RoundingMode.UP);
            clickField = new JFormattedTextField(format);
            clickField.setColumns(15);
            clickField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    double val = ((Number) clickField.getValue()).doubleValue();
                    if (model.inBounds(val)) {
                        switch (gripperState) {
                            case LEFT_SELECTED:
                                model.setLowValue(val);
                                break;
                            case RIGHT_SELECTED:
                                model.setHighValue(val);
                                break;
                            case BOTH_SELECTED:
                                break;
                            case NEITHER:
                                break;
                        }
                    }
                    clickPopup.hidePopup();
                }
            });
            clickField.setEditable(true);

            JPanel panel = new JPanel();
            panel.setBackground(clickField.getBackground());
            panel.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel.setLayout(new BorderLayout());
            panel.add(clickField, BorderLayout.CENTER);
            clickPopup.setDefaultFocusComponent(clickField);
            clickPopup.getContentPane().setLayout(new BorderLayout());
            clickPopup.getContentPane().add(panel, BorderLayout.CENTER);
            clickPopup.setOwner(this);


        }

        return clickPopup;

    }

    private void maybeHidePopup(Point p) {
        if (clickPopup != null) {
            if (clickPopup.isShowing()) {
                clickPopup.hidePopup();
            }
        }


    }

    public void mouseClicked(MouseEvent e) {
        gripperState = whichGripper(e.getPoint());
        if (gripperState != GRIPPER_STATE.NEITHER && e.getClickCount() == 2) {
            JidePopup popup = getClickPopup();
            popup.updateUI();
            if (popup.isShowing()) {
                popup.hidePopup();
            } else {
                popup.setOwner(this);
                popup.setResizable(false);
                popup.setMovable(false);
                popup.setAttachable(false);
                popup.setDefaultFocusComponent(clickField);

                if (gripperState == GRIPPER_STATE.LEFT_SELECTED) {
                    clickField.setValue(model.getLowValue());
                } else if (gripperState == GRIPPER_STATE.RIGHT_SELECTED) {
                    clickField.setValue(model.getHighValue());
                }

                Point screenPoint = e.getPoint();
                SwingUtilities.convertPointToScreen(screenPoint, (Component) e.getSource());
                popup.showPopup(screenPoint.x, screenPoint.y);
                popup.requestFocus();
            }


        }
    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
        gripperState = GRIPPER_STATE.NEITHER;    
        lastPoint = null;
        repaint();

    }


    private GRIPPER_STATE whichGripper(Point p) {
        Insets insets = getInsets();
        // because shapes are actually drawn on an offscreen image which is offset by insets.
        p.setLocation(p.x - insets.left - LEFT_CUSHION, p.y - insets.top - TOP_CUSHION);
        if (leftGripper.contains(p.x, p.y)) {
            return GRIPPER_STATE.LEFT_SELECTED;
        } else if (rightGripper.contains(p.x, p.y)) {
            return GRIPPER_STATE.RIGHT_SELECTED;
        } else
        if (p.x > leftGripper.getBounds().x && p.x < (rightGripper.getBounds().x + rightGripper.getBounds().width)) {
            return GRIPPER_STATE.BOTH_SELECTED;
        } else {
            return GRIPPER_STATE.NEITHER;
        }


    }

    public void mousePressed(MouseEvent e) {
        gripperState = whichGripper(e.getPoint());
        if (gripperState != GRIPPER_STATE.NEITHER) {
            lastPoint = e.getPoint();
        }

        maybeHidePopup(e.getPoint());

    }

    public void mouseMoved(MouseEvent e) {

    }

    public Dimension getPreferredSize() {
        return new Dimension(300, 50);
    }

    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFrame", "7.YTcWgxxjx1xjSnUqG:U1ldgGetfRn1");
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        LookAndFeelFactory.installJideExtension();

        JFrame jf = new JFrame();


        BiSlider bslider = new BiSlider(new NumberRangeModel(0, 100, -255, 255));
        bslider.setBorder(new EmptyBorder(10, 5, 10, 5));
        bslider.setPreferredSize(new Dimension(300, 60));

        jf.add(bslider, BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }
}
