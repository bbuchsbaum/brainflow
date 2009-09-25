package brainflow.colormap;

import brainflow.image.data.IImageData2D;
import brainflow.image.data.RGBAImage;
import brainflow.image.data.UByteImageData2D;
import brainflow.image.iterators.ValueIterator;

import java.awt.*;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 16, 2006
 * Time: 3:35:51 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractColorMap implements IColorMap {



    private double minimumValue;

    private double maximumValue;

    protected double lowClip;

    protected double highClip;


    public abstract int getMapSize();

    public abstract ColorInterval getInterval(int index);

    public abstract Color getColor(double value);


    public double getMaximumValue() {
        return maximumValue;
    }

    public double getMinimumValue() {
        return minimumValue;
    }

    protected void setMaximumValue(double _max) {
        maximumValue = _max;
    }

    protected void setMinimumValue(double _min) {
        minimumValue = _min;
    }


    protected double[] filterHighValue(double low, double high) {
        if (high > getMaximumValue()) {
            high = getMaximumValue();
        } else if (high < getMinimumValue()) {
            high = getMinimumValue();
        }

        if (high < low) {
            low = high;
        }

        return new double[]{low, high};


    }

    protected double[] filterLowValue(double low, double high) {
        if (low < getMinimumValue()) {
            low = getMinimumValue();
        } else if (low > getMaximumValue()) {
            low = getMaximumValue();
        }

        if (low > high) {
            high = low;
        }

        return new double[]{low, high};
    }


    public double getHighClip() {
        return highClip;
    }


    public double getLowClip() {
        return lowClip;
    }


    public abstract ListIterator<ColorInterval> iterator();

    public abstract AbstractColorBar createColorBar();


    public RGBAImage getRGBAImage(IImageData2D data) {


        int len = data.length();

        byte[][] rgba = new byte[4][len];
        int lastidx = getMapSize() - 1;

        ValueIterator iter = data.iterator();

        Color c0 = getInterval(0).getColor();
        Color cn = getInterval(lastidx).getColor();

        assert getLowClip() <= getHighClip();


        while (iter.hasNext()) {

            int i = iter.index();
            double val = iter.next();

          
            if (val < getLowClip()) {
                rgba[0][i] = (byte) c0.getRed();
                rgba[1][i] = (byte) c0.getGreen();
                rgba[2][i] = (byte) c0.getBlue();
                rgba[3][i] = (byte) c0.getAlpha();
            } else if (val > getHighClip()) {
                rgba[0][i] = (byte) cn.getRed();
                rgba[1][i] = (byte) cn.getGreen();
                rgba[2][i] = (byte) cn.getBlue();
                rgba[3][i] = (byte) cn.getAlpha();
            } else {
                Color ci = getColor(val);
                rgba[0][i] = (byte) ci.getRed();
                rgba[1][i] = (byte) ci.getGreen();
                rgba[2][i] = (byte) ci.getBlue();
                rgba[3][i] = (byte) ci.getAlpha();

            }

        }

        RGBAImage rgbaimage = new RGBAImage(data,
                new UByteImageData2D(data.getImageSpace(), rgba[0]),
                new UByteImageData2D(data.getImageSpace(), rgba[1]),
                new UByteImageData2D(data.getImageSpace(), rgba[2]),
                new UByteImageData2D(data.getImageSpace(), rgba[3]));

        return rgbaimage;


    }


   
}
