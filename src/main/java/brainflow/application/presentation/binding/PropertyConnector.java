package brainflow.application.presentation.binding;

import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 18, 2007
 * Time: 11:41:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class PropertyConnector<T> {

    private Property<T> prop1;

    private Property<T> prop2;

    private PListener listener1;

    private PListener listener2;


    public PropertyConnector(Property<T> p1, Property<T> p2) {
        prop1 = p1;
        prop2 = p2;

        initListeners();

    }

    private void initListeners() {
        listener1 = new PListener(prop1);
        listener2 = new PListener(prop2);
        BeanContainer.get().addListener(prop2, listener1);
        BeanContainer.get().addListener(prop1, listener2);

    }

    public void updateProperty() {
        prop2.set(prop1.get());
    }

    public void updateProperty2() {
        prop1.set(prop2.get());
    }

    public void reconnect() {
        BeanContainer.get().removeListener(prop2, listener1);
        BeanContainer.get().removeListener(prop1, listener2);
        initListeners();
    }

    public void disconnect() {
        BeanContainer.get().removeListener(prop2, listener1);
        BeanContainer.get().removeListener(prop1, listener2);

    }


    class PListener implements PropertyListener {
        Property<T> p;

        PListener(Property<T> _p) {
            p = _p;

        }

        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            RProperty<T> rprop = (RProperty<T>) prop;

            if (!rprop.get().equals(p.get())) {
                p.set(rprop.get());
            }
        }
    }


}
