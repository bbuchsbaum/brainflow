package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction2D;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.io.ImageInfo;
import brainflow.utils.DataType;

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

    


    public int indexOf(int x, int y) {
        return source.indexOf(x, y);
    }

    
    public double value(double x, double y, InterpolationFunction2D interp) {
        return predicate.mask(source.value(x, y, interp))? 1 : 0;
    }

    public double worldValue(double realx, double realy, InterpolationFunction2D interp) {
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

    public ImageSpace2D getImageSpace() {
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

        ImageIterator iter;

        public MaskedIterator() {
            iter = source.iterator();
        }

        public double next() {
            return predicate.mask(iter.next()) ? 1 : 0;
        }

        public void advance() {
            iter.advance();
        }

        public double previous() {
            return predicate.mask(iter.previous()) ? 1 : 0;
        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public double jump(int number) {
            return iter.jump(number);
        }

       
        public double nextRow() {
            return iter.nextRow();
        }

        public double nextPlane() {
            return iter.nextPlane();
        }

        public boolean hasNextRow() {
            return iter.hasNextRow();
        }

        public boolean hasNextPlane() {
            return iter.hasNextPlane();
        }

        public boolean hasPreviousRow() {
            return iter.hasPreviousRow();
        }

        public boolean hasPreviousPlane() {
            return iter.hasPreviousPlane();
        }

        public double previousRow() {
            return iter.previousRow();
        }

        public double previousPlane() {
            return iter.previousPlane();
        }

        public void set(double val) {
            iter.set(val);
        }

        public int index() {
            return iter.index();
        }
    }
}
