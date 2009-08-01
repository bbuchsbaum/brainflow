/*
 * DataSourceProgressEvent.java
 *
 * Created on June 22, 2006, 3:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.app.services;

import org.bushe.swing.event.AbstractEventServiceEvent;


/**
 *
 * @author buchs
 */
public class ProgressEvent extends AbstractEventServiceEvent {
    
    private int progress = 0;

    private String message = "";
    
    /** Creates a new instance of LoadableImageProgressEvent */
    public ProgressEvent(Object source, int _progress, String _message) {
        super(source);
        
        progress = _progress;
        message = _message;
    }
    
    
    public int getProgress() {
        return progress;
    }
    
    public String getMessage() {
        return message;
    }
    
    
    
    
    
}
