package org.boxwood.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 3, 2008
 * Time: 9:56:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeanSummary implements RealSummary {

    @Override
    public Double compute(IVariable<Double> var) {
        double sum=0;

        for (int i=0; i<var.length(); i++) {
            sum += var.valueAt(i);
        }

        return sum;

    }
    
    @Override
    public Double compute(RealVariable var) {
        double sum=0;

        for (int i=0; i<var.length(); i++) {
            sum += var.get(i);
        }

        return sum;

    }
}
