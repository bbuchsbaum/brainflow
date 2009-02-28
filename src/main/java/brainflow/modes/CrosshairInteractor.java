package brainflow.modes;

import brainflow.core.ImageView;
import brainflow.core.Viewport3D;
import brainflow.image.anatomy.AnatomicalPoint3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 29, 2006
 * Time: 5:34:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairInteractor extends ImageViewInteractor {

    private static final Logger log = Logger.getLogger(CrosshairInteractor.class.getName());

    private boolean dragging = false;


    public CrosshairInteractor(ImageView view) {
        super(view);
    }


    public CrosshairInteractor() {
    }

    public void mousePressed(MouseEvent event) {
        System.out.println("mouse pressed for cross " + event.getPoint());
        ImageView iview = getView();
        if (iview == null) {
            return;
        }

        AnatomicalPoint3D cursorPos = iview.getCursorPos();
        AnatomicalPoint3D ap = iview.getAnatomicalLocation(event.getComponent(), event.getPoint());
        AnatomicalPoint3D tap = ap.convertTo(cursorPos.getSpace());

        Viewport3D viewport = iview.getViewport();

        if (tap != null && viewport.inBounds(tap)) {
            iview.cursorPos.set(tap);
            
        }

    }

    private void moveCrosshair(Point p, Component source) {
        ImageView iview = getView();

        if (!iview.pointInPlot(source, p)) {
            return;
        }

        AnatomicalPoint3D cursorPos = iview.getCursorPos();



        AnatomicalPoint3D ap = iview.getAnatomicalLocation(source, p);
        ap = ap.convertTo(cursorPos.getSpace());

        if (ap != null && iview.getViewport().inBounds(ap)) {
            iview.cursorPos.set(ap);
           
        }
    }


    public void mouseReleased(MouseEvent event) {
        if (dragging) {
            if (SwingUtilities.isLeftMouseButton(event)) {
                moveCrosshair(event.getPoint(), (Component) event.getSource());

            }

            dragging = false;
        }
    }


    public void mouseDragged(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event) && !event.isAltDown() && !event.isControlDown() && !event.isShiftDown()) {
            dragging = true;
            moveCrosshair(event.getPoint(), (Component) event.getSource());

        }

    }
}
