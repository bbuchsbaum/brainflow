package brainflow.modes;

import brainflow.core.ImageView;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.axis.AxisRange;
import brainflow.core.Viewport3D;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 8, 2007
 * Time: 2:56:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class MouseWheelInteractor extends ImageViewInteractor {


    public void mouseWheelMoved(MouseWheelEvent e) {

        ImageView view = getView();
        if (view != null) {

            if (e.getWheelRotation() == -1) {
                scrollUp(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), view.getSelectedPlot().getComponent()));
            } else if (e.getWheelRotation() == 1) {
                scrollDown(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), view.getSelectedPlot().getComponent()));
            }
        }
    }


    private void scrollUp(Point p) {
        ImageView view = getView();
        Anatomy3D anatomy = view.getSelectedPlot().getDisplayAnatomy();
        Viewport3D viewport = view.getViewport();
        AxisRange xrange = viewport.getRange(anatomy.XAXIS);
        AxisRange yrange = viewport.getRange(anatomy.YAXIS);


   
        double xextent = xrange.getInterval() - (xrange.getInterval() * .08) -1;
        double yextent = yrange.getInterval() - (yrange.getInterval() * .08) -1;

        double xcenter = xrange.getCenter().getValue();
        double ycenter = yrange.getCenter().getValue();

        double xmin = xcenter - xextent/2.0;
        double ymin = ycenter - yextent/2.0;

        double xmax = xcenter + xextent/2.0;
        double ymax = ycenter + yextent/2.0;

        
        if (viewport.inBounds(anatomy.XAXIS, xmin) && viewport.inBounds(anatomy.YAXIS, ymin) && xextent >=2 && yextent >=2) {
            viewport.setAxesRange(anatomy.XAXIS, xmin, xmax, anatomy.YAXIS, ymin, ymax);
           

        }
        //


    }

    private void scrollDown(Point p) {
        ImageView view = getView();
        Anatomy3D anatomy = view.getSelectedPlot().getDisplayAnatomy();
        Viewport3D viewport = view.getViewport();
        AxisRange xrange = viewport.getRange(anatomy.XAXIS);
        AxisRange yrange = viewport.getRange(anatomy.YAXIS);


        double xextent = xrange.getInterval() + (xrange.getInterval() * .08) +1;
        double yextent = yrange.getInterval() + (yrange.getInterval() * .08) +1;

        double xcenter = xrange.getCenter().getValue();
        double ycenter = yrange.getCenter().getValue();

        double xmin = xcenter - (xextent/2.0);
        double ymin = ycenter - (yextent/2.0);

        double xmax = xcenter + (xextent/2.0);
        double ymax = ycenter + (yextent/2.0);


        if (viewport.inBounds(anatomy.XAXIS, xmin) && viewport.inBounds(anatomy.YAXIS, ymin) && xextent >=2 && yextent >=2) {
            viewport.setAxesRange(anatomy.XAXIS, xmin, xmax, anatomy.YAXIS, ymin, ymax);
                       
        }


    }


}
