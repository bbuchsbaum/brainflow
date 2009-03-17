package brainflow.image.operations;

import brainflow.image.space.ImageSpace3D;
import brainflow.image.space.Axis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 18, 2007
 * Time: 10:02:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchKernel3D implements ISearchKernel3D {


    private ImageSpace3D space;


    private int dimx;

    private int dimy;

    private int dimz;

    private int planexy;

    private final int i1, i2, i3, i4, i5, i6, i7;

    public SearchKernel3D(ImageSpace3D _space, double dx, double dy, double dz) {
        space = _space;
        dimx = space.getDimension(Axis.X_AXIS);
        dimy = space.getDimension(Axis.Y_AXIS);
        dimz = space.getDimension(Axis.Z_AXIS);
        planexy = dimx*dimy;

        i1 = 1;
        i2 = dimx;
        i3 = planexy;
        i4 = 1+dimx;
        i5 = 1+planexy;
        i6 = dimx+planexy;
        i7 = 1+dimy+planexy;

    }

  
    public IntegerStack pushKernel(int index, int[] labels, IntegerStack stack) {

        if (labels[index+i1] > 0) stack.push(index+i1); // x1, y0, z0
        if (labels[index+i2] > 0) stack.push(index+i2); // x0, y1, z0
        if (labels[index+i3] > 0) stack.push(index+i3); // x0, y0, z1
        if (labels[index+i4] > 0) stack.push(index+i4); // x1, y1, z0
        if (labels[index+i5] > 0) stack.push(index+i5); // x1, y0, z1
        if (labels[index+i6] > 0) stack.push(index+i6); // x0, y1, z1
        if (labels[index+i7] > 0) stack.push(index+i7); // x1, y1, z1
        return stack;

    }

    


}
