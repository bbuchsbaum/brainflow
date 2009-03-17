package brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 14, 2008
 * Time: 10:38:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMaskedData {

    public int cardinality();

    public boolean isTrue(int index);


}
