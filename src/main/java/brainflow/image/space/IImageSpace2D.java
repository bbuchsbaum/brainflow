package brainflow.image.space;


import brainflow.utils.Dimension2D;
import brainflow.math.Index3D;
import brainflow.math.Index2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 30, 2009
 * Time: 10:29:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageSpace2D extends IImageSpace, ICoordinateSpace2D {

    ImageMapping2D getMapping();


    Dimension2D<Integer> getDimension();

    float[] indexToWorld(int x, int y, int z);

    Index2D indexToGrid(int idx);

    int indexToGridX(int idx);

    int indexToGridY(int idx);

    float worldToGridX(float x, float y, float z);

    float worldToGridY(float x, float y, float z);

    float gridToWorldX(float x, float y, float z);

    float gridToWorldY(float x, float y, float z);

}
