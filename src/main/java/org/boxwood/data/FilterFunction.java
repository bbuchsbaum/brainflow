package org.boxwood.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 8:08:58 AM
 * To change this template use File | Settings | File Templates.
 */
public interface FilterFunction<T, K extends IVariable> {


    public K filter(IVariable<T> var);


}
