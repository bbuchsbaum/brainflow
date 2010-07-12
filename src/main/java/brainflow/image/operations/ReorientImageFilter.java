/*
 * ReorientImageFilter.java
 *
 * Created on May 14, 2003, 9:31 AM
 */

package brainflow.image.operations;


import brainflow.image.data.BasicImageData3D;
import brainflow.image.data.IImageData;
import brainflow.image.data.IImageData3D;
import brainflow.image.space.Axis;
import brainflow.image.space.ImageSpace3D;
import brainflow.utils.Point3D;

import java.util.ArrayList;
import java.util.List;


/**
 * @author bradley
 */
public class ReorientImageFilter extends brainflow.image.operations.AbstractImageFilter {

    private boolean flipx = false;
    private boolean flipy = false;
    private boolean flipz = false;

    List swapList = new ArrayList();

    /**
     * Creates a new instance of ReorientImageFilter
     */
    public ReorientImageFilter() {
    }

    public void setAxisFlip(Axis axis) {
        if (axis == Axis.X_AXIS) {
            flipx = true;
        } else if (axis == Axis.Y_AXIS)
            flipy = true;
        else if (axis == Axis.Z_AXIS)
            flipz = true;
    }


    public IImageData getOutput() {
        /* BasicImageData3D src = (BasicImageData3D) getSources().get(0);
 if (src == null) throw new RuntimeException("ImageFilter requires a source operations");
 if (flipx) swapList.add(new FlipX(src));
 if (flipy) swapList.add(new FlipY(src));
 if (flipz) swapList.add(new FlipZ(src));

 CoordinateSwap3D[] swaps = new CoordinateSwap3D[swapList.size()];
 if (swapList.size() == 0) {
     swaps = new CoordinateSwap3D[1];
     swaps[0] = new IdentitySwap();
 } else
     swapList.toArray(swaps);

 IImageData3D op = new BasicImageData3D(src.getCoordinateSpace(), getOutputDataType());
 ImageSpace3D space = (ImageSpace3D) src.getCoordinateSpace();
 XYZIterator iter = Space.createXYZiterator(space);


 Point3D holder = new Point3D();
 Point3D holder2 = new Point3D();
 while (iter.hasNext()) {
     holder.zero = iter.getXIndex();
     holder.zero = iter.getYIndex();
     holder.one = iter.getZIndex();
     double val = src.file((int) holder.zero, (int) holder.zero, (int) holder.one);
     for (int i = 0; i < swaps.length; i++) {
         swaps[i].swap(holder, holder2);
         holder = holder2;

     }
     System.out.println(holder);
     System.out.println(val);
     System.out.println(holder2);
     op.setValue((int) holder2.zero, (int) holder2.zero, (int) holder2.one, val);
     iter.nextIndex();
 }       */

        return null;


    }

    class FlipX {
        IImageData3D src;
        ImageSpace3D space;
        int xdim;

        public FlipX(IImageData3D _src) {
            src = _src;
            space = (ImageSpace3D) src.getImageSpace();
            xdim = space.getDimensionVector()[0];
        }

        public Point3D swap(brainflow.utils.Point3D in, brainflow.utils.Point3D out) {
            out.y = in.y;
            out.z = in.z;
            out.x = (xdim - 1) - in.x;
            return out;
        }

    }

    class FlipY {
        BasicImageData3D src;
        ImageSpace3D space;
        int ydim;

        public FlipY(BasicImageData3D _src) {
            src = _src;
            space = (ImageSpace3D) src.getImageSpace();
            ydim = space.getDimensionVector()[1];

        }

        public Point3D swap(brainflow.utils.Point3D in, brainflow.utils.Point3D out) {
            out.x = in.x;
            out.z = in.z;
            out.y = (ydim - 1) - in.y;
            return out;
        }

    }

    class FlipZ {
        IImageData3D src;
        ImageSpace3D space;
        int zdim;

        public FlipZ(BasicImageData3D _src) {
            src = _src;
            space = (ImageSpace3D) src.getImageSpace();
            zdim = space.getDimensionVector()[2];

        }

        public Point3D swap(brainflow.utils.Point3D in, brainflow.utils.Point3D out) {
            out.x = in.x;
            out.y = in.y;
            out.z = (zdim - 1) - in.z;
            return out;
        }

    }

}
