package brainflow.colormap;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 23, 2005
 * Time: 7:44:02 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractInterval implements Interval {

    protected double min;
    
    protected double max;

    public AbstractInterval(double _min, double _max) {
        assert Double.compare(_min, _max) <= 0 : "Interval() : max: " + max + " must be greater than or equal to min: " + min;
        max = _max;
        min = _min;
    }


    public abstract boolean containsValue(double v);

    public abstract boolean overlapsWith(Interval other);

    public abstract boolean containsInterval(Interval other);


    public double getSize() {
        return max - min;
    }



    public int compareTo(Object o) {
        Interval other = (Interval) o;
        if (min > other.getMinimum()) return 1;
        if (min < other.getMinimum()) return -1;
        return 0;
    }

    public final double getIntervalSize() {
        return getMaximum() - getMinimum();

    }


    public final double getMinimum() {
        return min;
    }

    public final double getMaximum() {
        return max;
    }

    public void setRange(double min, double max) {
        this.min = min;
        this.max = max;
    }


    public String toString() {
        return "[" + min + ", " + max + "]";
    }

    public int hashCode() {

        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean equals(Object other) {

        if (!(other instanceof Interval)) return false;

        Interval o1 = (Interval) other;

        int c1 = Double.compare(o1.getMinimum(), min);

        if (c1 != 0) return false;

        int c2 = Double.compare(o1.getMinimum(), min);

        if (c2 != 0) return false;

        return true;


    }

    public static class WithinIntervalComparator implements Comparator {
        public int compare(Object o, Object o1) {
            Double d1 = (Double) o1;
            Interval i1 = (Interval) o;
            if (i1.containsValue(d1)) {
                return 0;
            } else if (i1.getMinimum() > d1) return 1;

            else return -1;

        }

    }

    public static class IntervalComparator implements Comparator {

        public int compare(Object o, Object o1) {

            Interval i1 = (Interval) o;
            Interval i2 = (Interval) o1;

            if (i1.getMinimum() > i2.getMinimum()) return 1;

            else if (i1.getMinimum() < i2.getMinimum()) return -1;

            else if (i1.getMaximum() < i2.getMaximum()) return -1;

            else return 0;
        }
    }

}
