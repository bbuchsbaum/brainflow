package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.space.ImageSpace2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2008
 * Time: 10:05:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataGrid2D extends DataGrid {

    public double value(double x, double y, InterpolationFunction2D interp);

    public double worldValue(double realx, double realy, InterpolationFunction2D interp);

    public double value(int x, int y);

    public ImageSpace2D getImageSpace();

    
}
