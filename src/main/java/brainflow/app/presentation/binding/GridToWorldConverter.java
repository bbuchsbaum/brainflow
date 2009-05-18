package brainflow.app.presentation.binding;

import net.java.dev.properties.container.ObservableWrapper;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.WProperty;
import brainflow.image.anatomy.BrainPoint3D;
import brainflow.image.anatomy.GridPoint3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 30, 2009
 * Time: 9:38:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridToWorldConverter extends ObservableWrapper.ReadWrite<BrainPoint3D> {


    public GridToWorldConverter(BaseProperty<GridPoint3D> property) {
        super(property);
        //todo is this necessary?
        //BeanContainer.bind(this);
        //todo is this necessary?


    }

    private GridPoint3D getValue() {
        RProperty<GridPoint3D> prop = (RProperty<GridPoint3D>) getProperty();
        return prop.get();
    }

    @Override
    public void set(BrainPoint3D brainPoint3D) {
        if (brainPoint3D.getAnatomy() != getValue().getSpace().getMapping().getWorldAnatomy()) {
            throw new IllegalArgumentException("wrong anatomy " + brainPoint3D.getAnatomy() + " : it must match world anatomy of reference image space");
        }

        GridPoint3D gp = GridPoint3D.fromWorld(brainPoint3D.getX(), brainPoint3D.getY(), brainPoint3D.getZ(), getValue().getSpace());

        if (!gp.equals(getValue())) {
            WProperty<GridPoint3D> wprop = (WProperty<GridPoint3D>) getProperty();
            wprop.set(gp);
        }


    }

    @Override
    public BrainPoint3D get() {
        GridPoint3D ap = getValue();
        return ap.toWorld();
    }
}
