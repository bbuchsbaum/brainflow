package brainflow.core.mask;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 30, 2008
 * Time: 6:24:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IFunction<T> {


    public List<Class> getArgumentTypes();
    
    public int getNumArguments();

    public T evaluate(List<INode> arguments);

    public String getName();

    




}
