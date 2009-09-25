package brainflow.image.interpolation;


import org.boxwood.array.IDataGrid2D;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public interface InterpolationFunction2D {

    public double interpolate(double dx, double dy, IDataGrid2D data);
}