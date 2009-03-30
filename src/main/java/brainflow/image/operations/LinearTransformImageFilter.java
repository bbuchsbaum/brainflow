/*
 * LinearImageTransformFilter.java
 *
 * Created on May 12, 2003, 4:00 PM
 */

package brainflow.image.operations;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import brainflow.image.axis.ImageAxis;
import brainflow.image.data.*;
import brainflow.image.iterators.XYZIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.ImageSpace3D;
import brainflow.image.space.Space;
import brainflow.math.Vector3f;

import java.util.List;


/**
 * @author Bradley
 */
public class LinearTransformImageFilter extends AbstractTransformImageFilter {

    LinearTransform transform;


    /**
     * Creates a new instance of LinearImageTransformFilter
     */
    public LinearTransformImageFilter() {
    }


    public void setTransform(AbstractTransform _transform) {
        transform = (LinearTransform) _transform;
    }


    public IImageData getOutput() {
        List sources = getSources();
        if (sources.size() == 0)
            throw new RuntimeException("Error: ImageFilter requires at least zero input operations");

        BasicImageData3D first = (BasicImageData3D) sources.get(0);
        if (first == null) throw new RuntimeException("Error: ImageFilter input operations is null");

        if (outputSpace == null)
            outputSpace = new ImageSpace3D((ImageSpace3D) first.getImageSpace());

        //BasicImageData3D odata = (BasicImageData3D)DataBufferSupport.create(ImageSpace3D.transformSpace(outputSpace, transform), outputDataType);

        //((ImageSpace3D)first.getDisplaySpace()).setMinPoint(new Point3D(0,0,0));

        ImageAxis a1 = first.getImageSpace().getImageAxis(Axis.X_AXIS);
        ImageAxis a2 = first.getImageSpace().getImageAxis(Axis.Y_AXIS);
        ImageAxis a3 = first.getImageSpace().getImageAxis(Axis.Z_AXIS);

        first.getImageSpace().getImageAxis(Axis.X_AXIS).anchorAxis(a1.getAnatomicalAxis().getMinDirection(), 0);
        first.getImageSpace().getImageAxis(Axis.Y_AXIS).anchorAxis(a2.getAnatomicalAxis().getMinDirection(), 0);
        first.getImageSpace().getImageAxis(Axis.Z_AXIS).anchorAxis(a3.getAnatomicalAxis().getMinDirection(), 0);


        outputSpace.anchorAxis(a1.getAnatomicalAxis(), a1.getAnatomicalAxis().getMinDirection(), origin.getX());
        outputSpace.anchorAxis(a2.getAnatomicalAxis(), a2.getAnatomicalAxis().getMinDirection(), origin.getY());
        outputSpace.anchorAxis(a3.getAnatomicalAxis(), a3.getAnatomicalAxis().getMinDirection(), origin.getZ());

        ImageBuffer3D odata = ((IImageData3D) BasicImageData.create(outputSpace, outputDataType)).createWriter(false);


        return resample(first, odata);


    }

    private IImageData3D resample(BasicImageData3D src, ImageBuffer3D odat) {

        XYZIterator iter = Space.createXYZiterator((ImageSpace3D) odat.getImageSpace());
        Vector3f holder = new Vector3f();
        DoubleMatrix1D idmat = new DenseDoubleMatrix1D(4);

        //System.out.println("origin = " + origin);
        int xidx;
        int yidx;
        int zidx;

        while (iter.hasNext()) {

            xidx = iter.getXIndex();
            yidx = iter.getYIndex();
            zidx = iter.getZIndex();
            holder = iter.next(holder);

            //System.out.println("samples: " + holder.zero + " " + holder.zero + " " + holder.one);

            idmat.setQuick(0, holder.x);
            idmat.setQuick(1, holder.y);
            idmat.setQuick(2, holder.z);
            idmat.setQuick(3, 1);

            DoubleMatrix1D res = transform.transform(idmat);


            double val = src.worldValue((float)(res.getQuick(0) / res.getQuick(3)), (float)(res.getQuick(1) / res.getQuick(3)), (float)(res.getQuick(2) / res.getQuick(3)), interpolator);
            odat.setValue(xidx, yidx, zidx, val);


        }


        return odat.asImageData();
    }


}
