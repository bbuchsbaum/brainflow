package brainflow.image.anatomy;

import brainflow.image.axis.AxisRange;
import brainflow.image.axis.CoordinateAxis;
import brainflow.image.axis.ImageAxis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 12:31:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnatomicalPoint1D implements AnatomicalPoint {



    private double value;

    private CoordinateAxis axis;


    public AnatomicalPoint1D(AnatomicalAxis _anatomy, double value) {
        this.value = value;
        axis = new CoordinateAxis(new AxisRange(_anatomy, -1000,1000));
    }

    public AnatomicalPoint1D(CoordinateAxis axis, double value) {
        this.value = value;
        this.axis = axis;
    }

    public AnatomicalAxis getAnatomy() {
        return axis.getAnatomicalAxis();
    }

    public double getValue() {
        return value;
    }

    public AnatomicalPoint1D convertTo(CoordinateAxis other) {

        if (other.getAnatomicalAxis().sameDirection(getAnatomy())) {
            // todo what if other axis does not contain point?
            return new AnatomicalPoint1D(other, value);
        }

        if (other.getAnatomicalAxis() == getAnatomy().getFlippedAxis()) {
            // flipped
            double fval = (other.getMaximum() - value) + other.getMinimum();
           

            return new AnatomicalPoint1D(other, fval);

        }


        throw new IllegalArgumentException("cannot convert points between anatomical axes");
           

    }


    public int getNumDimensions() {
        return 1;
    }

    public AnatomicalPoint1D mirrorPoint(ImageAxis otherAxis) {
        assert otherAxis.getAnatomicalAxis().sameAxis(getAnatomy()) : "other axis cannot be orthogonal to this point";

        if (otherAxis.getAnatomicalAxis() == getAnatomy()) {
            //a copy
            return new AnatomicalPoint1D(getAnatomy(), getValue());
        } else {
            double nvalue = otherAxis.getRange().getEnd().getValue() - getValue()
                    + otherAxis.getRange().getBeginning().getValue();

            return new AnatomicalPoint1D(otherAxis.getAnatomicalAxis(), nvalue);
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

        AnatomicalPoint1D that = (AnatomicalPoint1D) o;

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

    public String toString() {
        return "[" + axis.getMinimum() +", " + axis.getMaximum() + "]" + " : " +  "[" + getAnatomy().toString() + "]" + getValue();
    }
}
