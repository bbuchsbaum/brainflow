package brainflow.image.space;

import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.AnatomicalPoint;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.axis.CoordinateAxis;
import brainflow.utils.IDimension;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 19, 2007
 * Time: 1:08:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICoordinateSpace {


    public double getExtent(Axis axis);

    public double getExtent(AnatomicalAxis axis);

    public Axis findAxis(AnatomicalAxis axis);

    public AnatomicalAxis getAnatomicalAxis(Axis axis);

    public CoordinateAxis getImageAxis(Axis axis);

    public CoordinateAxis getImageAxis(AnatomicalAxis axis, boolean ignoreDirection);

    public AnatomicalPoint getCentroid();


    public int getNumDimensions();

    public ICoordinateSpace union(ICoordinateSpace other);

    public Anatomy getAnatomy();

    public IDimension<Float> getOrigin();



}
