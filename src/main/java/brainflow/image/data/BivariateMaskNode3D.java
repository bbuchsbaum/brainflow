package brainflow.image.data;

import brainflow.image.iterators.BooleanIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.IImageSpace;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.operations.BinaryOperation;
import brainflow.utils.DataType;
import brainflow.utils.Dimension3D;
import brainflow.math.Index3D;
import brainflow.array.IDataGrid3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 11, 2008
 * Time: 8:45:00 AM
 * To change this template use File | Settings | File Templates.
 */

public class BivariateMaskNode3D implements IMaskedData3D {

    public final IImageData3D left;

    public final IImageData3D right;

    public final BinaryOperation operation;

    public BivariateMaskNode3D(IImageData3D _left, IImageData3D _right, BinaryOperation _operation) {
        left = _left;
        right = _right;
        operation = _operation;
    }

    public int cardinality() {
        BivariateMaskedDataNodeIterator iter = new BivariateMaskedDataNodeIterator();
        int count = 0;
        while (iter.hasNext()) {
            if (iter.next() > 0) {
                count++;
            }
        }

        return count;
    }

    @Override
    public boolean alwaysTrue() {
        return false;
    }

    public boolean isTrue(int index) {
        return operation.isTrue(left.value(index), right.value(index));
    }

    public boolean isTrue(int x, int y, int z) {
        return operation.isTrue(left.value(x, y, z), right.value(x, y, z));
    }

    public IImageSpace3D getImageSpace() {
        return left.getImageSpace();
    }

    public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
        return operation.isTrue(left.worldValue(realx, realy, realz, interp), right.worldValue(realx, realy, realz, interp)) ? 1 : 0;
    }

    public double value(float x, float y, float z, InterpolationFunction3D interp) {
        return operation.isTrue(left.value(x, y, z, interp), right.value(x, y, z, interp)) ? 1 : 0;
    }

    public double value(int x, int y, int z) {
        return operation.isTrue(left.value(x, y, z), right.value(x, y, z)) ? 1 : 0;
    }

    public int indexOf(int x, int y, int z) {
        return left.indexOf(x, y, z);
    }

    public Index3D indexToGrid(int idx) {
        return left.indexToGrid(idx);
    }



    public Anatomy3D getAnatomy() {
        return left.getAnatomy();
    }

    public DataType getDataType() {
        return DataType.INTEGER;
    }

    @Override
    public Dimension3D<Integer> dim() {
        return left.dim();
    }

    public int getDimension(Axis axisNum) {
        return left.getDimension(axisNum);
    }

    public ImageInfo getImageInfo() {
        return new ImageInfo(this);
    }

    public String getImageLabel() {
        return left.getImageLabel() + operation.operand() + right.getImageLabel();
    }

    public double value(int index) {
        return operation.isTrue(left.value(index), right.value(index)) ? 1 : 0;
    }

    public BooleanIterator valueIterator() {
        return new BivariateMaskedDataNodeIterator();
    }

    public double maxValue() {
        //todo not strictly correct
        return 1;
    }

    public double minValue() {
        //todo not strictly correct
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int length() {
        return left.length();
    }

    
    public IDataGrid3D subGrid(int x0, int x1, int y0, int y1, int z0, int z1) {
        throw new UnsupportedOperationException();
    }

    public ImageBuffer3D createBuffer(boolean clear) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return left.getImageLabel() + " " + operation + " " + right.getImageLabel();
    }

    class BivariateMaskedDataNodeIterator implements BooleanIterator {

        ValueIterator iter;

        public BivariateMaskedDataNodeIterator() {
            iter = left.valueIterator();
        }

        public double next() {
            advance();
            return value(iter.index());

        }

        @Override
        public boolean nextBoolean() {
            advance();
            return  isTrue(iter.index());
     
        }

        public IImageSpace getImageSpace() {
            return left.getImageSpace();
        }

        @Override
        public void advance() {
            iter.advance();
        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public int index() {
            return iter.index();
        }
    }
}
