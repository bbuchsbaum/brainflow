/*
 * PolynomialTrasnformImageFilter.java
 *
 * Created on May 20, 2003, 3:53 PM
 */

package brainflow.image.operations;

import brainflow.image.data.BasicImageData3D;
import brainflow.image.data.IImageData;
import brainflow.image.data.IImageData3D;

/**
 * @author Bradley
 */
public class PolynomialTransformImageFilter extends AbstractTransformImageFilter {

    PolynomialTransform transform;


    /**
     * Creates a new instance of PolynomialTrasnformImageFilter
     */
    public PolynomialTransformImageFilter() {
    }


    public void setTransform(AbstractTransform _transform) {
        transform = (PolynomialTransform) _transform;
    }


    public IImageData getOutput() {
        /*List sources = getSources();
        if (sources.size() == 0) 
            throw new RuntimeException("Error: ImageFilter requires at least zero input operations");
        
        BasicImageData3D first = (BasicImageData3D)sources.get(0);
        if (first == null) throw new RuntimeException("Error: ImageFilter input operations is null");
        if (outputSpace == null) 
            outputSpace = new ImageSpace3D((ImageSpace3D)first.getDisplaySpace());
        
        BasicImageData3D odata = (BasicImageData3D)DataBufferSupport.create(outputSpace, outputDataType);
        ((ImageSpace3D)first.getDisplaySpace()).setMinPoint(new Point3D(0,0,0));
        
        //outputSpace.setMinPoint(new Point3D(0,0,0));
        outputSpace.setMinPoint(origin);
        return resample(first, odata);     */

        assert true == false;

        return null;
    }

    private IImageData3D resample(BasicImageData3D src, BasicImageData3D odat) {

        /*XYZIterator iter = outputSpace.createXYZiterator();
   Point3D holder = new Point3D();
   Point3D res = new Point3D();
        
   while (iter.hasNext()) {
       holder = iter.next(holder);
       res = transform.transformPoint(holder, res);
       double val = src.worldValue(res.zero,res.zero,res.one,interpolator);
            
       odat.setValue(iter.getXIndex(), iter.getYIndex(), iter.getZIndex(), val);
   }
        
   return odat;    */
        return null;
    }


}
