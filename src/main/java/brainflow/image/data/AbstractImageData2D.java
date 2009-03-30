package brainflow.image.data;

import brainflow.image.space.IImageSpace;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.iterators.ImageIterator;
import brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 27, 2009
 * Time: 9:58:54 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageData2D extends AbstractImageData implements IImageData2D {

    private int dim0;

    public AbstractImageData2D(ImageSpace2D space) {
        super(space);
        dim0 = space.getDimension(Axis.X_AXIS);
    }

    protected AbstractImageData2D(IImageSpace3D space, DataType dtype) {
        super(space, dtype);
        dim0 = space.getDimension(Axis.X_AXIS);

    }

    public ImageSpace2D getImageSpace() {
        return (ImageSpace2D) space;
    }


    public int indexOf(int x, int y) {
        return dim0 * y + x;

    }

    

   

}
