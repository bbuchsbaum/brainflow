package brainflow.colormap.operations;

import brainflow.utils.NumberUtils;

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

public class ClampMapOp implements ColorMapTransformOp {

    int max = 255;
    int min = 0;

    public ClampMapOp(int _min, int _max) {
        if ((min < 0) || (min > 254))
            min = 0;
        else
            min = _min;

        if ((max > 255) || (max <= min))
            max = 255;
        else
            max = _max;

    }

    public IndexColorModel transformMap(IndexColorModel icm) {
        byte[][] table = ColorMapUtils.extractTable(icm);
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                int cur = NumberUtils.ubyte(table[i][j]);
                if (cur > max)
                    table[i][j] = (byte) max;
                else if (cur < min)
                    table[i][j] = (byte) min;
            }
        }

        IndexColorModel retIcm = new IndexColorModel(8, icm.getMapSize(), table[0], table[1], table[2], table[3]);

        return retIcm;
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