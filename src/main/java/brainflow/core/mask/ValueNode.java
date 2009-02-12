package brainflow.core.mask;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 30, 2008
 * Time: 1:53:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ValueNode<T> extends INode, LeafNode {


    public String getSymbol();

    public boolean isNumber();

    public T evaluate();



    //public T negate();


}
