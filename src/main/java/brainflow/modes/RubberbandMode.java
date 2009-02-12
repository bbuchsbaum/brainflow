/*
 * RubberbandMode.java
 *
 * Created on February 21, 2003, 1:27 PM
 */

package brainflow.modes;
import brainflow.graphics.*;
import brainflow.core.BrainCanvas;

import java.awt.event.*;

/**
 *
 * @author  Bradley
 */
public class RubberbandMode extends brainflow.modes.ImageCanvasMode implements MouseListener, MouseMotionListener {
    
    
    Rubberband rubberband;
    /** Creates a new instance of RubberbandMode */
    public RubberbandMode() {
    }
    
    public RubberbandMode(BrainCanvas _canvas) {
        super.setImageCanvas(_canvas);
        rubberband = new Rubberband(canvas);
        rubberband.setActive(true);
    }
    
    public Rubberband getRubberband() {
        return rubberband;
    }
    
    public void addActionListener(ActionListener listener) {
        rubberband.addActionListener(listener);
    }
    
   
    
    public void mouseReleased(MouseEvent event) {}
    public void mouseClicked(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    
    /** Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&Drop operation.
     *
     */
    public void mouseDragged(MouseEvent e) {
    }
    
    /** Invoked when the mouse cursorPos has been moved onto a component
     * but no buttons have been pushed.
     *
     */
    public void mouseMoved(MouseEvent e) {
    }
    
    /** Invoked when a mouse button has been pressed on a component.
     *
     */
    public void mousePressed(MouseEvent e) {
    }
    
}
