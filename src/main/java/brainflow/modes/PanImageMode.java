package brainflow.modes;

import brainflow.core.ImageView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 15, 2007
 * Time: 9:38:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class PanImageMode extends ImageCanvasMode {

    private Point dragBegin = null;

    private boolean dragging;
    private Point lastPoint = null;
    private ImageView selectedView;


    private final int LEFT_CONTROL_MASK = MouseEvent.BUTTON1_DOWN_MASK + MouseEvent.CTRL_MASK;


    public void mousePressed(MouseEvent event) {
        if ((event.getModifiers() & LEFT_CONTROL_MASK) == LEFT_CONTROL_MASK) {
            ImageView iview = canvas.whichView((Component) event.getSource(), event.getPoint());
            if (iview == null) return;
            if (canvas.isSelectedView(iview)) {
                lastPoint = event.getPoint();
                selectedView = canvas.getSelectedView();
                Timer timer = new Timer(20, new ActionListener() {
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

    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void mouseDragged(MouseEvent event) {

        if (selectedView == null) return;

        Point mousePoint = event.getPoint();
        assert lastPoint != null;

        double shiftx = mousePoint.getX() - lastPoint.getX();
        double shifty = mousePoint.getY() - lastPoint.getY();
        //Viewport3D viewport = selectedView.getModel().getDisplayParameters().getViewport().getProperty();

    }

    public Cursor getCursor() {
        return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    }

}
