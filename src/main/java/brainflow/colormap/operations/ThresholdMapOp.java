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

public class ThresholdMapOp implements ColorMapTransformOp {
    int threshold;


    public ThresholdMapOp(int _threshold) {
        threshold = _threshold;

    }

    public void setThreshold(int _threshold) {
        threshold = _threshold;
    }

    public IndexColorModel transformMap(IndexColorModel icm) {
        if (!icm.hasAlpha())
            throw new IllegalArgumentException("Must have alpha channel in order to threshold!");

        byte[][] table = ColorMapUtils.extractTable(icm);
        //byte[][] copy = new table[table.length];

        for (int i = 0; i < table[3].length; i++) {
            if (i < threshold)
                table[3][i] = 0;
            else
                table[3][i] = (byte) 255;
        }

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


}