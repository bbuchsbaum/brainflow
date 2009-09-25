package brainflow.image.data;

import brainflow.image.anatomy.Anatomy;
import brainflow.image.io.ImageInfo;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.utils.DataType;
import org.boxwood.array.IDataGrid;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: May 2, 2005
 * Time: 1:00:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData extends IDataGrid {


    public DataType getDataType();

    public Anatomy getAnatomy();

    public int getDimension(Axis axisNum);

    public double maxValue();

    public double minValue();
 
    public ImageInfo getImageInfo();
   
    public String getImageLabel();

    public IImageSpace getImageSpace();

    //public ImageBuffer createWriter(boolean clear);

    //public IImageData multiply(double val);

    

    

}
