package brainflow.math;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2008
 * Time: 2:02:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Index3D extends Index2D implements IIndex3D {

    private final int i3;

    

    public Index3D(int i1, int i2, int i3) {
        super(i1, i2);
        this.i3 = i3;
    }

    public int length() {
        return 3;
    }

    public final int i3() {
        return i3;

    }

    public int product() {
        return i1() * i2() * i3();
    }

    public int value(int i) {
        if (i < 0 || i > 2) throw new IllegalArgumentException("illegal index " + i + " for Index3D");
        switch (i) {
            case 0:
                return i1();
            case 1:
                return i2();
            case 2:
                return i3();
            default:
                throw new AssertionError("can't happen");
        }


    }

    public int[] toArray() {
        return new int[] { i1(), i2(), i3() };
    }


    public String toString() {
        return "Index3D{" +
                " i1 = " + i1() +
                " i2= " + i2() +
                " i3= " + i3() +
                " }";
    }
}