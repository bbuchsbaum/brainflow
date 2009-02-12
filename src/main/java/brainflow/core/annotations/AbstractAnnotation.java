package brainflow.core.annotations;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 13, 2007
 * Time: 7:13:43 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAnnotation implements IAnnotation {

    public static final String VISIBLE_PROPERTY = "visible";

    private boolean visible = true;

    protected PropertyChangeSupport support = new PropertyChangeSupport(this);


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        boolean old = this.visible;
        this.visible = visible;
        support.firePropertyChange(AbstractAnnotation.VISIBLE_PROPERTY, old, isVisible());
    }

}
