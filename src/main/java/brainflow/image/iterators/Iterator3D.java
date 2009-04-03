package brainflow.image.iterators;

import brainflow.image.data.DataGrid3D;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 26, 2009
 * Time: 9:44:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Iterator3D implements ImageIterator {

    private int index;

    private DataGrid3D data;

    private IImageSpace3D space;

    private int len;
    private int end;
    private int begin = 0;
    private int planeSize;

    public Iterator3D(DataGrid3D _data) {
        data = _data;
        space = data.getImageSpace();
        index = 0;
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        len = space.getNumSamples();
        end = len;
    }

    public double next() {
        double dat = data.value(index);
        index++;
        return dat;
    }

    public final void advance() {
        index++;
    }

    public void set(double val) {
        throw new UnsupportedOperationException();
    }

    public double previous() {
        return data.value(--index);
    }

    public boolean hasNext() {
        if (index < end) return true;
        return false;
    }

    public double jump(int number) {
        index += number;
        return data.value(number);
    }

    /*public boolean canJump(int number) {

        if (((index + number) < end) && ((index - number) >= begin))
            return true;
        return false;
    } */

    public double nextRow() {
        index += space.getDimension(Axis.X_AXIS);
        return data.value(index);
    }

    public double nextPlane() {
        index += planeSize;
        return data.value(index);
    }

    public boolean hasNextRow() {
        if ((index + space.getDimension(Axis.X_AXIS)) < len)
            return true;
        return false;
    }

    public boolean hasNextPlane() {
        if ((index + planeSize) < len)
            return true;
        return false;
    }

    public boolean hasPreviousRow() {
        if ((index - space.getDimension(Axis.X_AXIS)) >= begin)
            return true;
        return false;
    }

    public boolean hasPreviousPlane() {
        if ((index - planeSize) >= begin)
            return true;
        return false;
    }

    public double previousRow() {
        index -= space.getDimension(Axis.X_AXIS);
        return data.value(index);
    }

    public double previousPlane() {
        index -= planeSize;
        return data.value(index);
    }

    public final int index() {
        return index;
    }


}

