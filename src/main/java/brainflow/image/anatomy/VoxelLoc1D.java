package brainflow.image.anatomy;

import brainflow.image.axis.ImageAxis;
import brainflow.utils.NumberUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 9, 2009
 * Time: 3:46:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class VoxelLoc1D {


    private float gridX;

    private ImageAxis axis;

    public final LocationType unit = LocationType.VOXEL;

    public VoxelLoc1D(float gridX, ImageAxis axis) {
        this.gridX = gridX;
        this.axis = axis;
    }

    public double getValue() {
        return gridX;
    }

    public VoxelLoc1D reverse() {
        return new VoxelLoc1D(axis.getNumSamples() - gridX, axis.flip());

    }

    public AnatomicalAxis getAnatomy() {
        return axis.getAnatomicalAxis();
    }

    public ImageAxis getImageAxis() {
        return axis;
    }

    public SpatialLoc1D toReal() {
        return new SpatialLoc1D(axis.getAnatomicalAxis(),
                gridX * axis.getSpacing() + axis.getMinimum());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VoxelLoc1D that = (VoxelLoc1D) o;

        if (!NumberUtils.equals(that.gridX, gridX, .001)) return false;
        if (axis != null ? !axis.equals(that.axis) : that.axis != null) return false;
        if (unit != that.unit) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (gridX != +0.0f ? Float.floatToIntBits(gridX) : 0);
        result = 31 * result + (axis != null ? axis.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GridPoint1D{" +
                "gridX=" + gridX +
                ", axis=" + axis +
                ", unit=" + unit +
                '}';
    }
}
