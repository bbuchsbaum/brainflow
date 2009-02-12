package brainflow.image.space;

import brainflow.math.Vector2f;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 19, 2008
 * Time: 11:51:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageMapping2D extends ImageMapping {

    public void gridToWorld(int i, int j, Vector2f out);

    public Point2D gridToWorld(int i, int j, int k);

    public void worldToGrid(Vector2f in, Vector2f out);

    public Vector2f worldToGrid(Vector2f in);
}
