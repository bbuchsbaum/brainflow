package brainflow.image.space;

import brainflow.image.iterators.XYZIterator;
import brainflow.image.LinearSet1D;
import brainflow.image.LinearSet3D;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.axis.ImageAxis;
import brainflow.image.axis.AxisRange;
import brainflow.image.axis.CoordinateAxis;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 12, 2004
 * Time: 2:15:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Space {

    public static IImageSpace3D createImageSpace(int xdim, int ydim, int zdim, int dx, int dy, int dz) {
        ImageAxis xaxis = new ImageAxis(0, xdim*dx, AnatomicalAxis.LEFT_RIGHT, xdim);
        ImageAxis yaxis = new ImageAxis(0, ydim*dy, AnatomicalAxis.POSTERIOR_ANTERIOR, ydim);
        ImageAxis zaxis = new ImageAxis(0, zdim*dz, AnatomicalAxis.INFERIOR_SUPERIOR, zdim);

        return new ImageSpace3D(xaxis, yaxis, zaxis);
    }

    public static boolean containsPoint(IImageSpace3D space, AnatomicalPoint3D pt) {
        AnatomicalPoint3D cpt = pt.convertTo(space);
        if (space.getImageAxis(Axis.X_AXIS).contains(cpt.getValue(Axis.X_AXIS.getId())) &&
            space.getImageAxis(Axis.Y_AXIS).contains(cpt.getValue(Axis.Y_AXIS.getId())) &&
            space.getImageAxis(Axis.Z_AXIS).contains(cpt.getValue(Axis.Z_AXIS.getId()))) {
            return true;
        }

        return false;


    }

    


    public static IImageSpace createImageSpace(ImageAxis ... axes) {

        if (axes.length > 3) {
            throw new IllegalArgumentException("Cannot create an IImageSpace with more than three axes");
        }

        if (axes.length == 1) {
            //return new ImageSpace1D();
            throw new UnsupportedOperationException();
        }
        else if (axes.length == 2) {
            return new ImageSpace2D(axes[0], axes[1]);
        }
        else if (axes.length == 3) {
            return new ImageSpace3D(axes[0], axes[1], axes[2]);
        }

        else throw new RuntimeException();

    }

    public static ICoordinateSpace createCoordinateSpace(CoordinateAxis... axes) {

        if (axes.length > 3) {
            throw new IllegalArgumentException("Cannot create an IImageSpace with more than three axes");
        }

        if (axes.length == 1) {
            // todo implement CoordinateSpace1D  ??
            throw new UnsupportedOperationException();
        }
        else if (axes.length == 2) {
            // todo implement CoordinateSpace2D
            throw new UnsupportedOperationException();
        }
        else if (axes.length == 3) {
            return new CoordinateSpace3D(axes[0], axes[1], axes[2]);
        }

        else throw new RuntimeException();

    }


    public static XYZIterator createXYZiterator(ImageSpace3D space) {
        AxisRange a1 = space.getImageAxis(Axis.X_AXIS).getRange();
        AxisRange a2 = space.getImageAxis(Axis.Y_AXIS).getRange();
        AxisRange a3 = space.getImageAxis(Axis.Z_AXIS).getRange();


        LinearSet1D xset = new LinearSet1D(a1.getBeginning().getValue(), a1.getEnd().getValue(), space.getDimension(Axis.X_AXIS));
        LinearSet1D yset = new LinearSet1D(a2.getBeginning().getValue(), a2.getEnd().getValue(), space.getDimension(Axis.Y_AXIS));
        LinearSet1D zset = new LinearSet1D(a3.getBeginning().getValue(), a3.getEnd().getValue(), space.getDimension(Axis.Z_AXIS));
        LinearSet3D pset = new LinearSet3D(xset, yset, zset);
        return pset.iterator();


    }
}
