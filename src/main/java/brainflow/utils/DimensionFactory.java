package brainflow.utils;

import java.util.List;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 21, 2007
 * Time: 12:02:45 PM
 */
public class DimensionFactory {

    public static IDimension create(List<Integer> dims) {
        Integer[] ret = new Integer[dims.size()];
        dims.toArray(ret);
        return DimensionFactory.create(ret);
    }


    public static IDimension create(Integer... dims) {
        if (dims.length == 1) {
            return new Dimension1D<Integer>(dims);
        }
        if (dims.length == 2) {
            return new Dimension2D<Integer>(dims);
        }

        if (dims.length == 3) {
            return new Dimension3D<Integer>(dims);
        }

        if (dims.length == 4) {
            return new Dimension4D<Integer>(dims);
        }

        if (dims.length == 5) {
            return new Dimension5D<Integer>(dims);
        } else throw new IllegalArgumentException("cannt create object with " + dims.length + " dimensions");
    }


    public static IDimension create(Double... dims) {
        if (dims.length == 1) {
            return new Dimension1D<Double>(dims);
        }
        if (dims.length == 2) {
            return new Dimension2D<Double>(dims);
        }

        if (dims.length == 3) {
            return new Dimension3D<Double>(dims);
        }

        if (dims.length == 4) {
            return new Dimension4D<Double>(dims);
        }

        if (dims.length == 5) {
            return new Dimension5D<Double>(dims);
        } else throw new IllegalArgumentException("cannt create object with " + dims.length + " dimensions");

    }
}
