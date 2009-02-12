package brainflow.colormap.operations;
import java.awt.image.*;
import java.awt.Point;

import java.awt.image.RenderedImage;
import java.awt.*;

/**
 * Title:        Parvenu
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class ColorMapUtils {

  public ColorMapUtils() {
  }


  public static RenderedImage createSingleBandedImage(byte[] data, int width, int height) {

    WritableRaster raster = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                        width, height, 1, new Point(0,0));
    raster.setDataElements(0,0, width, height, data);

    BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    bimg.setData(raster);

    return bimg;
  }


  public static RenderedImage insertColorMap(RenderedImage img, IndexColorModel icm) {
    assert false;
    return null;


  }



  /*public static RenderedImage createTiledImage(byte[] data, int width, int height, IndexColorModel icm) {
    WritableRaster raster = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                        width, height, 1, new Point(0,0));
    raster.setDataElements(0,0, width, height, data);

    TiledImage timg = new TiledImage(raster.getMinX(), raster.getMinY(), raster.getWidth(), raster.getHeight(), 0,0,
                                     raster.getSampleModel(), icm);

    timg.setData(raster);

    ImageLayout tileLayout = new ImageLayout(timg);
    tileLayout.setTileHeight(timg.getHeight());
    tileLayout.setTileWidth(timg.getWidth());
    RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, tileLayout);
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(timg);
    return JAI.create("format", pb, hints);


  }  */



  /*public static RenderedImage paletteToRGB(RenderedImage src, boolean hasAlpha) {
    if (hasAlpha)
      return ColorMapUtils.paletteToRGB(src);

    RenderedImage dst = null;
    if(src.getColorModel() instanceof IndexColorModel) {
      IndexColorModel icm = (IndexColorModel)src.getColorModel();
      byte[][] data = new byte[3][icm.getMapSize()];
      icm.getReds(data[0]);
      icm.getGreens(data[1]);
      icm.getBlues(data[2]);



      LookupTableJAI lut = new LookupTableJAI(data);

      dst = JAI.create("lookup", src, lut);
    }
    else {
      dst = src;
    }

    return dst;
  } */


  /*public static RenderedImage paletteToRGB(RenderedImage src) {
    RenderedImage dst = null;

    if(src.getColorModel() instanceof IndexColorModel) {
      IndexColorModel icm = (IndexColorModel)src.getColorModel();
      byte[][] data = new byte[icm.getNumComponents()][icm.getMapSize()];
      icm.getReds(data[0]);
      icm.getGreens(data[1]);
      icm.getBlues(data[2]);

      if (icm.hasAlpha())
        icm.getAlphas(data[3]);



      LookupTableJAI lut = new LookupTableJAI(data);

      dst = JAI.create("lookup", src, lut);
    }
    else {
      dst = src;
    }

    return dst;
  }   */


  public static RenderedImage createImage(byte[] data, int width, int height, IndexColorModel icm) {

    RenderedImage dst = null;


    BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_INDEXED,icm);

    WritableRaster wr = WritableRaster.createBandedRaster(DataBuffer.TYPE_BYTE,
                        width, height, 1, new Point(0,0));
    wr.setDataElements(0,0, width, height, data);

    bi.setData(wr);
    dst = bi;

    return dst;

  }

  public static RenderedImage createImage(byte[] data, int width, int height, IndexColorModel icm, boolean hasAlpha) {
    if (hasAlpha) {
      return ColorMapUtils.createImage(data, width, height, icm);
    }

    //icm = org.lcbr.gui.GeneralLuts.removeAlphaBand(icm);
    RenderedImage dst = null;

    BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_INDEXED,icm);
    WritableRaster wr = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                        width, height, 1, new Point(0,0));
    wr.setDataElements(0,0, width, height, data);
    bi.setData(wr);
    dst = bi;
    return dst;
  }

  public static BufferedImage createCompatibleImage(int width, int height) {
    GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice screen = local.getDefaultScreenDevice();
    GraphicsConfiguration config = screen.getDefaultConfiguration();
    return config.createCompatibleImage(width, height);
  }
   

  public static byte[][] extractTable(IndexColorModel icm) {
    byte[][] table = new byte[4][icm.getMapSize()];

    icm.getReds(table[0]);
    icm.getGreens(table[1]);
    icm.getBlues(table[2]);
    icm.getAlphas(table[3]);

    return table;
  }


}