package brainflow.colormap.operations;

import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;

/**
 * Title:        Parvenu
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 *
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class BaseColorMapOp implements ColorMapTransformOp {


    public BaseColorMapOp() {
    }


    public IndexColorModel transformMap(IndexColorModel icm) {
        /**@todo: Implement this lcbr.gui.operations.operations.ColorMapTransformOp method*/
        throw new java.lang.UnsupportedOperationException("Method transformMap() not yet implemented.");
    }

    public byte[][] copyBytes(byte[][] table) {
        byte[][] copy = new byte[table.length][];
        for (int i = 0; i < table.length; i++) {
            copy[i] = new byte[table[i].length];
            System.arraycopy(table[i], 0, copy[i], 0, table[i].length);
        }
        return copy;
    }

    public RenderedImage transformImage(RenderedImage img) {

        if (!(img.getColorModel() instanceof IndexColorModel))
            throw new IllegalArgumentException("Cannot transform this operations: doesn'three have IndexColorModel");

        IndexColorModel icm = (IndexColorModel) img.getColorModel();
        icm = transformMap(icm);
        RenderedImage retImg = ColorMapUtils.insertColorMap(img, icm);

        return retImg;
    }

}