package brainflow.image.data;

import brainflow.array.IArray3D;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.iterators.BooleanIterator;
import brainflow.image.iterators.ImageValueIterator;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.space.IImageSpace3D;
import brainflow.utils.DataType;
import cern.colt.map.OpenIntDoubleHashMap;
import cern.colt.map.OpenIntIntHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 22, 2010
 * Time: 9:14:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class SparseImageData3D extends AbstractImageData3D implements IArray3D {

    private OpenIntDoubleHashMap valueMap = new OpenIntDoubleHashMap();

    public SparseImageData3D(IImageSpace3D space, DataType dtype, IMaskedData3D mask, IImageData3D values) {
        super(space, dtype);
        if (mask.getImageSpace().sameGeometry(values.getImageSpace())) {
            throw new IllegalArgumentException("mask and values must have same geometry (same dimensions");
        }

        init(mask, values);
    }

    private void init(IMaskedData3D mask, IImageData3D values) {
        BooleanIterator iter = mask.valueIterator();
        while (iter.hasNext()) {
            if (iter.nextBoolean()) {
                double val = values.value(iter.index());
                valueMap.put(iter.index(), val);
            }
        }
       

    }

    @Override
    public ImageBuffer3D createBuffer(boolean clear) {
        throw new UnsupportedOperationException();

    }

    @Override
    public double value(float x, float y, float z, InterpolationFunction3D interp) {
        return interp.interpolate(x,y,z,this);

    }

    @Override
    public double value(int i, int j, int k) {
        return value(indexOf(i,j,k));
    }

    @Override
    public ValueIterator valueIterator() {
        return new ImageValueIterator(this);
    }

    @Override
    public double value(int i) {
        return valueMap.get(i);
    }


}
