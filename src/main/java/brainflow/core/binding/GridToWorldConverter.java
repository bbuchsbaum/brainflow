package brainflow.core.binding;

import brainflow.image.anatomy.GridLoc3D;
import brainflow.image.anatomy.SpatialLoc3D;
import net.java.dev.properties.container.ObservableWrapper;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.WProperty;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 30, 2009
 * Time: 9:38:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridToWorldConverter extends ObservableWrapper.ReadWrite<SpatialLoc3D> {


    public GridToWorldConverter(BaseProperty<GridLoc3D> property) {
        super(property);
        //BeanContainer.bind(this);

    }

    private GridLoc3D getValue() {
        RProperty<GridLoc3D> prop = (RProperty<GridLoc3D>) getProperty();
        return prop.get();
    }

    @Override
    public void set(SpatialLoc3D spatialLoc3D) {
        if (spatialLoc3D.getAnatomy() != getValue().getSpace().getMapping().getWorldAnatomy()) {
            throw new IllegalArgumentException("wrong anatomy " + spatialLoc3D.getAnatomy() + " : it must match world anatomy of reference image space");
        }

        GridLoc3D gp = GridLoc3D.fromWorld(spatialLoc3D.getX(), spatialLoc3D.getY(), spatialLoc3D.getZ(), getValue().getSpace());

        if (!gp.equals(getValue())) {
            WProperty<GridLoc3D> wprop = (WProperty<GridLoc3D>) getProperty();
            wprop.set(gp);
        }


    }

    @Override
    public SpatialLoc3D get() {
        GridLoc3D ap = getValue();
        return ap.toWorld();
    }
}
