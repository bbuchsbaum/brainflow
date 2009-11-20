package brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 26, 2009
 * Time: 9:02:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IntImageData3D extends IImageData3D {

    public int getIntValue(int x, int y, int z);

    public IntImageBuffer3D createBuffer(boolean clear);


}
