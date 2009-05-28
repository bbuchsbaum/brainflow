package brainflow.core.binding;

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
public class DoubleToStringConverter extends ObservableWrapper.ReadWrite<String> {

    private NumberFormat formatter = NumberFormat.getNumberInstance();

    public DoubleToStringConverter(BaseProperty<Double> property) {
        super(property);
        BeanContainer.bind(property);
        formatter.setMaximumFractionDigits(1);
    }

    private double getValue() {
        RProperty<Double> prop = (RProperty<Double>) getProperty();
        return prop.get().doubleValue();
    }

    @Override
    public String get() {
        double val = getValue();
        return formatter.format(val);
    }

    @Override
    public void set(String s) {

        double val = getValue();

        try {
            val = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            //throw e;
        }

        WProperty<Double> wprop = (WProperty<Double>) getProperty();
        wprop.set(val);
        
    }
}
