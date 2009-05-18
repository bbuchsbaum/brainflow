package brainflow.image.space;

import brainflow.image.anatomy.AnatomicalDirection;
import brainflow.image.anatomy.BrainPoint3D;
import brainflow.image.anatomy.Anatomy3D;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 12, 2004
 * Time: 2:31:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageOrigin3D implements IImageOrigin {

    private BrainPoint3D origin;

    private AnatomicalDirection[] dirs;

    public ImageOrigin3D(AnatomicalDirection a1, AnatomicalDirection a2, AnatomicalDirection a3, double x, double y, double z) {

        origin = new BrainPoint3D(Anatomy3D.matchAnatomy(a1,a2,a3), x,y,z);

        dirs = new AnatomicalDirection[3];
        dirs[0] = a1;
        dirs[1] = a2;
        dirs[2] = a3;
    }

    public AnatomicalDirection[] getOriginalDirection() {
        return dirs;
    }

    public BrainPoint3D getOrigin() {
        return origin;
    }
}
