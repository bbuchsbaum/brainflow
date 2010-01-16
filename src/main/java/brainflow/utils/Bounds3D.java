package brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 23, 2009
 * Time: 5:58:46 PM
 * To change this template use File | Settings | File Templates.
 */

public class Bounds3D<T extends Number> implements IBounds<T>, java.io.Serializable {

    Bounds1D<T> xbounds;

    Bounds1D<T> ybounds;

    Bounds1D<T> zbounds;

    public Bounds3D(T x0, T x1, T y0, T y1, T z0, T z1) {
        xbounds = new Bounds1D<T>(x0,x1);
        ybounds = new Bounds1D<T>(y0,y1);
        zbounds = new Bounds1D<T>(z0,z1);
    }

    @Override
    public T getMin(int dim) {
        switch (dim) {
            case 0: return xbounds.getMin(0);
            case 1: return ybounds.getMin(0);
            case 2: return zbounds.getMin(0);
        }

        throw new IllegalArgumentException("illegal dim : " + dim);
    }

    @Override
    public T getMax(int dim) {
        switch (dim) {
           case 0: return xbounds.getMax(0);
           case 1: return ybounds.getMax(0);
           case 2: return zbounds.getMax(0);
        }

        throw new IllegalArgumentException("illegal dim : " + dim);

    }

    @Override
    public Bounds1D getBounds(int dim) {
         switch (dim) {
            case 0: return xbounds;
            case 1: return ybounds;
            case 2: return zbounds;
        }

        throw new IllegalArgumentException("illegal dim : " + dim);

    }

    

    @Override
    public int numDim() {
        return 3;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Number getDim(int dimnum) {
         switch (dimnum) {
            case 0: return xbounds.getDim(0);
            case 1: return ybounds.getDim(0);
            case 2: return zbounds.getDim(0);
        }

        throw new IllegalArgumentException("illegal dim : " + dimnum);
    }

    @Override
    public Number product() {
        return xbounds.getDim(0).doubleValue() * ybounds.getDim(0).doubleValue() * zbounds.getDim(0).doubleValue();
    }

    @Override
    public Number[] toArray() {
        return new Number[] { xbounds.getMin(0), xbounds.getMax(0), ybounds.getMin(0), ybounds.getMax(0), zbounds.getMin(0), zbounds.getMax(0)};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bounds3D bounds3D = (Bounds3D) o;

        if (xbounds != null ? !xbounds.equals(bounds3D.xbounds) : bounds3D.xbounds != null) return false;
        if (ybounds != null ? !ybounds.equals(bounds3D.ybounds) : bounds3D.ybounds != null) return false;
        if (zbounds != null ? !zbounds.equals(bounds3D.zbounds) : bounds3D.zbounds != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = xbounds != null ? xbounds.hashCode() : 0;
        result = 31 * result + (ybounds != null ? ybounds.hashCode() : 0);
        result = 31 * result + (zbounds != null ? zbounds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bounds3D{" +
                "xbounds=" + xbounds +
                ", ybounds=" + ybounds +
                ", zbounds=" + zbounds +
                '}';
    }
}
