package brainflow.app;

import brainflow.image.io.IImageDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 19, 2004
 * Time: 4:47:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LoadableImageReceiver {

    public boolean canAccept(IImageDataSource[] img);
}
