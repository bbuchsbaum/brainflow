package brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 26, 2009
 * Time: 10:11:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IntImageBuffer3D extends ImageBuffer3D {

    public void setIntValue(int x, int y, int z, int val);

    public IntImageData3D asImageData();


}
