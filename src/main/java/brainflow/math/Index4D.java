package brainflow.math;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 11, 2010
 * Time: 7:03:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class Index4D extends Index3D implements IIndex4D {

    private final int i4;

    public Index4D(int i1, int i2, int i3, int i4) {
        super(i1, i2, i3);
        this.i4 = i4;
    }

    @Override
    public int i4() {
        return i4;
    }

    @Override
    public int length() {
        return 4;
    }

    @Override
    public int product() {
        return super.product() * i4;
    }

    @Override
    public int value(int i) {
        if (i < 0 || i > 3) throw new IllegalArgumentException("illegal index " + i + " for Index3D");
        switch (i) {
            case 0:
                return i1();
            case 1:
                return i2();
            case 2:
                return i3();
            case 3:
                return i4();
            default:
                throw new AssertionError("can't happen");
        }
    }

    @Override
    public int[] toArray() {
        return new int[] { i1(), i2(), i3(), i4() };
    }

    public String toString() {
        return "Index3D{" +
                " i1 = " + i1() +
                " i2= " + i2() +
                " i3= " + i3() +
                " i4= " + i4() +
                " }";
    }
}
