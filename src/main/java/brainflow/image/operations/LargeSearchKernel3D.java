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


    //private int dimx;
    //private int dimy;
    //private int dimz;

    private int planexy;

    private int i1, i2, i3, i4, i5, i6, i7,
    i8, i9, i10, i11, i12, i13,
    i14, i15, i16, i17, i18, i19, i20, i21,
    i22, i23, i24, i25, i26;

    private int numElements;
    
    public LargeSearchKernel3D(ImageSpace3D _space) {
        space = _space;
        int dimx = space.getDimension(Axis.X_AXIS);
        int dimy = space.getDimension(Axis.Y_AXIS);
        int dimz = space.getDimension(Axis.Z_AXIS);
        planexy = dimx*dimy;
        numElements = space.getNumSamples();

        i1 = -1 -dimx -planexy;   // x0, y0, z0
        i2 = -dimx -planexy;      // x1, y0, z0
        i3 = 1 -dimx -planexy;    // x2, y0, z0
        i4 = -1 -planexy;         // x0, y1, z0
        i5 = -planexy;            // x1, y1, z0
        i6 = -planexy + 1;        // x2, y1, z0
        i7 = -1 -planexy+dimx;    // x0, y2, z0
        i8 = -planexy + dimx;     // x1, y2, z0
        i9 = -planexy + dimx + 1; // x2, y2, z0
        i10 = -1 -dimx;           // x0, y0, z1;
        i11 = -dimx;              // x1, y0, z1
        i12 = 1 -dimx;            // x2, y0, z1;
        i13 = -1;                 // x0, y1, z1;
        i14 = 1;                  // x2, y1, z1;
        i15 = -1+dimx;            // x0, y2, z1;
        i16 = dimx;               // x1, y2, z1;
        i17 = 1+dimx;             // x2, y2, z1;
        i18 = -1 -dimx +planexy;  // x0, y0, z2;
        i19 = -dimx +planexy;     // x0, y1, z2;
        i20 = 1 -dimx +planexy;
        i21 = -1 +planexy;
        i22 = planexy;
        i23 = planexy + 1;
        i24 = -1 +planexy+dimx;
        i25 = planexy + dimx;
        i26 = planexy + dimx + 1;

    }

    private final void dopush(int index, int i, int[] labels, IntegerStack stack) {
        int val = index+i;
        if (val >= 0 && val < numElements && labels[val] > 0) stack.push(val);

    }


    public IntegerStack pushKernel(int index, int[] labels, IntegerStack stack) {
        dopush(index, i1, labels, stack);
        dopush(index, i2, labels, stack);
        dopush(index, i3, labels, stack);
        dopush(index, i4, labels, stack);
        dopush(index, i5, labels, stack);
        dopush(index, i6, labels, stack);
        dopush(index, i7, labels, stack);
        dopush(index, i8, labels, stack);
        dopush(index, i9, labels, stack);
        dopush(index, i10, labels, stack);
        dopush(index, i11, labels, stack);
        dopush(index, i12, labels, stack);
        dopush(index, i13, labels, stack);
        dopush(index, i14, labels, stack);
        dopush(index, i15, labels, stack);
        dopush(index, i16, labels, stack);
        dopush(index, i17, labels, stack);
        dopush(index, i18, labels, stack);
        dopush(index, i19, labels, stack);
        dopush(index, i20, labels, stack);
        dopush(index, i21, labels, stack);
        dopush(index, i22, labels, stack);
        dopush(index, i23, labels, stack);
        dopush(index, i24, labels, stack);
        dopush(index, i25, labels, stack);
        dopush(index, i26, labels, stack);





        return stack;

    }




}
