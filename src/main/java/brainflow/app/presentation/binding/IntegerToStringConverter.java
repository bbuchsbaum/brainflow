package brainflow.app.presentation.binding;

import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.WProperty;
import net.java.dev.properties.container.ObservableWrapper;
import net.java.dev.properties.container.BeanContainer;

import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 16, 2007
 * Time: 8:27:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class IntegerToStringConverter extends ObservableWrapper.ReadWrite<String> {

    private NumberFormat formatter = NumberFormat.getIntegerInstance();

    public IntegerToStringConverter(BaseProperty<Integer> property) {
        super(property);
        BeanContainer.bind(property);
        formatter.setMaximumFractionDigits(1);
    }

    private int getValue() {
        RProperty<Integer> prop = (RProperty<Integer>) getProperty();
        return prop.get().intValue();
    }

    @Override
    public String get() {
        int val = getValue();
        return formatter.format(val);
    }

    @Override
    public void set(String s) {
       
        int val = Integer.parseInt(s);
        WProperty<Integer> wprop = (WProperty<Integer>) getProperty();
        wprop.set(val);

    }
}