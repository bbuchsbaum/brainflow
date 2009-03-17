package brainflow.image.data;

import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.operations.BinaryOperation;
import brainflow.utils.DataType;
import brainflow.math.Index3D;

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

    public void setValue(int x, int y, int z, double val) {
        throw new UnsupportedOperationException();
    }

    public Anatomy3D getAnatomy() {
        return left.getAnatomy();
    }

    public DataType getDataType() {
        return DataType.INTEGER;
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

    public ImageIterator iterator() {
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

    public int numElements() {
        return left.numElements();
    }

    private void setValue(int idx, double val) {
        throw new UnsupportedOperationException();
    }

    public DataWriter3D createWriter(boolean clear) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return left.getImageLabel() + " " + operation + " " + right.getImageLabel();
    }

    class BivariateMaskedDataNodeIterator implements ImageIterator {

        ImageIterator iter;

        public BivariateMaskedDataNodeIterator() {
            iter = left.iterator();
        }

        public double next() {          
            double val =  value(iter.index());
            advance();
            return val;

        }

        public void advance() {
            iter.advance();
        }

        public double previous() {
            iter.jump(-1);
            return value(iter.index());

        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public double jump(int number) {
            return iter.jump(number);
        }

        public boolean canJump(int number) {
            return iter.canJump(number);
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
            throw new UnsupportedOperationException();
        }

        public int index() {
            return iter.index();
        }
    }
}
