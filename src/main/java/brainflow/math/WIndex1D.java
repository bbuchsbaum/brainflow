package brainflow.math;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2008
 * Time: 1:55:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class WIndex1D implements IIndex, IIndex1D {

    private int i1;

    public WIndex1D(int val) {
        i1 = val;
    }

    public int i1() {
        return i1;
    }

    public final void set_i1(int _i1) {
        i1 = _i1;
    }

    public int length() {
        return 1;
    }

    public int product() {
        return i1;
    }

    public int value(int i) {
        if ( i != 0) throw new IllegalArgumentException("illegal index " + i + " for Index1D");
        return i1;
    }

    public int[] toArray() {
        return new int[] { i1 };
    }
}