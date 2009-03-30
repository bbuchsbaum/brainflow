package brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 26, 2009
 * Time: 10:12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IntImageBuffer2D extends ImageBuffer2D {

    public void setIntValue(int x, int y, int val);

    public IntImageData2D asImageData();


}
