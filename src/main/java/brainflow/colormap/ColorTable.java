package brainflow.colormap;

import cern.colt.list.DoubleArrayList;
import brainflow.app.BrainFlowException;
import brainflow.math.ArrayUtils;
import brainflow.math.BSpline;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ColorTable {

    public static final IndexColorModel SPECTRUM = getSpectrum(255);

    public static final IndexColorModel GRAYSCALE = getGrayScale(0, 255, true);
    
    public static final IndexColorModel GRAYSCALE_NO_ALPHA = getGrayScale(0, 255, false);

    public static final Map<String, IndexColorModel> COLOR_MAPS = new HashMap<String, IndexColorModel>();

    static {
        COLOR_MAPS.put("Spectrum", SPECTRUM);
        COLOR_MAPS.put("Gray Scale", GRAYSCALE);
        COLOR_MAPS.put("Greens", ColorBrewer.Greens);
        COLOR_MAPS.put("Reds", ColorBrewer.Reds);
        

    }



    public ColorTable() {
    }

    public static byte[][] extractTable(IndexColorModel icm) {
        byte[][] table = new byte[4][icm.getMapSize()];

        icm.getReds(table[0]);
        icm.getGreens(table[1]);
        icm.getBlues(table[2]);
        icm.getAlphas(table[3]);

        return table;
    }

    public static byte[][] extractTable(LinearColorMap2 lcm) {
        byte[][] table = new byte[4][lcm.getMapSize()];

        for (int i = 0; i < lcm.getMapSize(); i++) {
            table[0][i] = (byte) lcm.getInterval(i).getRed();
            table[1][i] = (byte) lcm.getInterval(i).getGreen();
            table[2][i] = (byte) lcm.getInterval(i).getBlue();
            table[3][i] = (byte) lcm.getInterval(i).getAlpha();
        }

        return table;
    }


    public static ImageIcon createImageIcon(IndexColorModel icm, int swatchWidth, int swatchHeight) {
        LinearColorMap2 cmap = new LinearColorMap2(0, 255, icm);
        LinearColorBar cbar = new LinearColorBar(cmap, SwingConstants.HORIZONTAL);

        cbar.setSize(swatchWidth, swatchHeight);
        BufferedImage bimg = new BufferedImage(swatchWidth,
                swatchHeight, BufferedImage.TYPE_3BYTE_BGR);

        cbar.setColorMap(cmap);
        cbar.paintComponent(bimg.createGraphics());

        ImageIcon icon = new ImageIcon(bimg);
        return icon;
    }

    public static ImageIcon createImageIcon(Color c, int swatchWidth, int swatchHeight) {
        BufferedImage bimg = new BufferedImage(swatchWidth,
                swatchHeight, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2 = bimg.createGraphics();
        g2.setPaint(c);
        g2.fillRect(0, 0, swatchWidth, swatchHeight);
        g2.dispose();

        ImageIcon icon = new ImageIcon(bimg);
        return icon;
    }

    public static IndexColorModel invert(IndexColorModel input) {
        byte[][] rgba = ColorTable.extractTable(input);
        int ncols = rgba[0].length;
        int nrows = rgba.length;

        byte[][] out = new byte[rgba.length][rgba[0].length];


        for (int i=0; i<nrows; i++) {
            for (int j=0; j<ncols; j++) {
                out[i][ncols-j-1] = rgba[i][j];
            }
        }

        return new IndexColorModel(8, ncols, out[0], out[1], out[2], out[3]);
    }




    public static IndexColorModel createHueIntensityRamp(Color clr) {

        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        byte[] alpha = new byte[256];


        float[] hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), null);


        float hue = hsb[0];
        float sat = hsb[1];
        float brightness = .25f;

        float brightnessIncrement = (float)(.75/256);


        for (int i = 0; i < 256; i++) {


            Color c = Color.getHSBColor(hue, sat, brightness);

            brightness = brightness + brightnessIncrement;

            reds[i] = (byte) c.getRed();
            greens[i] = (byte) c.getGreen();
            blues[i] = (byte) c.getBlue();
            alpha[i] = (byte) 255;
        }

        IndexColorModel icm = new IndexColorModel(8, reds.length, reds, greens, blues, alpha);
        return icm;


    }


    public static IndexColorModel createIndexColorModel(List<Color> clrs) {
        byte[] red = new byte[clrs.size()];
        byte[] green = new byte[clrs.size()];
        byte[] blue = new byte[clrs.size()];
        byte[] alpha= new byte[clrs.size()];

        int i=0;
        for (Color clr : clrs) {
            red[i] = (byte)clr.getRed();
            green[i] = (byte)clr.getGreen();
            blue[i] = (byte)clr.getBlue();
            alpha[i] = (byte)clr.getAlpha();
            i++;

        }

        IndexColorModel icm = new IndexColorModel(8, red.length, red, green, blue, alpha);
        return icm;

    }

    public static java.util.List<Color> createColorGradient(IndexColorModel model, int bins) {
        if (bins < 1) throw new IllegalArgumentException("bins must be greater than 0");

        Color[] ret = new Color[bins];
        ret[0] = new Color(model.getRed(0), model.getGreen(0), model.getBlue(0), model.getAlpha(0));

        double[] lookup = linearRamp(0, model.getMapSize()-1, bins);
        System.out.println("lookup : " + Arrays.toString(lookup));

        for (int i=0; i<lookup.length; i++) {
            int index = (int)lookup[i];
            ret[i] = new Color(model.getRed(index), model.getGreen(index), model.getBlue(index), model.getAlpha(index));
                
        }

        return Arrays.asList(ret);


    }

    public static java.util.List<Color> createColorGradient(Color c1, Color c2, int bins) {
        List<Color> glist = new ArrayList<Color>();
        int r1, r2;
        int g1, g2;
        int b1, b2;

        r1 = c1.getRed();
        g1 = c1.getGreen();
        b1 = c1.getBlue();
        r2 = c2.getRed();
        g2 = c2.getGreen();
        b2 = c2.getBlue();

        float rslope = (float) (r2 - r1) / (float) (bins);
        float gslope = (float) (g2 - g1) / (float) (bins);
        float bslope = (float) (b2 - b1) / (float) (bins);

        for (int i = 0; i < bins; i++) {
            if (i == 0) {
                glist.add(c1);
            } else if (i == (bins-1)) {
                glist.add(c2);
            } else {
                glist.add(new Color((int) (r1 + rslope * i), (int) (g1 + gslope * i), (int) (b1 + bslope * i)));
            }

        }

        return glist;


    }

    public static IndexColorModel concatenate(IndexColorModel[] icm) {
        int mapSize = 0;
        for (int i = 0; i < icm.length; i++) {
            mapSize += icm[i].getMapSize();
        }

        byte[] nreds = new byte[mapSize];
        byte[] ngreens = new byte[mapSize];
        byte[] nblues = new byte[mapSize];
        byte[] nalphas = new byte[mapSize];

        int pos = 0;
        for (int i = 0; i < icm.length; i++) {
            byte[] tmp = new byte[icm[i].getMapSize()];
            int csize = icm[i].getMapSize();
            icm[i].getReds(tmp);
            System.arraycopy(tmp, 0, nreds, pos, csize);

            icm[i].getGreens(tmp);
            System.arraycopy(tmp, 0, ngreens, pos, csize);

            icm[i].getBlues(tmp);
            System.arraycopy(tmp, 0, nblues, pos, csize);

            icm[i].getAlphas(tmp);
            System.arraycopy(tmp, 0, nalphas, pos, csize);

            pos += csize;
        }

        return new IndexColorModel(8, mapSize, nreds, ngreens, nblues, nalphas);
    }


    public static double[] linearRamp(double begin, double end, int numsteps) {
        //assert numsteps > 1;

        double[] ramp = new double[numsteps];

        double stepSize = (end - begin) / (numsteps - 1);
        ramp[0] = begin;

        for (int i = 1; i < ramp.length; i++) {
            ramp[i] = ramp[i - 1] + stepSize;
        }

        ramp[ramp.length - 1] = end;
        return ramp;
    }

    public static IndexColorModel getGrayScale(int min, int max, boolean alphaBand) {
        if (min < 0 || max > 255)
            throw new IllegalArgumentException("min & max must be between 0 and 255");

        int minScale = min;
        int maxScale = max;
        byte gray[] = new byte[256];
        byte grayPixels[] = new byte[256];
        byte alpha[] = new byte[256];
        for (int i = 0; i < 256; i++)
            alpha[i] = (byte) 255;
        for (int i = 0; i < 256; i++)
            grayPixels[i] = (byte) i;

        for (int i = 0; i < 256; i++)
            if (i <= minScale)
                gray[i] = grayPixels[0];
            else if (i >= maxScale)
                gray[i] = grayPixels[255];
            else {
                int idx = (int) (255.0 * (float) (i - minScale) /
                        (float) (maxScale - minScale));
                gray[i] = grayPixels[idx];
            }

        if (alphaBand) {

            IndexColorModel icm = new IndexColorModel(8, 256, gray, gray, gray, alpha);
            return icm;
        } else
            return new IndexColorModel(8, 256, gray, gray, gray);


    }


    public static IndexColorModel createConstantMap(Color c) {
        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        byte[] alpha = new byte[256];

        for (int i = 0; i < 256; i++) {
            reds[i] = (byte) c.getRed();
            greens[i] = (byte) c.getGreen();
            blues[i] = (byte) c.getBlue();
            alpha[i] = (byte) 255;
        }

        IndexColorModel icm = new IndexColorModel(8, reds.length, reds, greens, blues, alpha);
        return icm;

    }

    /*public static int[] resample(double[] values, int opSize) {
        double[] xpoints = new double[opSize];

        for (int i = 0; i < xpoints.length; i++) {
            xpoints[i] = i;
        }
        BSpline spline1 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(ArrayUtils.unsignedBytesToDoubles(reds)));

    } */
    public static IndexColorModel resampleMap(IndexColorModel icm, int newMapSize) {
        int mapSize = icm.getMapSize();
        byte[] reds = new byte[mapSize];
        byte[] greens = new byte[mapSize];
        byte[] blues = new byte[mapSize];
        byte[] alphas = new byte[mapSize];

        icm.getReds(reds);
        icm.getGreens(greens);
        icm.getBlues(blues);
        icm.getAlphas(alphas);

        double[] xpoints = new double[icm.getMapSize()];

        for (int i = 0; i < xpoints.length; i++) {
            xpoints[i] = i;
        }


        BSpline spline1 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(ArrayUtils.unsignedBytesToDoubles(reds)));
        BSpline spline2 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(ArrayUtils.unsignedBytesToDoubles(blues)));
        BSpline spline3 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(ArrayUtils.unsignedBytesToDoubles(greens)));
        BSpline spline4 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(ArrayUtils.unsignedBytesToDoubles(alphas)));


        DoubleArrayList[] nreds = spline1.evaluateSpline(newMapSize);
        DoubleArrayList[] nblues = spline2.evaluateSpline(newMapSize);
        DoubleArrayList[] ngreens = spline3.evaluateSpline(newMapSize);
        DoubleArrayList[] nalphas = spline4.evaluateSpline(newMapSize);

        nreds[1].trimToSize();
        ngreens[1].trimToSize();
        nblues[1].trimToSize();
        nalphas[1].trimToSize();

        double[] dreds = nreds[1].elements();
        double[] dgreens = ngreens[1].elements();
        double[] dblues = nblues[1].elements();
        double[] dalphas = nalphas[1].elements();

        IndexColorModel nmodel = new IndexColorModel(8, newMapSize, ArrayUtils.castToBytes(dreds), ArrayUtils.castToBytes(dgreens),
                ArrayUtils.castToBytes(dblues), ArrayUtils.castToBytes(dalphas));

        return nmodel;


    }


    public static IndexColorModel resampleMap(int mapSize, int[] reds, int[] greens, int[] blues, int[] alphas) {


        double[] xpoints = new double[mapSize];

        for (int i = 0; i < xpoints.length; i++) {
            xpoints[i] = i;
        }


        BSpline spline1 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(ArrayUtils.castToDoubles(reds)));
        BSpline spline2 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(ArrayUtils.castToDoubles(blues)));
        BSpline spline3 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(ArrayUtils.castToDoubles(greens)));
        BSpline spline4 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(ArrayUtils.castToDoubles(alphas)));


        DoubleArrayList[] nreds = spline1.evaluateSpline(mapSize);
        DoubleArrayList[] nblues = spline2.evaluateSpline(mapSize);
        DoubleArrayList[] ngreens = spline3.evaluateSpline(mapSize);
        DoubleArrayList[] nalphas = spline4.evaluateSpline(mapSize);

        nreds[1].trimToSize();
        ngreens[1].trimToSize();
        nblues[1].trimToSize();
        nalphas[1].trimToSize();

        double[] dreds = nreds[1].elements();
        double[] dgreens = ngreens[1].elements();
        double[] dblues = nblues[1].elements();
        double[] dalphas = nalphas[1].elements();

        IndexColorModel nmodel = new IndexColorModel(8, mapSize, ArrayUtils.castToBytes(dreds), ArrayUtils.castToBytes(dgreens),
                ArrayUtils.castToBytes(dblues), ArrayUtils.castToBytes(dalphas));

        return nmodel;


    }

    public static void main(String[] args) {

    }

    public static IndexColorModel createFromXML(String rgbaXML) throws BrainFlowException {

        IndexColorModel icm = null;

        try {
            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(new java.io.StringReader(rgbaXML));
            Element root = doc.getRootElement();
            Element info = root.getChild("MapInfo");
            int size = info.getAttribute("mapSize").getIntValue();

            // name never registered ...
            String name = info.getAttribute("mapName").getValue();
            // is name even necessary?

            byte[] reds = new byte[size];
            byte[] greens = new byte[size];
            byte[] blues = new byte[size];
            byte[] alphas = new byte[size];

            Element table = root.getChild("Table");
            java.util.List entries = table.getChildren("color");
            for (Iterator iter = entries.iterator(); iter.hasNext();) {
                Element e = (Element) iter.next();
                int index = e.getAttribute("index").getIntValue();
                int red = e.getAttribute("r").getIntValue();
                int green = e.getAttribute("g").getIntValue();
                int blue = e.getAttribute("b").getIntValue();
                int alpha = e.getAttribute("a").getIntValue();
                reds[index] = (byte) red;
                greens[index] = (byte) green;
                blues[index] = (byte) blue;
                alphas[index] = (byte) alpha;

            }


            icm = new IndexColorModel(8, size, reds, greens, blues, alphas);


        } catch (JDOMException e) {
            throw new BrainFlowException("failed to parse XML color map", e);
        } catch (IOException e) {
            throw new BrainFlowException("failed to parse XML color map", e);
        }


        return icm;
    }

    public static IndexColorModel createFromXMLInputStream(InputStream istream) throws BrainFlowException {
        assert istream != null : "ColorTable.createFromXMLInputStream passed a null InputStream";

        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
            while (true) {
                String line = reader.readLine();

                if (line == null) {
                    break;
                }
                sb.append(line);
            }

            return ColorTable.createFromXML(sb.toString());

        } catch (IOException e) {
            throw new BrainFlowException("Error loading color model from XML stream", e);
        }

    }


    public static IndexColorModel getSpectrum(int alph) {
        Color c;
        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        byte[] alpha = new byte[256];

        float step = 1 / 310f;
        float val = 0f;

        for (int i = 255; i >= 0; i--, val += step) {
            c = Color.getHSBColor(val, 1f, 1f);

            reds[i] = (byte) c.getRed();
            greens[i] = (byte) c.getGreen();
            blues[i] = (byte) c.getBlue();
            alpha[i] = (byte) alph;
        }


        IndexColorModel icm = new IndexColorModel(8, reds.length, reds, greens, blues, alpha);
        return icm;
    }

    public static IndexColorModel getRgb332(int alph) {
        Color c;

        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        byte[] alpha = new byte[256];

        for (int i = 0; i < 256; i++) {
            reds[i] = (byte) (i & 0xe0);
            greens[i] = (byte) ((i << 3) & 0xe0);
            blues[i] = (byte) ((i << 6) & 0xc0);
            alpha[i] = (byte) alph;
        }
        IndexColorModel icm = new IndexColorModel(8, reds.length, reds, greens, blues, alpha);
        return icm;
    }


}