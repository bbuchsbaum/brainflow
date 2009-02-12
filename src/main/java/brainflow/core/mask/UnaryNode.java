package brainflow.core.mask;

import brainflow.image.operations.UnaryOperand;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Nov 6, 2008
 * Time: 9:04:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnaryNode extends AbstractNode {

    private INode child;

    private UnaryOperand operand;

    public UnaryNode(INode node, UnaryOperand operand) {
        child = node;
        child.setParent(this);
        this.operand = operand;
    }

    public int depth() {
        return 1 + child.depth();
    }

    public INode getChild() {
        return child;
    }

    public UnaryOperand getOp() {
        return operand;
    }


    public void apply(TreeWalker walker) {
        walker.caseUnaryNode(this);
    }

    public void replaceChild(INode oldChild, INode newChild) {
        child = newChild;
    }

    @Override
    public List<INode> getChildren() {
        return Arrays.asList(child);
    }
}
