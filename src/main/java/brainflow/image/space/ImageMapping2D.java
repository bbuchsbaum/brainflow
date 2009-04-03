package brainflow.image.space;

import brainflow.math.Vector2f;
import brainflow.math.Vector3f;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 19, 2008
 * Time: 11:51:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageMapping2D extends ImageMapping {

    public Vector2f getOrigin();

    public Vector2f gridToWorld(int i, int j, Vector2f out);

    public Vector2f gridToWorld(int i, int j);

    public Vector2f gridToWorld(float i, float j);

    public Vector2f gridToWorld(float i, float j, Vector2f out);

    public Vector2f gridToWorld(Vector2f in);

    public Vector2f worldToGrid(Vector2f in, Vector2f out);

    public Vector2f worldToGrid(Vector2f in);

    public float worldToGridX(float x, float y);

    public float worldToGridY(float x, float y);

   

    public float gridToWorldX(float x, float y);

    public float gridToWorldY(float x, float y);


}
