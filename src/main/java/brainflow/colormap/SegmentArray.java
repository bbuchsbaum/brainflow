package brainflow.colormap;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 4, 2007
 * Time: 12:19:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class SegmentArray {

    private double[] boundaries;

    private List<Interval> intervals;

    public SegmentArray(double[] _boundaries) {
        if (!isMonotonic(_boundaries)) {
            throw new IllegalArgumentException("Boundary array must be sorted");
        }

        boundaries = _boundaries;
        
        int numIntervals = boundaries.length-1;
        intervals = new ArrayList<Interval>(numIntervals);

        for (int i=1; i<numIntervals+1; i++) {
            IndexedInterval ival = new IndexedInterval(i, boundaries);
            intervals.add(ival);          
        }
    }
    public SegmentArray(int _numIntervals) {

        intervals = new ArrayList<Interval>(_numIntervals);
        boundaries = new double[_numIntervals + 1];

        for (int i = 1; i < _numIntervals+1; i++) {
            IndexedInterval ival = new IndexedInterval(i, boundaries);
            intervals.add(ival);
        }

    }


    public SegmentArray(int _numIntervals, double _min, double _max) {
        double min = _min;
        double max = _max;

        intervals = new ArrayList<Interval>(_numIntervals);
        boundaries = new double[_numIntervals + 1];

        boundaries[0] = min;

        double cur = min;
        double bucketSize = (max - min) / (_numIntervals);

        for (int i = 1; i < (_numIntervals - 2); i++) {
            boundaries[i] = cur;
            cur = cur + bucketSize;
            IndexedInterval ival = new IndexedInterval(i, boundaries);
            intervals.add(ival);
        }

        boundaries[_numIntervals] = max;
    }

    public SegmentArray(int _numIntervals, double lowClip, double highClip, double _min, double _max) {
        double min = _min;
        double max = _max;

        intervals = new ArrayList<Interval>(_numIntervals);
        boundaries = new double[_numIntervals + 1];

        boundaries[0] = min;
        boundaries[1] = lowClip;

        //low clip?
        double bucketSize = (highClip - lowClip) / (_numIntervals-3);
        double cur = lowClip + bucketSize;

        for (int i = 2; i < (_numIntervals-1); i++) {
            boundaries[i] = cur;
            cur = cur + bucketSize;
            IndexedInterval ival = new IndexedInterval(i, boundaries);
            intervals.add(ival);
        }

        boundaries[_numIntervals -1] = highClip;
        boundaries[_numIntervals] = max;
    }

    public double[] getBoundaries() {
        return boundaries.clone();
    }

    public Interval getInterval(int index) {
        return intervals.get(index);
    }

    public int getNumIntervals() {
        return intervals.size();
    }

    public double getMaximum() {
        return intervals.get(intervals.size()-1).getMaximum();
    }

    public double getMinimum() {
        return intervals.get(0).getMinimum();
    }

    

    //public double getBoundary(int segment) {
    //    return boundaries[segment];
    //}

    public double getUpperBound(int interval) {
        IndexedInterval idx = (IndexedInterval) getInterval(interval);
        return idx.getMaximum();
    }

    public double getLowerBound(int interval) {
        IndexedInterval idx = (IndexedInterval) getInterval(interval);
        return idx.getMinimum();
    }

    public void setLowerBound(int interval, double value) {
        IndexedInterval idx = (IndexedInterval) getInterval(interval);
        boundaries[idx.getIndex() - 1] = value;

    }

    public int indexOf(double value) {
        if (value <= boundaries[0]) {
            return 0;
        }
        if (value >= boundaries[boundaries.length - 1]) {
            return getNumIntervals() - 1;
        }

        int bottom = 0;
        int top = boundaries.length - 1;

        int mid;

        while (top != bottom) {
            mid = (int) ((top + bottom) / 2.0);

            if (value >= boundaries[mid] && value < boundaries[mid + 1]) {
                return mid;
            } else if (value > boundaries[mid]) {
                bottom = mid;
            } else {
                top = mid;
            }
        }

        return -1;
    }

    public void setUpperBound(int interval, double value) {

        IndexedInterval idx = (IndexedInterval) getInterval(interval);
        boundaries[idx.getIndex()] = value;

    }

    public void equalizeIntervals(int startIndex, int endIndex) {

        if (startIndex < 0 || endIndex >= getNumIntervals()) {
            throw new IllegalArgumentException("index range is outside map range: (" + startIndex +
                    ", " + endIndex + "),  map size = " + getNumIntervals());
        } else if ((endIndex - startIndex) < 1) {
            throw new IllegalArgumentException("index range must be greater than 1!");
        }


        int range = endIndex - startIndex + 1;

        Interval begin = getInterval(startIndex);
        Interval end = getInterval(endIndex);

        double bucketSize = (end.getMaximum() - begin.getMinimum()) / range;
        double start = begin.getMinimum();

        if (endIndex == (getNumIntervals()-1)) {
            endIndex--;
        }

        for (int i = startIndex; i <= endIndex; i++) {
            boundaries[i + 1] = start + bucketSize;
            start = start + bucketSize;
        }



    }

    public boolean isMonotonic(double[] boundaries) {
        double prev = boundaries[0];
        for (int i = 1; i < boundaries.length; i++) {
            if (boundaries[i] < prev) {
                return false;
            }
            prev = boundaries[i];
        }

        return true;
    }


}
