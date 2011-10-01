package brainflow.image.data;

import brainflow.array.IArray;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.utils.DataType;
import brainflow.utils.IDimension;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: May 2, 2005
 * Time: 1:00:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData extends IArray {


    public DataType getDataType();

    public Anatomy getAnatomy();

    public int getDimension(Axis axisNum);

    public double maxValue();

    public double minValue();
 
    public ImageInfo getImageInfo();
   
    public String getImageLabel();

    public IImageSpace getImageSpace();


    //public ImageBuffer createBuffer(boolean clear);

    //public IImageData multiply(double val);

    

    

}
