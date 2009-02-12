package brainflow.colormap.operations;

import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;

/**
 * Title:        Parvenu
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class AlphaThresholdOp extends BaseColorMapOp {

  int min = 255;
  int max = 0;
  int constant = 255;

  public AlphaThresholdOp(int _min, int _max) {
    min = _min;
    max = _max;

  }

  public void setThreshold(int _min, int _max) {
    min = _min;
    max = _max;
  }

  public int[] getThreshold() {
    return new int[] { min, max };
  }

  public void setAlphaConstant(int _constant) {
    if (_constant > 255)
      constant = 255;
    else
      constant = _constant;
  }


  public IndexColorModel transformMap(IndexColorModel icm) {
    //if (!icm.hasAlpha())
    //  throw new IllegalArgumentException("Must have alpha channel in order to threshold!");

    byte[][] table = ColorMapUtils.extractTable(icm);
    byte[][] copy = copyBytes(table);

    for (int i=0; i<table[3].length; i++) {
      if (i > max)
        copy[3][i] = (byte)(constant);
      else if (i < min)
        copy[3][i] = (byte)(constant);
      else
        copy[3][i] = 0;
    }

    icm = new IndexColorModel(8, icm.getMapSize(), copy[0], copy[1], copy[2], copy[3]);

    return icm;
  }



  public RenderedImage transformImage(RenderedImage img) {
    /**@todo: Implement this lcbr.gui.operations.operations.ColorMapTransformOp method*/
    throw new java.lang.UnsupportedOperationException("Method transformImage() not yet implemented.");
  }


}