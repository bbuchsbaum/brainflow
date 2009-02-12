package brainflow.image.operations;

import brainflow.math.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 16, 2008
 * Time: 8:51:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ITransform3D {

    public Vector3f transform(double x, double y, double z);

    public Vector3f transform(Vector3f ivec);

    public Vector3f transform(Vector3f ivec, Vector3f ovec);



}