package brainflow.math;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2008
 * Time: 1:55:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector1D implements IVector {

    public final double val1;

    public Vector1D(double val) {
        val1 = val;
    }

    public int length() {
        return 1;
    }

    public double product() {
        return val1;
    }

    public double value(int i) {
        if ( i != 0) throw new IllegalArgumentException("illegal index " + i + " for Vector1D");
        return val1;
    }
}
