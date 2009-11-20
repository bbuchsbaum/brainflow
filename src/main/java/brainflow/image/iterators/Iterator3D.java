package brainflow.image.iterators;

import brainflow.array.IDataGrid3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 26, 2009
 * Time: 9:44:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Iterator3D implements ValueIterator {

    private int index;

    private IDataGrid3D data;

    //private IImageSpace3D space;

    private int len;
    private int end;


    public Iterator3D(IDataGrid3D _data) {
        data = _data;
        //space = data.getImageSpace();
        index = 0;
        
        len = data.length();
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

