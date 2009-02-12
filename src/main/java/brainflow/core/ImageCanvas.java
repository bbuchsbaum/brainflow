package brainflow.core;


import brainflow.modes.ImageCanvasMode;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 27, 2004
 * Time: 11:35:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageCanvas extends JComponent implements MouseListener, MouseMotionListener {


    private BrainCanvasModel canvasModel = new BrainCanvasModel();

    private ImageCanvasSelection canvasSelection;

    private JRootPane rootPane = new JRootPane();

    private Logger log = Logger.getLogger(ImageCanvas.class.getName());

    private EventListenerList listenerList = new EventListenerList();


    public ImageCanvas() {
        super();
        setLayout(new BorderLayout());

        rootPane.getGlassPane().setVisible(true);
        //rootPane.getGlassPane().addMouseListener(this);
        //rootPane.getGlassPane().addMouseMotionListener(this);
        add(rootPane, "Center");

        //canvasSelection = new ImageCanvasSelection(this);
        initListener();

       


    }

    private void initListener() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.addAWTEventListener(new AWTEventListener() {

            public void eventDispatched(AWTEvent event) {

                if (event instanceof MouseEvent) {

                    MouseEvent me = (MouseEvent) event;
                    if (!SwingUtilities.isDescendingFrom(rootPane, (Component) me.getSource())) {
                        return;
                    }
                    Point absp = SwingUtilities.convertPoint((Component) me.getSource(), me.getX(), me.getY(), rootPane);


                    if (absp.getX() < 0 || absp.getX() > rootPane.getWidth() ||
                            absp.getY() < 0 || absp.getY() > rootPane.getHeight()) {

                        return;
                    }

                    switch (me.getID()) {
                        case MouseEvent.MOUSE_MOVED:
                            ImageCanvas.this.mouseMoved(me);
                            break;
                        case MouseEvent.MOUSE_DRAGGED:
                            ImageCanvas.this.mouseDragged(me);
                            break;
                        case MouseEvent.MOUSE_PRESSED:
                            ImageCanvas.this.mousePressed(me);
                            break;
                        case MouseEvent.MOUSE_RELEASED:
                            ImageCanvas.this.mouseReleased(me);
                            break;
                        case MouseEvent.MOUSE_CLICKED:
                            ImageCanvas.this.mouseClicked(me);
                            break;
                        case MouseEvent.MOUSE_ENTERED:
                            ImageCanvas.this.mouseEntered(me);
                            break;
                        case MouseEvent.MOUSE_EXITED:
                            ImageCanvas.this.mouseExited(me);
                            break;
                        default:



                    }


                }

            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK);

    }

    public ImageCanvasSelection getCanvasSelection() {
        return canvasSelection;
    }


    

    public BrainCanvasModel getImageCanvasModel() {
        return canvasModel;
    }


    public ImageView whichSelectedView(Point p) {
        Component c = rootPane.getLayeredPane().findComponentAt(p);
        if (c instanceof ImageView) {
            return (ImageView) c;
        }

        Container ct = SwingUtilities.getAncestorOfClass(ImageView.class, c);

        if (ct != null & ct instanceof ImageView) {
            ImageView iv = (ImageView) ct;
            if (iv == canvasModel.getSelectedView())
                return iv;
        }

        return null;


    }

    public ImageView whichView(Component source, Point p) {
        p = SwingUtilities.convertPoint(source, p, rootPane.getLayeredPane());
        Component c = rootPane.getLayeredPane().findComponentAt(p);

        if (c instanceof ImageView) {
            return findParentView((ImageView) c);
        }

        Container ct = SwingUtilities.getAncestorOfClass(ImageView.class, c);

        if (ct != null & ct instanceof ImageView) {
            return findParentView((ImageView) ct);
        }

        return null;

    }

    private ImageView findParentView(ImageView view) {
        ImageView ret = view;

        while (view.getParent() instanceof ImageView) {
            ret = (ImageView) view.getParent();
            view = ret;
        }

        return ret;

    }

    public Component whichComponent(Point p) {

        Component c = rootPane.getLayeredPane().findComponentAt(p);
        return c;


    }


    public IImagePlot whichPlot(Point p) {
        Component c = rootPane.getLayeredPane().findComponentAt(p);
        Container ct = SwingUtilities.getAncestorOfClass(ImageView.class, c);
        if (ct != null & ct instanceof ImageView) {
            ImageView iview = (ImageView) ct;
            Point relPoint = SwingUtilities.convertPoint(rootPane.getLayeredPane(), p, iview);
            IImagePlot iplot = iview.whichPlot(relPoint);
            return iplot;
        }

        return null;


    }

    public java.util.List<IImagePlot> getPlotList() {
        java.util.List<IImagePlot> plots = new ArrayList<IImagePlot>();
        for (ImageView view : canvasModel.getImageViews()) {
            plots.addAll(view.getPlots());
        }

        return plots;
    }

    public boolean isSelectedView(ImageView view) {
        if (view == canvasModel.getSelectedView())
            return true;
        else
            return false;
    }


    public void addSpecialMouseListener(MouseListener listener) {
        if (Arrays.asList(listenerList.getListenerList()).contains(listener)) {
            log.warning("attempting to add listener twice to image canvas.");
            return;
        }

        listenerList.add(MouseListener.class, listener);
        //mouseListeners.add(listener);
        //rootPane.getGlassPane().addMouseListener(listener);

    }

    public void removeSpecialMouseListener(MouseListener listener) {

        listenerList.remove(MouseListener.class, listener);
        //mouseListeners.remove(listener);
        //rootPane.getGlassPane().removeMouseListener(listener);
    }

    public void addSpecialMouseMotionListener(MouseMotionListener listener) {
        if (Arrays.asList(listenerList.getListenerList()).contains(listener)) {
            log.warning("attempting to add listener twice to image canvas.");
            return;
        }
        listenerList.add(MouseMotionListener.class, listener);
        //mouseMotionListeners.add(listener);
        //rootPane.getGlassPane().addMouseMotionListener(listener);
    }

    public void removeSpecialMouseMotionListener(MouseMotionListener listener) {
        listenerList.remove(MouseMotionListener.class, listener);
        //mouseMotionListeners.remove(listener);
        //rootPane.getGlassPane().removeMouseMotionListener(listener);
    }


    public ImageCanvasMode getCanvasMode() {
        return canvasSelection.getCanvasMode();
    }


    public JLayeredPane getLayeredPane() {
        return rootPane.getLayeredPane();
    }

    public java.util.List<ImageView> getViews() {
        return canvasModel.getImageViews();
    }


    public void setSelectedView(ImageView view) {
        canvasSelection.setSelectedView(view);
    }

    public ImageView getSelectedView() {
        return canvasModel.getSelectedView();
    }

    /*public void swapImageView(ImageView oldView, ImageView newView) {
        //assert canvasModel.getImageViews().contains(oldView);
        Point loc = oldView.getLocation();

        boolean sel = false;
        if (oldView == canvasModel.getSelectedView()) {
            sel = true;
        }

        canvasModel.removeImageView(oldView);

        newView.setLocation(loc);
        canvasModel.addImageView(newView);

        if (sel) {
            canvasModel.setSelectedView(newView);
        }


    } */

    public void removeImageView(ImageView view) {
        if (view == getSelectedView()) {
            canvasSelection.clearSelection();
        }

        canvasModel.removeImageView(view);

        rootPane.getLayeredPane().remove(view);
    }

    /*private void addImageView(ImageView view, Point location) {
        view.setSize(view.getPreferredSize());
        view.setLocation(location);
        canvasModel.addImageView(view);
        //rootPane.getLayeredPane().add(view, JLayeredPane.DEFAULT_LAYER);

    } */

    public void addImageView(ImageView view) {
        //view.pack();
        //view.setVisible(true);
        view.setSize(view.getPreferredSize());

        ImageView squatter = whichView(rootPane.getLayeredPane(), new Point(100, 100));
        if (squatter == null) {
            view.setLocation(100, 100);
        } else {
            Point p = squatter.getLocation();
            view.setLocation(new Point(p.x + 50, p.y + 50));
        }


        canvasModel.addImageView(view);
        rootPane.getLayeredPane().add(view, JLayeredPane.DEFAULT_LAYER);
        rootPane.getLayeredPane().moveToFront(view);

    }


    public void moveToFront(ImageView view) {
        //assert canvasModel.getImageViews().contains(view);
        rootPane.getLayeredPane().moveToFront(view);
    }


    public void mouseClicked(MouseEvent e) {
        canvasSelection.mouseClicked(e);
        MouseListener[] mouseListeners = listenerList.getListeners(MouseListener.class);
        for (MouseListener ml : mouseListeners) {
            ml.mouseClicked(e);
        }

        //redispatchMouseEvent(e);
    }

    public void mousePressed(MouseEvent e) {
        canvasSelection.mousePressed(e);
        MouseListener[] mouseListeners = listenerList.getListeners(MouseListener.class);
        for (MouseListener ml : mouseListeners) {
            ml.mousePressed(e);
        }
        //redispatchMouseEvent(e);

    }

    public void mouseReleased(MouseEvent e) {
        canvasSelection.mouseReleased(e);
        MouseListener[] mouseListeners = listenerList.getListeners(MouseListener.class);
        int i = 0;
        for (MouseListener ml : mouseListeners) {
            ml.mouseReleased(e);
            i++;
        }

    }

    public void mouseEntered(MouseEvent e) {
        canvasSelection.mouseEntered(e);
        requestFocus();
        MouseListener[] mouseListeners = listenerList.getListeners(MouseListener.class);
        for (MouseListener ml : mouseListeners) {
            ml.mouseEntered(e);
        }
    }

    public void mouseExited(MouseEvent e) {
        canvasSelection.mouseExited(e);
        MouseListener[] mouseListeners = listenerList.getListeners(MouseListener.class);
        for (MouseListener ml : mouseListeners) {
            ml.mouseExited(e);
        }
        //redispatchMouseEvent(e);


    }

    public void mouseDragged(MouseEvent e) {
        canvasSelection.mouseDragged(e);
        MouseMotionListener[] mouseMotionListeners = listenerList.getListeners(MouseMotionListener.class);
        for (MouseMotionListener ml : mouseMotionListeners) {
            ml.mouseDragged(e);
        }
        //redispatchMouseEvent(e);

    }

    public void mouseMoved(MouseEvent e) {
        canvasSelection.mouseMoved(e);
        MouseMotionListener[] mouseMotionListeners = listenerList.getListeners(MouseMotionListener.class);

        for (MouseMotionListener ml : mouseMotionListeners) {
            ml.mouseMoved(e);
        }
        //redispatchMouseEvent(e);

    }

    /*private void redispatchMouseEvent(MouseEvent e) {

        Point glassPanePoint = e.getPoint();
        Container container = rootPane.getLayeredPane();
        Point containerPoint = SwingUtilities.convertPoint(rootPane.getGlassPane(),
                glassPanePoint,
                container);


        if (containerPoint.zero < 0 | containerPoint.zero < 0) {

            rootPane.dispatchEvent(new MouseEvent((Component) e.getSource(), e.getID(),
                    e.getWhen(),
                    e.getModifiers(),
                    glassPanePoint.zero,
                    glassPanePoint.zero,
                    e.getClickCount(),
                    e.isPopupTrigger()));// forward event....


        } else {
            //The mouse event is probably over the content pane.
            //Find out exactly which component it's over.
            Component component =
                    SwingUtilities.getDeepestComponentAt(container,
                            containerPoint.zero,
                            containerPoint.zero);

            Component ancestor = SwingUtilities.getAncestorOfClass(ImageView.class, component);


            if (ancestor != null) {

                ImageView selView = (ImageView) ancestor;
                while (ancestor.getParent() instanceof ImageView) {
                    selView = (ImageView) ancestor.getParent();
                    ancestor = selView;
                }

                if (canvasModel.getSelectedView() != selView) {
                    // not selected, do not forward mouse event ...
                } else {
                    Point componentPoint = SwingUtilities.convertPoint(rootPane.getGlassPane(),
                            glassPanePoint,
                            component);


                    component.dispatchEvent(new MouseEvent(component, e.getID(),
                            e.getWhen(),
                            e.getModifiers(),
                            componentPoint.zero,
                            componentPoint.zero,
                            e.getClickCount(),
                            e.isPopupTrigger()));// forward event....


                }
            }

        }
    }           */


}
