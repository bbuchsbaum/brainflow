package brainflow.utils;

import java.text.NumberFormat;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class Range implements IRange {

    private double min;
    private double max;


    public Range(IRange range) {
        min = range.getMin();
        max = range.getMax();
    }

    public Range(double _min, double _max) {
        //assert _max >= _min;
        max = _max;
        min = _min;
    }

    public final double getInterval() {
        return max - min;
    }

    public final double getMin() {
        return min;
    }

    public final double getMax() {
        return max;
    }

    public void setRange(double _min, double _max) {
        if (_min > _max) {
            throw new IllegalArgumentException("min cannot exceed max " + _min + " > " + _max);
        }

        this.min = _min;
        this.max = _max;

    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public boolean contains(Range range) {
        if (range.getMin() >= min && range.getMax() <= max)
            return true;
        return false;
    }

    public final boolean contains(double val) {
        if (val >= min && val <= max)
            return true;
        return false;
    }

    public Range union(Range other) {
        double nmin = 0;
        double nmax = 0;

        nmin = Math.min(min, other.min);
        nmax = Math.max(max, other.max);

        return new Range(nmin, nmax);
    }

    public String toString() {
        NumberFormat format = NumberFormat.getInstance();
        return "(" + format.format(min) + ", " + format.format(max) + ")";
    }

    public String classInfo() {
        ToStringGenerator gen = new ToStringGenerator();
        return gen.generateToString(this);
    }


}