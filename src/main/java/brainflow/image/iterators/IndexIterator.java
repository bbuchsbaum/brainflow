package brainflow.image.iterators;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 12, 2008
 * Time: 9:27:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IndexIterator {

    public boolean hasNext();

    public int get();

    public int next();
}
