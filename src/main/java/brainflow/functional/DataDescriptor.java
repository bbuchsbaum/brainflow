package brainflow.functional;

import brainflow.image.io.ImageFormat;
import brainflow.image.io.ImageInfo;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Aug 10, 2004
 * Time: 3:10:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataDescriptor {

    String[] getContentNames();
    String[] getHeaderNames();
    ImageFormat getImageFormat();
    ImageInfo readImageInfo();
    FunctionalImageReader createFunctionalImageReader();


}
