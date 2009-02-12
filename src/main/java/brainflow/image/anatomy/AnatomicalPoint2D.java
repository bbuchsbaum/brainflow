package brainflow.image.anatomy;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.axis.ImageAxis;

import java.beans.PropertyChangeSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 12:29:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnatomicalPoint2D implements AnatomicalPoint {

    private Anatomy2D anatomy;

    private double x;
    private double y;


    private PropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    public AnatomicalPoint2D(Anatomy2D _anatomy, double x, double y) {
        this.x = x;
        this.y = y;

        anatomy = _anatomy;
    }


    
    public static AnatomicalPoint2D convertPoint(ImageSpace2D fromSpace, AnatomicalPoint2D from, Anatomy2D to) {

        ImageAxis to_x = fromSpace.getImageAxis(to.XAXIS, true);
        ImageAxis to_y = fromSpace.getImageAxis(to.YAXIS, true);

        AnatomicalPoint1D a1 = from.getValue(to_x.getAnatomicalAxis(), to_x.getMinimum(), to_x.getMaximum());
        AnatomicalPoint1D a2 = from.getValue(to_y.getAnatomicalAxis(), to_y.getMinimum(), to_y.getMaximum());

        assert a1.getAnatomy() == to.XAXIS;
        assert a2.getAnatomy() == to.YAXIS;


        return new AnatomicalPoint2D(to, a1.getValue(), a2.getValue());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        double oldProperty = this.x;
        this.x = x;
        changeSupport.firePropertyChange("zero", oldProperty, this.x);

    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        double oldProperty = this.y;
        this.y = y;
        changeSupport.firePropertyChange("zero", oldProperty, this.y);
    }

    public Anatomy2D getAnatomy() {
        return anatomy;
    }

    public int getNumDimensions() {
        return 2;
    }

    public double getValue(int axisNum) {

        if (axisNum == 0) {
            return getX();
        } else if (axisNum == 1) {
            return getY();
        } else throw new IllegalArgumentException("illegal axis number " + axisNum);


    }

    public AnatomicalPoint1D getValue(AnatomicalAxis axis) {
        if (axis.sameDirection(anatomy.XAXIS)) {
            return new AnatomicalPoint1D(axis, x);
        } else if (axis.sameDirection(anatomy.YAXIS)) {
            return new AnatomicalPoint1D(axis, y);
        } else {
            throw new IllegalArgumentException("axis " + axis + " is not a member of this coordinate system : " + anatomy);
        }
    }


    public AnatomicalPoint1D getValue(AnatomicalAxis axis, double min, double max) {
        if (axis.sameAxis(anatomy.XAXIS)) {
            return new AnatomicalPoint1D(axis, anatomy.XAXIS.convertValue(axis, min, max, x));
        } else if (axis.sameAxis(anatomy.YAXIS)) {
            return new AnatomicalPoint1D(axis, anatomy.YAXIS.convertValue(axis, min, max, y));
        } else {
            throw new AssertionError();
        }
    }


}
