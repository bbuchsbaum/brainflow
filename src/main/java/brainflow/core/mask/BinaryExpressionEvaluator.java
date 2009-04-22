package brainflow.core.mask;

import brainflow.core.ImageViewModel;

import brainflow.image.data.IMaskedData3D;
import brainflow.image.data.IImageData;

import java.text.ParseException;

import org.codehaus.jparsec.error.ParserException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 14, 2009
 * Time: 1:09:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryExpressionEvaluator {

    //todo throw proper exception
    public static IMaskedData3D eval(String expression, ImageViewModel model) {
        BinaryExpressionParser parser = createParser(model);

        INode node = parser.createParser().parse(expression);
        RootNode root = new RootNode(node);
        VariableSubstitution varsub = new VariableSubstitution(model);
        RootNode vnode = varsub.start(root);

        MaskEvaluator masksub = new MaskEvaluator();
        RootNode res = masksub.start(vnode);

        MaskDataNode maskNode = (MaskDataNode) res.getChild();
        return maskNode.getData();
        

    }


     private static BinaryExpressionParser createParser(final ImageViewModel model) {
        BinaryExpressionParser parser = new BinaryExpressionParser(new Context<IImageData>() {
            public IImageData getValue(String symbol) {
                int index = mapIndex(symbol);

                if (index < 0 || (index > model.size() - 1)) {
                    throw new IllegalArgumentException("illegal layer index " + index);
                }

                return model.get(index).getData();

            }


            private int mapIndex(String varName) {
                if (!varName.toUpperCase().startsWith("V")) {
                    throw new IllegalArgumentException("illegal variable name : " + varName);
                }

                String numberPart = varName.substring(1);
                return Integer.parseInt(numberPart) - 1;


            }
        });

        return parser;


    }
}
