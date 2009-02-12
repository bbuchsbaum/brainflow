package brainflow.image.space;

import brainflow.image.axis.CoordinateAxis;
import brainflow.image.anatomy.AnatomicalAxis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 19, 2007
 * Time: 1:29:23 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCoordinateSpace implements ICoordinateSpace {

    

    public double getExtent(Axis axis) {
        return getAxes()[axis.getId()].getExtent();
    }

    public int getNumDimensions() {
        return getAxes().length;
    }

    protected abstract CoordinateAxis[] getAxes();

    

    public CoordinateAxis getImageAxis(Axis axis) {
        assert axis.getId() < getNumDimensions();
        return getAxes()[axis.getId()];
    }

    private CoordinateAxis whichAxisIgnoreDirection(AnatomicalAxis aaxis) {
        CoordinateAxis[] axes = getAxes();

        for (int i = 0; i < axes.length; i++) {

            if (aaxis == axes[i].getAnatomicalAxis()) {

                return axes[i];
            } else if (aaxis == axes[i].getAnatomicalAxis().getFlippedAxis()) {

                return axes[i];
            }
        }

        return null;
    }

    private CoordinateAxis whichAxis(AnatomicalAxis aaxis) {
        CoordinateAxis[] axes = getAxes();
        for (int i = 0; i < axes.length; i++) {
            if (aaxis == axes[i].getAnatomicalAxis()) {
                return axes[i];
            }
        }

        return null;
    }

    public CoordinateAxis getImageAxis(AnatomicalAxis axis, boolean ignoreDirection) {
        CoordinateAxis iaxis = null;
        if (ignoreDirection)
            iaxis = whichAxisIgnoreDirection(axis);
        else
            iaxis = whichAxis(axis);

        if (iaxis == null) {
            throw new IllegalArgumentException("AbstractImageSpace.getImageAxis(...): request for ImageAxis for " +
                    "AnatomicalAxis that is not represented in this space.");
        }

        return iaxis;

    }


    public AnatomicalAxis getAnatomicalAxis(Axis axis) {
        CoordinateAxis[] axes = getAxes();
        return axes[axis.getId()].getAnatomicalAxis();

    }

    public Axis findAxis(AnatomicalAxis axis) {
        CoordinateAxis[] axes = getAxes();
        if ((axes[0].getAnatomicalAxis() == axis)    || (axes[0].getAnatomicalAxis().getFlippedAxis() == axis))
            return Axis.X_AXIS;
        else if (axes[1].getAnatomicalAxis() == axis || (axes[1].getAnatomicalAxis().getFlippedAxis() == axis))
            return Axis.Y_AXIS;
        else if (axes[2].getAnatomicalAxis() == axis || (axes[2].getAnatomicalAxis().getFlippedAxis() == axis))
            return Axis.Z_AXIS;

        return null;
    }

   
    public boolean sameAxes(ICoordinateSpace other) {
        if (other.getNumDimensions() != getNumDimensions()) {
            return false;
        }
        for (int i = 0; i < getNumDimensions(); i++) {
            if (getAnatomicalAxis(Axis.getAxis(i)) != other.getAnatomicalAxis(Axis.getAxis(i))) {
                return false;
            }

        }

        return true;
    }

    public double getExtent(AnatomicalAxis axis) {
        CoordinateAxis iaxis = whichAxis(axis);

        if (iaxis == null) {
            throw new IllegalArgumentException("AbstractCoordinateSpace.getExtent(...): request for extent" +
                    " of AnatomicalAxis that is not represented in this space.");
        }

        return iaxis.getExtent();

    }
}
