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

public class Dimension4D<T extends Number> implements IDimension, java.io.Serializable {


    public T zero;
    public T one;
    public T two;
    public T three;


    public Dimension4D(Dimension4D<T> dim) {
        zero = dim.zero;
        one = dim.one;
        two = dim.two;
        three = dim.three;
    }


    public Dimension4D(T[] pt) {
        if (pt.length != 4) {
            throw new IllegalArgumentException("Dimension4D: supplied array must have length = 4!");
        }
        zero = pt[0];
        one = pt[1];
        two = pt[2];
        three = pt[3];
    }

    public Dimension4D(T _zero, T _one, T _two, T _three) {
        zero = _zero;
        one = _one;
        two = _two;
        three = _three;
    }

    public T getDim(int dimnum) {
        if (dimnum == 0) return zero;
        if (dimnum == 1) return one;
        if (dimnum == 2) return two;
        if (dimnum == 3) return three;

        throw new IllegalArgumentException("illegal dimension number " + dimnum + " for class " + getClass());
    }

    public int product() {
        return zero.intValue() * one.intValue() * two.intValue() * three.intValue();
    }

    public int numDim() {
        return 4;
    }

    public String toString() {
        return "[" + zero + ", " + one + ", " + two + ", " + three + "]";
    }


}