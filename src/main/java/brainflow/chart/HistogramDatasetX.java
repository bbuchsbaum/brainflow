package brainflow.chart;

import org.jfree.data.*;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.xy.IntervalXYDataset;

import brainflow.image.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class HistogramDatasetX extends AbstractDataset implements IntervalXYDataset {

    
    Histogram histogram;
    
    double startx;
    double endx;
    
    double[] bins;
    double[] binIntervals;
    
    public HistogramDatasetX(Histogram _histogram) {
        histogram = _histogram;
       // bins = histogram.computeBins();
      //  binIntervals = histogram.getBinIntervals(histogram.getBinSize());
    }

    protected double[] getBins() {
        return bins;
    }
    
    public double getStartXValue(int series, int item) {
        return binIntervals[item];
    }
    
    public Number getStartX(int series, int item) {
        return binIntervals[item];
    }
    
    public double getEndXValue(int series, int item) {
        return binIntervals[item + 1];
    }
    
    public Number getEndX(int series, int item) {
        return binIntervals[item+1];
    }
    
    public double getStartYValue(int series, int item) {
        return 0;
    }
    
    public Number getStartY(int series, int item) {
        return 0;
    }
    
    public double getEndYValue(int series, int item) {
        return bins[item];
        
    }
    
    public Number getEndY(int series, int item) {
        return bins[item];
    }
    
    public int getItemCount(int series) {
        return bins.length;
    }
    
    public double getXValue(int series, int item) {
        return (binIntervals[item] + binIntervals[item + 1]) / 2;
    }
    
    public double getYValue(int series, int item) {
        return bins[item];
    }
    
    public Number getX(int series, int item) {
        return (binIntervals[item] + binIntervals[item + 1]) / 2;
    }
    
    public Number getY(int series, int item) {
        return bins[item];
    }
    
    public brainflow.utils.Range getBinInterval(int item) {
        return new brainflow.utils.Range(binIntervals[item], binIntervals[item + 1]);
    }
    
    public brainflow.utils.Range getXRange() {
        return new brainflow.utils.Range(binIntervals[0], binIntervals[binIntervals.length - 1]);
    }
    
    public brainflow.utils.Range getYRange() {
        return new brainflow.utils.Range(0, bins[histogram.getHighestBin()]);
    }
    
    public int getSeriesCount() {
        return 1;
    }
    
    public String getSeriesName(int series) {
        
        return "histogram";
    }
    
    public void addChangeListener(DatasetChangeListener listener) {
        /**@todo: Implement this com.jrefinery.data.Dataset method*/
        //throw new java.lang.UnsupportedOperationException("Method addChangeListener() not yet implemented.");
    }
    
    public void removeChangeListener(DatasetChangeListener listener) {
        /**@todo: Implement this com.jrefinery.data.Dataset method*/
        //throw new java.lang.UnsupportedOperationException("Method removeChangeListener() not yet implemented.");
    }
    
    public DomainOrder getDomainOrder() {
        return DomainOrder.NONE;
    }
    
    public Comparable getSeriesKey(int series) {
        return series;
    }
    
    public int indexOf(Comparable seriesKey) {
        return 0;
        
    }
}
