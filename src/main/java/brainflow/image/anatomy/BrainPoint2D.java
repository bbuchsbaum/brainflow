package brainflow.image.anatomy;


import brainflow.image.space.ImageSpace2D;
import brainflow.image.axis.ImageAxis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 12:29:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrainPoint2D implements BrainPoint {

    private Anatomy2D anatomy;

    private BrainPoint1D x;

    private BrainPoint1D y;

    public BrainPoint2D(Anatomy2D _anatomy, double x, double y) {
        anatomy = _anatomy;      
        this.x = new BrainPoint1D(anatomy.XAXIS, x);
        this.y = new BrainPoint1D(anatomy.YAXIS, y);


    }


    public static BrainPoint2D convertPoint(ImageSpace2D fromSpace, BrainPoint2D from, Anatomy2D to) {

        ImageAxis to_x = fromSpace.getImageAxis(to.XAXIS, true);
        ImageAxis to_y = fromSpace.getImageAxis(to.YAXIS, true);

        BrainPoint1D a1 = from.getValue(to_x.getAnatomicalAxis(), to_x.getMinimum(), to_x.getMaximum());
        BrainPoint1D a2 = from.getValue(to_y.getAnatomicalAxis(), to_y.getMinimum(), to_y.getMaximum());

        assert a1.getAnatomy() == to.XAXIS;
        assert a2.getAnatomy() == to.YAXIS;


        return new BrainPoint2D(to, a1.getValue(), a2.getValue());
    }

    public BrainPoint1D getX() {
        return x;
    }



    public BrainPoint1D getY() {
        return y;
    }


    public Anatomy2D getAnatomy() {
        return anatomy;
    }

    public int getNumDimensions() {
        return 2;
    }

    public BrainPoint1D getValue(int axisNum) {

        if (axisNum == 0) {
            return getX();
        } else if (axisNum == 1) {
            return getY();
        } else throw new IllegalArgumentException("illegal axis number " + axisNum);


    }

    public BrainPoint1D getValue(AnatomicalAxis axis) {
        if (axis.sameDirection(anatomy.XAXIS)) {
            return x;
        } else if (axis.sameDirection(anatomy.YAXIS)) {
            return y;
        } else {
            throw new IllegalArgumentException("axis " + axis + " is not a member of this coordinate system : " + anatomy);
        }
    }


    public BrainPoint1D getValue(AnatomicalAxis axis, double min, double max) {
        if (axis.sameAxis(anatomy.XAXIS)) {
            return new BrainPoint1D(axis, anatomy.XAXIS.convertValue(axis, min, max, x.getValue()));
        } else if (axis.sameAxis(anatomy.YAXIS)) {
            return new BrainPoint1D(axis, anatomy.YAXIS.convertValue(axis, min, max, y.getValue()));
        } else {
            throw new AssertionError();
        }
    }


}
