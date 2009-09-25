package brainflow.colormap;

import brainflow.image.data.IImageData;
import brainflow.image.iterators.ValueIterator;
import brainflow.utils.NumberUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 1, 2007
 * Time: 12:20:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscreteColorMap extends AbstractColorMap {

    private static final Logger log = Logger.getLogger(DiscreteColorMap.class.getName());


    private final SegmentArray segments;


    private final List<ColorInterval> intervals;

    // need to abstract notion of boundary and interval.

    /*public DiscreteColorMap(List<Color> clrs, Double... boundaries) {
     if (clrs.size() != (boundaries.length-1)) {
         throw new IllegalArgumentException("must provide zero less color than number of color boundaries");
     }

     boundaryArray = new double[boundaries.length];
     for (int i=0; i<boundaries.length; i++) {
         boundaryArray[i] = boundaries[i];
     }

     for (int i=1; i<boundaries.length; i++) {
         ColorInterval ival = new ColorInterval(new IndexedInterval(i, boundaryArray ), clrs.get(i-1));
         intervals.add(ival);

     }

 }   */


    private DiscreteColorMap(SegmentArray _segments, List<ColorInterval> _intervals) {
        segments = _segments;
        int numIntervals = segments.getNumIntervals();

        int isize = _intervals.size() - 1;
        intervals = new ArrayList<ColorInterval>(segments.getNumIntervals());
        for (int i = 0; i < numIntervals; i++) {
            double perc = (double) i / (double) (numIntervals - 1);
            int index = (int) (perc * isize);
            ColorInterval ival = new ColorInterval(segments.getInterval(i), _intervals.get(index).getColor());
            intervals.add(ival);
        }

        equalizeIntervals(0, getMapSize() - 1);
    }

    public DiscreteColorMap(List<Color> clrs, List<Double> boundaries) {
        if (clrs.size() < 1) {
            throw new IllegalArgumentException("Supplied Color List must have length >= 1");
        }

        intervals = new ArrayList<ColorInterval>(boundaries.size() - 1);
        segments = new SegmentArray(boundaries.size()-1);
        segments.setLowerBound(0, boundaries.get(0));

        for (int i = 0; i < (boundaries.size() - 1); i++) {
            segments.setUpperBound(i, boundaries.get(i + 1));
            Color clr = clrs.get(i % clrs.size());
            intervals.add(new ColorInterval(segments.getInterval(i), clr));
        }


    }

    public DiscreteColorMap(List<Color> clrs, double[] boundaries) {
        if (clrs.size() < 1) {
            throw new IllegalArgumentException("Supplied Color List must have length >= 1");
        }

        intervals = new ArrayList<ColorInterval>(boundaries.length - 1);
        segments = new SegmentArray(boundaries.length-1);
        segments.setLowerBound(0, boundaries[0]);

        for (int i = 0; i < (boundaries.length - 1); i++) {
            segments.setUpperBound(i, boundaries[i + 1]);
            Color clr = clrs.get(i % clrs.size());
            intervals.add(new ColorInterval(segments.getInterval(i), clr));
        }


    }


    public DiscreteColorMap(IColorMap cmap) {
        segments = new SegmentArray(cmap.getMapSize());
        segments.setLowerBound(0, cmap.getMinimumValue());

        ListIterator<ColorInterval> iter = cmap.iterator();

        intervals = new ArrayList<ColorInterval>(cmap.getMapSize());


        while (iter.hasNext()) {
            ColorInterval ci = iter.next();
            int i = iter.previousIndex();
            segments.setUpperBound(i, ci.getMaximum());
            intervals.add(new ColorInterval(segments.getInterval(i), ci.getColor()));
        }
    }




    public double getMaximumValue() {
        return segments.getUpperBound(segments.getNumIntervals() - 1);
    }

    public double getMinimumValue() {
        return segments.getLowerBound(0);
    }

    public int getMapSize() {
        return intervals.size();
    }

    public List<Color> getColors() {
        List<Color> clr = new ArrayList<Color>(intervals.size() + 1);
        for (int i = 0; i < clr.size(); i++) {
            clr.add(intervals.get(i).getColor());
        }

        return clr;
    }

    public ColorInterval getInterval(int index) {
        return intervals.get(index);
    }

    public Color getColor(double value) {
        int idx = segments.indexOf(value);
        return intervals.get(idx).getColor();
    }

    public void setColor(int index, Color clr) {
        intervals.get(index).setColor(clr);
        //changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);

    }

    public void equalizeIntervals(int startIndex, int endIndex) {
        segments.equalizeIntervals(startIndex, endIndex);
        //changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);

    }

    public DiscreteColorMap shrink(int shrinkBy) {
        if (shrinkBy < 1 || shrinkBy > (getMapSize() - 1)) {
            throw new IllegalArgumentException("Cannot shrink more intervals than map contains");
        }

        int nsize = segments.getNumIntervals() + 1 - shrinkBy;

        double[] nbounds = new double[nsize];
        double[] oldbounds = segments.getBoundaries();

        System.arraycopy(oldbounds, 0, nbounds, 0, nsize - 1);
        nbounds[nsize - 1] = oldbounds[oldbounds.length - 1];

        SegmentArray nsegments = new SegmentArray(nbounds);

        return new DiscreteColorMap(nsegments, intervals);


    }

    public DiscreteColorMap grow(int growBy) {
        if (growBy < 1 || (growBy + getMapSize()) > IColorMap.MAXIMUM_INTERVALS) {
            throw new IllegalArgumentException("Cannot grow mapo by " + growBy + " intervals.");
        }

        int nsize = segments.getNumIntervals() + 1 + growBy;

        double[] nbounds = new double[nsize];
        double[] oldbounds = segments.getBoundaries();
        System.arraycopy(oldbounds, 0, nbounds, 0, oldbounds.length);

        for (int i = getMapSize(); i < nsize; i++) {
            nbounds[i] = oldbounds[oldbounds.length - 1];
        }

        SegmentArray nsegments = new SegmentArray(nbounds);

        return new DiscreteColorMap(nsegments, intervals);
    }


    public void replaceInterval(int index, double min, double max, Color clr) {
        ColorInterval ival = getInterval(index);
        min = Math.max(min, getMinimumValue());
        max = Math.min(max, getMaximumValue());
        if (index == 0) {
            setUpperBound(index, max);
            //setBoundary(index + 1, max);
        } else if (index == getMapSize() - 1) {
            setUpperBound(index - 1, min);
        } else {
            if (!NumberUtils.equals(ival.getMinimum(), min, .0001)) {
                setUpperBound(index - 1, min);
            }
            if (!NumberUtils.equals(ival.getMaximum(), max, .0001)) {
                setUpperBound(index, max);
            }
        }

        ival.setColor(clr);
        //changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);
    }

    private void setLowerBound(int index, double value) {
        if (index < 1) {
            throw new IllegalArgumentException("Cannot change the low boundary for the first bin (e.g. the absolute minimum)");
        }
        if (index > (segments.getNumIntervals() - 1)) {
            throw new IllegalArgumentException("index " + index + " is greater than number of intervals " + segments.getNumIntervals());
        }

        setUpperBound(index - 1, value);
    }


    private void setUpperBound(int index, double value) {
        double oldValue = segments.getUpperBound(index);
        double maxValue = segments.getMaximum();
        double minValue = segments.getMinimum();


        if (value > oldValue) {
            double totalSpan = maxValue - oldValue;
            double difference = value - oldValue;

            int begin = index + 1;
            int end = segments.getNumIntervals() - 1;

            segments.setUpperBound(index, value);

            double prev = oldValue;
            for (int i = begin; i < end; i++) {
                double oldInterval = segments.getUpperBound(i) - prev;
                double perc = oldInterval / totalSpan;
                double localfudge = perc * difference;
                prev = segments.getUpperBound(i);

                segments.setUpperBound(i,
                        Math.min(segments.getLowerBound(i) + oldInterval - localfudge, maxValue));
            }
        } else if (value < oldValue) {
            double difference = oldValue - value;
            double totalSpan = oldValue - minValue;

            double prev = oldValue;
            segments.setUpperBound(index, value);

            for (int i = index - 1; i >= 0; i--) {
                double oldInterval = prev - segments.getLowerBound(i + 1);
                double perc = oldInterval / totalSpan;
                double localfudge = perc * difference;
                prev = segments.getUpperBound(i);

                segments.setUpperBound(i,
                        Math.max(segments.getUpperBound(i + 1) - oldInterval + localfudge, minValue));

            }

        }


    }

    /*private void setBoundary(int boundary, double value) {
        //assert boundary > 0 && boundary < (boundaryArray.length - 1);
        //assert value >= boundaryArray[0] && value <= boundaryArray[boundaryArray.length - 1];


        double oldValue = boundaryArray[boundary];
        double maxValue = boundaryArray[boundaryArray.length - 1];
        double minValue = boundaryArray[0];

        if (oldArray == null) {
            oldArray = new double[boundaryArray.length];
        }

        System.arraycopy(boundaryArray, 0, oldArray, 0, boundaryArray.length);

        if (value > oldValue) {
            double totalSpan = maxValue - oldValue;
            double difference = value - oldValue;

            int begin = boundary + 1;
            int end = boundaryArray.length - 1;

            boundaryArray[boundary] = value;
            for (int i = begin; i < end; i++) {
                double oldInterval = oldArray[i] - oldArray[i - 1];
                double perc = oldInterval / totalSpan;
                double localfudge = perc * difference;

                boundaryArray[i] = Math.min(boundaryArray[i - 1] + oldInterval - localfudge, maxValue);

            }
        } else if (value < oldValue) {
            double difference = oldValue - value;
            double totalSpan = oldValue - minValue;


            boundaryArray[boundary] = value;

            for (int i = boundary - 1; i > 0; i--) {
                double oldInterval = oldArray[i + 1] - oldArray[i];
                double perc = oldInterval / totalSpan;
                double localfudge = perc * difference;
                boundaryArray[i] = Math.max(boundaryArray[i + 1] - oldInterval + localfudge, minValue);

            }

        }

        assert isMonotonic() : "not monotonic";


    }   */


    public void setHighClip(double _highClip) {

        ColorInterval ival = intervals.get(intervals.size() - 1);

        if (_highClip > ival.getMaximum()) {
            _highClip = ival.getMaximum();
        }

        if (Double.compare(_highClip, ival.getMinimum()) == 0) return;

        double oldHighClip = getHighClip();


        setUpperBound(intervals.size() - 2, _highClip);


        //changeSupport.firePropertyChange(HIGH_CLIP_PROPERTY, oldHighClip, getHighClip());

    }

    public void setLowClip(double _lowClip) {
        ColorInterval ival = intervals.get(0);
        if (_lowClip < ival.getMinimum()) {
            _lowClip = ival.getMinimum();

        }

        if (Double.compare(_lowClip, ival.getMaximum()) == 0) return;
        double oldLowClip = getLowClip();


        setUpperBound(0, _lowClip);

        //changeSupport.firePropertyChange(LOW_CLIP_PROPERTY, oldLowClip, getLowClip());
    }

    public DiscreteColorMap newClipRange(double lowClip, double highClip, double min, double max) {
        //todo figure out what to do with max
        return new DiscreteColorMap(new SegmentArray(segments.getNumIntervals(), lowClip, highClip,
                segments.getMinimum(), segments.getMaximum()), intervals);

    }

    public double getHighClip() {
        return segments.getLowerBound(intervals.size() - 1);
    }


    public double getLowClip() {
        return segments.getUpperBound(0);
    }

    public ListIterator<ColorInterval> iterator() {
        return intervals.listIterator();
    }

    public AbstractColorBar createColorBar() {
        return new DiscreteColorBar(this, SwingUtilities.HORIZONTAL);
    }

    public byte[] getInterleavedRGBAComponents(IImageData data) {

        int len = data.length();
        byte[] rgba = new byte[len * 4];

        double minValue = getMinimumValue();
        double maxValue = getMaximumValue();

        ColorInterval firstInterval = getInterval(0);
        ColorInterval lastInterval = getInterval(getMapSize() - 1);

        ValueIterator iter = data.iterator();
        while (iter.hasNext()) {
            int i = iter.index();
            double val = iter.next();
            int offset = i * 4;
            if (val <= minValue) {
                rgba[offset] = (byte) firstInterval.getAlpha();
                rgba[offset + 1] = (byte) firstInterval.getBlue();
                rgba[offset + 2] = (byte) firstInterval.getGreen();
                rgba[offset + 3] = (byte) firstInterval.getRed();
            } else if (val >= maxValue) {
                rgba[offset] = (byte) lastInterval.getAlpha();
                rgba[offset + 1] = (byte) lastInterval.getBlue();
                rgba[offset + 2] = (byte) lastInterval.getGreen();
                rgba[offset + 3] = (byte) lastInterval.getRed();
            } else {
                Color clr = getColor(val);
                rgba[offset] = (byte) clr.getAlpha();
                rgba[offset + 1] = (byte) clr.getBlue();
                rgba[offset + 2] = (byte) clr.getGreen();
                rgba[offset + 3] = (byte) clr.getRed();
            }
        }

        return rgba;
    }


    public byte[][] getRGBAComponents(IImageData data) {
        return new byte[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }


    public static void main(String[] args) {
        LinearColorMap2 cmap = new LinearColorMap2(0, 255, ColorTable.SPECTRUM);
        DiscreteColorMap tmp = new DiscreteColorMap(cmap);
        tmp.setUpperBound(34, 125);


    }
}
