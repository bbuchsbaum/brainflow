package brainflow.core.mask;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 5:03:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface INode {


    public List<INode> getChildren();

    public boolean isLeaf();

    public int depth();
    
    public void apply(TreeWalker walker);

    public void setParent(INode parent);

    public INode getParent();

    public void replaceChild(INode oldChild, INode newChild);

    public void replaceBy(INode node);
    
}
