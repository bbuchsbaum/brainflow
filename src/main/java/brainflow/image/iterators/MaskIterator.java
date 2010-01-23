package brainflow.image.iterators;

import brainflow.image.data.IMaskedData;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 23, 2010
 * Time: 11:04:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskIterator implements BooleanIterator {

    private IMaskedData data;

    private int i=-1;

    private int len;

    public MaskIterator(IMaskedData data) {
        this.data = data;
        len=data.length()-1;
    }

    @Override
    public boolean nextBoolean() {
         i++;
        return data.isTrue(i);
    }

    @Override
    public void advance() {
       i++;
    }

    @Override
    public boolean hasNext() {
        return i < len;
    }

    @Override
    public int index() {
        return i;
    }

    @Override
    public double next() {
        i++;
        return data.value(i);
    }
}
