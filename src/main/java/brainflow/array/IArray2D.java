package brainflow.array;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.utils.Dimension2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 4, 2009
 * Time: 12:24:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArray2D extends IArray {
    
    public double value(double x, double y, InterpolationFunction2D interp);

    public double value(int i, int j);

    public int indexOf(int i, int j);

    @Override
    Dimension2D<Integer> dim();


}
