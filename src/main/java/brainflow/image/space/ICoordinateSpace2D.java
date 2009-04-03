package brainflow.image.space;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.Anatomy2D;
import brainflow.image.anatomy.AnatomicalPoint2D;
import brainflow.utils.Dimension3D;
import brainflow.utils.Dimension2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 30, 2009
 * Time: 10:29:30 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICoordinateSpace2D extends ICoordinateSpace {

    Anatomy2D getAnatomy();

    Dimension2D<Float> getOrigin();

    AnatomicalPoint2D getCentroid();
}
