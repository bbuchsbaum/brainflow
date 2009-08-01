package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace2D;
import brainflow.image.space.IImageSpace;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.io.ImageInfo;
import brainflow.utils.DataType;
import brainflow.utils.IDimension;
import brainflow.utils.Dimension2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 28, 2007
 * Time: 9:50:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskedData2D implements IImageData2D, IMaskedData2D {

    private IImageData2D source;

    private MaskPredicate predicate;


    public MaskedData2D(IImageData2D src, MaskPredicate predicate) {
        super();
        source = src;
        this.predicate = predicate;
    }


    @Override
    public Dimension2D<Integer> getDimensions() {
        return source.getDimensions();
    }

    public int indexOf(int x, int y) {
        return source.indexOf(x, y);
    }

    @Override
    public double value(float x, float y, InterpolationFunction2D interp) {
        return predicate.mask(source.value(x, y, interp))? 1 : 0;
    }
    
    @Override
    public double worldValue(float realx, float realy, InterpolationFunction2D interp) {
        return predicate.mask(source.worldValue(realx, realy, interp)) ? 1 : 0;
    }

   

    public boolean isTrue(int index) {
        return predicate.mask(source.value(index));
    }

    public boolean isTrue(int x, int y) {
        return predicate.mask(source.value(x, y));
    }

    public double value(int x, int y) {
        return predicate.mask(source.value(x, y)) ? 1 : 0;
    }

    public double value(int index) {
        return predicate.mask(source.value(index)) ? 1 : 0;
    }


    public int cardinality() {
        //todo this code is duplicated from MaskedData3D
        
        MaskedIterator iter = new MaskedIterator();
        int count = 0;
        while (iter.hasNext()) {
            if (iter.next() > 0) {
                count++;
            }
        }

        return count;

    }

    public ImageBuffer2D createWriter(boolean clear) {
        throw new UnsupportedOperationException("MaskedData2D does not support data writing");
    }

    public ImageIterator iterator() {
        return new MaskedIterator();

    }

    public IImageSpace2D getImageSpace() {
        return source.getImageSpace();
    }

    public DataType getDataType() {
        return source.getDataType();
    }

    public Anatomy getAnatomy() {
        return source.getAnatomy();
    }

    public int getDimension(Axis axisNum) {
        return source.getDimension(axisNum);
    }

    public double maxValue() {
        return source.maxValue();
    }

    public double minValue() {
        return source.minValue();
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


    class MaskedIterator implements ImageIterator {

        ValueIterator iter;

        public MaskedIterator() {
            iter = source.iterator();
        }

        public double next() {
            return predicate.mask(iter.next()) ? 1 : 0;
        }

        public void advance() {
            iter.advance();
        }


        public boolean hasNext() {
            return iter.hasNext();
        }

        public int index() {
            return iter.index();
        }

        @Override
        public IImageSpace getImageSpace() {
            return getImageSpace();
        }
    }
}
