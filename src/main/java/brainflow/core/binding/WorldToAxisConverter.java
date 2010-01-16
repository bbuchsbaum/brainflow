package brainflow.core.binding;

import brainflow.image.anatomy.SpatialLoc3D;
import brainflow.image.space.Axis;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.ObservableWrapper;
import net.java.dev.properties.container.BeanContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 16, 2007
 * Time: 6:01:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorldToAxisConverter extends ObservableWrapper.ReadWrite<Double> {

    private Axis axis;


    public WorldToAxisConverter(BaseProperty<SpatialLoc3D> property, Axis _axis) {
        super(property);
        BeanContainer.bind(property);
        axis = _axis;

    }

    private SpatialLoc3D getValue() {
        RProperty<SpatialLoc3D> prop = (RProperty<SpatialLoc3D>) getProperty();
        return prop.get();
    }



    @Override
    public Double get() {

        SpatialLoc3D ap = getValue();


        double ret = ap.getValue(axis.getId());

        return (double)Math.round(ret);
    }

    @Override
    public void set(Double val) {

        Property<SpatialLoc3D> wprop = (Property<SpatialLoc3D>) getProperty();
        SpatialLoc3D old = wprop.get();

        System.out.println("setting world to " + val + " for axis " + axis);
        System.out.println("old point = " + old);


        if (axis == Axis.X_AXIS) {
            wprop.set(new SpatialLoc3D(old.getAnatomy(), val, old.getY(), old.getZ()));
        } else if (axis == Axis.Y_AXIS) {
            System.out.println("new point : " + new SpatialLoc3D(old.getAnatomy(), old.getX(), val, old.getZ()));
            wprop.set(new SpatialLoc3D(old.getAnatomy(), old.getX(), val, old.getZ()));
        } else if (axis == Axis.Z_AXIS) {
            wprop.set(new SpatialLoc3D(old.getAnatomy(), old.getX(), old.getY(), val));
        } else {
            throw new AssertionError("illegal image axis : " + axis);
        }

        
    }


}