package brainflow.array;


import brainflow.image.interpolation.InterpolationFunction4D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 22, 2009
 * Time: 9:19:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IDataGrid4D extends IDataGrid, IArray4D {

    public double value(double x, double y, double z, double t, InterpolationFunction4D interp);

    
}
