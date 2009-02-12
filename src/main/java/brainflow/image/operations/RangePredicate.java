package brainflow.image.operations;


import cern.colt.function.DoubleProcedure;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 1:27:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RangePredicate implements DoubleProcedure {

    protected double min;

    protected double max;

    public RangePredicate() {
    }

    public RangePredicate(double _min, double _max) {
        min = _min;
        max = _max;

        if (min <= max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
    }

    public abstract boolean apply(double v);


}
