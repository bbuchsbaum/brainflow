package brainflow.utils;

import java.text.NumberFormat;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class Dimension3D<T extends Number> implements IDimension, java.io.Serializable {


    public T zero;

    public T one;

    public T two;


    public Dimension3D(Dimension3D<T> dim) {
        zero = dim.zero;
        one = dim.one;
        two = dim.two;
    }


    public Dimension3D(T[] pt) {
        if (pt.length != 3) {
            throw new IllegalArgumentException("Dimension3D: supplied array must have length = 3!");
        }
        zero = pt[0];
        one = pt[1];
        two = pt[2];
    }

    public Dimension3D(T _zero, T _one, T _two) {
        zero = _zero;
        one = _one;
        two = _two;
    }

    public T getDim(int dimnum) {
        if (dimnum == 0) return zero;
        if (dimnum == 1) return one;
        if (dimnum == 2) return two;

        throw new IllegalArgumentException("illegal dimension number " + dimnum + " for class " + getClass());
    }

    public int product() {
        return zero.intValue() * one.intValue() * two.intValue();
    }

    public int numDim() {
        return 3;
    }

    public String toString() {
        NumberFormat f = NumberFormat.getNumberInstance();
        f.setMaximumFractionDigits(2);
        return "[" + f.format(zero) + ", " + f.format(one) + ", " + f.format(two) + "]";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dimension3D)) return false;

        Dimension3D that = (Dimension3D) o;

        if (one != null ? !one.equals(that.one) : that.one != null) return false;
        if (two != null ? !two.equals(that.two) : that.two != null) return false;
        if (zero != null ? !zero.equals(that.zero) : that.zero != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (zero != null ? zero.hashCode() : 0);
        result = 31 * result + (one != null ? one.hashCode() : 0);
        result = 31 * result + (two != null ? two.hashCode() : 0);
        return result;
    }
}