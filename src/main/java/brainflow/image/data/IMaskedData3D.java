package brainflow.image.data;

import brainflow.image.iterators.BooleanIterator;
import brainflow.image.iterators.ValueIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 9, 2007
 * Time: 11:44:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMaskedData3D extends IImageData3D, IMaskedData {


    public boolean isTrue(int x, int y, int z);

    @Override
    public BooleanIterator valueIterator();
}
