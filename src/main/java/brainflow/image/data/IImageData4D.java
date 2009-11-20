package brainflow.image.data;

import brainflow.array.IArray4D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.interpolation.InterpolationFunction4D;
import brainflow.image.space.IImageSpace3D;
import brainflow.math.Index3D;
import brainflow.utils.Dimension3D;
import brainflow.utils.Dimension4D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 22, 2009
 * Time: 9:17:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData4D extends IImageData, IArray4D {


    public double worldValue(float wx, float wy, float wz, float wt, InterpolationFunction4D interp);
   
    //public Index4D indexToGrid(int idx);

    public IImageSpace3D getImageSpace();

    public ImageBuffer4D createBuffer(boolean clear);

    public Anatomy3D getAnatomy();


}
