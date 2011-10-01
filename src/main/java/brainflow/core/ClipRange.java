package brainflow.core;


import brainflow.utils.IRange;
import brainflow.utils.Range;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 14, 2007
 * Time: 9:34:24 PM
 * To change this template use File | Settings | File Templates.
 */


public class ClipRange implements IClipRange {

    private final double minValue;

    private final double maxValue;

    private final double lowClip;

    private final double highClip;

    private final double clipInterval;




    public ClipRange(double min, double max, double lowClip, double highClip) {
        //BeanContainer.bind(this);

        //if (min > lowClip) throw new IllegalArgumentException("min cannot exceed lowClip");
        //if (max < highClip) throw new IllegalArgumentException("max cannot be less than highClip");
        if (lowClip > highClip) throw new IllegalArgumentException("lowClip cannot exceed highClip");
        if (min > max) throw new IllegalArgumentException("min cannot exceed max");



        this.maxValue = max;
        this.minValue = min;

        this.highClip = Math.min(highClip, maxValue);
        this.lowClip = Math.max(lowClip, min);
        this.clipInterval = highClip - lowClip;
    }


    public double getHighClip() {
        return highClip;
    }

    //public void setClipRange(double low, double high) {
    //    lowClip.set(low);
    //    highClip.set(high);
    //}

    //public void setHighClip(double highClip) {
    //    this.highClip.set(highClip);
    //}

    public double getLowClip() {
        return lowClip;
    }

    //public void setLowClip(double lowClip) {
    //   this.lowClip.set(lowClip);
    //}

    public double getInterval() {
        return highClip - lowClip;
    }

    public IRange getInnerRange() {
        return new Range(lowClip, highClip);
    }

    public double getMin() {
        return minValue;

    }

    public double getMax() {
        return maxValue;
                
    }

    public IClipRange newClipRange(double min, double max, double lowclip, double highclip) {
        //todo check validity
         return new ClipRange(min, max, lowclip, highclip);
    }



    public boolean contains(double val) {
        if (val > highClip || val < lowClip) {
            return false;
        } else {
            return true;
        }
       

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClipRange clipRange = (ClipRange) o;

        if (Double.compare(clipRange.highClip, highClip) != 0) return false;
        if (Double.compare(clipRange.lowClip, lowClip) != 0) return false;
        if (Double.compare(clipRange.maxValue, maxValue) != 0) return false;
        if (Double.compare(clipRange.minValue, minValue) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = minValue != +0.0d ? Double.doubleToLongBits(minValue) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = maxValue != +0.0d ? Double.doubleToLongBits(maxValue) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = lowClip != +0.0d ? Double.doubleToLongBits(lowClip) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = highClip != +0.0d ? Double.doubleToLongBits(highClip) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ClipRange{" +
                "minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", lowClip=" + lowClip +
                ", highClip=" + highClip +
                '}';
    }
}
