package brainflow.modes;

import brainflow.core.ImageView;

import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 5, 2007
 * Time: 7:00:56 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageViewInteractor implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

    private ImageView view;

    public ImageViewInteractor(ImageView view) {
        this.view = view;
    }

    public ImageViewInteractor() {
    }


    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        if (getView() != null) {
            getView().removeMouseListener(this);
            getView().removeMouseMotionListener(this);
            getView().removeMouseWheelListener(this);
        }

        if (view != null) {
            this.view = view;
            view.addMouseListener(this);
            view.addMouseMotionListener(this);
            getView().addMouseWheelListener(this);
        }
    }


    public void mouseDragged(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseMoved(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void keyTyped(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyReleased(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyPressed(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void mouseWheelMoved(MouseWheelEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
