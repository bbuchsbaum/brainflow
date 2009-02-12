package org.boxwood.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 3, 2008
 * Time: 9:52:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RealSummary extends SummaryFunction<Double> {

    @Override
    public abstract Double compute(IVariable<Double> var);

    public abstract Double compute(RealVariable var);
  
}
