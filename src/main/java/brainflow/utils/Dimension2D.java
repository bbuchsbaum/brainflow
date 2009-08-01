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

public class Dimension2D<T extends Number> implements IDimension<T>, java.io.Serializable {


    public T zero;
    public T one;

    public Dimension2D(Dimension2D<T> dim) {
        zero = dim.zero;
        one = dim.one;

    }


    public Dimension2D(T[] pt) {
        if (pt.length != 2) {
            throw new IllegalArgumentException("Dimension2D: supplied array must have length = 3!");
        }
        zero = pt[0];
        one = pt[1];

    }

    public Dimension2D(T _zero, T _one) {
        zero = _zero;
        one = _one;

    }

    public T getDim(int dimnum) {
        if (dimnum == 0) return zero;
        if (dimnum == 1) return one;

        throw new IllegalArgumentException("illegal dimension number " + dimnum + " for class " + getClass());
    }

    public Number product() {
        return zero.intValue() * one.intValue();
    }

    public int numDim() {
        return 2;
    }

    public String toString() {
        return "[" + zero + ", " + one + "]";
    }

    @Override
    public Number[] toArray() {
        return new Number[] { zero, one };
    }


}