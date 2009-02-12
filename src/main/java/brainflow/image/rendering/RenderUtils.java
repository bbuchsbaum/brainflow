package brainflow.image.rendering;

import brainflow.math.ArrayUtils;
import brainflow.utils.NumberUtils;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class RenderUtils {

    public RenderUtils() {
    }

    /*public static BufferedImage createRGBAImage(byte[] rgba, int width, int height) {
        SampleModel sm = RasterFactory.createPixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 4);

        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        //TiledImage tiledImage = new TiledImage(0, 0, width, height, 0, 0, sm, cm);
        DataBuffer buffer = new DataBufferByte(rgba, width * height);

        Raster raster = RasterFactory.createWritableRaster(sm, buffer, new Point(0, 0));

        // set the TiledImage data to that of the Raster
        //tiledImage.setData(raster);
        bimg.setData(raster);

        //GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //GraphicsDevice gd = ge.getDefaultScreenDevice();
        //GraphicsConfiguration gf = gd.getDefaultConfiguration();

        //BufferedImage destImage = gf.createCompatibleImage((int) bimg.getWidth(), (int) bimg.getHeight());
        //Graphics2D g2 = destImage.createGraphics();
        //g2.drawRenderedImage(bimg, AffineTransform.getTranslateInstance(0, 0));

        return bimg;
        //RenderedImageAdapter img = new RenderedImageAdapter((RenderedImage) tiledImage);


    } */

    /*public byte[] extractData(byte[][] rgba) {
      byte data[] = new byte[rgba.length*4];

      for(int i=0;i<rgba.length;i++){
          int pixel = pixmap[i];
          byte a = (byte)((pixel >> 24) & 0xff);
          byte r  = (byte)((pixel >> 16) & 0xff);
          byte g = (byte)((pixel >>  8) & 0xff);
          byte b = (byte)((pixel      ) & 0xff);

          if(numbands == 4){
            data[i*numbands+0] = r;
            data[i*numbands+1] = g;
            data[i*numbands+2]= b;
            data[i*numbands+3] = a;
          } else {
            data[i*numbands+0] = r;
            data[i*numbands+1] = g;
            data[i*numbands+2]= b;
          }
      }
      return data;
   } */


    public static BufferedImage createInterleavedBufferedImage(byte[][] rgba, int width, int height, boolean isPremultiplied) {


        int[] pixels = new int[width * height];
        for (int i = 0; i < pixels.length; i++) {

            pixels[i] = (NumberUtils.ubyte(rgba[3][i]) << 24) | (NumberUtils.ubyte(rgba[0][i]) << 16) | (NumberUtils.ubyte(rgba[1][i]) << 8) | NumberUtils.ubyte(rgba[2][i]);


        }

        DataBuffer buffer = new DataBufferInt(pixels, width * height);

        //DirectColorModel dcm = new DirectColorModel(ColorSpace.get(ColorSpace.CS_sRGB),
        //        32, 0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000, false, DataBuffer.TYPE_INT);


        SinglePixelPackedSampleModel sm = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, width, height,
                new int[]{0xff0000, 0xff00, 0xff, 0xff000000});

        WritableRaster raster = Raster.createWritableRaster(sm, buffer, new Point(0, 0));

        BufferedImage bimg = null;
        if (isPremultiplied) {
            bimg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        } else {
            bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        bimg.setData(raster);

        return bimg;

    }


    public static BufferedImage createBufferedImage(byte[][] rgba, int width, int height) {
        ComponentColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB),

                true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

        DataBufferByte buffer = new DataBufferByte(rgba, width * height);
        BandedSampleModel sm = new BandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 4);

        WritableRaster raster = Raster.createWritableRaster(sm, buffer, new Point(0, 0));
        BufferedImage bimg = new BufferedImage(cm, raster, false, null);

        return bimg;
    }

    public static void main(String[] args) {

        GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = local.getDefaultScreenDevice();
        GraphicsConfiguration config = screen.getDefaultConfiguration();
        BufferedImage bimg = config.createCompatibleImage(256, 256, Transparency.TRANSLUCENT);
        System.out.println(bimg.getColorModel());
        System.out.println(bimg.getSampleModel());
        System.out.println("pixel size " + bimg.getColorModel().getPixelSize());
        System.out.println(bimg.getColorModel().getTransferType());
        byte a = 127;
        byte b = 127;
        byte c = 127;
        byte d = 127;

        int all = (a << 24) | (b << 16) | (c << 8) | (d);
        System.out.println("all = " + Integer.toHexString(all));

        System.out.println(Integer.toHexString(all & 0xff000000));
        System.out.println(Integer.toHexString(all & 0x00ff0000));
        System.out.println(Integer.toHexString(all & 0x0000ff00));
        System.out.println(Integer.toHexString(all & 0x000000ff));


    }

    /* public static RenderedImage createRGBAImage(byte[][] rgba, int width, int height) {


      SampleModel sm = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE,
              width,
              height,
              4);


      ColorModel cm = PlanarImage.createColorModel(sm);

      // create a TiledImage using the float SampleModel
      TiledImage tiledImage = new TiledImage(0, 0, width, height, 0, 0, sm, cm);
      DataBuffer buffer = new DataBufferByte(rgba, width * height);

      Raster raster = RasterFactory.createWritableRaster(sm, buffer, new Point(0, 0));

      // set the TiledImage data to that of the Raster
      tiledImage.setData(raster);

      RenderedImageAdapter img = new RenderedImageAdapter((RenderedImage) tiledImage);

      return img;
  }  */

    public static RenderedImage createSingleBandedImage(byte[] data, int width, int height) {

        WritableRaster raster = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                width, height, 1, new Point(0, 0));
        raster.setDataElements(0, 0, width, height, data);
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        bimg.setData(raster);


        return bimg;
    }


    public static IndexColorModel createIndexColorModel(double[][] table) {
        int numbands = table.length;

        byte[] red = ArrayUtils.castToBytes(table[0]);
        byte[] green = ArrayUtils.castToBytes(table[1]);
        byte[] blue = ArrayUtils.castToBytes(table[2]);

        byte[] alpha = null;

        if (table.length < 4) {
            alpha = new byte[256];
            java.util.Arrays.fill(alpha, (byte) 255);
        } else {
            alpha = ArrayUtils.castToBytes(table[3]);
        }


        return new IndexColorModel(8, 256, red, green, blue, alpha);
    }

    /*public static RenderedImage createTiledImage(byte[] data, int width, int height, IndexColorModel icm) {
        WritableRaster raster = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                width, height, 1, new Point(0, 0));
        raster.setDataElements(0, 0, width, height, data);

        TiledImage timg = new TiledImage(raster.getMinX(), raster.getMinY(), raster.getWidth(), raster.getHeight(), 0, 0,
                raster.getSampleModel(), icm);

        timg.setData(raster);

        ImageLayout tileLayout = new ImageLayout(timg);
        tileLayout.setTileHeight(timg.getHeight());
        tileLayout.setTileWidth(timg.getWidth());
        RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, tileLayout);
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(timg);
        return JAI.create("format", pb, hints);


    }*/

    /*public static RenderedImage paletteToRGB(RenderedImage src, boolean hasAlpha) {
       if (hasAlpha)
           return RenderUtils.paletteToRGB(src);

       RenderedImage dst = null;
       if (src.getColorModel() instanceof IndexColorModel) {
           IndexColorModel icm = (IndexColorModel) src.getColorModel();
           byte[][] data = new byte[3][icm.getMapSize()];
           //System.out.println("num components = " + icm.getNumComponents());
           icm.getReds(data[0]);
           icm.getGreens(data[1]);
           icm.getBlues(data[2]);


           LookupTableJAI lut = new LookupTableJAI(data);

           dst = JAI.create("lookup", src, lut);
       } else {
           dst = src;
       }

       return dst;
   } */

    /*public static RenderedImage paletteToRGB(RenderedImage src) {
      RenderedImage dst = null;

      if (src.getColorModel() instanceof IndexColorModel) {
          IndexColorModel icm = (IndexColorModel) src.getColorModel();
          byte[][] data = new byte[icm.getNumComponents()][icm.getMapSize()];
          //System.out.println("num components = " + icm.getNumComponents());
          icm.getReds(data[0]);
          icm.getGreens(data[1]);
          icm.getBlues(data[2]);

          if (icm.hasAlpha())
              icm.getAlphas(data[3]);


          LookupTableJAI lut = new LookupTableJAI(data);

          dst = JAI.create("lookup", src, lut);
      } else {
          dst = src;
      }

      return dst;
  }  */


    public static RenderedImage createImage(byte[] data, int width, int height, IndexColorModel icm) {

        RenderedImage dst = null;


        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, icm);

        WritableRaster wr = WritableRaster.createBandedRaster(DataBuffer.TYPE_BYTE,
                width, height, 1, new Point(0, 0));
        wr.setDataElements(0, 0, width, height, data);

        bi.setData(wr);
        dst = bi;

        return dst;

    }

    public static RenderedImage createImage(byte[] data, int width, int height, IndexColorModel icm, boolean hasAlpha) {
        if (hasAlpha) {
            return RenderUtils.createImage(data, width, height, icm);
        }

        RenderedImage dst = null;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, icm);
        WritableRaster wr = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                width, height, 1, new Point(0, 0));
        wr.setDataElements(0, 0, width, height, data);
        bi.setData(wr);
        dst = bi;
        return dst;
    }

    public static BufferedImage createCompatibleImage(int width, int height) {

        GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = local.getDefaultScreenDevice();
        GraphicsConfiguration config = screen.getDefaultConfiguration();
        return config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
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