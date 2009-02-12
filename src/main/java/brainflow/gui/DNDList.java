/**
 * This is an example of a component, which serves as a DragSource as 
 * well as Drop Target.
 * To illustrate the concept, JList has been used as a droppable target
 * and a draggable source.
 * Any component can be used instead of a JList.
 * The code also contains debugging messages which can be used for 
 * diagnostics and understanding the flow of events.
 *
 * @version 1.0
 */

package brainflow.gui;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.io.IOException;

import javax.swing.JList;


public class DNDList extends JList implements DropTargetListener, DragSourceListener, DragGestureListener {


    /**
     * enables this component to be a dropTarget
     */

    DropTarget dropTarget = null;

    /**
     * enables this component to be a Drag Source
     */
    DragSource dragSource = null;

    DNDComponentInterface dndComponent;


    /**
     * constructor - initializes the DropTarget and DragSource.
     */

    public DNDList(DNDComponentInterface _dndComponent) {
        dndComponent = _dndComponent;
        dropTarget = new DropTarget(this, this);
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
    }

    /**
     * is invoked when you are dragging over the DropSite
     */

    public void dragEnter(DropTargetDragEvent event) {

        // debug messages for diagnostics

        event.acceptDrag(DnDConstants.ACTION_MOVE);
    }

    /**
     * is invoked when you are exit the DropSite without dropping
     */

    public void dragExit(DropTargetEvent event) {


    }

    /**
     * is invoked when a drag operation is going on
     */

    public void dragOver(DropTargetDragEvent event) {

    }

    /**
     * a drop has occurred
     */


    public void drop(DropTargetDropEvent event) {

        try {
            Transferable transferable = event.getTransferable();

            // we accept only Strings
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {

                event.acceptDrop(DnDConstants.ACTION_MOVE);
                Point loc = event.getLocation();
                int idx = this.locationToIndex(loc);
                String s = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                addElement(idx, s);
                event.getDropTargetContext().dropComplete(true);
            } else {
                event.rejectDrop();
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Exception" + exception.getMessage());
            event.rejectDrop();
        }
        catch (UnsupportedFlavorException ufException) {
            ufException.printStackTrace();
            System.err.println("Exception" + ufException.getMessage());
            event.rejectDrop();
        }
    }

    /**
     * is invoked if the use modifies the current drop gesture
     */


    public void dropActionChanged(DropTargetDragEvent event) {
    }

    /**
     * a drag gesture has been initiated
     */

    public void dragGestureRecognized(DragGestureEvent event) {

        Object selected = getSelectedValue();
        if (selected != null) {
            StringSelection text = new StringSelection(selected.toString());

            // as the name suggests, starts the dragging
            dragSource.startDrag(event, DragSource.DefaultMoveDrop, text, this);
        }
    }

    /**
     * this message goes to DragSourceListener, informing it that the dragging
     * has ended
     */

    public void dragDropEnd(DragSourceDropEvent event) {
        if (event.getDropSuccess()) {
            removeElement();
        }
    }

    /**
     * this message goes to DragSourceListener, informing it that the dragging
     * has entered the DropSite
     */

    public void dragEnter(DragSourceDragEvent event) {

    }

    /**
     * this message goes to DragSourceListener, informing it that the dragging
     * has exited the DropSite
     */

    public void dragExit(DragSourceEvent event) {


    }

    /**
     * this message goes to DragSourceListener, informing it that the dragging is currently
     * ocurring over the DropSite
     */

    public void dragOver(DragSourceDragEvent event) {


    }

    /**
     * is invoked when the user changes the dropAction
     */

    public void dropActionChanged(DragSourceDragEvent event) {

    }

    /**
     * adds elements to itself
     */

    public void addElement(int idx, Object s) {
        dndComponent.addElement(idx, s);
    }

    /**
     * removes an element from itself
     */

    public void removeElement() {
        dndComponent.removeElement(getSelectedValue());
    }

}
