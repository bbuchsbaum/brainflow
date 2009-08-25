package brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 23, 2009
 * Time: 9:55:36 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IBounds<T extends Number> extends IDimension {

    public T getMin(int dim);

    public T getMax(int dim);

    public Bounds1D<T> getBounds(int dim);


}
