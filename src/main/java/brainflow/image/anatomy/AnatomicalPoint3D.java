package brainflow.image.anatomy;


import brainflow.image.axis.CoordinateAxis;
import brainflow.image.space.Axis;
import brainflow.image.space.CoordinateSpace3D;
import brainflow.image.space.ICoordinateSpace3D;
import brainflow.image.space.IImageSpace3D;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 22, 2004
 * Time: 2:48:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnatomicalPoint3D implements AnatomicalPoint {


    private ICoordinateSpace3D space;

    private AnatomicalPoint1D x;

    private AnatomicalPoint1D y;

    private AnatomicalPoint1D z;


    public AnatomicalPoint3D(Anatomy3D _anatomy, double x, double y, double z) {

        space = new CoordinateSpace3D(_anatomy);
        this.x = new AnatomicalPoint1D(space.getImageAxis(Axis.X_AXIS), x);
        this.y = new AnatomicalPoint1D(space.getImageAxis(Axis.Y_AXIS), y);
        this.z = new AnatomicalPoint1D(space.getImageAxis(Axis.Z_AXIS), z);

    }

    public AnatomicalPoint3D(ICoordinateSpace3D space, double x, double y, double z) {

        this.space = space;
        this.x = new AnatomicalPoint1D(space.getImageAxis(Axis.X_AXIS), x);
        this.y = new AnatomicalPoint1D(space.getImageAxis(Axis.Y_AXIS), y);
        this.z = new AnatomicalPoint1D(space.getImageAxis(Axis.Z_AXIS), z);
    }

    public AnatomicalPoint3D snapToBounds() {
        //todo this is not a very good method

        double newx = x.getValue();
        double newy = y.getValue();
        double newz = z.getValue();

        boolean changed = false;

        if (x.getValue() < space.getImageAxis(Axis.X_AXIS).getMinimum()) {
            newx = space.getImageAxis(Axis.X_AXIS).getMinimum();
            changed = true;
        } else if (x.getValue() > space.getImageAxis(Axis.X_AXIS).getMaximum()) {
            newx = space.getImageAxis(Axis.X_AXIS).getMaximum();
            changed = true;
        }

        if (y.getValue() < space.getImageAxis(Axis.Y_AXIS).getMinimum()) {
            newy = space.getImageAxis(Axis.Y_AXIS).getMinimum();
            changed = true;
        } else if (y.getValue() > space.getImageAxis(Axis.Y_AXIS).getMaximum()) {
            newy = space.getImageAxis(Axis.Y_AXIS).getMaximum();
            changed = true;
        }

        if (z.getValue() < space.getImageAxis(Axis.Z_AXIS).getMinimum()) {
            newz = space.getImageAxis(Axis.Z_AXIS).getMinimum();
            changed = true;
        } else if (z.getValue() > space.getImageAxis(Axis.Z_AXIS).getMaximum()) {
            newz = space.getImageAxis(Axis.Z_AXIS).getMaximum();
            changed = true;
        }

        if (changed) {
            AnatomicalPoint3D ret = new AnatomicalPoint3D(space, newx, newy, newz);
            return ret;

        } else {
            return this;
        }


    }

    public Anatomy3D getAnatomy() {
        return space.getAnatomy();
    }

    public ICoordinateSpace3D getSpace() {
        return space;
    }

    public AnatomicalPoint3D replace(AnatomicalPoint1D pt) {
        if (pt.getAnatomy() == x.getAnatomy()) {
            return new AnatomicalPoint3D(space, pt.getValue(), y.getValue(), z.getValue());
        } else if (pt.getAnatomy() == y.getAnatomy()) {
            return new AnatomicalPoint3D(space, x.getValue(), pt.getValue(), z.getValue());
        } else if (pt.getAnatomy() == z.getAnatomy()) {
            return new AnatomicalPoint3D(space, x.getValue(), y.getValue(), pt.getValue());
        } else {
            throw new IllegalArgumentException("supplied point has incomaptible axis : " + pt.getAnatomy());
        }
    }

    public AnatomicalPoint3D convertTo(ICoordinateSpace3D other) {

        //todo what happens if "other" is not contained by "this"?

        AnatomicalPoint1D to_x = get(other.getAnatomy().XAXIS);
        AnatomicalPoint1D to_y = get(other.getAnatomy().YAXIS);
        AnatomicalPoint1D to_z = get(other.getAnatomy().ZAXIS);

        AnatomicalPoint1D retx = to_x.convertTo(other.getImageAxis(Axis.X_AXIS));
        AnatomicalPoint1D rety = to_y.convertTo(other.getImageAxis(Axis.Y_AXIS));
        AnatomicalPoint1D retz = to_z.convertTo(other.getImageAxis(Axis.Z_AXIS));


        return new AnatomicalPoint3D(other, retx.getValue(), rety.getValue(), retz.getValue());


    }

    public static AnatomicalPoint3D convertToWorld(AnatomicalPoint3D pt, IImageSpace3D space) {
        if (space.getAnatomy() != pt.getAnatomy()) {
            throw new IllegalArgumentException("AnatomicalPoint3D and ImageSpace arguments must have same anatomical orientation");
        }
        //pt = pt.convertTo(space);

        double gridx = space.getImageAxis(Axis.X_AXIS).gridPosition(pt.getX());
        double gridy = space.getImageAxis(Axis.Y_AXIS).gridPosition(pt.getY());
        double gridz = space.getImageAxis(Axis.Z_AXIS).gridPosition(pt.getZ());

        float[] ret = space.gridToWorld((float) gridx, (float) gridy, (float) gridz);
        pt = new AnatomicalPoint3D(Anatomy3D.REFERENCE_ANATOMY, ret[0], ret[1], ret[2]);
        return pt;

    }

    public static AnatomicalPoint3D convertFromWorld(AnatomicalPoint3D worldpt, IImageSpace3D space) {

        float[] gridpos = space.worldToGrid((float) worldpt.getX(), (float) worldpt.getY(), (float) worldpt.getZ());
        double x1 = space.getImageAxis(Axis.X_AXIS).gridToReal(gridpos[0]);
        double y1 = space.getImageAxis(Axis.Y_AXIS).gridToReal(gridpos[1]);
        double z1 = space.getImageAxis(Axis.Z_AXIS).gridToReal(gridpos[2]);

        return new AnatomicalPoint3D(space, x1, y1, z1);
    }


    private AnatomicalPoint1D get(AnatomicalAxis axis) {
        if (axis.sameAxis(getAnatomy().XAXIS)) {
            return x;
        } else if (axis.sameAxis(getAnatomy().YAXIS)) {
            return y;
        } else if (axis.sameAxis(getAnatomy().ZAXIS)) {
            return z;
        } else {
            throw new IllegalArgumentException("axis : " + axis + " incompatible with CoordinateSpace " + space);
        }

    }


    public AnatomicalPoint1D getValue(AnatomicalAxis axis) {
        CoordinateAxis caxis = getSpace().getImageAxis(axis, true);
        if (axis.sameAxis(getAnatomy().XAXIS)) {
            return new AnatomicalPoint1D(axis, getAnatomy().XAXIS.convertValue(axis, caxis.getMinimum(), caxis.getMaximum(), x.getValue()));
        } else if (axis.sameAxis(getAnatomy().YAXIS)) {
            return new AnatomicalPoint1D(axis, getAnatomy().YAXIS.convertValue(axis, caxis.getMinimum(), caxis.getMaximum(), y.getValue()));
        } else if (axis.sameAxis(getAnatomy().ZAXIS)) {
            return new AnatomicalPoint1D(axis, getAnatomy().ZAXIS.convertValue(axis, caxis.getMinimum(), caxis.getMaximum(), z.getValue()));
        } else {
            throw new IllegalArgumentException("illegal axis " + axis + " for this coordinate space");
        }
    }


    public AnatomicalPoint1D getValue(AnatomicalAxis axis, double min, double max) {
        if (axis.sameAxis(getAnatomy().XAXIS)) {
            return new AnatomicalPoint1D(axis, getAnatomy().XAXIS.convertValue(axis, min, max, x.getValue()));
        } else if (axis.sameAxis(getAnatomy().YAXIS)) {
            return new AnatomicalPoint1D(axis, getAnatomy().YAXIS.convertValue(axis, min, max, y.getValue()));
        } else if (axis.sameAxis(getAnatomy().ZAXIS)) {
            return new AnatomicalPoint1D(axis, getAnatomy().ZAXIS.convertValue(axis, min, max, z.getValue()));
        } else {
            throw new IllegalArgumentException("illegal axis " + axis + " for this coordinate space");
        }
    }

    /*public void setValue(AnatomicalPoint1D val) {
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


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnatomicalPoint3D)) return false;

        AnatomicalPoint3D that = (AnatomicalPoint3D) o;

        if (space != null ? !space.equals(that.space) : that.space != null) return false;
        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        if (z != null ? !z.equals(that.z) : that.z != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (space != null ? space.hashCode() : 0);
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }

    public AnatomicalPoint3D clone() {
        return new AnatomicalPoint3D(space, x.getValue(), y.getValue(), z.getValue());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAnatomy().XAXIS).append("-").append(getAnatomy().YAXIS).append("-").append(getAnatomy().ZAXIS);
        sb.append("X: ").append(getX()).append(" Y: ").append(getY()).append(" Z: ").append(getZ());
        return sb.toString();
    }

    public static void main(String[] args) {
        AnatomicalPoint3D a = new AnatomicalPoint3D(Anatomy3D.AXIAL_LAI, 12, 50, 12);
        AnatomicalPoint3D b = new AnatomicalPoint3D(Anatomy3D.AXIAL_LPI, 12, 50, 12);


    }
}
