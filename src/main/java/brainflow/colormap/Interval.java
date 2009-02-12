package brainflow.colormap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 23, 2005
 * Time: 11:00:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Interval extends Comparable {


   
    public boolean containsInterval(Interval other);

    public boolean containsValue(double v);

    public boolean overlapsWith(Interval other);
    
    public int compareTo(Object o);

    public double getMinimum();

    public double getMaximum();

    public double getSize();



}
