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

public class SelectRangeOp extends BaseColorMapOp  {
  int low = 0;
  int high = 255;

  public SelectRangeOp(int _low, int _high) {
    low = _low;
    high = _high;
  }

  public void setRange(int _low, int _high) {
    low = _low;
    high = _high;
  }

  public int[] getRange() {
    return new int[] { low, high };
  }

  public IndexColorModel transformMap(IndexColorModel icm) {
    int range = high-low;
    if (range <= 0)
      throw new IllegalArgumentException("SelectRangeOp: high bound must be greater than low bound!");

    byte[][] table = ColorMapUtils.extractTable(icm);
    byte[][] ntable = copyBytes(table);




    int mapsize = icm.getMapSize();


    for (int i=0; i<3; i++) {
      float idx = 1;
      for (int j=0; j<table[i].length; j++) {

        if (j < low)
          ntable[i][j] = table[i][0];
        else if (j > high)
          ntable[i][j] = table[i][table[i].length-1];
        else {
          float value = (idx/(float)range) * (float)mapsize;

          int rnd = (int)value;

          if (rnd >= mapsize)
            rnd = mapsize-1;
          ntable[i][j] = table[i][rnd];
          idx++;
        }

      }
    }

    icm = new IndexColorModel(8, icm.getMapSize(), ntable[0], ntable[1], ntable[2], ntable[3]);
    return icm;
  }




}