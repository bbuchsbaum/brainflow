package brainflow.image.data;

import brainflow.image.space.ImageSpace2D;
import brainflow.image.space.IImageSpace2D;
import brainflow.image.interpolation.InterpolationFunction2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 1:10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData2D extends IImageData, DataGrid2D {

    //public double value(float x, float y, InterpolationFunction2D interp);

    //public double valueAtReal(float x, float y, InterpolationFunction2D interp);

    //public double valueAtWorld(float x, float y, InterpolationFunction2D interp);

    public double worldValue(float x, float y, InterpolationFunction2D interp);
    
    public int indexOf(int x, int y);

    public IImageSpace2D getImageSpace();

    public ImageBuffer2D createWriter(boolean clear);

}
