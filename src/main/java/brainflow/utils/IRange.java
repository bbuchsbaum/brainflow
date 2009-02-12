package brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 4:25:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IRange {

    public double getInterval();

    public double getMin();

    public double getMax();

    //public void setMax(double max);

    //public void setMin(double min);

    //public void setRange(double min, double max);

    public boolean contains(double val);

    public String toString();
}
