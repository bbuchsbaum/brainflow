/*
 * ImageFilter.java
 *
 * Created on March 22, 2003, 5:49 PM
 */

package brainflow.image.operations;
import brainflow.image.data.IImageData;
import brainflow.utils.DataType;


/**
 *
 * @author  Bradley
 */
public interface ImageFilter {
    
    public void addInput(IImageData data);

    public void setInput(int i, IImageData _data);

    public IImageData getOutput();

    public void setOutputDataType(DataType dtype);
    
}
