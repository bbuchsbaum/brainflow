package brainflow.image.data;

import brainflow.array.IArray3D;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.math.Index3D;
import brainflow.utils.Dimension3D;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 1:10:02 PM
 * To change this template use File | Settings | File Templates.
 */


public interface IImageData3D extends IImageData, IArray3D {

   
    public double worldValue(float wx, float wy, float wz, InterpolationFunction3D interp);

    public Index3D indexToGrid(int idx);

    public IImageSpace3D getImageSpace();

    public ImageBuffer3D createBuffer(boolean clear);

    public Anatomy3D getAnatomy();


}
