package brainflow.image.operations;

import brainflow.math.Vector2f;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 16, 2008
 * Time: 8:51:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ITransform2D {

    public Vector2f transform(double x, double y);

    public Vector2f transform(Vector2f ivec);

    public Vector2f transform(Vector2f ivec, Vector2f ovec);

    

}
