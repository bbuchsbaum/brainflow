package brainflow.image.space;

import brainflow.math.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 19, 2008
 * Time: 11:10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageMapping3D {

    public Vector3f getOrigin();
    
    public Vector3f gridToWorld(int i, int j, int k, Vector3f out);

    public Vector3f gridToWorld(int i, int j, int k);

    public Vector3f gridToWorld(float i, float j, float k);

    public Vector3f gridToWorld(float i, float j, float k, Vector3f out);


    public Vector3f worldToGrid(Vector3f in, Vector3f out);

    public Vector3f worldToGrid(Vector3f in);

    public float worldToGridX(float x, float y, float z);

    public float worldToGridY(float x, float y, float z);

    public float worldToGridZ(float x, float y, float z);
       
    public float gridToWorldX(float x, float y, float z);

    public float gridToWorldY(float x, float y, float z);

    public float gridToWorldZ(float x, float y, float z);

    
}
