package brainflow.modes;

import brainflow.core.ImageView;
import brainflow.core.BrainCanvas;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ImageTranslationMode extends ImageCanvasMode implements MouseListener, MouseMotionListener {

    Point dragBegin = null;
    Point dragEnd = null;
    boolean dragging = false;


    public ImageTranslationMode(BrainCanvas _canvas) {
        super.setImageCanvas(_canvas);

    }

    
    public void mousePressed(MouseEvent event) {
        dragBegin = event.getPoint();
    }

    public void mouseDragged(MouseEvent event) {
        dragging = true;
        ImageView selectedView = canvas.whichSelectedView(event.getPoint());
        if (selectedView != null) {
            dragEnd = event.getPoint();
            float transx = (float) (dragEnd.getX() - dragBegin.getX());
            float transy = (float) (dragEnd.getY() - dragBegin.getY());
            //canvas.whichPlot()
            //JFreeImagePlot iplot = selectedPane.getImagePlot();
            //iplot.translate(-transx, -transy);

            dragBegin = dragEnd;
        }
    }


    public void mouseReleased(MouseEvent event) {
        if (dragging) {
            ImageView selectedView = canvas.whichSelectedView(event.getPoint());
            if (selectedView != null) {
                dragEnd = event.getPoint();
                float transx = (float) (dragEnd.getX() - dragBegin.getX());
                float transy = (float) (dragEnd.getY() - dragBegin.getY());
                //selectedPane.getImagePlot().translate(-transx, -transy);
            }
        }

        dragging = false;
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mouseClicked(MouseEvent event) {
    }

    public void mouseMoved(MouseEvent event) {
    }


}