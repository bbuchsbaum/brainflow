package brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 23, 2009
 * Time: 6:18:06 PM
 * To change this template use File | Settings | File Templates.
 */

public class Bounds2D<T extends Number> implements IBounds<T>, java.io.Serializable {

    Bounds1D<T> xbounds;

    Bounds1D<T> ybounds;

    @Override
    public T getMin(int dim) {
        switch (dim) {
            case 0: return xbounds.getMin(0);
            case 1: return ybounds.getMin(0);

        }

        throw new IllegalArgumentException("illegal dim : " + dim);
    }

    @Override
    public T getMax(int dim) {
        switch (dim) {
           case 0: return xbounds.getMax(0);
           case 1: return ybounds.getMax(0);

        }

        throw new IllegalArgumentException("illegal dim : " + dim);

    }

    @Override
    public Bounds1D<T> getBounds(int dim) {
        switch (dim) {
            case 0: return xbounds;
            case 1: return ybounds;

        }

        throw new IllegalArgumentException("illegal dim : " + dim);
        
    }

    @Override
    public int numDim() {
        return 2;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Number getDim(int dimnum) {
        switch (dimnum) {
            case 0: return xbounds.getDim(0);
            case 1: return ybounds.getDim(0);

        }

        throw new IllegalArgumentException("illegal dim : " + dimnum);


    }

    @Override
    public Number product() {
        return xbounds.getDim(0).doubleValue() * ybounds.getDim(0).doubleValue();
    }

    @Override
    public Number[] toArray() {
        return new Number[] { xbounds.getMin(0), xbounds.getMax(0), ybounds.getMin(0), ybounds.getMax(0)};
    }
}
