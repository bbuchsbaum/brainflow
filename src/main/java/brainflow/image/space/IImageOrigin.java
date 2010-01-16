package brainflow.image.space;

import brainflow.image.anatomy.AnatomicalDirection;
import brainflow.image.anatomy.BrainLoc;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 12, 2004
 * Time: 2:29:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageOrigin {


    public AnatomicalDirection[] getOriginalDirection();

    public BrainLoc getOrigin();



}
