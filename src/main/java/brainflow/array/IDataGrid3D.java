package brainflow.array;

import brainflow.image.interpolation.InterpolationFunction3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2008
 * Time: 10:07:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IDataGrid3D extends IDataGrid, IArray3D {

    
    public double value(float x, float y, float z, InterpolationFunction3D interp);




 
}
