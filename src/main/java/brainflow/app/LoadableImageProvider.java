package brainflow.app;

import brainflow.image.io.IImageDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 20, 2004
 * Time: 2:49:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LoadableImageProvider {

    public IImageDataSource[] requestLoadableImages();
}
