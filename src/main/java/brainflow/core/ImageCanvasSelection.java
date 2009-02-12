package brainflow.core;


import brainflow.modes.*;
import brainflow.utils.IResizeableBorder;
import brainflow.utils.ISelectableBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 28, 2006
 * Time: 10:39:21 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageCanvasSelection implements MouseListener, MouseMotionListener {


    private BrainCanvas canvas;
    private ImageCanvasMode canvasMode;

    private ImageView preSelectedView;


    private PlotResizeMode resizeMode;
    private PlotTranslationMode translateMode;
    private MoveCrosshairMode crosshairMode;

    enum CURSOR_STATE {
        OVER_SELECTED_VIEW,
        OVER_UNSELECTED_VIEW,
        OVER_CANVAS,
        OVER_RESIZE_HANDLE,
        OVER_GRIPPER_NORTH,
        OVER_GRIPPER_SOUTH,
        OVER_GRIPPER_EAST,
        OVER_GRIPPER_WEST,
        OVER_GRIPPER_NORTH_WEST,
        OVER_GRIPPER_SOUTH_WEST,
        OVER_GRIPPER_NORTH_EAST,
        OVER_GRIPPER_SOUTH_EAST,
        OVER_CROSSHAIR,
        ELSEWHERE

    }

    private CURSOR_STATE cursorState = CURSOR_STATE.ELSEWHERE;


    public ImageCanvasSelection(BrainCanvas canvas) {
        this.canvas = canvas;

        //resizeMode = new PlotResizeMode(canvas);
        //translateMode = new PlotTranslationMode(canvas);
        //crosshairMode = new MoveCrosshairMode(canvas);

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

    private void updateCursorState(CURSOR_STATE state, ImageView cursorView) {
        if (state == cursorState) {
            return;
        }


        switch (state) {
            case OVER_CANVAS:
                clearPreSelection();
                updateCursor(Cursor.getDefaultCursor());
                break;
            case OVER_UNSELECTED_VIEW:
                setPreSelectedView(cursorView);
                updateCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                break;
            case OVER_SELECTED_VIEW:
                updateCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                setCanvasMode(translateMode);
                break;
            case OVER_CROSSHAIR:
                updateCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setCanvasMode(crosshairMode);
                break;
            case OVER_GRIPPER_NORTH:
                resizeMode.setDragDirection(PlotResizeMode.N);
                setCanvasMode(resizeMode);
                updateCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                break;
            case OVER_GRIPPER_SOUTH:
                resizeMode.setDragDirection(PlotResizeMode.S);
                setCanvasMode(resizeMode);
                updateCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                break;
            case OVER_GRIPPER_EAST:
                resizeMode.setDragDirection(PlotResizeMode.E);
                setCanvasMode(resizeMode);
                updateCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                break;
            case OVER_GRIPPER_WEST:
                resizeMode.setDragDirection(PlotResizeMode.W);
                setCanvasMode(resizeMode);
                updateCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                break;
            case OVER_GRIPPER_NORTH_WEST:
                resizeMode.setDragDirection(PlotResizeMode.NW);
                setCanvasMode(resizeMode);
                updateCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                break;
            case OVER_GRIPPER_NORTH_EAST:
                resizeMode.setDragDirection(PlotResizeMode.NE);
                setCanvasMode(resizeMode);
                updateCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                break;
            case OVER_GRIPPER_SOUTH_WEST:
                resizeMode.setDragDirection(PlotResizeMode.SW);
                setCanvasMode(resizeMode);
                updateCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                break;
            case OVER_GRIPPER_SOUTH_EAST:
                resizeMode.setDragDirection(PlotResizeMode.SE);
                setCanvasMode(resizeMode);
                updateCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                break;
            case ELSEWHERE:
                updateCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        cursorState = state;


    }

    private IResizeableBorder.HANDLE_LOCATION getResizeHandle(Point p, ImageView view) {
        IResizeableBorder border = (IResizeableBorder) view.getBorder();
        IResizeableBorder.HANDLE_LOCATION loc = border.getHandle(p);
        return loc;
    }

    private CURSOR_STATE getAppropriateState(IResizeableBorder.HANDLE_LOCATION handle, CURSOR_STATE defaultState) {

        CURSOR_STATE state;

        switch (handle) {
            case NULL:
                state = defaultState;
                break;
            case TOP_LEFT:
                state = CURSOR_STATE.OVER_GRIPPER_NORTH_WEST;
                break;
            case TOP_MIDDLE:
                state = CURSOR_STATE.OVER_GRIPPER_NORTH;
                break;
            case TOP_RIGHT:
                state = CURSOR_STATE.OVER_GRIPPER_NORTH_EAST;
                break;
            case SIDE_RIGHT:
                state = CURSOR_STATE.OVER_GRIPPER_EAST;
                break;
            case SIDE_LEFT:
                state = CURSOR_STATE.OVER_GRIPPER_WEST;
                break;
            case BOTTOM_RIGHT:
                state = CURSOR_STATE.OVER_GRIPPER_SOUTH_EAST;
                break;
            case BOTTOM_MIDDLE:
                state = CURSOR_STATE.OVER_GRIPPER_SOUTH;
                break;
            case BOTTOM_LEFT:
                state = CURSOR_STATE.OVER_GRIPPER_SOUTH_WEST;
                break;
            default:
                state = defaultState;


        }

        return state;

    }


    private ImageView getPreSelectedView() {
        return preSelectedView;
    }

    public ImageView getSelectedView() {
        return canvas.getImageCanvasModel().getSelectedView();
    }

    private void setPreSelectedView(ImageView view) {
        if (view != preSelectedView) {

            ISelectableBorder border = (ISelectableBorder) view.getBorder();
            border.setPreSelected(true);
            preSelectedView = view;
            view.repaint();
        }

    }


    public void clearPreSelection() {
        if (preSelectedView != null) {
            ISelectableBorder border = (ISelectableBorder) preSelectedView.getBorder();
            border.setPreSelected(false);
            preSelectedView.repaint();
            preSelectedView = null;
        }

    }

    public void clearSelection() {
        ImageView view = getSelectedView();
        if (view != null) {
            ISelectableBorder border = (ISelectableBorder) view.getBorder();
            border.setSelected(false);
            view.repaint();
        }

    }

    public void setSelectedView(MouseEvent e, ImageView view) {
        setSelectedView(view);
        //translateMode.mousePressed(e);
        //setCanvasMode(translateMode);


    }

    public void setSelectedView(ImageView view) {
        clearSelection();

        if (view != null) {
            ISelectableBorder border = (ISelectableBorder) view.getBorder();
            border.setSelected(true);
            canvas.moveToFront(view);
            view.repaint();
        }


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
            if (!canvasMode.stillInterestedBefore(mouseevent, MouseAction.MOUSE_DRAGGED)) {
                computeState(mouseevent);

            } else {
                canvasMode.mouseDragged(mouseevent);
            }
        }


    }

    public void mouseEntered(MouseEvent mouseevent) {
        if (canvasMode != null) {
            canvasMode.mouseEntered(mouseevent);
        }
    }

    public void mouseExited(MouseEvent mouseevent) {
        updateCursorState(CURSOR_STATE.ELSEWHERE, null);
        if (canvasMode != null) {
            canvasMode.mouseExited(mouseevent);
        }
    }

    private void computeState(MouseEvent event) {
        ImageView view = canvas.whichView((Component) event.getSource(), event.getPoint());

        // view is null
        if (view == null) {
            updateCursorState(CURSOR_STATE.OVER_CANVAS, null);

        } else if (view != canvas.getSelectedView()) {
            updateCursorState(CURSOR_STATE.OVER_UNSELECTED_VIEW, view);

        } else if (view == canvas.getSelectedView()) {

            // means we're inside a selected view
            Point p = SwingUtilities.convertPoint((Component) event.getSource(), event.getPoint(), view);
            IResizeableBorder.HANDLE_LOCATION handle = getResizeHandle(p, view);
            CURSOR_STATE state = getAppropriateState(handle, CURSOR_STATE.OVER_SELECTED_VIEW);

            IImagePlot plot = view.whichPlot(p);
            Point2D cross = null;

            if (plot != null) {
                cross = view.getCrosshairLocation(view.whichPlot(p));
            }

            if (state != CURSOR_STATE.OVER_SELECTED_VIEW) {
                updateCursorState(state, view);
            } else if ((cross != null) && (Math.abs(cross.getX() - p.getX()) < 4) &&
                      (Math.abs(cross.getY() - p.getY()) < 4)) {
                updateCursorState(CURSOR_STATE.OVER_CROSSHAIR, view);
            } else {
                updateCursorState(CURSOR_STATE.OVER_SELECTED_VIEW, view);
            }


        }

    }

    public void mouseMoved(MouseEvent mouseevent) {
        computeState(mouseevent);

    }


    public void mousePressed(MouseEvent mouseevent) {
        ImageView view = canvas.whichView((Component) mouseevent.getSource(), mouseevent.getPoint());
        if (view == null) {
            clearSelection();
            setSelectedView(null);
        } else if (view != canvas.getSelectedView()) {
           // canvas.getCanvasSelection().clearPreSelection();
           // canvas.getCanvasSelection().setSelectedView(mouseevent, view);
            computeState(mouseevent);
            canvasMode.mousePressed(mouseevent);
        } else if (canvasMode != null) {
            canvasMode.mousePressed(mouseevent);
        }

    }

    public void mouseReleased(MouseEvent mouseevent) {
        if (canvasMode != null) {
            canvasMode.mouseReleased(mouseevent);
        }
    }


}
