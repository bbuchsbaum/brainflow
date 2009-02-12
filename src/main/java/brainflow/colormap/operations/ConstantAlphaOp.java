package brainflow.colormap.operations;

import java.awt.image.*;
/**
 * Title:        Parvenu
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class ConstantAlphaOp extends BaseColorMapOp {

  int constant = 255;

  public ConstantAlphaOp(int _constant) {
    constant = _constant;
  }

  public void setConstant(int _constant) {
    constant = _constant;
  }

  public int getConstant() {
    return constant;
  }

  public IndexColorModel transformMap(IndexColorModel icm) {

    byte[][] table = ColorMapUtils.extractTable(icm);
    byte[][] copy = copyBytes(table);

    for (int i=0; i<copy[3].length; i++) {
        //if ( NumberUtils.ubyte(copy[3][i]) > 0 ) {
          copy[3][i] = (byte)constant;
        //}

    }

    icm = new IndexColorModel(8, icm.getMapSize(), copy[0], copy[1], copy[2], copy[3]);

    return icm;
  }

}