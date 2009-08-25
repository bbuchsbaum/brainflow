package brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 23, 2009
 * Time: 6:05:09 PM
 * To change this template use File | Settings | File Templates.
 */

public class Bounds1D<T extends Number> implements IBounds<T>, java.io.Serializable {

    private T x0, x1;

    public Bounds1D(T x0, T x1) {
        this.x0 = x0;
        this.x1 = x1;
    }

    @Override
    public T getMin(int dim) {
        if (dim != 0) throw new IllegalArgumentException("illegal dim: " + dim);
        return x0;
    }

    @Override
    public T getMax(int dim) {
        if (dim != 0) throw new IllegalArgumentException("illegal dim: " + dim);
        return x1;
    }

    @Override
    public Bounds1D<T> getBounds(int dim) {
        if (dim != 0) throw new IllegalArgumentException("illegal dim: " + dim);
        return this;
    }

    

    @Override
    public int numDim() {
        return 1;
    }

    @Override
    public Number getDim(int dimnum) {
        if (dimnum != 0) throw new IllegalArgumentException("illegal dim: " + dimnum);
        return x1.doubleValue() - x0.doubleValue();
    }

    @Override
    public Number product() {
        return x1.doubleValue() - x0.doubleValue();

    }

    @Override
    public Number[] toArray() {
        return new Number[] { x0, x1};
    }
}
