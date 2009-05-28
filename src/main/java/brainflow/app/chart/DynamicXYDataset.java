package brainflow.app.chart;


import cern.colt.list.*;
import cern.jet.stat.Descriptive;
import java.util.*;
import brainflow.utils.Pair;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class DynamicXYDataset extends AbstractDataset implements org.jfree.data.xy.XYDataset {
    
    
    public static final int DOMAIN = 0;

    public static final int RANGE = 1;
    
    private ArrayList<Pair> seriesPairs = new ArrayList<Pair>();

    private ArrayList<String> seriesNames = new ArrayList<String>();
    
    private ArrayList changeListeners = new ArrayList();
    
    
    public DynamicXYDataset() {
    }
    
    public DoubleArrayList getSeries(int series, int seriesType) {
        if (series >= seriesPairs.size())
            throw new IllegalArgumentException("DynamicXYDataset: no series " + series + " found!");
        
        //assert (seriesType == 0) || (seriesType == 1);
        
        Pair p = seriesPairs.get(series);
        if (seriesType == DOMAIN) {
            DoubleArrayList dlist = (DoubleArrayList) p.left();
            return dlist;
        } else if (seriesType == RANGE) {
            DoubleArrayList dlist = (DoubleArrayList) p.right();
            return dlist;
        }
        
        return null;
    }
    
    
    public int getItemCount(int seriesNum) {
        DoubleArrayList dlist = getSeries(seriesNum, DOMAIN);
        return dlist.size();
    }
    
    public void setXYSeries(int series, double[] xvals, double[] yvals) {
        //  assert (series > 0) && (series < getSeriesCount());
        
        seriesPairs.set(series, new Pair<DoubleArrayList, DoubleArrayList>(new DoubleArrayList(xvals), new DoubleArrayList(yvals)));
        fireDatasetChanged();
    }
    
    public void addXYSeries(double[] xvals, double[] yvals) {
        if (xvals.length != yvals.length)
            throw new IllegalArgumentException("DynamicXYDataset: xvals.length must equal yvals.length!");
        
        DoubleArrayList xlist = new DoubleArrayList(xvals);
        DoubleArrayList ylist = new DoubleArrayList(yvals);
        
        seriesPairs.add(new Pair(xlist, ylist));
    }
    
    public int indexOf(int series, double xvalue) {
        DoubleArrayList xlist = getSeries(series, DOMAIN);
        int index = xlist.indexOf(xvalue);
        return index;
    }

    public void setXValue(int series, int item, double value) {
        DoubleArrayList xlist = getSeries(series, DOMAIN);
        if (item >= 0 && item < xlist.size()) {
            xlist.set(item, value);
            fireDatasetChanged();
        }
             
    }
    
    public void setYValue(int series, int item, double value) {
        DoubleArrayList ylist = getSeries(series, RANGE);
        if (item >= 0 && item < ylist.size()) {
            ylist.set(item, value);
            fireDatasetChanged();
        }

    }
    
    
    public void addXYValue(int series, double xvalue, double yvalue) {
        
        DoubleArrayList xlist = getSeries(series, DOMAIN);
        xlist.add(xvalue);
        xlist.sort();
        int index = xlist.indexOf(xvalue);
        
        DoubleArrayList ylist = getSeries(series, RANGE);
        ylist.beforeInsert(index, yvalue);
        
        fireDatasetChanged();
    }
    
    public void setYBaseLine(int series, double base, double max) {
        double curMin = getMinYValue(series);
        double incr = base - curMin;
        
        DoubleArrayList ylist = getSeries(series, RANGE);
        for (int i = 0; i < ylist.size(); i++) {
            double val = ylist.get(i);
            val += incr;
            if (val > max)
                val = max;
            if (val < base)
                val = base;
            ylist.set(i, val);
        }
        fireDatasetChanged();
    }
    
    
    public void addYConstant(int series, double constant, double min, double max) {
        DoubleArrayList ylist = getSeries(series, RANGE);
        for (int i = 0; i < ylist.size(); i++) {
            double val = ylist.get(i);
            val += constant;
            if (val > max)
                val = max;
            if (val < min)
                val = min;
            ylist.set(i, val);
        }
        fireDatasetChanged();
    }
    
    public void multiplyYConstant(int series, double constant, double min, double max) {
        DoubleArrayList ylist = getSeries(series, RANGE);
        if (min > max)
            min = max;
        
        for (int i = 0; i < ylist.size(); i++) {
            double val = ylist.get(i);
            val *= constant;
            if (val > max)
                val = max;
            if (val < min)
                val = min;
            ylist.set(i, val);
        }
        fireDatasetChanged();
    }
    
    
    public double getMaxYValue(int series) {
        DoubleArrayList ylist = getSeries(series, RANGE);
        return Descriptive.max(ylist);
    }
    
    public double getMinYValue(int series) {
        DoubleArrayList ylist = getSeries(series, RANGE);
        return Descriptive.min(ylist);
    }
    
    public double getMaxXValue(int series) {
        DoubleArrayList xlist = getSeries(series, DOMAIN);
        return Descriptive.max(xlist);
    }
    
    public double getMinXValue(int series) {
        DoubleArrayList xlist = getSeries(series, DOMAIN);
        return Descriptive.min(xlist);
    }
    
    
    public double getXValue(int series, int item) {
        return getSeries(series, DOMAIN).getQuick(item);
    }
    
    public Number getX(int series, int item) {
        return getSeries(series, DOMAIN).getQuick(item);
    }
    
    public double getYValue(int series, int item) {
        return getSeries(series, RANGE).getQuick(item);
    }
    
    public Number getY(int series, int item) {
        return getSeries(series, RANGE).getQuick(item);
    }
    
    public int getSeriesCount() {
        return seriesPairs.size();
    }
    
    public String getSeriesName(int series) {
        return seriesNames.get(series).toString();
    }
    
    public void setSeriesNames(String[] names) {
        seriesNames.clear();
        for (int i = 0; i < names.length; i++) {
            seriesNames.add(names[i]);
        }
    }
    
    public int getNearestSeries(double x, double y) {
        double distance = Float.MAX_VALUE;
        
        int series = 0;
        for (int i = 0; i < seriesPairs.size(); i++) {
            Pair p = (Pair) seriesPairs.get(i);
            DoubleArrayList xlist = (DoubleArrayList) p.left();
            DoubleArrayList ylist = (DoubleArrayList) p.right();
            for (int j = 0; j < xlist.size(); j++) {
                double _x = xlist.getQuick(j);
                double _y = ylist.getQuick(j);
                double d = Math.abs(x - _x) * Math.abs(y - _y);
                if (d < distance) {
                    series = i;
                    distance = d;
                }
            }
            
        }
        
        return series;
    }


    public double horizontalDistance(int series, double x, double y, int item) {
        Pair p = (Pair) seriesPairs.get(series);
        DoubleArrayList xlist = (DoubleArrayList) p.left();

        double _x = xlist.getQuick(item);


        return Math.abs(x - _x);
    }
    
    
    public int nearestItem(int series, double x, double y) {
        Pair p = (Pair) seriesPairs.get(series);
        DoubleArrayList xlist = (DoubleArrayList) p.left();
        DoubleArrayList ylist = (DoubleArrayList) p.right();
        
        double minDistance = Double.MAX_VALUE;
        int item = -1;
        for (int j = 0; j < xlist.size(); j++) {
            double _x = xlist.getQuick(j);
            double _y = ylist.getQuick(j);
            double d = Math.sqrt(Math.pow(Math.abs(x - _x),2) + Math.pow(Math.abs(y - _y),2));
            if (d < minDistance) {
                minDistance = d;
                item = j;
            }
        }


        return item;
    }
    
    
    public int[] onDataPoint(double x, double y) {
        
        double distance = 8;
        int series = -1;
        int item = -1;
        for (int i = 0; i < seriesPairs.size(); i++) {
            Pair p = (Pair) seriesPairs.get(i);
            DoubleArrayList xlist = (DoubleArrayList) p.left();
            DoubleArrayList ylist = (DoubleArrayList) p.right();
            for (int j = 0; j < xlist.size(); j++) {
                double _x = xlist.getQuick(j);
                double _y = ylist.getQuick(j);
                
               double d = Math.pow(Math.abs(x - _x),2) + Math.pow(Math.abs(y - _y),2); 
                if (d < distance) {
                    series = i;
                    item = j;
                    distance = d;
                    
                }
            }
        }
        
        
        return new int[]{series, item};
    }
    
    
    protected void fireDatasetChanged() {
        for (int i = 0; i < changeListeners.size(); i++) {
            DatasetChangeListener listener = (DatasetChangeListener) changeListeners.get(i);
            listener.datasetChanged(new DatasetChangeEvent(this, this));
        }
    }
    
    public void addChangeListener(DatasetChangeListener parm1) {
        changeListeners.add(parm1);
    }
    
    public void removeChangeListener(DatasetChangeListener parm1) {
        changeListeners.remove(parm1);
    }
    
        /* (non-Javadoc)
         * @see org.jfree.data.xy.XYDataset#getDomainOrder()
         */
    public DomainOrder getDomainOrder() {
        return DomainOrder.ASCENDING;
    }
    
    public Comparable getSeriesKey(int series) {
        // series key is simply series number (?)
        return new Integer(series);       
    }
    
    public int indexOf(Comparable seriesKey) {
        int seriesCount = getSeriesCount();
        for (int s = 0; s < seriesCount; s++) {
            if (getSeriesKey(s).equals(seriesKey)) {
                return s;
            }
        }
        return -1;
    }
}