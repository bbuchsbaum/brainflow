/*
 * TolerantScalar.java
 *
 * Created on April 25, 2003, 3:21 PM
 */

package brainflow.utils;

/**
 * @author Bradley
 */
public class TolerantScalar {

    private double val;

    private double tolerance = .00001;

    /**
     * Creates a new instance of TolerantScalar
     */
    public TolerantScalar(double _val) {
        val = _val;
    }

    public TolerantScalar(double val, double tolerance) {
        this.val = val;
        this.tolerance = tolerance;
    }

    public double valueOf() {
        return val;
    }

    public int hashCode() {
        final long temp = val != +0.0d ? Double.doubleToLongBits(val) : 0L;
        return (int) (temp ^ (temp >>> 32));
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TolerantScalar that = (TolerantScalar) o;

        if (Double.compare(that.val, val) != 0) return false;

        return true;
    }


}
