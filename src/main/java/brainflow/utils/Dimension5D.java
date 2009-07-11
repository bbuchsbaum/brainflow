package brainflow.utils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class Dimension5D<T extends Number> implements IDimension<T>, java.io.Serializable {


    public T zero;
    public T one;
    public T two;
    public T three;
    public T four;


    public Dimension5D(Dimension5D<T> dim) {
        zero = dim.zero;
        one = dim.one;
        two = dim.two;
        three = dim.three;
        four = dim.four;
    }


    public Dimension5D(T[] pt) {
        if (pt.length != 5) {
            throw new IllegalArgumentException("Dimension5D: supplied array must have length = 4!");
        }
        zero = pt[0];
        one = pt[1];
        two = pt[2];
        three = pt[3];
        four = pt[4];
    }

    public Dimension5D(T _zero, T _one, T _two, T _three, T _four) {
        zero = _zero;
        one = _one;
        two = _two;
        three = _three;
        four = _four;
    }

    public T getDim(int dimnum) {
        if (dimnum == 0) return zero;
        if (dimnum == 1) return one;
        if (dimnum == 2) return two;
        if (dimnum == 3) return three;
        if (dimnum == 4) return four;

        throw new IllegalArgumentException("illegal dimension number " + dimnum + " for class " + getClass());
    }

    public int product() {
        return zero.intValue() * one.intValue() * two.intValue() * three.intValue() * four.intValue();
    }

    public int numDim() {
        return 4;
    }

    public String toString() {
        return "[" + zero + ", " + one + ", " + two + ", " + three + ", " + four + "]";
    }

    @Override
    public Number[] toArray() {
        return new Number[] { zero, one, two, three, four };
    }


}