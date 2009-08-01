package brainflow.image.operations;

import brainflow.image.data.IImageData;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 29, 2009
 * Time: 12:54:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageFunctor<T extends IImageData> {


    public T process(T input);
}
