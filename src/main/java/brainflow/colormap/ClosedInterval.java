package brainflow.colormap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 11, 2007
 * Time: 1:02:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClosedInterval extends AbstractInterval {


    public ClosedInterval(double _min, double _max) {
        super(_min, _max);    //To change body of overridden methods use File | Settings | File Templates.
    }


    public boolean overlapsWith(Interval other) {
        if (other.getMaximum() <= min) return false;
        if (other.getMinimum() >= max) return false;
        return true;
    }


    public boolean containsInterval(Interval other) {
        if (min < other.getMinimum() && max > other.getMaximum()) {
            return true;
        }

        return false;

    }

    public boolean containsValue(double v) {
        if (v > min && v < max) return true;
        return false;
    }
}
