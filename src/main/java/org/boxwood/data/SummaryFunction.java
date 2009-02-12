package org.boxwood.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 3, 2008
 * Time: 9:48:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SummaryFunction<T> {

    public T compute(IVariable<T> var);
}
