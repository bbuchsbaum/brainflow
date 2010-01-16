package brainflow.image.anatomy;


import brainflow.image.space.Axis;
import brainflow.image.space.ICoordinateSpace3D;
import brainflow.image.space.IImageSpace3D;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 22, 2004
 * Time: 2:48:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpatialLoc3D implements BrainLoc {


    private SpatialLoc1D x;

    private SpatialLoc1D y;

    private SpatialLoc1D z;

    private Anatomy3D anatomy;


    public SpatialLoc3D(Anatomy3D _anatomy, double x, double y, double z) {
        anatomy = _anatomy;

        this.x = new SpatialLoc1D(anatomy.XAXIS, x);
        this.y = new SpatialLoc1D(anatomy.YAXIS, y);
        this.z = new SpatialLoc1D(anatomy.ZAXIS, z);

    }


    public SpatialLoc3D snapToBounds(IImageSpace3D space) {
        if (space.getAnatomy() != getAnatomy()) {
            throw new IllegalArgumentException("supplied image space must have same anatomy as BrainPoint instance, space argument " + space.getAnatomy() + " does not equal " + anatomy);
        }

        double newx = x.getValue();
        double newy = y.getValue();
        double newz = z.getValue();

        if (x.getValue() < space.getImageAxis(Axis.X_AXIS).getMinimum()) {
            newx = space.getImageAxis(Axis.X_AXIS).getMinimum();
        } else if (x.getValue() > space.getImageAxis(Axis.X_AXIS).getMaximum()) {
            newx = space.getImageAxis(Axis.X_AXIS).getMaximum();
        }

        if (y.getValue() < space.getImageAxis(Axis.Y_AXIS).getMinimum()) {
            newy = space.getImageAxis(Axis.Y_AXIS).getMinimum();
        } else if (y.getValue() > space.getImageAxis(Axis.Y_AXIS).getMaximum()) {
            newy = space.getImageAxis(Axis.Y_AXIS).getMaximum();
        }

        if (z.getValue() < space.getImageAxis(Axis.Z_AXIS).getMinimum()) {
            newz = space.getImageAxis(Axis.Z_AXIS).getMinimum();
        } else if (z.getValue() > space.getImageAxis(Axis.Z_AXIS).getMaximum()) {
            newz = space.getImageAxis(Axis.Z_AXIS).getMaximum();
        }

        SpatialLoc3D ret = new SpatialLoc3D(space.getAnatomy(), newx, newy, newz);
        return ret;


    }

    public Anatomy3D getAnatomy() {
        return anatomy;
    }


    public SpatialLoc3D replace(SpatialLoc1D pt) {
        if (pt.getAnatomy() == x.getAnatomy()) {
            return new SpatialLoc3D(getAnatomy(), pt.getValue(), y.getValue(), z.getValue());
        } else if (pt.getAnatomy() == y.getAnatomy()) {
            return new SpatialLoc3D(getAnatomy(), x.getValue(), pt.getValue(), z.getValue());
        } else if (pt.getAnatomy() == z.getAnatomy()) {
            return new SpatialLoc3D(getAnatomy(), x.getValue(), y.getValue(), pt.getValue());
        } else {
            throw new IllegalArgumentException("supplied point has incomaptible axis : " + pt.getAnatomy());
        }
    }

    public SpatialLoc3D convertTo(ICoordinateSpace3D otherSpace) {

        //todo what happens if "other" is not contained by "this"?

        SpatialLoc1D to_x = getValue(otherSpace.getAnatomy().XAXIS);
        SpatialLoc1D to_y = getValue(otherSpace.getAnatomy().YAXIS);
        SpatialLoc1D to_z = getValue(otherSpace.getAnatomy().ZAXIS);

        SpatialLoc1D retx = to_x.convertTo(otherSpace.getImageAxis(Axis.X_AXIS));
        SpatialLoc1D rety = to_y.convertTo(otherSpace.getImageAxis(Axis.Y_AXIS));
        SpatialLoc1D retz = to_z.convertTo(otherSpace.getImageAxis(Axis.Z_AXIS));


        return new SpatialLoc3D(otherSpace.getAnatomy(), retx.getValue(), rety.getValue(), retz.getValue());


    }

    public static SpatialLoc3D convertToWorld(SpatialLoc3D pt, IImageSpace3D space) {
        //todo this conversion seems like a hack ...
        pt = pt.convertTo(space);

        if (space.getAnatomy() != pt.getAnatomy()) {
            throw new IllegalArgumentException("SpatialLoc3D and ImageSpace arguments must have same anatomical orientation: " +
                    "point: " + pt.getAnatomy() + " space: " + space.getAnatomy());
        }

        double gridx = space.getImageAxis(Axis.X_AXIS).gridPosition(pt.getX());
        double gridy = space.getImageAxis(Axis.Y_AXIS).gridPosition(pt.getY());
        double gridz = space.getImageAxis(Axis.Z_AXIS).gridPosition(pt.getZ());

        float[] ret = space.gridToWorld((float) gridx, (float) gridy, (float) gridz);
        pt = new SpatialLoc3D(Anatomy3D.REFERENCE_ANATOMY, ret[0], ret[1], ret[2]);
        return pt;

    }

    public static SpatialLoc3D convertFromWorld(SpatialLoc3D worldpt, IImageSpace3D space) {

        float[] gridpos = space.worldToGrid((float) worldpt.getX(), (float) worldpt.getY(), (float) worldpt.getZ());
        double x1 = space.getImageAxis(Axis.X_AXIS).gridToReal(gridpos[0]);
        double y1 = space.getImageAxis(Axis.Y_AXIS).gridToReal(gridpos[1]);
        double z1 = space.getImageAxis(Axis.Z_AXIS).gridToReal(gridpos[2]);

        return new SpatialLoc3D(space.getAnatomy(), x1, y1, z1);
    }


    public SpatialLoc1D getValue(AnatomicalAxis axis) {
        if (axis.sameAxis(getAnatomy().XAXIS)) {
            return x;
        } else if (axis.sameAxis(getAnatomy().YAXIS)) {
            return y;
        } else if (axis.sameAxis(getAnatomy().ZAXIS)) {
            return z;
        } else {
            throw new IllegalArgumentException("axis : " + axis + " incompatible with Anatomy3D " + getAnatomy());
        }

    }

    /* public BrainPoint1D getValue(ImageAxis axis) {
 AnatomicalAxis anataxis = axis.getAnatomicalAxis();
 if (anataxis == getAnatomy().XAXIS) {
     return x;
 } else if (anataxis == getAnatomy().YAXIS) {
     return y;
 } else if (anataxis == getAnatomy().ZAXIS) {
     return z;
 } else if (anataxis.getFlippedAxis() == getAnatomy().XAXIS) {
     return x;
 } else if (anataxis.getFlippedAxis() == getAnatomy().YAXIS) {
     return y;
 } else if (anataxis.getFlippedAxis() == getAnatomy().ZAXIS) {
     return z;
 }  else {
     throw new IllegalArgumentException("axis : " + axis + " incompatible with Anatomy3D " + getAnatomy());
 }

}       */


    /*public BrainPoint1D getValueold(AnatomicalAxis axis) {
        //getAnatomy().matchAxis()

        if (axis.sameAxis(getAnatomy().XAXIS)) {
            return new BrainPoint1D(axis, getAnatomy().XAXIS.convertValue(axis, caxis.getMinimum(), caxis.getMaximum(), x.getValue()));
        } else if (axis.sameAxis(getAnatomy().YAXIS)) {
            return new BrainPoint1D(axis, getAnatomy().YAXIS.convertValue(axis, caxis.getMinimum(), caxis.getMaximum(), y.getValue()));
        } else if (axis.sameAxis(getAnatomy().ZAXIS)) {
            return new BrainPoint1D(axis, getAnatomy().ZAXIS.convertValue(axis, caxis.getMinimum(), caxis.getMaximum(), z.getValue()));
        } else {
            throw new IllegalArgumentException("illegal axis " + axis + " for this coordinate space");
        }
    } */


    public SpatialLoc1D getValue(AnatomicalAxis axis, double min, double max) {
        if (axis.sameAxis(getAnatomy().XAXIS)) {
            return new SpatialLoc1D(axis, getAnatomy().XAXIS.convertValue(axis, min, max, x.getValue()));
        } else if (axis.sameAxis(getAnatomy().YAXIS)) {
            return new SpatialLoc1D(axis, getAnatomy().YAXIS.convertValue(axis, min, max, y.getValue()));
        } else if (axis.sameAxis(getAnatomy().ZAXIS)) {
            return new SpatialLoc1D(axis, getAnatomy().ZAXIS.convertValue(axis, min, max, z.getValue()));
        } else {
            throw new IllegalArgumentException("illegal axis " + axis + " for this coordinate space");
        }
    }

    /*public void setValue(BrainPoint1D val) {
     if (val.getAnatomy().sameAxis(anatomy.XAXIS)) {
         x = anatomy.XAXIS.convertValue(val.getAnatomy(), val.evaluate());
     } else if (val.getAnatomy().sameAxis(anatomy.YAXIS)) {
         y = anatomy.YAXIS.convertValue(val.getAnatomy(), val.evaluate());
     } else if (val.getAnatomy().sameAxis(anatomy.ZAXIS)) {
         z = anatomy.ZAXIS.convertValue(val.getAnatomy(), val.evaluate());
     } else {
         throw new AssertionError();
     }

  }  */


    public double getX() {
        return x.getValue();
    }

    public double getY() {
        return y.getValue();
    }

    public double getZ() {
        return z.getValue();
    }


    public int getNumDimensions() {
        return 3;
    }

    public double getValue(int axisNum) {

        if (axisNum == 0) {
            return getX();
        } else if (axisNum == 1) {
            return getY();
        } else if (axisNum == 2) {
            return getZ();
        } else throw new IllegalArgumentException("illegal axis number " + axisNum);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpatialLoc3D that = (SpatialLoc3D) o;

        if (!anatomy.equals(that.anatomy)) return false;
        if (!x.equals(that.x)) return false;
        if (!y.equals(that.y)) return false;
        if (!z.equals(that.z)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        result = 31 * result + z.hashCode();
        result = 31 * result + anatomy.hashCode();
        return result;
    }

    public SpatialLoc3D clone() {
        return new SpatialLoc3D(getAnatomy(), x.getValue(), y.getValue(), z.getValue());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAnatomy().XAXIS).append("-").append(getAnatomy().YAXIS).append("-").append(getAnatomy().ZAXIS);
        sb.append("X: ").append(getX()).append(" Y: ").append(getY()).append(" Z: ").append(getZ());
        return sb.toString();
    }

    public static void main(String[] args) {
        SpatialLoc3D a = new SpatialLoc3D(Anatomy3D.AXIAL_LAI, 12, 50, 12);
        SpatialLoc3D b = new SpatialLoc3D(Anatomy3D.AXIAL_LPI, 12, 50, 12);


    }
}