package brainflow.math;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2008
 * Time: 1:55:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Index1D implements IIndex, IIndex1D {

    private final int i1;

    public Index1D(int val) {
        i1 = val;
    }

    public final int i1() {
        return i1;
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