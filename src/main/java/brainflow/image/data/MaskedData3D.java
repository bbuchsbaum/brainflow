package brainflow.image.data;

import brainflow.utils.DataType;
import brainflow.utils.IDimension;
import brainflow.utils.Dimension3D;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.IImageSpace;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.iterators.ValueIterator;
import brainflow.math.Index3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 9, 2007
 * Time: 9:56:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskedData3D implements IMaskedData3D {

    private IImageData3D source;

    private MaskPredicate predicate;

    
    public MaskedData3D(IImageData3D src, MaskPredicate predicate) {
        source = src;
        this.predicate = predicate;
    }

    public MaskPredicate getPredicate() {
        return predicate;
    }

    public IImageData3D getSource() {
        return source;
    }

    @Override
    public Dimension3D<Integer> getDimensions() {
        return source.getDimensions();
    }

    public Index3D indexToGrid(int idx) {
        return source.indexToGrid(idx);
    }

    public int indexOf(int x, int y, int z) {
        return source.indexOf(x, y, z);
    }

    public double value(float x, float y, float z, InterpolationFunction3D interp) {
        return predicate.mask(source.value(x, y, z, interp)) ? 1 : 0;
    }

    public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
        return predicate.mask(source.worldValue(realx, realy, realz, interp)) ? 1 : 0;
    }

    public boolean isTrue(int index) {
        return predicate.mask(source.value(index));
    }

    public boolean isTrue(int x, int y, int z) {
        return predicate.mask(source.value(x, y, z));
    }

    public double value(int index) {
        return predicate.mask(source.value(index)) ? 1 : 0;
    }


    public double value(int x, int y, int z) {
        return predicate.mask(source.value(x, y, z)) ? 1 : 0;
    }



    public IImageSpace3D getImageSpace() {
        return source.getImageSpace();
    }

    public DataType getDataType() {
        return DataType.INTEGER;
    }

    public Anatomy3D getAnatomy() {
        return source.getAnatomy();
    }

    public int getDimension(Axis axisNum) {
        return source.getDimension(axisNum);
    }

    public double maxValue() {
        //todo needs to be computed
        return 1;
    }

    public double minValue() {
        //todo needs to be computed
        return 0;
    }

    public int numElements() {
        return source.numElements();
    }

    public ImageInfo getImageInfo() {
        return source.getImageInfo();
    }

    
    public String getImageLabel() {
        return source.getImageLabel();
    }

    public ImageIterator iterator() {
        return new MaskedIterator();
    }

    public ImageBuffer3D createWriter(boolean clear) {
        throw new UnsupportedOperationException("Cannot create writer for class " + getClass());
    }

    public int cardinality() {
        MaskedIterator iter = new MaskedIterator();
        int count = 0;
        while (iter.hasNext()) {
            if (iter.next() > 0) {
                count++;
            }
        }

        return count;

    }

    class MaskedIterator implements ImageIterator {

        ValueIterator iter;

        public MaskedIterator() {
            iter = source.iterator();
        }

        public final double next() {
            return predicate.mask(iter.next()) ? 1 : 0;
        }

        public final void advance() {
            iter.advance();
        }

        public final boolean hasNext() {
            return iter.hasNext();
        }

        public final int index() {
            return iter.index();
        }

        @Override
        public IImageSpace getImageSpace() {
            return source.getImageSpace();
        }
    }
}
