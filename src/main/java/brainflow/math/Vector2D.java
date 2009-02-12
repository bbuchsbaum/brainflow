package brainflow.math;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2008
 * Time: 1:59:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector2D extends Vector1D {

    public final double val2;

    public Vector2D(double val1, double val2) {
        super(val1);
        this.val2 = val2;
    }

    public int length() {
        return 2;
    }

    public double product() {
        return val1*val2;
    }

    public double value(int i) {
        if (i <0 || i > 1) throw new IllegalArgumentException("illegal index " + i + " for Vector2D");
        switch(i) {
            case 0:
                return val1;
            case 1:
                return val2;
            default:
                throw new AssertionError("can't happen");
        }

    }
}
