package brainflow.application.presentation.inspection;

import com.jidesoft.grid.Property;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Oct 15, 2007
 * Time: 9:24:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class JidePropertyWrapper extends Property {

    private Object value;

    public JidePropertyWrapper(Object value, String displayName, String category) {
        this.value = value;
        this.setDisplayName(displayName);
        this.setCategory(category);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setType(Class aClass) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
