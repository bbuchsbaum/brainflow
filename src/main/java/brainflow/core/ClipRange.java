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



    /*public final Property<Double> lowClip = new ObservableProperty<Double>(0.0) {
        public void set(Double aDouble) {

            if (aDouble > highClip.get()) {

                highClip.set(aDouble);
            }

            super.set(aDouble);


        }

        


    };

    public final Property<Double> highClip = new ObservableProperty<Double>(0.0) {
        public void set(Double aDouble) {
        
            if (aDouble < lowClip.get().doubleValue()) {

                lowClip.set(get());
            }

            super.set(aDouble);
        }
    };*/



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
        if (lowClip <= val && highClip >= val) {
            return true;
        }

        return false;
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
