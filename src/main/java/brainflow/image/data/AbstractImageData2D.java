package brainflow.image.data;

import brainflow.image.space.*;
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

    public AbstractImageData2D(IImageSpace2D space) {
        super(space);
        dim0 = space.getDimension(Axis.X_AXIS);
    }

    public AbstractImageData2D(IImageSpace2D space, DataType dtype, String imageLabel) {
        super(space, dtype, imageLabel);
        dim0 = space.getDimension(Axis.X_AXIS);

    }

    public AbstractImageData2D(IImageSpace2D space, DataType dtype) {
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
