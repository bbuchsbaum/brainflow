package brainflow.array;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 29, 2009
 * Time: 7:59:38 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArrayBuffer4D extends IArrayBuffer, IArray4D {

    public void set(int i, int j, int k, int m, double val);

    public void set(int i, int j, int k, int m, int val);

    public void set(int i, int j, int k, int m, short val);

    public void set(int i, int j, int k, int m, float val);

    public void set(int i, int j, int k, int m, long val);

    public void set(int i, int j, int k, int m, byte val);



}
