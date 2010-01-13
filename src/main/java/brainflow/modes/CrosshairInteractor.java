package brainflow.modes;

import brainflow.core.ImageView;
import brainflow.image.anatomy.GridLoc3D;

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
        moveCrosshair(event.getPoint(), (Component) event.getSource());

    }

    private void moveCrosshair(Point p, Component source) {
        ImageView iview = getView();

        if (iview == null || !iview.pointInPlot(source, p)) {
            return;
        }


        GridLoc3D gp = iview.getAnatomicalLocation(source, p);

        if (gp != null && iview.getViewport().inBounds(gp.toReal())) {
            iview.cursorPos.set(gp);

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
