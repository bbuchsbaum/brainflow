package brainflow.modes;

import brainflow.core.ImageView;
import brainflow.image.anatomy.SpatialLoc2D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.core.Viewport3D;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 8, 2007
 * Time: 7:12:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class PanningInteractor extends ImageViewInteractor {

    private boolean dragging = false;

    private Point lastPoint;

    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.isControlDown()) {
            dragging = true;
            pan(e);
        }

    }


    public void mousePressed(MouseEvent e) {
        lastPoint = e.getPoint();
    }

    private void pan(MouseEvent e) {
        if (lastPoint != null) {

            ImageView view = getView();
            SpatialLoc2D lastpos = view.getSelectedPlot().translateScreenToAnat(SwingUtilities.convertPoint(e.getComponent(), lastPoint, view.getSelectedPlot().getComponent()));
            SpatialLoc2D curpos = view.getSelectedPlot().translateScreenToAnat(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), view.getSelectedPlot().getComponent()));

            double dx = curpos.getX().getValue() - lastpos.getX().getValue();
            double dy = curpos.getY().getValue() - lastpos.getY().getValue();

            Anatomy3D anatomy = view.getSelectedPlot().getDisplayAnatomy();
            Viewport3D viewport = view.getViewport();
            viewport.setAxisMin(anatomy.XAXIS, viewport.getXAxisMin() + dx);
            viewport.setAxisMin(anatomy.YAXIS, viewport.getYAxisMin() + dy);

            lastPoint = e.getPoint();

        }

    }


    public void mouseReleased(MouseEvent e) {
        lastPoint = null;
        dragging = false;


    }
}
