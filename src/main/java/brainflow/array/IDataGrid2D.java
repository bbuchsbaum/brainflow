package brainflow.array;

import brainflow.image.interpolation.InterpolationFunction2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2008
 * Time: 10:05:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IDataGrid2D extends IDataGrid, IArray2D {

    public double value(double x, double y, InterpolationFunction2D interp);

}
