package brainflow.array;

import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.utils.Dimension3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 4, 2009
 * Time: 12:25:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArray3D extends IArray {

    public double value(float x, float y, float z, InterpolationFunction3D interp);
    
    public double value(int i, int j, int k);

    public int indexOf(int i, int j, int k);

    @Override
    Dimension3D<Integer> dim();
}
