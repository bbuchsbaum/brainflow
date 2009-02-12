/*
 * DataSourceProgressEvent.java
 *
 * Created on June 22, 2006, 3:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.application.services;

/**
 *
 * @author buchs
 */
public class DataSourceProgressEvent extends ProgressEvent {

    public enum State {
        INITIATED,
        DONE,
        LOADING,
        CANCELLED;
    }

    private State state;
    
    /** Creates a new instance of LoadableImageProgressEvent */
    public DataSourceProgressEvent(Object source, int _progress, String _message, State _state) {
        super(source, _progress, _message);
        state = _state;
    }


    public State getState() {
        return state;
    }
}
