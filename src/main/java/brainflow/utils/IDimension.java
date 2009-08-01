package brainflow.utils;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 21, 2007
 * Time: 11:19:29 AM
 */
public interface IDimension<T extends Number> {


    public int numDim();

    public T getDim(int dimnum);

    public Number product();

    public Number[] toArray();
}
