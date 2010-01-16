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
public class IndexLoc1D {


    private int indexX;

    private ImageAxis axis;

    
    public final LocationType unit = LocationType.GRID;

    public IndexLoc1D(int indexX, ImageAxis axis) {
        if (indexX < 0 || indexX > (axis.getNumSamples()-1)) {
            throw new IllegalArgumentException("index " + indexX + " out of bounds for axis " + axis);
        }
        this.indexX = indexX;
        this.axis = axis;

    }

    public double getValue() {
        return indexX;
    }

    public IndexLoc1D reverse() {
        return new IndexLoc1D(axis.getNumSamples() - indexX, axis.flip());

    }

    public AnatomicalAxis getAnatomy() {
        return axis.getAnatomicalAxis();
    }

    public ImageAxis getImageAxis() {
        return axis;
    }

    public SpatialLoc1D toReal() {
        return new SpatialLoc1D(axis.getAnatomicalAxis(),
                indexX * axis.getSpacing() + axis.getMinimum() + axis.getSpacing()/2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexLoc1D that = (IndexLoc1D) o;

        if (!NumberUtils.equals(that.indexX, indexX, .001)) return false;
        if (axis != null ? !axis.equals(that.axis) : that.axis != null) return false;
        if (unit != that.unit) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (indexX != +0.0f ? Float.floatToIntBits(indexX) : 0);
        result = 31 * result + (axis != null ? axis.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GridPoint1D{" +
                "indexX=" + indexX +
                ", axis=" + axis +
                ", unit=" + unit +
                '}';
    }
}