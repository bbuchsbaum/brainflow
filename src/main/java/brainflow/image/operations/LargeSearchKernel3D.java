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
public class LargeSearchKernel3D implements ISearchKernel3D {


    private ImageSpace3D space;


    private int dimx;
    private int dimy;
    private int dimz;

    private int planexy;

    private int i1, i2, i3, i4, i5, i6, i7;
    private int i8, i9, i10, i11, i12, i13;
    
    public LargeSearchKernel3D(ImageSpace3D _space, double dx, double dy, double dz) {
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
        i8 = planexy -1 -dimx;
        //todo i9?
        i10 = planexy -dimx;
        i11 = planexy +1 -dimx;
        i12 = planexy -1;
        i13 = planexy -1 + dimx;

    }


    public IntegerStack pushKernel(int index, int[] labels, IntegerStack stack) {

        if (labels[index+i1] > 0) stack.push(index+i1); // x1, y0, z0
        if (labels[index+i2] > 0) stack.push(index+i2); // x0, y1, z0
        if (labels[index+i3] > 0) stack.push(index+i3); // x0, y0, z1
        if (labels[index+i4] > 0) stack.push(index+i4); // x1, y1, z0
        if (labels[index+i5] > 0) stack.push(index+i5); // x1, y0, z1
        if (labels[index+i6] > 0) stack.push(index+i6); // x0, y1, z1
        if (labels[index+i7] > 0) stack.push(index+i7); // x1, y1, z1
        if (labels[index+i8] > 0) stack.push(index+i8); // x0, y1, z1
        if (labels[index+i9] > 0) stack.push(index+i9); // x1, y1, z1
        if (labels[index+i10] > 0) stack.push(index+i10); // x1, y1, z1
        if (labels[index+i11] > 0) stack.push(index+i11); // x1, y1, z1
        if (labels[index+i12] > 0) stack.push(index+i12); // x1, y1, z1
        if (labels[index+i13] > 0) stack.push(index+i13); // x1, y1, z1
        return stack;

    }




}
