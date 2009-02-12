package org.boxwood.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 3, 2008
 * Time: 9:39:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class RealVariable implements IVariable<Double> {

    private final double[] vals;

    private final String name;

    public RealVariable(String name, int length) {
        this.name = name;
        vals = new double[length];
    }

    public RealVariable(String name, double[] vals) {
        this.name = name;
        this.vals = vals;
    }

    public String name() {
        return name;
    }

    public int length() {
        return vals.length;
    }

    public Double valueAt(int i) {
        return vals[i];
    }

    public double get(int i) {
        return vals[i];
    }



    public IVariable<Double> filter(FilterFunction<Double, ? extends IVariable<Double>> func) {
        return func.filter(this);
    }

    public Double apply(SummaryFunction<Double> func) {
        return func.compute(this);
    }

    public Double apply(RealSummary func) {
        return func.compute(this);
    }

    public Class getElementType() {
        return Double.class;
    }
}
