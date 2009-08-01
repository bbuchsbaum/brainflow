package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.space.IImageSpace2D;
import brainflow.utils.Dimension2D;
import brainflow.utils.IDimension;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2008
 * Time: 10:05:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataGrid2D extends DataGrid {

    public double value(float x, float y, InterpolationFunction2D interp);

    //public double worldValue(double realx, double realy, InterpolationFunction2D interp);

    public double value(int i, int j);

    @Override
    Dimension2D<Integer> getDimensions();
}
