package brainflow.image.interpolation;

import brainflow.array.IArray4D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 28, 2009
 * Time: 4:19:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface InterpolationFunction4D {

    public double interpolate(double dx, double dy, double dz, double dt, IArray4D data);
}
