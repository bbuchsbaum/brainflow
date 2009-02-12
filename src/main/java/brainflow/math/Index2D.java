package brainflow.math;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2008
 * Time: 1:59:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Index2D extends Index1D implements IIndex2D {

    private final int i2;

    public Index2D(int i1, int i2) {
        super(i1);
        this.i2 = i2;
    }

    public int length() {
        return 2;
    }

    public final int i2() {
        return i2;

    }

    public int product() {
        return i1() * i2();
    }

    public int value(int i) {
        if (i < 0 || i > 1) throw new IllegalArgumentException("illegal index " + i + " for Index2D");
        switch (i) {
            case 0:
                return i1();
            case 1:
                return i2();
            default:
                throw new AssertionError("can't happen");
        }

    }

    public int[] toArray() {
        return new int[] { i1(), i2()};
    }
}