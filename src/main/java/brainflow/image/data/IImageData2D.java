package brainflow.image.data;

import brainflow.image.space.ImageSpace2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 1:10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData2D extends IImageData, DataGrid2D {


    public int indexOf(int x, int y);

    public ImageSpace2D getImageSpace();

    public ImageBuffer2D createWriter(boolean clear);

}
