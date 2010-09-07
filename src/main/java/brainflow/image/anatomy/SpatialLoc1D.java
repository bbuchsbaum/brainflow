package brainflow.image.anatomy;

import brainflow.image.axis.CoordinateAxis;
import brainflow.image.axis.ImageAxis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 12:31:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpatialLoc1D implements BrainLoc {



    private double value;

    private AnatomicalAxis axis;


    public SpatialLoc1D(AnatomicalAxis _anatomy, double value) {
        this.value = value;
        axis = _anatomy;
    }


    public AnatomicalAxis getAnatomy() {
        return axis;
    }

    public double getValue() {
        return value;
    }

    public SpatialLoc1D convertTo(CoordinateAxis other) {

        if (other.getAnatomicalAxis() == getAnatomy()) {
            // todo what if other axis does not contain point?
            return new SpatialLoc1D(other.getAnatomicalAxis(), value);
        }

        if (other.getAnatomicalAxis() == getAnatomy().getFlippedAxis()) {
            // flipped
            double fval = (other.getMaximum() - value) + other.getMinimum();
            return new SpatialLoc1D(other.getAnatomicalAxis(), fval);

        }

        throw new IllegalArgumentException("cannot convert points between anatomical axes");

    }


    public int getNumDimensions() {
        return 1;
    }

    public SpatialLoc1D mirrorPoint(ImageAxis otherAxis) {
        assert otherAxis.getAnatomicalAxis().sameAxis(getAnatomy()) : "other axis cannot be orthogonal to this point";

        if (otherAxis.getAnatomicalAxis() == getAnatomy()) {
            //a copy
            return new SpatialLoc1D(getAnatomy(), getValue());
        } else {
            double nvalue = otherAxis.getRange().getEnd().getValue() - getValue()
                    + otherAxis.getRange().getBeginning().getValue();

            return new SpatialLoc1D(otherAxis.getAnatomicalAxis(), nvalue);
        }


    }


    public double getValue(int axisNum) {


        if (axisNum == 0) {
            return getValue();
        }

        throw new IllegalArgumentException("axisNum must be zero for 1 dimensional point");
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpatialLoc1D that = (SpatialLoc1D) o;

        if (Double.compare(that.value, value) != 0) return false;
        if (axis != null ? !axis.equals(that.axis) : that.axis != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        long temp;
        temp = value != +0.0d ? Double.doubleToLongBits(value) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (axis != null ? axis.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BrainPoint1D{" +
                "file=" + value +
                ", axis=" + axis +
                '}';
    }
}