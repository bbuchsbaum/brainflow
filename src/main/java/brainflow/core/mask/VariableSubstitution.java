package brainflow.core.mask;

import brainflow.core.ImageViewModel;
import brainflow.image.data.IImageData;
import brainflow.image.data.Data;



/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 6:14:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariableSubstitution extends AnalysisAdapter {

    private ImageViewModel model;


   
    public VariableSubstitution(ImageViewModel model) {
        this.model = model;
    }


    private int mapIndex(String varName) {
        if (!varName.toUpperCase().startsWith("V")) {
            throw new IllegalArgumentException("illegal variable name : " + varName);
        }

        return Integer.parseInt(varName.substring(1)) -1 ;


    }

    

    public void caseConstantNode(ConstantNode node) {
        double val =  node.evaluate();
        node.replaceBy(new ImageDataNode("" + val, Data.createConstantData(val, model.getSelectedLayer().getData().getImageSpace())));
    }

    public void caseVariableNode(VariableNode node) {

        int index = mapIndex(node.getVarName());

        if (index < 0 || (index > model.size() - 1) ) {
            throw new IllegalArgumentException("illegal layer index " + index);
        }

        IImageData data = model.get(index).getData();
        node.replaceBy(new ImageDataNode(node.getSymbol(), data));
    }




}
