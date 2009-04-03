/*
 * NearestNeighborInterpolator.java
 *
 * Created on May 13, 2003, 10:48 AM
 */

package brainflow.image.interpolation;

import brainflow.image.data.DataGrid3D;
import brainflow.image.space.Axis;

/**
 * @author bradley
 */
public class NearestNeighborInterpolator implements InterpolationFunction3D {

    /**
     * Creates a new instance of NearestNeighborInterpolator
     */
    public NearestNeighborInterpolator() {
    }

    public double interpolate(double dx, double dy, double dz, DataGrid3D data) {
        int x_up = (int) Math.floor(dx + .5);
        int y_up = (int) Math.floor(dy + .5);
        int z_up = (int) Math.floor(dz + .5);

        double total;

        //todo slow ....
        int d1 = data.getImageSpace().getDimension(Axis.X_AXIS);
        int d2 = data.getImageSpace().getDimension(Axis.Y_AXIS);
        int d3 = data.getImageSpace().getDimension(Axis.Z_AXIS);


        if (x_up >= d1)
            //x_up = data.getDisplaySpace().getDimensions()[0]-1;
            return 0;
        else if (x_up < 0)
            //x_up = 0;
            return 0;
        if (y_up >= d2) {
            return 0;
        }
        //y_up = data.getDisplaySpace().getDimensions()[1]-1;
        //y_up=0;
        else if (y_up < 0) {
            return 0;
            //y_up = 0;
        }

        if (z_up >= d3)
            //z_up = data.getDisplaySpace().getDimensions()[2]-1;
            return 0;
        else if (z_up < 0)
            //z_up = 0;
            return 0;

        total = data.value(x_up, y_up, z_up);

        return total;
    }

}
