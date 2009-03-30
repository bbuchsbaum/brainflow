/*
 * ImageDisplayModelEvent.java
 *
 * Created on June 26, 2006, 12:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.app.services;

import brainflow.app.BrainFlowProject;
import brainflow.app.toplevel.BrainFlowProjectEvent;
import brainflow.core.IImageDisplayModel;
import org.bushe.swing.event.AbstractEventServiceEvent;

import javax.swing.event.ListDataEvent;

/**
 * @author buchs
 */
public class ImageDisplayModelEvent extends AbstractEventServiceEvent {

    public enum TYPE {
        LAYER_ADDED,
        LAYER_REMOVED,
        LAYER_CHANGED,
        LAYER_INTERVAL_ADDED,
        LAYER_INTERVAL_REMOVED
    }

    private BrainFlowProjectEvent event;

    private TYPE type;


    /**
     * Creates a new instance of ImageDisplayModelEvent
     */

    public ImageDisplayModelEvent(BrainFlowProjectEvent _event, TYPE _type) {
        super(_event);
        event = _event;
        type = _type;
    }


    public TYPE getType() {
        return type;
    }

    public ListDataEvent getListDataEvent() {
        //todo do we need to expose low level event especially since it might be null?
        // could have getIndex
        // etc ..
        return event.getListDataEvent();
    }

    public IImageDisplayModel getModel() {
        return event.getModel();
    }

    public BrainFlowProject getProject() {
        return event.getProject();
    }

}
