package brainflow.array;

import brainflow.image.interpolation.InterpolationFunction4D;
import brainflow.utils.Dimension4D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 4, 2009
 * Time: 12:26:33 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArray4D extends IArray {
    
    public double value(double x, double y, double z, double v, InterpolationFunction4D interp);

    public double value(int i, int j, int k, int m);

    public int indexOf(int i, int j, int k, int m);
                                                       
    @Override
    Dimension4D<Integer> dim();
}
