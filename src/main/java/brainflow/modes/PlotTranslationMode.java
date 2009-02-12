package brainflow.modes;

import brainflow.core.ImageView;
import brainflow.core.BrainCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class PlotTranslationMode extends ImageCanvasMode {


    private Point dragBegin = null;
    private Point dragEnd = null;
    private Point currentLoc = null;
    private boolean dragging = false;

    private JComponent currentPane = null;
    private Rectangle lastShadow = null;

    public PlotTranslationMode(BrainCanvas _canvas) {
        super.setImageCanvas(_canvas);

    }


    public void mousePressed(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            ImageView iview = canvas.whichView((Component)event.getSource(), event.getPoint());
            if (iview == null) return;
            if (canvas.isSelectedView(iview)) {
                currentPane = iview;
                Timer timer = new Timer(100, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (dragging) {
                            canvas.setCursor(getCursor());
                        }
                    }
                });

                timer.setRepeats(false);
                timer.start();

                dragBegin = event.getLocationOnScreen();

            }
        }

    }

    public void mouseReleased(MouseEvent event) {

        if (dragging) {
            move(event);

        }


        dragging = false;
        currentPane = null;
        currentLoc = null;
        lastShadow = null;

        canvas.setCursor(Cursor.getDefaultCursor());


    }

    private void eraseShadow(Graphics2D g2) {
        if (lastShadow != null) {
            g2.drawRect(lastShadow.x, lastShadow.y, lastShadow.width, lastShadow.height);
        }
    }

    private void drag(MouseEvent event, JComponent currentPane) {

        Point paneLoc = currentPane.getLocationOnScreen();

        Point mousePoint = event.getLocationOnScreen();
        Point npoint = new Point((int) (mousePoint.getX() - dragBegin.getX()), (int) (mousePoint.getY() - dragBegin.getY()));

        Graphics2D g2 = (Graphics2D) canvas.getGraphics();

        g2.setXORMode(Color.white);

        if (currentLoc != null) {
            eraseShadow(g2);
        }


        Point origin = new Point(paneLoc.x + npoint.x, paneLoc.y + npoint.y);
        SwingUtilities.convertPointFromScreen(origin, canvas);
        g2.drawRect(origin.x, origin.y, currentPane.getWidth(), currentPane.getHeight());

        lastShadow = new Rectangle(origin.x, origin.y, currentPane.getWidth(), currentPane.getHeight());
        currentLoc = npoint;


    }

    private void move(MouseEvent event) {

        assert currentPane != null;
        Graphics2D g2 = (Graphics2D) canvas.getGraphics();
        g2.setXORMode(Color.white);
        eraseShadow(g2);

        Point paneLoc = currentPane.getLocationOnScreen();
        Point mousePoint = event.getLocationOnScreen();
        Point npoint = new Point((int) (mousePoint.getX() - dragBegin.getX()), (int) (mousePoint.getY() - dragBegin.getY()));
        Point origin = new Point(paneLoc.x + npoint.x, paneLoc.y + npoint.y);
        SwingUtilities.convertPointFromScreen(origin, currentPane.getParent());

        currentPane.setBounds(new Rectangle(origin.x, origin.y, currentPane.getWidth(), currentPane.getHeight()));


    }


    public Cursor getCursor() {
        return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    }


    public void mouseDragged(MouseEvent event) {
        if (currentPane != null && SwingUtilities.isLeftMouseButton(event)) {
            if (!dragging) {
                dragging = true;
                canvas.setCursor(getCursor());
            }
            drag(event, currentPane);
        }

    }

    public void mouseMoved(MouseEvent event) {

    }

    public void mouseEntered(MouseEvent event) {

    }

    public void mouseExited(MouseEvent event) {

    }

    public void mouseClicked(MouseEvent event) {
    }


}