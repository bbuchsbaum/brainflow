package brainflow.core.mask;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 5:22:27 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractNode implements INode {

    private INode parent;

    public INode getParent() {
        return parent;
    }

    public void setParent(INode parent) {
        this.parent = parent;
    }

    public List<INode> getChildren() {
        return Arrays.asList();
    }

    public boolean isLeaf() {
        return getChildren().size() == 0;
    }

   

    public void replaceChild(INode oldChild, INode newChild) {
        throw new UnsupportedOperationException();

    }

    public void replaceBy(INode node) {
        if (parent != null) {
            parent.replaceChild(this, node);
        } else {
            throw new RuntimeException("cannot replace parentless node");
        }
    }


}
