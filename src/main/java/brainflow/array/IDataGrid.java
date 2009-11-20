package brainflow.array;

import brainflow.image.iterators.ValueIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2008
 * Time: 10:04:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IDataGrid extends IArray {
  

    public ValueIterator valueIterator();


}
