package brainflow.core.mask;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 18, 2008
 * Time: 3:31:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class RootNode implements INode {

    private INode child;


    public RootNode(INode child) {
        this.child = child;
        child.setParent(this);
    }

    public List<INode> getChildren() {
        return Arrays.asList(child);
    }

    public INode getChild() {
        return child;
    }

    public INode getParent() {
        return null;
    }

    public boolean isLeaf() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int depth() {
        return 1 + child.depth();
    }

    public void apply(TreeWalker walker) {
        walker.caseRootNode(this);
    }

    public void setParent(INode parent) {
        throw new UnsupportedOperationException();
    }

    public void replaceChild(INode oldChild, INode newChild) {
        child = newChild;
    }

    public void replaceBy(INode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "root";
    }
}
