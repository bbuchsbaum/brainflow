package brainflow.image.data;

import brainflow.image.space.IImageSpace3D;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.math.Index3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 1:10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData3D extends IImageData, DataAccessor3D {


    public int indexOf(int x, int y, int z);

    public Index3D indexToGrid(int idx);

    public IImageSpace3D getImageSpace();

    public DataWriter3D createWriter(boolean clear);

    public Anatomy3D getAnatomy();
}
