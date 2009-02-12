package brainflow.core.mask;

import brainflow.image.operations.BinaryOperand;
import brainflow.image.operations.BinaryOperation;
import brainflow.image.operations.Operations;
import brainflow.image.operations.UnaryOperand;
import brainflow.image.data.IMaskedData3D;
import brainflow.image.data.MaskedData3D;
import brainflow.image.data.IImageData3D;
import brainflow.image.data.MaskPredicate;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 5:04:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConstantNode extends AbstractNode implements ValueNode<Double> {


    private double value;


    public ConstantNode(double value) {
        this.value = value;
    }

    public String toString() {
        return "" + value;
    }

    public Double evaluate() {
        return value;
    }

    public String getSymbol() {
        return "" + value;
    }

    public boolean isNumber() {
        return true;
    }

    public int depth() {
        return 0;
    }

    public LeafNode accept(LeafNode leaf, BinaryOperand op) {
        return leaf.visitConstant(this, op);
    }


    public LeafNode visitImage(ImageDataNode left, BinaryOperand op) {

        final BinaryOperation bop = Operations.lookup(op);

        IMaskedData3D mdat = new MaskedData3D((IImageData3D) left.evaluate(), new MaskPredicate() {
            public boolean mask(double ovalue) {
                return bop.isTrue(value, ovalue);
            }
        });

        return new MaskDataNode(mdat);
    }

    public LeafNode visitUnary(UnaryOperand op) {
        switch (op) {
            case ADD:
                return this;
            case NEGATE:
                System.out.println("negating node");
                return new ConstantNode(-value);
            default:
                throw new SemanticError("unsupported operation " + op + " for value : " + value);
        }

    }

    public LeafNode visitConstant(ConstantNode other, BinaryOperand op) {
        switch (op) {
            case AND:
                throw new SemanticError("Cannot apply " + op + " to two constants");
            case EQ:
                if (other.value == value) {
                    return new ConstantNode(1);
                } else {
                    return new ConstantNode(0);
                }
            case GT:
                if (other.value > value) {
                    return new ConstantNode(1);
                } else {
                    return new ConstantNode(0);
                }

            case GT_EQ:
                if (other.value >= value) {
                    return new ConstantNode(1);
                } else {
                    return new ConstantNode(0);
                }
            case LT:
                if (other.value < value) {
                    return new ConstantNode(1);
                } else {
                    return new ConstantNode(0);
                }
            case LT_EQ:
                if (other.value <= value) {
                    return new ConstantNode(1);
                } else {
                    return new ConstantNode(0);
                }

            case OR:
                throw new SemanticError("Cannot apply " + op + " to two constants");
            default:
                throw new SemanticError("error: reached default switch case ...");
        }
    }

    public LeafNode visitMask(MaskDataNode other, BinaryOperand op) {
        throw new UnsupportedOperationException("");
    }

    public boolean isLeaf() {
        return true;
    }

    public void apply(TreeWalker walker) {
        walker.caseConstantNode(this);
    }

    public List<INode> getChildren() {
        return Arrays.asList();
    }
}
