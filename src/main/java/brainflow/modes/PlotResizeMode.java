package brainflow.modes;

import brainflow.core.ImageView;
import brainflow.core.BrainCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class PlotResizeMode extends ImageCanvasMode implements MouseListener, MouseMotionListener {

    private static final Logger log = Logger.getLogger(PlotResizeMode.class.getName());

    private Point dragBegin = null;

    private boolean dragging = false;

    public static int N = 0;
    public static int S = 1;
    public static int NW = 2;
    public static int NE = 3;
    public static int E = 4;
    public static int W = 5;
    public static int SE = 6;
    public static int SW = 7;

    private int dragDirection = -1;

    private boolean interested = true;


    public PlotResizeMode(BrainCanvas _canvas) {
        super.setImageCanvas(_canvas);
    }

    public int getDragDirection() {
        return dragDirection;
    }

    public void setDragDirection(int dragDirection) {
        assert dragDirection >= 0 && dragDirection <= 7;
        this.dragDirection = dragDirection;
    }


    public boolean stillInterestedBefore(MouseEvent event, MouseAction action) {
        ImageView selectedView = canvas.getSelectedView();
        if (selectedView == null) {
            return false;
        }

        Rectangle bounds =selectedView.getBounds();
       
        Point relpos = SwingUtilities.convertPoint((Component)event.getSource(), event.getPoint(), selectedView);
        if ( (relpos.x < -200) || (relpos.y < -200)) {
            interested = false;
        } else if ( (relpos.x > (bounds.getWidth() + 175)) || (relpos.y > (bounds.getHeight() + 175)) )  {
            interested = false;
        } else {
            interested  = true;
        }


        return interested;
    }

    public boolean stillInterestedAfter(MouseEvent event, MouseAction action) {
        return interested;
    }

    public void mousePressed(MouseEvent event) {
        ImageView selectedView = canvas.getSelectedView();
        if (selectedView != null) {
            dragBegin = event.getPoint();
            dragging = true;
        }

    }



    public void mouseDragged(MouseEvent event) {

        ImageView selectedView = canvas.getSelectedView();
       
        
        if (selectedView != null && dragging && interested) {


            Point npoint =  event.getPoint();

            int dx = (int) (npoint.getX() - dragBegin.getX());
            int dy = (int) (npoint.getY() - dragBegin.getY());

            Rectangle drawArea = selectedView.getBounds();
            Rectangle nArea;

            if (dragDirection == N) {
                nArea = new Rectangle((int) drawArea.getX(), (int) drawArea.getY() + dy,
                        (int) (drawArea.getWidth()), (int) (drawArea.getHeight() - dy));
            } else if (dragDirection == S) {
                nArea = new Rectangle((int) drawArea.getX(), (int) drawArea.getY(),
                        (int) (drawArea.getWidth()), (int) (drawArea.getHeight() + dy));

            } else if (dragDirection == NE) {
                nArea = new Rectangle((int) drawArea.getX(), (int) drawArea.getY() + dy,
                        (int) (drawArea.getWidth() + dx), (int) (drawArea.getHeight() - dy));

            } else if (dragDirection == NW) {
                nArea = new Rectangle((int) drawArea.getX() + dx, (int) drawArea.getY() + dy,
                        (int) (drawArea.getWidth() - dx), (int) (drawArea.getHeight() - dy));

            } else if (dragDirection == E) {
                nArea = new Rectangle((int) drawArea.getX(), (int) drawArea.getY(),
                        (int) (drawArea.getWidth() + dx), (int) (drawArea.getHeight()));

            } else if (dragDirection == W) {
                nArea = new Rectangle((int) drawArea.getX() + dx, (int) drawArea.getY(),
                        (int) (drawArea.getWidth() - dx), (int) (drawArea.getHeight()));

            } else if (dragDirection == SW) {
                nArea = new Rectangle((int) drawArea.getX() + dx, (int) drawArea.getY(),
                        (int) (drawArea.getWidth() - dx), (int) (drawArea.getHeight() + dy));

            } else if (dragDirection == SE) {
                nArea = new Rectangle((int) drawArea.getX(), (int) drawArea.getY(),
                        (int) (drawArea.getWidth() + dx), (int) (drawArea.getHeight() + dy));

            } else {
                return;

            }


            selectedView.setBounds(nArea);
            selectedView.revalidate();


            dragBegin = npoint;
        }


    }


    public void mouseMoved(MouseEvent event) {


    }
   

    public void mouseReleased(MouseEvent event) {
        dragging = false;
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {

    }

    public void mouseClicked(MouseEvent event) {
    }

}