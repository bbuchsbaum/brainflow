package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.space.IImageSpace3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 22, 2009
 * Time: 9:19:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataGrid4D extends DataGrid {

    //public double value(float x, float y, float z, float t, InterpolationFunction3D interp);

    //public double worldValue(float worldx, float worldy, float worldz, InterpolationFunction3D interp);

    public double value(int i, int j, int k, int m);

    //public IImageSpace3D getImageSpace();


}
