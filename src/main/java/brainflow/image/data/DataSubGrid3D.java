package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.ImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.axis.ImageAxis;
import brainflow.utils.Dimension3D;
import brainflow.array.IDataGrid3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jun 30, 2009
 * Time: 10:46:54 PM
 * To change this template use File | Settings | File Templates.
 */


public class DataSubGrid3D  {

    private IImageData3D wrapped;

    private IImageSpace3D space;

    private int xoffset;

    private int xlen;

    private int yoffset;

    private int ylen;

    private int zoffset;

    private int zlen;

    private int indexOffset;

    public DataSubGrid3D(IImageData3D wrapped, int xoffset, int xlen, int yoffset, int ylen, int zoffset, int zlen) {
        this.wrapped = wrapped;
        this.xoffset = xoffset;
        this.xlen = xlen;
        this.yoffset = yoffset;
        this.ylen = ylen;
        this.zoffset = zoffset;
        this.zlen = zlen;

        int planeSize = wrapped.dim().getDim(0) * wrapped.dim().getDim(1);
        int dim0 = wrapped.dim().getDim(0);

        indexOffset = zoffset * planeSize + dim0 * yoffset + xoffset;

        initSpace();
    }


    private void initSpace() {
        ImageAxis xaxis = wrapped.getImageSpace().getImageAxis(Axis.X_AXIS);
        ImageAxis yaxis = wrapped.getImageSpace().getImageAxis(Axis.Y_AXIS);
        ImageAxis zaxis = wrapped.getImageSpace().getImageAxis(Axis.Z_AXIS);

        double x1 = xaxis.gridToReal(xoffset);
        double x2 = x1 + xaxis.getSpacing() * xlen;

        double y1 = xaxis.gridToReal(yoffset);
        double y2 = y1 + yaxis.getSpacing() * ylen;

        double z1 = xaxis.gridToReal(zoffset);
        double z2 = z1 + zaxis.getSpacing() * zlen;


        ImageAxis newxaxis = new ImageAxis(x1, x2, xaxis.getAnatomicalAxis(), xlen);
        ImageAxis newyaxis = new ImageAxis(y1, y2, yaxis.getAnatomicalAxis(), ylen);
        ImageAxis newzaxis = new ImageAxis(z1, z2, zaxis.getAnatomicalAxis(), zlen);

        space = new ImageSpace3D(newxaxis, newyaxis, newzaxis);

    }


    public double value(float x, float y, float z, InterpolationFunction3D interp) {
        //return interp.interpolate(x, y, z, this);
        throw new UnsupportedOperationException();
    }


    public IDataGrid3D subGrid(int x0, int x1, int y0, int y1, int z0, int z1) {
        throw new UnsupportedOperationException();
    }


    public Dimension3D<Integer> dim() {
        return new Dimension3D<Integer>(xlen, ylen, zlen);
    }


    public int indexOf(int i, int j, int k) {
        return wrapped.indexOf(i-xoffset, j-yoffset, k-zoffset);
    }


    public double value(int x, int y, int z) {
        return wrapped.value(x + xoffset, y + yoffset, z + zoffset);
    }



    public double value(int index) {
        return wrapped.value(index + indexOffset);
    }


    public int length() {
        return space.getNumSamples();
    }


    public ValueIterator iterator() {
        //return new Iterator3D(this);
        throw new UnsupportedOperationException();
    }


}
