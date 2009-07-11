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

public class Dimension1D<T extends Number> implements IDimension<T>, java.io.Serializable {


    public T zero;

    public Dimension1D(Dimension1D<T> dim) {
        zero = dim.zero;

    }


    public Dimension1D(T[] pt) {
        if (pt.length != 1) {
            throw new IllegalArgumentException("illegal dimension number " + pt.length + " for class " + getClass());

        }
        zero = pt[0];

    }

    public Dimension1D(T _zero) {
        zero = _zero;

    }

    public int product() {
        return zero.intValue();
    }

    public T getDim(int dimnum) {
        if (dimnum == 0) return zero;

        throw new IllegalArgumentException("illegal dimension number " + dimnum + " for class " + getClass());
    }

    public int numDim() {
        return 1;
    }

    public String toString() {
        return "[" + zero + "]";
    }

    @Override
    public Number[] toArray() {
        return new Number[] { zero };
    }


}