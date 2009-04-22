package brainflow.colormap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 1, 2007
 * Time: 12:24:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexedInterval implements Interval {
    
    private final double[] boundaryArray;

    private int index;

    public IndexedInterval(int _index, double[] _boundaryArray) {
        if (_index < 1 || _index >= (_boundaryArray.length) ) {
            throw new IllegalArgumentException("index outside boundary array: " + _index);
        }
        boundaryArray = _boundaryArray;
        index = _index;
    }


    public boolean overlapsWith(Interval other) {
        if (other.getMaximum() <= getMinimum()) return false;
        if (other.getMinimum() >= getMaximum()) return false;
        return true;

    }

    public boolean containsInterval(Interval other) {
        if (getMinimum() < other.getMinimum() && getMaximum() > other.getMaximum()) return true;

        return false;

    }

    public boolean containsValue(double v) {
        if (v >= getMinimum() && v <= getMaximum()) return true;

        return false;
    }

    public int compareTo(Object o) {
         Interval other = (Interval) o;
        if (getMinimum() > other.getMinimum()) return 1;
        if (getMaximum() < other.getMinimum()) return -1;
        return 0;
    }

    public double getMinimum() {
        return boundaryArray[index-1];
    }

    public int getIndex() {
        return index;
    }

    public double getMaximum() {
        return boundaryArray[index];
    }

    public double getSize() {
        return getMaximum() - getMinimum();
    }




}
