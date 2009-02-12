package org.boxwood.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 3, 2008
 * Time: 9:37:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IVariable<T> {

    public String name();

    public T valueAt(int i);

    public int length();

    public T apply(SummaryFunction<T> func);

    public IVariable<T>  filter(FilterFunction<T, ? extends IVariable<T>> func);

    public Class<?> getElementType();




}
