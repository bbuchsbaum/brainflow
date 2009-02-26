package brainflow.core.mask;

import brainflow.core.IImageDisplayModel;
import brainflow.image.data.IImageData;
import brainflow.image.data.ImageData;



/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 6:14:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariableSubstitution extends AnalysisAdapter {

    private IImageDisplayModel model;


   
    public VariableSubstitution(IImageDisplayModel model) {
        this.model = model;
    }


    private int mapIndex(String varName) {
        System.out.println("varname " + varName);
        if (!varName.toUpperCase().startsWith("V")) {
            throw new IllegalArgumentException("illegal variable name : " + varName);
        }

        String numberPart = varName.substring(1);
        System.out.println("number part " + numberPart);

        int index = Integer.parseInt(numberPart) -1 ;

        System.out.println("index " + index);
        return index;

    }

    

    public void caseConstantNode(ConstantNode node) {
        double val =  node.evaluate().doubleValue();
        node.replaceBy(new ImageDataNode(""+val, ImageData.createConstantData(val, model.getSelectedLayer().getData().getImageSpace())));
    }

    public void caseVariableNode(VariableNode node) {

        int index = mapIndex(node.getVarName());

        if (index < 0 || (index > model.getNumLayers() - 1) ) {
            throw new IllegalArgumentException("illegal layer index " + index);
        }

        IImageData data = model.getLayer(index).getData();
        node.replaceBy(new ImageDataNode(node.getSymbol(), data));
    }




}
