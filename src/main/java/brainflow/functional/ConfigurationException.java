package brainflow.functional;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Apr 27, 2005
 * Time: 11:36:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationException extends Exception {

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    public ConfigurationException(String message) {
        super(message);   
    }
}
