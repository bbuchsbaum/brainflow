package brainflow.application.presentation.binding;

import brainflow.image.axis.ImageAxis;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.WProperty;
import net.java.dev.properties.container.ObservableWrapper;
import net.java.dev.properties.container.BeanContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 16, 2007
 * Time: 6:01:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateToIndexConverter extends ObservableWrapper.ReadWrite<Integer> {

    private ImageAxis axis;

    public CoordinateToIndexConverter(BaseProperty<Double> property, ImageAxis _axis) {
        super(property);
        BeanContainer.bind(property);
        axis = _axis;

    }

    private double getValue() {
        RProperty<Double> prop = (RProperty<Double>) getProperty();
        return prop.get().doubleValue();
    }

    @Override
    public Integer get() {
        double val = getValue();
        return axis.nearestSample(val);
    }

    @Override
    public void set(Integer i) {
        double newval = axis.valueOf(i).getValue();
        WProperty<Double> wprop = (WProperty<Double>) getProperty();
        wprop.set(newval); 
    }


}
