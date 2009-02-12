package brainflow.core;

import brainflow.modes.*;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 28, 2006
 * Time: 10:39:21 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageCanvasSelection2 implements MouseListener, MouseMotionListener {


    private BrainCanvas canvas;
    private ImageCanvasMode canvasMode;


    private MoveCrosshairMode crosshairMode;


    public ImageCanvasSelection2(BrainCanvas canvas) {
        this.canvas = canvas;

        //resizeMode = new PlotResizeMode(canvas);
        //translateMode = new PlotTranslationMode(canvas);
        crosshairMode = new MoveCrosshairMode(canvas);
        setCanvasMode(crosshairMode);

    }

    public void setCanvas(BrainCanvas canvas) {
        this.canvas = canvas;
    }

    public IBrainCanvas getCanvas() {
        return canvas;
    }


    public ImageCanvasMode getCanvasMode() {
        return canvasMode;
    }

    private void setCanvasMode(ImageCanvasMode canvasMode) {
        if (canvasMode == this.canvasMode) return;

        //if (this.canvasMode != null) {
        //    this.canvasMode.turnOff();
        //}

        this.canvasMode = canvasMode;
        //this.canvasMode.turnOn();
    }

    private void updateCursor(Cursor cursor) {
        if (cursor != canvas.getCursor()) {
            canvas.setCursor(cursor);

        }
    }


    public ImageView getSelectedView() {
        return canvas.getImageCanvasModel().getSelectedView();
    }




    public void setSelectedView(MouseEvent e, ImageView view) {
        setSelectedView(view);

    }

    public void setSelectedView(ImageView view) {
        canvas.getImageCanvasModel().setSelectedView(view);


    }

    public void keyPressed(KeyEvent keyevent) {

        if (canvasMode != null) {
            canvasMode.keyPressed(keyevent);
        }

    }

    public void keyReleased(KeyEvent keyevent) {

        if (canvasMode != null) {
            canvasMode.keyReleased(keyevent);
        }
    }

    public void keyTyped(KeyEvent keyevent) {

        if (canvasMode != null) {
            canvasMode.keyTyped(keyevent);
        }
    }

    public void mouseClicked(MouseEvent mouseevent) {

        if (canvasMode != null) {
            canvasMode.mouseClicked(mouseevent);
        }

    }

    public void mouseDragged(MouseEvent mouseevent) {
        if (canvasMode != null) {

            canvasMode.mouseDragged(mouseevent);

        }


    }

    public void mouseEntered(MouseEvent mouseevent) {
        if (canvasMode != null) {
            canvasMode.mouseEntered(mouseevent);
        }
    }

    public void mouseExited(MouseEvent mouseevent) {
        if (canvasMode != null) {
            canvasMode.mouseExited(mouseevent);
        }
    }


    public void mouseMoved(MouseEvent mouseevent) {

    }


    public void mousePressed(MouseEvent mouseevent) {
        if (canvasMode != null) {
            canvasMode.mousePressed(mouseevent);
        }

    }

    public void mouseReleased(MouseEvent mouseevent) {
        if (canvasMode != null) {
            canvasMode.mouseReleased(mouseevent);
        }
    }


}
