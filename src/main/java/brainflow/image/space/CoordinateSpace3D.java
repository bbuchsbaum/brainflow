package brainflow.image.space;

import brainflow.image.anatomy.SpatialLoc1D;
import brainflow.image.axis.CoordinateAxis;
import brainflow.image.axis.ImageAxis;
import brainflow.image.axis.AxisRange;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.utils.Dimension3D;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 3:31:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateSpace3D extends AbstractCoordinateSpace implements ICoordinateSpace3D {


    private Dimension3D<Float> origin;

    private Anatomy3D anatomy = null;

    private CoordinateAxis[] axes;


    public CoordinateSpace3D(Anatomy3D anatomy) {

        setAnatomy(anatomy);

        createImageAxes(3);

        initAxis(new CoordinateAxis(anatomy.XAXIS, new AxisRange(anatomy.XAXIS, -100, 100)), Axis.X_AXIS);
        initAxis(new CoordinateAxis(anatomy.YAXIS, new AxisRange(anatomy.YAXIS, -100, 100)), Axis.Y_AXIS);
        initAxis(new CoordinateAxis(anatomy.ZAXIS, new AxisRange(anatomy.ZAXIS, -100, 100)), Axis.Z_AXIS);

        origin = new Dimension3D<Float>(-100f, -100f, -100f);

    }


    public CoordinateSpace3D(CoordinateAxis xaxis, CoordinateAxis yaxis, CoordinateAxis zaxis) {
        Anatomy3D check = Anatomy3D.matchAnatomy(xaxis.getAnatomicalAxis(), yaxis.getAnatomicalAxis(), zaxis.getAnatomicalAxis());
        assert check != null;

        setAnatomy(check);

        createImageAxes(3);

        initAxis(xaxis, Axis.X_AXIS);
        initAxis(yaxis, Axis.Y_AXIS);
        initAxis(zaxis, Axis.Z_AXIS);

        origin = new Dimension3D<Float>((float)xaxis.getRange().getBeginning().getValue(),
                (float)yaxis.getRange().getBeginning().getValue(), (float)zaxis.getRange().getBeginning().getValue());

    }

    protected CoordinateAxis[] getAxes() {
        return axes;
    }

    protected void createImageAxes(int num) {
        axes = new CoordinateAxis[num];
    }

    protected void initAxis(CoordinateAxis iaxis, Axis aaxis) {
        axes[aaxis.getId()] = iaxis;
    }

    private void setAnatomy(Anatomy3D _anatomy) {
        anatomy = _anatomy;
    }

    public Anatomy3D getAnatomy() {
        return anatomy;
    }


    public Dimension3D<Float> getOrigin() {
        return origin;
    }


    public ICoordinateSpace union(ICoordinateSpace other) {
         if (!sameAxes(other)) {
            throw new IllegalArgumentException("cannot perform union for ImageSpaces with different axis orientations");
        }

       
        CoordinateAxis[] axes = new ImageAxis[getNumDimensions()];

        for (int i = 0; i < axes.length; i++) {
            AxisRange range1 = other.getImageAxis(Axis.getAxis(i)).getRange();
            AxisRange range2 = getImageAxis(Axis.getAxis(i)).getRange();


            AxisRange nrange = range1.union(range2);
            axes[i] = new CoordinateAxis(nrange.getAnatomicalAxis(), nrange);



        }

        return Space.createCoordinateSpace(axes);

    }

    public SpatialLoc3D getCentroid() {

        CoordinateAxis a1 = getImageAxis(Axis.X_AXIS);
        CoordinateAxis a2 = getImageAxis(Axis.Y_AXIS);
        CoordinateAxis a3 = getImageAxis(Axis.Z_AXIS);

        SpatialLoc1D x = a1.getRange().getCenter();
        SpatialLoc1D y = a2.getRange().getCenter();
        SpatialLoc1D z = a3.getRange().getCenter();

        return new SpatialLoc3D(getAnatomy(), x.getValue(), y.getValue(), z.getValue());


    }


    public CoordinateSpace3D(CoordinateSpace3D space) {
        setAnatomy(space.getAnatomy());
        createImageAxes(3);
        initAxis(space.getImageAxis(Axis.X_AXIS), Axis.X_AXIS);
        initAxis(space.getImageAxis(Axis.Y_AXIS), Axis.Y_AXIS);
        initAxis(space.getImageAxis(Axis.Z_AXIS), Axis.Z_AXIS);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoordinateSpace3D)) return false;

        CoordinateSpace3D that = (CoordinateSpace3D) o;

        if (anatomy != null ? !anatomy.equals(that.anatomy) : that.anatomy != null) return false;
        if (!Arrays.equals(axes, that.axes)) return false;
        if (origin != null ? !origin.equals(that.origin) : that.origin != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (origin != null ? origin.hashCode() : 0);
        result = 31 * result + (anatomy != null ? anatomy.hashCode() : 0);
        result = 31 * result + (axes != null ? Arrays.hashCode(axes) : 0);
        return result;
    }
}
