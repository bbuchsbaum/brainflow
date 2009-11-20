package brainflow.array;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 29, 2009
 * Time: 7:57:32 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArrayBuffer2D extends IArrayBuffer, IArray2D {

    public void set(int i, int j, double val);

    public void set(int i, int j, int val);

    public void set(int i, int j, short val);

    public void set(int i, int j, float val);

    public void set(int i, int j, long val);

    public void set(int i, int j, byte val);
}
