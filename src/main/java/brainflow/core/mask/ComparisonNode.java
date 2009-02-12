package brainflow.core.mask;

import brainflow.image.operations.BinaryOperand;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 5:14:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ComparisonNode extends AbstractNode {


    private INode left;

    private INode right;

    private BinaryOperand op;

    public ComparisonNode(INode left, INode right, BinaryOperand op) {
        this.left = left;
        this.right = right;
        this.op = op;

        left.setParent(this);
        right.setParent(this);
    }

    public List<INode> getChildren() {
        return Arrays.asList(left, right);
    }

    public BinaryOperand getOp() {
        return op;
    }

    public AbstractNode reduce(ImageDataNode other, BinaryOperand op) {
        throw new UnsupportedOperationException();
    }

    public int depth() {
        return 1 + Math.max(left.depth(), right.depth());
    }

    public INode left() {
        return left;
    }

    public INode right() {
        return right;
    }

    public boolean isLeaf() {
        return false;
    }

    public void apply(TreeWalker walker) {
        walker.caseComparisonNode(this);
    }

    public void replaceChild(INode oldChild, INode newChild) {
        if (left == oldChild) {
            left = newChild;
        } else if (right == oldChild) {
            right = newChild;
        }
    }

    public String toString() {
        return left + " " + op + " " + right;
    }
}
