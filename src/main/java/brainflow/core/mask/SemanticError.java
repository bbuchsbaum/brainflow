package brainflow.core.mask;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 13, 2008
 * Time: 1:16:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SemanticError extends RuntimeException {
    
    public SemanticError(String message) {
        super(message);
    }
}
