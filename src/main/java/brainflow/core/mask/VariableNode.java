package brainflow.core.mask;

import brainflow.image.operations.BinaryOperand;
import brainflow.image.operations.UnaryOperand;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 5:05:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariableNode extends AbstractNode implements ValueNode {

   
    private String varName;

    private Context context;

    public VariableNode(String varName, Context context) {
        this.varName = varName;
        this.context = context;
    }

    public String getVarName() {
        return varName;
    }

    public String getSymbol() {
        return varName;
    }

    public boolean isNumber() {
        return false;
    }

    public Object evaluate() {
        return context.getValue(varName);
    }

    public String toString() {
        return varName;
    }

    public boolean isLeaf() {
        return true;
    }

    public int depth() {
        return 0;  
    }

    public void apply(TreeWalker walker) {
        walker.caseVariableNode(this);
    }

    public List<INode> getChildren() {
        return Arrays.asList();
    }

    public LeafNode visitUnary(UnaryOperand op) {
        throw new UnsupportedOperationException();       
    }

    public LeafNode visitImage(ImageDataNode other, BinaryOperand op) {
        throw new UnsupportedOperationException();
    }

    public LeafNode visitMask(MaskDataNode other, BinaryOperand op) {
        throw new UnsupportedOperationException();
    }

    public LeafNode visitConstant(ConstantNode other, BinaryOperand op) {
        throw new UnsupportedOperationException();
    }

    public LeafNode accept(LeafNode leaf, BinaryOperand op) {
        throw new UnsupportedOperationException();
    }
}
