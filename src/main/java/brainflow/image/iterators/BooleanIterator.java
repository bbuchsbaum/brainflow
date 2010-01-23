package brainflow.image.iterators;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 22, 2009
 * Time: 9:24:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BooleanIterator extends ValueIterator {

    public boolean nextBoolean();

    public void advance();

    public boolean hasNext();

    public int index();


}