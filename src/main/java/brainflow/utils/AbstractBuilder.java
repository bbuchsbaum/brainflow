package brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 16, 2009
 * Time: 2:22:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractBuilder {

    protected boolean isBuilt;

    public void checkBuilt() {
        if (this.isBuilt) {
            throw new IllegalStateException("The object cannot be modified after built");
        }
    }
}


