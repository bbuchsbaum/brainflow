package brainflow.array;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 29, 2009
 * Time: 7:56:50 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArrayBuffer extends IArray {

    public void set(int i, double val);

    public void set(int i, float val);

    public void set(int i, short val);

    public void set(int i, int val);

    public void set(int i, long val);

    public void set(int i, byte val);


}
