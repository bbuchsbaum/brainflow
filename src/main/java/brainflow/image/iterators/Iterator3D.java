package brainflow.image.iterators;

import brainflow.image.data.DataGrid3D;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 26, 2009
 * Time: 9:44:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Iterator3D implements ValueIterator {

    private int index;

    private DataGrid3D data;

    //private IImageSpace3D space;

    private int len;
    private int end;


    public Iterator3D(DataGrid3D _data) {
        data = _data;
        //space = data.getImageSpace();
        index = 0;
        
        len = data.numElements();
        end = len;
    }

    public double next() {
        double dat = data.value(index);
        index++;
        return dat;
    }

    public void advance() {
        index++;
    }



    public boolean hasNext() {
        if (index < end) return true;
        return false;
    }



    public final int index() {
        return index;
    }


}

