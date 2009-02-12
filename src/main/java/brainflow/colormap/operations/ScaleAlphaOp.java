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

public class ScaleAlphaOp implements ColorMapTransformOp {


    public static int LINEAR = 0;
    public static int SQUARE_ROOT = 1;
    public static int SQUARED = 2;

    int type;

    public ScaleAlphaOp(int scaleType) {
        if (scaleType == LINEAR)
            type = LINEAR;
        else if (scaleType == SQUARE_ROOT)
            type = SQUARE_ROOT;
        else if (scaleType == SQUARED)
            type = SQUARED;
        else
            type = LINEAR;


    }

    public IndexColorModel transformMap(IndexColorModel icm) {
        if (!icm.hasAlpha())
            throw new IllegalArgumentException("Must have alpha channel in order to scale alpha!");

        byte[][] table = ColorMapUtils.extractTable(icm);
        table[3] = linearScale(table[3]);

        icm = new IndexColorModel(8, icm.getMapSize(), table[0], table[1], table[2], table[3]);
        return icm;
    }

    public RenderedImage transformImage(RenderedImage img) {

        if (!(img.getColorModel() instanceof IndexColorModel))
            throw new IllegalArgumentException("Cannot transform this operations: doesn'three have IndexColorModel");

        IndexColorModel icm = (IndexColorModel) img.getColorModel();
        icm = transformMap(icm);
        RenderedImage retImg = ColorMapUtils.insertColorMap(img, icm);

        return retImg;
    }


    public byte[] linearScale(byte[] alpha) {
        for (int i = 0; i < alpha.length; i++) {
            float f = (float) i / (alpha.length - 1);
            if (type == SQUARE_ROOT)
                f = (float) Math.sqrt(f);
            else if (type == SQUARED)
                f = (float) Math.pow(f, 2);

            alpha[i] = (byte) (f * 255f);

            //alpha[i] = (byte)255;
        }

        return alpha;
    }


}