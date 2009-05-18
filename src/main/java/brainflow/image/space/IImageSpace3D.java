package brainflow.image.space;

import brainflow.utils.Dimension3D;
import brainflow.math.Index3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 12, 2008
 * Time: 9:20:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageSpace3D extends IImageSpace, ICoordinateSpace3D {


    public ImageMapping3D getMapping();

    public Dimension3D<Integer> getDimension();

    public float[] indexToWorld(int x, int y, int z);

    public Index3D indexToGrid(int idx);

    public int indexToGridX(int idx);

    public int indexToGridY(int idx);

    public int indexToGridZ(int idx);

    public float worldToGridX(float x, float y, float z);

    public float worldToGridY(float x, float y, float z);

    public float worldToGridZ(float x, float y, float z);

    public float gridToWorldX(float x, float y, float z);

    public float gridToWorldY(float x, float y, float z);

    public float gridToWorldZ(float x, float y, float z);

    public float[] worldToGrid(float x, float y, float z);
}
