package brainflow.core.mask;

import brainflow.image.data.IImageData;
import brainflow.image.operations.BinaryOperand;
import brainflow.image.operations.UnaryOperand;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 30, 2008
 * Time: 6:19:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class FunctionNode extends AbstractNode implements ValueNode<IImageData> {

    private IFunction<IImageData> function;

    private List<INode> argumentNodes;

    public FunctionNode(IFunction<IImageData> function, List<INode> argumentNodes) {
        this.function = function;
        this.argumentNodes = argumentNodes;

        for (INode node : argumentNodes) {
            node.setParent(this);
        }
    }

    public String getSymbol() {
        return function.getName();
    }

    public boolean isNumber() {
        return false;
    }

    public IImageData evaluate() {
        return function.evaluate(argumentNodes);
    }

    public List<INode> getChildren() {
        return argumentNodes;
    }

    public int depth() {
        int md = 0;
        for (INode node : argumentNodes) {
            md = Math.max(md, node.depth());
        }

        return md + 1;
    }

    public void apply(TreeWalker walker) {
        walker.caseFunctionNode(this);
    }

    @Override
    public String toString() {
        return function.getName();
    }

    public LeafNode visitUnary(UnaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LeafNode visitImage(ImageDataNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LeafNode visitMask(MaskDataNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LeafNode visitConstant(ConstantNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LeafNode accept(LeafNode leaf, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
