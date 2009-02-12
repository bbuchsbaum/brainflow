package brainflow.display;

import java.awt.image.AffineTransformOp;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 3, 2006
 * Time: 10:20:46 PM
 * To change this template use File | Settings | File Templates.
 */


public enum InterpolationType {

    NEAREST_NEIGHBOR(AffineTransformOp.TYPE_NEAREST_NEIGHBOR),
    LINEAR(AffineTransformOp.TYPE_BILINEAR),
    CUBIC(AffineTransformOp.TYPE_BICUBIC);


    private int id;

    InterpolationType(int _id) {
        id = _id;

    }

    public int getID() {
        return id;
    }
}
