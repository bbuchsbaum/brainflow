package brainflow.image.operations;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 18, 2007
 * Time: 2:41:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ISearchKernel3D {

    public IntegerStack pushKernel(int index, int[] labels, IntegerStack stack);
}
