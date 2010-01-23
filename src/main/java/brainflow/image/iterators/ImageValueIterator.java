package brainflow.image.iterators;

import brainflow.image.data.IImageData;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 23, 2010
 * Time: 11:32:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageValueIterator implements ValueIterator {

    IImageData data;

    int i = -1;

    int len;

    public ImageValueIterator(IImageData data) {
        this.data = data;
        len = data.length()-1;
    }

    @Override
    public double next() {
        i++;
        return data.value(i);
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
}
