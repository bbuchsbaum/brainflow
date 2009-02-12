package brainflow.image;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Jul 3, 2003
 * Time: 3:57:42 PM
 * To change this template use Options | File Templates.
 */
public interface IndexConverter2D {

    public int[] convertXY(int x, int y);

    public int[] convertXY(int x, int y, int[] out);

}
