package brainflow.math;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2008
 * Time: 2:02:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector3D extends Vector2D {

    public final double val3;

    public Vector3D(double val1, double val2, double val3) {
        super(val1, val2);
        this.val3 = val3;
    }

    public int length() {
        return 3;
    }

    public double product() {
        return val1 * val2 * val3;
    }

    public double value(int i) {
        if (i < 0 || i > 2) throw new IllegalArgumentException("illegal index " + i + " for Vector3D");
        switch (i) {
            case 0:
                return val1;
            case 1:
                return val2;
            case 2:
                return val3;
            default:
                throw new AssertionError("can't happen");
        }


    }
}
