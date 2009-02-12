package brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Nov 29, 2008
 * Time: 12:40:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataWriter3D extends DataWriter, DataAccessor3D {


    public void setValue(int x, int y, int z, double val);

    public IImageData3D asImageData();
        
}
