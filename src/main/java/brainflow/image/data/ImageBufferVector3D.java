package brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 28, 2009
 * Time: 4:56:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageBufferVector3D extends ImageBuffer, IImageDataVector3D {

    public void setValue(int x, int y, int z, int t, double val);
}
