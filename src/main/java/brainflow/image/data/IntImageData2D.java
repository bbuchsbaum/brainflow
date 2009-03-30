package brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 26, 2009
 * Time: 9:54:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IntImageData2D extends IImageData2D  {

    public int getIntValue(int x, int y);

    ImageBuffer2D createWriter(boolean clear);
}
