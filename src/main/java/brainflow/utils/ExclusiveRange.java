package brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 8, 2007
 * Time: 9:31:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExclusiveRange implements IRange {

    private double min;

    private double max;


    public ExclusiveRange(IRange range) {
        min = range.getMin();
        max = range.getMax();

        if (min > max) {
            throw new IllegalStateException("min cannot exceed max " + min + " > " + max);
        }
    }

    public ExclusiveRange(double _min, double _max) {
        if (_min > _max) {
            throw new IllegalArgumentException("min cannot exceed max " + _min + " > " + _max);
        }

        max = _max;
        min = _min;
    }

    public void setRange(double _min, double _max) {
        if (_min > _max) {
            throw new IllegalArgumentException("min cannot exceed max " + _min + " > " + _max);
        }

        this.min = _min;
        this.max = _max;

    }

    public boolean contains(double val) {
        if (val <= min || val >= max)
            return true;
        return false;
    }

    public double getInterval() {
        return max - min;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public void setMax(double max) {
        if (getMin() > max) {
            throw new IllegalArgumentException("min cannot exceed max " + getMin() + " > " + max);
        }

        this.max = max;


    }

    public void setMin(double min) {
        if (min > getMax()) {
            throw new IllegalArgumentException("min cannot exceed max " + min + " > " + getMax());
        }

        this.min = min;

    }
}
