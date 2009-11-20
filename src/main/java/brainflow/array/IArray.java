package brainflow.array;

import brainflow.image.iterators.ValueIterator;
import brainflow.utils.IDimension;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 4, 2009
 * Time: 12:21:17 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArray {

    public ValueIterator valueIterator();

    public double value(int i);

    public int length();

    public IDimension<Integer> dim();


}
