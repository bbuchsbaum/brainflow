package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.space.IImageSpace3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2008
 * Time: 10:07:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataGrid3D extends DataGrid {

    
    public double value(float x, float y, float z, InterpolationFunction3D interp);

    public double worldValue(float worldx, float worldy, float worldz, InterpolationFunction3D interp);

    public double value(int x, int y, int z);

    public IImageSpace3D getImageSpace();



}
