/*
 * NearestNeighborInterpolator.java
 *
 * Created on May 13, 2003, 10:48 AM
 */

package brainflow.image.interpolation;

import brainflow.array.IArray3D;

/**
 * @author bradley
 */
public class NearestNeighborInterpolator implements InterpolationFunction3D {

    /**
     * Creates a new instance of NearestNeighborInterpolator
     */
    public NearestNeighborInterpolator() {
    }

    public double interpolate(double dx, double dy, double dz, IArray3D data) {
        int x_up = (int) Math.floor(dx + .5);
        int y_up = (int) Math.floor(dy + .5);
        int z_up = (int) Math.floor(dz + .5);

        double total;

        //todo slow ....
        int d1 = data.dim().getDim(0);
        int d2 = data.dim().getDim(1);
        int d3 = data.dim().getDim(2);


        if (x_up >= d1)
            //x_up = data.getDisplaySpace().dim()[0]-1;
            return 0;
        else if (x_up < 0)
            //x_up = 0;
            return 0;
        if (y_up >= d2) {
            return 0;
        }
        //y_up = data.getDisplaySpace().dim()[1]-1;
        //y_up=0;
        else if (y_up < 0) {
            return 0;
            //y_up = 0;
        }

        if (z_up >= d3)
            //z_up = data.getDisplaySpace().dim()[2]-1;
            return 0;
        else if (z_up < 0)
            //z_up = 0;
            return 0;

        total = data.value(x_up, y_up, z_up);

        return total;
    }

}
