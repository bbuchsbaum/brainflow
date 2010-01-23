package brainflow.core.binding;

import brainflow.image.anatomy.SpatialLoc3D;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.math.Index3D;
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
public class CoordinateToIndexConverter2 extends ObservableWrapper.ReadWrite<Integer> {

    private Axis axis;

    private IImageSpace3D space;

    public CoordinateToIndexConverter2(BaseProperty<SpatialLoc3D> property, IImageSpace3D _space, Axis _axis) {
        super(property);
        //todo is this necessary?
        BeanContainer.bind(this);
        //todo is this necessary?
        axis = _axis;
        space = _space;

    }

    private SpatialLoc3D getValue() {
        RProperty<SpatialLoc3D> prop = (RProperty<SpatialLoc3D>) getProperty();
        return prop.get();
    }

    private Index3D getGridValue() {
        SpatialLoc3D ap = getValue();
        float[] gpt = space.worldToGrid((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        return new Index3D(Math.round(gpt[0]), Math.round(gpt[1]), Math.round(gpt[2]));

    }

    @Override
    public Integer get() {
        SpatialLoc3D ap = getValue();

        float ret;
        if (axis == Axis.X_AXIS) {
            ret = space.worldToGridX((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        } else if (axis == Axis.Y_AXIS) {
            ret = space.worldToGridY((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        } else if (axis == Axis.Z_AXIS) {
            ret = space.worldToGridZ((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        } else {
            throw new AssertionError("illegal image axis : " + axis);
        }

        System.out.println("requesting spatial loc " + ret);

        return Math.round(ret);
    }

    @Override
    public void set(Integer i) {

        Index3D voxel = getGridValue();
        System.out.println("setting coordinate to index " + i);
        System.out.println("axis : " + axis);
        System.out.println("setting voxel index to " + i);
        System.out.println("grid value : " + voxel);

        if (axis == Axis.X_AXIS) {
             voxel = new Index3D(i, voxel.i2(), voxel.i3());
        } else if (axis == Axis.Y_AXIS) {
            voxel = new Index3D(voxel.i1(), i, voxel.i3());
        } else if (axis == Axis.Z_AXIS) {
            voxel = new Index3D(voxel.i1(), voxel.i2(), i);
        } else {
            throw new AssertionError("illegal image axis : " + axis);
        }


        float[] ret = space.gridToWorld(voxel.i1(),  voxel.i2(), voxel.i3());

        SpatialLoc3D nap = new SpatialLoc3D(space.getMapping().getWorldAnatomy(), ret[0], ret[1], ret[2]);

        WProperty<SpatialLoc3D> wprop = (WProperty<SpatialLoc3D>) getProperty();
        wprop.set(nap);
    }


}