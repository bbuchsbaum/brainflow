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



    ImageMapping3D getMapping();


    Dimension3D<Integer> getDimension();

    float[] indexToWorld(int x, int y, int z);


    Index3D indexToGrid(int idx);

    int indexToGridX(int idx);

    int indexToGridY(int idx);

    int indexToGridZ(int idx);

    float worldToGridX(float x, float y, float z);

    float worldToGridY(float x, float y, float z);

    float worldToGridZ(float x, float y, float z);

    float gridToWorldX(float x, float y, float z);

    float gridToWorldY(float x, float y, float z);

    float gridToWorldZ(float x, float y, float z);
}
