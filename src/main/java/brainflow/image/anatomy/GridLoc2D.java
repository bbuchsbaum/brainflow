package brainflow.image.anatomy;

import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace2D;
import brainflow.image.axis.ImageAxis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 9, 2009
 * Time: 1:31:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridLoc2D {


    private GridLoc1D gridX, gridY;
    
    private IImageSpace2D space;

    public final LocationType unit = LocationType.GRID;


    public GridLoc2D(double x, double y, IImageSpace2D space) {
        this.gridX = clamp(x, space.getImageAxis(Axis.X_AXIS));
        this.gridY = clamp(y, space.getImageAxis(Axis.Y_AXIS));
        this.space = space;
    }

    private GridLoc1D clamp(double val, ImageAxis axis) {
        val = Math.min(val, axis.getMaximum());
        val = Math.max(val, axis.getMinimum());
        return new GridLoc1D((float)val, axis);
    }

    public static GridLoc2D fromWorld(double x, double y, IImageSpace2D space) {
        float[] grid = space.worldToGrid((float) x, (float) y);
        return new GridLoc2D(grid[0], grid[1], space);
    }

    public static GridLoc2D fromReal(float x, float y,  IImageSpace2D space) {
        double gridx = space.getImageAxis(Axis.X_AXIS).gridPosition(x);
        double gridy = space.getImageAxis(Axis.Y_AXIS).gridPosition(y);
        return new GridLoc2D(gridx, gridy, space);
    }

    public static GridLoc2D fromReal(SpatialLoc2D bp, IImageSpace2D space) {
        if (space.getAnatomy() != bp.getAnatomy()) {
            throw new IllegalArgumentException("incompatible axes: BrainPoint3D " + bp.getAnatomy() + " does not equals IIMageSpace3D anatomy: " + space.getAnatomy());
        }
        double gridx = space.getImageAxis(Axis.X_AXIS).gridPosition(bp.getX().getValue());
        double gridy = space.getImageAxis(Axis.Y_AXIS).gridPosition(bp.getY().getValue());
        return new GridLoc2D(gridx, gridy, space);
    }

    public static GridLoc2D fromReal(double x, double y, IImageSpace2D space) {
        double gridx = space.getImageAxis(Axis.X_AXIS).gridPosition(x);
        double gridy = space.getImageAxis(Axis.Y_AXIS).gridPosition(y);
         return new GridLoc2D(gridx, gridy, space);
    }


    public Anatomy2D getGridAnatomy() {
        return space.getAnatomy();
    }

    public Anatomy2D getWorldAnatomy() {
        return space.getMapping().getWorldAnatomy();
    }

    public IImageSpace2D getSpace() {
        return space;
    }

    public double getValue(AnatomicalAxis axis) {
        if (axis.sameAxis(getGridAnatomy().XAXIS)) {
            return gridX.getValue();
        } else if (axis.sameAxis(getGridAnatomy().YAXIS)) {
            return gridY.getValue();
        } else {
            throw new IllegalArgumentException("axis : " + axis + " incompatible with Anatomy3D " + getGridAnatomy());
        }

    }

    public SpatialLoc2D toWorld() {
        float[] vals = space.gridToWorld((float)gridX.getValue(), (float)gridY.getValue());
        return new SpatialLoc2D(space.getMapping().getWorldAnatomy(), vals[0], vals[1]);
    }

    public SpatialLoc2D toReal() {
        return new SpatialLoc2D(getGridAnatomy(),
                gridX.getValue() * space.getSpacing(Axis.X_AXIS) + space.getImageAxis(Axis.X_AXIS).getMinimum(),
                gridY.getValue() * space.getSpacing(Axis.Y_AXIS) + space.getImageAxis(Axis.Y_AXIS).getMinimum());

    }

    public GridLoc1D getX() {
        return gridX;
    }

    public GridLoc1D getY() {
        return gridY;
    }


}
