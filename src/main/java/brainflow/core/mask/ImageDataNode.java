package brainflow.core.mask;

import brainflow.image.data.*;
import brainflow.image.operations.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 7:06:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageDataNode extends AbstractNode implements ValueNode<IImageData>, LeafNode, LeafVisitable {

    private IImageData data;

    private String symbol;

    public ImageDataNode(String symbol, IImageData data) {
        this.data = data;
        this.symbol = symbol;
    }


    public String getSymbol() {
        return symbol;
    }

    public IImageData evaluate() {
        return data;
    }

    public boolean isNumber() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int depth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LeafNode accept(LeafNode leaf, BinaryOperand op) {
        return leaf.visitImage(this, op);
    }

    public LeafNode visitUnary(UnaryOperand op) {
        switch (op) {
            case ADD:
                return this;
            case NEGATE:
                return new ImageDataNode("-" + getSymbol(), ImageData.createScaledData((IImageData3D) data, -1));
            default:
                throw new SemanticError("unsupported operation " + op + " for operand : " + getSymbol());
        }


    }

    public LeafNode visitImage(ImageDataNode left, BinaryOperand op) {

        BivariateMaskNode3D bdata = new BivariateMaskNode3D((IImageData3D) left.evaluate(), (IImageData3D) evaluate(), Operations.lookup(op));
        return new MaskDataNode(bdata);
    }

    public LeafNode visitMask(MaskDataNode left, BinaryOperand op) {
        BinaryOperation bop = Operations.lookup(op);
        if (!(bop instanceof BooleanOperation)) {
            throw new SemanticError("illegal operation : " + op);
        }

        BooleanOperation boolop = (BooleanOperation) bop;

        IMaskedData3D mdat = new MaskedData3D((IImageData3D) evaluate(), new MaskPredicate() {
            public boolean mask(double value) {
                return value > 0;
            }
        });

        BooleanMaskNode3D data = new BooleanMaskNode3D(left.getData(), mdat, boolop);
        return new MaskDataNode(data);
    }

    public LeafNode visitConstant(ConstantNode left, BinaryOperand op) {
        IImageData3D cdat = ImageData.createConstantData(left.evaluate().doubleValue(), data.getImageSpace());
        BivariateMaskNode3D bdata = new BivariateMaskNode3D(cdat, (IImageData3D) evaluate(), Operations.lookup(op));
        return new MaskDataNode(bdata);

    }

    public void apply(TreeWalker walker) {
        walker.caseImageDataNode(this);
    }

    @Override
    public String toString() {
        return data.getImageLabel();
    }
}
