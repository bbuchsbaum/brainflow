package brainflow.array;

import brainflow.image.iterators.ValueIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 4, 2009
 * Time: 10:03:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConstantValueIterator implements ValueIterator {

    private double value;

    private int i=0;

    private int len;

    public ConstantValueIterator(double value, int len) {
        this.value=value;
        this.len=len;
    }

    @Override
    public double next() {
        i++;
        return value;

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


