package brainflow.image.interpolation;

import org.boxwood.array.IDataGrid3D;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public interface InterpolationFunction3D {

    public double interpolate(double dx, double dy, double dz, IDataGrid3D data);

}