package org.boxwood.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 3, 2008
 * Time: 10:09:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class FactorVariable implements IVariable<Level> {

    private final String name="";


    public String name() {
        return name;
    }

    public Level apply(SummaryFunction<Level> func) {
        return func.compute(this);
    }

    public IVariable<Level> filter(FilterFunction<Level, ? extends IVariable<Level>> func) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int length() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Level valueAt(int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Class getElementType() {
        return Level.class;
    }
}
