package brainflow.image.data;

import brainflow.array.IArray2D;
import brainflow.image.space.IImageSpace2D;
import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.utils.Dimension2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 1:10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData2D extends IImageData, IArray2D {

    public double worldValue(float x, float y, InterpolationFunction2D interp);

    public IImageSpace2D getImageSpace();

    public ImageBuffer2D createBuffer(boolean clear);



}
