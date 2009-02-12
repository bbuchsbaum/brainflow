package brainflow.colormap.operations;
import java.awt.image.IndexColorModel;

/**
 * Title:        LCBR Home Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class AlphaRampOp extends BaseColorMapOp {

  int startIndex=0;

  public AlphaRampOp() {
  }

  public void setStartIndex(int index) {
   startIndex=index;
   if (startIndex>255)
    startIndex=255;
  }

  public IndexColorModel transformMap(IndexColorModel icm) {
    if (!icm.hasAlpha())
      throw new IllegalArgumentException("Must have alpha channel in order to threshold!");

    byte[][] table = ColorMapUtils.extractTable(icm);
    byte[][] copy = copyBytes(table);

    byte[] ramp = new byte[icm.getMapSize()];

    for (int i=startIndex; i<ramp.length; i++) {
      ramp[i] = (byte)i;
    }

    icm = new IndexColorModel(8, icm.getMapSize(), copy[0], copy[1], copy[2], ramp);

    return icm;
  }

}