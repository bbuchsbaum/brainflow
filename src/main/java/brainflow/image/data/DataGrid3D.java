package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.space.IImageSpace3D;
import brainflow.utils.IDimension;
import brainflow.utils.Dimension2D;
import brainflow.utils.Dimension3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2008
 * Time: 10:07:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataGrid3D extends DataGrid {

    
    public double value(float x, float y, float z, InterpolationFunction3D interp);

    public double value(int i, int j, int k);

    public DataGrid3D subGrid(int x0, int x1, int y0, int y1, int z0, int z1);


    @Override
    Dimension3D<Integer> getDimensions();
}
