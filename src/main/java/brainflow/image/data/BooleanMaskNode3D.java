package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.iterators.BooleanIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.IImageSpace;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.operations.BooleanOperation;
import brainflow.image.operations.Operations;
import brainflow.utils.DataType;
import brainflow.utils.Dimension3D;
import brainflow.math.Index3D;
import brainflow.array.IDataGrid3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 19, 2007
 * Time: 1:16:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class BooleanMaskNode3D implements IMaskedData3D {

    private IMaskedData3D left;

    private IMaskedData3D right;

    private BooleanOperation operation = Operations.AND;


    public BooleanMaskNode3D(IMaskedData3D left, IMaskedData3D right) {
        if (left.getImageSpace().sameGeometry(right.getImageSpace())) {
            throw new IllegalArgumentException("left and right masks must have same geometry");
        }
        this.left = left;
        this.right = right;

    }

    public BooleanMaskNode3D(IMaskedData3D left, IMaskedData3D right, BooleanOperation operation) {
        if (left.getImageSpace().sameGeometry(right.getImageSpace())) {
            throw new IllegalArgumentException("left and right masks must have same geometry");
        }
        this.left = left;
        this.right = right;
        this.operation = operation;

    }

    @Override
    public boolean alwaysTrue() {
        return left.alwaysTrue() && right.alwaysTrue();
    }

    public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
        return operation.isTrue((int) left.worldValue(realx, realy, realz, interp), (int) right.worldValue(realx, realy, realz, interp)) ? 1 : 0;
    }

    @Override
    public Dimension3D<Integer> dim() {
        return left.dim();
    }

    public boolean isTrue(int index) {
        return operation.isTrue(left.isTrue(index), right.isTrue(index));
    }

    public boolean isTrue(int x, int y, int z) {
        return operation.isTrue(left.isTrue(x, y, z), right.isTrue(x, y, z));
    }

    public double value(int index) {
        return operation.isTrue(left.isTrue(index), right.isTrue(index)) ? 1 : 0;
    }

    public double value(float x, float y, float z, InterpolationFunction3D interp) {
        return operation.isTrue((int) left.value(x, y, z, interp), (int) right.value(x, y, z, interp)) ? 1 : 0;
    }

    public double value(int x, int y, int z) {
        return operation.isTrue(left.isTrue(x, y, z), right.isTrue(x, y, z)) ? 1 : 0;
    }

    public int indexOf(int x, int y, int z) {
        return left.indexOf(x, y, z);
    }

    @Override
    public ImageBuffer3D createBuffer(boolean clear) {
        throw new UnsupportedOperationException();
    }

    public Index3D indexToGrid(int idx) {
        return left.indexToGrid(idx);
    }

    public Anatomy3D getAnatomy() {
        return left.getAnatomy();
    }

    public DataType getDataType() {
        return left.getDataType();
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

    public IImageSpace3D getImageSpace() {
        return left.getImageSpace();
    }

    public double maxValue() {
        //todo not strictly correct
        return Math.max(left.maxValue(), right.maxValue());
    }

    public double minValue() {
        //todo not strictly correct
        return Math.min(left.minValue(), right.minValue());

    }

    public int length() {
        return left.length();
    }

    public BooleanIterator valueIterator() {
        return new MaskedDataNodeIterator();
    }

   
    public IDataGrid3D subGrid(int x0, int x1, int y0, int y1, int z0, int z1) {
        throw new UnsupportedOperationException();
    }

    public int cardinality() {
        MaskedDataNodeIterator iter = new MaskedDataNodeIterator();
        int count = 0;
        while (iter.hasNext()) {
            if (iter.next() > 0) {
                count++;
            }
        }

        return count;
    }

    public String toString() {
        return left.getImageLabel() + " " + operation.toString() + " " + right.getImageLabel();
    }


    class MaskedDataNodeIterator implements BooleanIterator {

        ValueIterator iter;

        public MaskedDataNodeIterator() {
            iter = left.valueIterator();
        }

        public final double next() {
            advance();
            int idx = iter.index();

            return operation.isTrue(left.isTrue(idx), right.isTrue(idx)) ? 1 : 0;

        }

        @Override
        public boolean nextBoolean() {
            advance();
            int idx = iter.index();

            return operation.isTrue(left.isTrue(idx), right.isTrue(idx));

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



        //@Override
        public IImageSpace getImageSpace() {
            return left.getImageSpace();
        }
    }
}
