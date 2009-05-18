package brainflow.image.anatomy;

import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.axis.ImageAxis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 25, 2009
 * Time: 10:08:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class GridPoint3D {


    private GridPoint1D gridX, gridY, gridZ;

    private IImageSpace3D space;

    public final BrainUnit unit = BrainUnit.VOXEL;


    public GridPoint3D(double x, double y, double z, IImageSpace3D space) {
        this.gridX = clamp(x, space.getImageAxis(Axis.X_AXIS));
        this.gridY = clamp(y, space.getImageAxis(Axis.Y_AXIS));
        this.gridZ = clamp(z, space.getImageAxis(Axis.Z_AXIS));
        this.space = space;
    }

    private GridPoint1D clamp(double val, ImageAxis axis) {
        val = Math.min(val, axis.getNumSamples());
        val = Math.max(val, 0);

        return new GridPoint1D((float) val, axis);
    }

    public static GridPoint3D fromWorld(double x, double y, double z, IImageSpace3D space) {
        float[] grid = space.worldToGrid((float) x, (float) y, (float) z);
        return new GridPoint3D(grid[0], grid[1], grid[2], space);
    }

    public static GridPoint3D fromReal(float x, float y, float z, IImageSpace3D space) {
        double gridx = space.getImageAxis(Axis.X_AXIS).gridPosition(x);
        double gridy = space.getImageAxis(Axis.Y_AXIS).gridPosition(y);
        double gridz = space.getImageAxis(Axis.Z_AXIS).gridPosition(z);
        return new GridPoint3D(gridx, gridy, gridz, space);
    }

    //public static GridPoint3D fromReal(BrainPoint1D one, BrainPoint1D two, BrainPoint1D three, IImageSpace3D space) {
    //GridPoint1D gp1 = GridPoint1D.f
    //}


    public static GridPoint3D fromReal(BrainPoint3D bp, IImageSpace3D space) {
        if (space.getAnatomy() != bp.getAnatomy()) {
            throw new IllegalArgumentException("incompatible axes: BrainPoint3D " + bp.getAnatomy() + " does not equals IIMageSpace3D anatomy: " + space.getAnatomy());
        }
        double gridx = space.getImageAxis(Axis.X_AXIS).gridPosition(bp.getX());
        double gridy = space.getImageAxis(Axis.Y_AXIS).gridPosition(bp.getY());
        double gridz = space.getImageAxis(Axis.Z_AXIS).gridPosition(bp.getZ());
        return new GridPoint3D(gridx, gridy, gridz, space);
    }


    public static GridPoint3D fromReal(double x, double y, double z, IImageSpace3D space) {
        double gridx = space.getImageAxis(Axis.X_AXIS).gridPosition(x);
        double gridy = space.getImageAxis(Axis.Y_AXIS).gridPosition(y);
        double gridz = space.getImageAxis(Axis.Z_AXIS).gridPosition(z);
        return new GridPoint3D(gridx, gridy, gridz, space);
    }


    public Anatomy3D getAnatomy() {
        return space.getAnatomy();
    }

    public Anatomy3D getWorldAnatomy() {
        return space.getMapping().getWorldAnatomy();
    }

    public IImageSpace3D getSpace() {
        return space;
    }

    public GridPoint1D getValue(AnatomicalAxis axis, boolean flip) {
        if (axis.sameAxis(getAnatomy().XAXIS)) {
            if (!flip || axis == getAnatomy().XAXIS) return gridX;
            else {
                return gridX.reverse();
            }
        } else if (axis.sameAxis(getAnatomy().YAXIS)) {
            if (!flip || axis == getAnatomy().YAXIS) return gridY;
            else {
                return gridY.reverse();
            }
        } else if (axis.sameAxis(getAnatomy().ZAXIS)) {
            if (!flip || axis == getAnatomy().ZAXIS) return gridZ;
            else {
                return gridZ.reverse();
            }
        } else {
            throw new IllegalArgumentException("axis : " + axis + " incompatible with Anatomy3D " + getAnatomy());
        }

    }


    public BrainPoint3D toWorld() {
        float[] vals = space.gridToWorld((float) gridX.getValue(), (float) gridY.getValue(), (float) gridZ.getValue());
        return new BrainPoint3D(space.getMapping().getWorldAnatomy(), vals[0], vals[1], vals[2]);
    }

    public BrainPoint3D toReal() {
        return new BrainPoint3D(getAnatomy(),
                gridX.getValue() * space.getSpacing(Axis.X_AXIS) + space.getImageAxis(Axis.X_AXIS).getMinimum(),
                gridY.getValue() * space.getSpacing(Axis.Y_AXIS) + space.getImageAxis(Axis.Y_AXIS).getMinimum(),
                gridZ.getValue() * space.getSpacing(Axis.Z_AXIS) + space.getImageAxis(Axis.Z_AXIS).getMinimum());

    }

    public GridPoint3D replace(BrainPoint1D pt) {
        BrainPoint3D bp = toReal();
        return GridPoint3D.fromReal(bp.replace(pt), space);
    }

    public GridPoint1D getX() {
        return gridX;
    }

    public GridPoint1D getY() {
        return gridY;
    }

    public GridPoint1D getZ() {
        return gridZ;
    }


    @Override
    public String toString() {
        return "GridPoint3D{" +
                "gridX=" + gridX.getValue() +
                ", gridY=" + gridY.getValue() +
                ", gridZ=" + gridZ.getValue() +
                //", space=" + space +
                //", unit=" + unit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GridPoint3D that = (GridPoint3D) o;

        if (gridX != null ? !gridX.equals(that.gridX) : that.gridX != null) return false;
        if (gridY != null ? !gridY.equals(that.gridY) : that.gridY != null) return false;
        if (gridZ != null ? !gridZ.equals(that.gridZ) : that.gridZ != null) return false;
        if (space != null ? !space.equals(that.space) : that.space != null) return false;
        if (unit != that.unit) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = gridX != null ? gridX.hashCode() : 0;
        result = 31 * result + (gridY != null ? gridY.hashCode() : 0);
        result = 31 * result + (gridZ != null ? gridZ.hashCode() : 0);
        result = 31 * result + (space != null ? space.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }
}
