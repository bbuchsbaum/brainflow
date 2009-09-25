/*
 * TrilinearInterpolator.java
 *
 * Created on May 13, 2003, 4:08 PM
 */

package brainflow.image.interpolation;

import org.boxwood.array.IDataGrid3D;

/**
 * @author bradley
 */
public class TrilinearInterpolator implements InterpolationFunction3D {

    /**
     * Creates a new instance of TrilinearInterpolator
     */
    public TrilinearInterpolator() {
    }

    public double interpolate(double dx, double dy, double dz, IDataGrid3D data) {
        double a, b, c, d, e, f;
        if (dx < 0 || dy < 0 || dz < 0)
            return 0;


        if (dx > (data.dim().getDim(0) - 1) ) {
            return 0;
        }
        if (dy > (data.dim().getDim(1) - 1) ) {
            return 0;
        }
        if (dz > (data.dim().getDim(2) - 1) ) {
            return 0;
        }


        int x_up = (int) Math.ceil(dx);
        int x_down = (int) Math.floor(dx);
        int y_up = (int) Math.ceil(dy);
        int y_down = (int) Math.floor(dy);
        int z_up = (int) Math.ceil(dz);
        int z_down = (int) Math.floor(dz);

        if (x_up == x_down) {
            a = 0;
            d = 1;
        } else {
            a = dx - x_down;
            d = x_up - dx;
        }
        if (y_up == y_down) {
            b = 0;
            e = 1;
        } else {
            b = dy - y_down;
            e = y_up - dy;
        }
        if (z_up == z_down) {
            c = 0;
            f = 1;
        } else {
            c = dz - z_down;
            f = z_up - dz;
        }

        /*Interpolate */

        int xidx = x_down;
        int yidx = y_down;
        int zidx = z_down;

        double total = data.value(xidx, yidx, zidx) * d * e * f;
        double val;

        if (x_up != x_down) {
            xidx++;
            val = data.value(xidx, yidx, zidx);
            total += val * a * e * f;
        }

        if (y_up != y_down) {
            yidx++;
            xidx = x_down;

            val = data.value(xidx, yidx, zidx);
            total += val * d * b * f;

            if (x_up != x_down) {
                xidx++;
                val = data.value(xidx, yidx, zidx);
                total += val * a * b * f;
            }
        }

        if (z_up != z_down) {
            zidx++;
            yidx = y_down;
            xidx = x_down;
            val = data.value(xidx, yidx, zidx);
            total += val * d * e * c;

            if (x_up != x_down) {
                xidx++;
                val = data.value(xidx, yidx, zidx);
                total += val * a * e * c;
            }

            if (y_up != y_down) {
                yidx++;
                xidx = x_down;
                val = data.value(xidx, yidx, zidx);
                total += val * d * b * c;

                if (x_up != x_down) {
                    xidx++;
                    val = data.value(xidx, yidx, zidx);
                    ;
                    total += val * a * b * c;
                }
            }
        }
        return total;

    }


}
