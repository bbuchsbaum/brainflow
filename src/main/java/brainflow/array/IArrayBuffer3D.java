package brainflow.array;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 29, 2009
 * Time: 7:58:35 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArrayBuffer3D extends IArrayBuffer, IArray3D {


    public void set(int i, int j, int k, double val);

    public void set(int i, int j, int k, int val);

    public void set(int i, int j, int k, short val);

    public void set(int i, int j, int k, float val);

    public void set(int i, int j, int k, long val);

    public void set(int i, int j, int k, byte val);



}
