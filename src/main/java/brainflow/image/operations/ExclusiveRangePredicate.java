package brainflow.image.operations;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 1:31:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExclusiveRangePredicate extends RangePredicate {


    public boolean apply(double v) {
        if (v < min || v > max) return true;
        else return false;
    }
}
