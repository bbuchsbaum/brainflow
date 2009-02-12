/*
 * BrainFlowException.java
 *
 * Created on February 4, 2003, 10:36 AM
 */

package brainflow.application;

/**
 * @author Bradley
 */
public class BrainFlowException extends java.lang.Exception {

    /**
     *
     */
    private static final long serialVersionUID = 8377682156060867344L;

    /**
     * Creates a new instance of <code>BrainflowException</code> without detail message.
     */
    public BrainFlowException() {
    }


    /**
     * Constructs an instance of <code>BrainflowException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public BrainFlowException(String msg) {
        super(msg);
    }

    public BrainFlowException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BrainFlowException(Throwable cause) {
        super(cause);
    }


}
