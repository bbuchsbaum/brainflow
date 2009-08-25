package brainflow.core.mask;

import brainflow.image.data.*;
import brainflow.image.operations.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 12, 2008
 * Time: 9:25:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskDataNode extends AbstractNode implements ValueNode<IMaskedData3D>, LeafVisitable {


    private IMaskedData3D mask;

    private String name;


    public MaskDataNode(IMaskedData3D mask) {
        this.mask = mask;
        name = mask.getImageLabel();
    }


    public MaskDataNode(String name, IMaskedData3D mask) {
        this.mask = mask;
        this.name = name;
    }

    public void apply(TreeWalker walker) {
        walker.caseMaskDataNode(this);
    }

    public IMaskedData3D getData() {
        return mask;
    }

    public String getSymbol() {
        return name;
    }

    public boolean isNumber() {
        return false;
    }

    public IMaskedData3D evaluate() {
        return mask;
    }

    public LeafNode accept(LeafNode leaf, BinaryOperand op) {
        return leaf.visitMask(this, op );
    }

    public LeafNode visitImage(ImageDataNode left, BinaryOperand op) {
        BinaryOperation bop = Operations.lookup(op);

        if (!(bop instanceof BooleanOperation)) {
            throw new SemanticError("illegal operation : " + op);
        }

        BooleanOperation boolop = (BooleanOperation) bop;

        IMaskedData3D mdat = new MaskedData3D((IImageData3D) left.evaluate(), new MaskPredicate() {
            public boolean mask(double value) {
                return value > 0;
            }
        });

        BooleanMaskNode3D data = new BooleanMaskNode3D(mdat, mask, boolop);
        return new MaskDataNode(data);

    }

    public LeafNode visitConstant(ConstantNode left, BinaryOperand op) {
        BinaryOperation bop = Operations.lookup(op);

        if (!(bop instanceof BooleanOperation)) {
            throw new SemanticError("illegal operation : " + op);
        }

        BooleanOperation boolop = (BooleanOperation) bop;

        IImageData3D cdat = Data.createConstantData(left.evaluate().doubleValue(), mask.getImageSpace());

        IMaskedData3D mdat = new MaskedData3D(cdat, new MaskPredicate() {
            public boolean mask(double value) {
                return value > 0;
            }
        });

        BooleanMaskNode3D data = new BooleanMaskNode3D(mdat, mask, boolop);
        return new MaskDataNode(data);

    }

    public LeafNode visitMask(MaskDataNode left, BinaryOperand op) {
        BinaryOperation bop = Operations.lookup(op);

        if (!(bop instanceof BooleanOperation)) {
            throw new SemanticError("illegal operation : " + op);
        }

        BooleanOperation boolop = (BooleanOperation) bop;


        BooleanMaskNode3D data = new BooleanMaskNode3D(left.getData(), mask, boolop);
        return new MaskDataNode(data);
    }

    public ValueNode visitUnary(UnaryOperand op) {
        switch (op) {
            case ADD:
                throw new SemanticError("error: invalid operation " + op + " for binary operand");
            case NEGATE:
                throw new SemanticError("error: invalid operation " + op + " for binary operand");
            default:
                throw new RuntimeException("default switch case ...");
        }

    }

    public int depth() {
        return 0;
    }

    public String toString() {

        return mask.toString();
    }
}
