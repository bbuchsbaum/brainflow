package brainflow.image.space;

import brainflow.image.anatomy.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2006
 * Time: 5:35:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageOrigin2D implements IImageOrigin {

    private SpatialLoc2D origin;

    private AnatomicalDirection[] dirs;

    public ImageOrigin2D(AnatomicalDirection a1, AnatomicalDirection a2, double x, double y) {

        origin = new SpatialLoc2D(Anatomy2D.matchAnatomy(a1, a2), x, y);

        dirs = new AnatomicalDirection[3];
        dirs[0] = a1;
        dirs[1] = a2;

    }


    public AnatomicalDirection[] getOriginalDirection() {
        return dirs;
    }

    public BrainPoint getOrigin() {
        return origin;
    }
}
