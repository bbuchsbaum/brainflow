package brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2006
 * Time: 11:54:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface QuickIterator {

    public boolean hasNext();

    public int next();

    public void reset();
    
    public int size();

}
