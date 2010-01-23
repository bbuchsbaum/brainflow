package brainflow.array;

import brainflow.image.iterators.ValueIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 27, 2009
 * Time: 9:56:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArrayValueIterator implements ValueIterator {

    private IArray array;

    private int i=-1;

    private int len;

    public ArrayValueIterator(IArray array) {
        this.array=array;
        len=array.length()-1;
    }

    @Override
    public double next() {
        i++;
        return array.value(i);

    }

    @Override
    public int index() {
        return i;
    }

    @Override
    public void advance() {
        i++;
    }

    @Override
    public boolean hasNext() {
        return i < len;
    }
}
