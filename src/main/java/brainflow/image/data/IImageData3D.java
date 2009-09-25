package brainflow.image.data;

import brainflow.image.space.IImageSpace3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.math.Index3D;
import org.boxwood.array.IDataGrid3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 1:10:02 PM
 * To change this template use File | Settings | File Templates.
 */


public interface IImageData3D extends IImageData, IDataGrid3D {

    public double value(float x, float y, float z, InterpolationFunction3D interp);

    //public double valueAtReal(float rx, float ry, float rz, InterpolationFunction3D interp);

    //public double valueAtWorld(float wx, float wy, float wz, InterpolationFunction3D interp);

    public double worldValue(float wx, float wy, float wz, InterpolationFunction3D interp);

    public int indexOf(int x, int y, int z);

    public Index3D indexToGrid(int idx);

    public IImageSpace3D getImageSpace();

    public ImageBuffer3D createWriter(boolean clear);

    public Anatomy3D getAnatomy();
}
