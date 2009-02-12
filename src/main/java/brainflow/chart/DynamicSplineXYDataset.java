package brainflow.chart;

import cern.colt.list.DoubleArrayList;
import brainflow.math.BSpline;
import brainflow.math.CatmullRomSpline;
import brainflow.utils.Pair;

import java.util.ArrayList;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class DynamicSplineXYDataset extends DynamicXYDataset {

    private ArrayList splinePairs = new ArrayList();

    private int numSamples;
    private DynamicXYDataset dataset;


    public DynamicSplineXYDataset(DynamicXYDataset _dataset, int _numSamples) {
        numSamples = _numSamples;
        dataset = _dataset;
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            updateSpline(i);
        }
    }

    public DoubleArrayList getSeries(int series, int seriesType) {
        return dataset.getSeries(series, seriesType);
    }

    public DoubleArrayList getSpline(int series, int seriesType) {
        if (series >= splinePairs.size())
            throw new IllegalArgumentException("DynamicSplineXYDataset: no spline " + series + " found!");

        Pair p = (Pair) splinePairs.get(series);
        if (seriesType == DOMAIN) {
            return (DoubleArrayList) p.left();
        } else if (seriesType == RANGE) {
            return (DoubleArrayList) p.right();
        }

        return null;
    }

    public int onLine(double x, double y) {
        double distance = Float.MAX_VALUE;

        int series = 0;

        for (int i = 0; i < splinePairs.size(); i++) {
            Pair p = (Pair) splinePairs.get(i);
            DoubleArrayList xlist = (DoubleArrayList) p.left();
            DoubleArrayList ylist = (DoubleArrayList) p.right();
            for (int j = 0; j < xlist.size(); j++) {
                double d = Math.pow(x - xlist.get(j), 2) + Math.pow(y - ylist.get(j), 2);
                if (d < distance) {
                    series = i;
                    distance = d;

                }
            }
        }


        if (distance < 30)
            return series;
        return -1;
    }


    public int getNearestSpline(double x, double y) {
        double distance = Float.MAX_VALUE;

        int series = 0;
        double minx = 0, miny = 0;
        for (int i = 0; i < splinePairs.size(); i++) {
            Pair p = (Pair) splinePairs.get(i);
            DoubleArrayList xlist = (DoubleArrayList) p.left();
            DoubleArrayList ylist = (DoubleArrayList) p.right();
            for (int j = 0; j < xlist.size(); j++) {
                double d = Math.pow(x - xlist.get(j), 2) + Math.pow(y - ylist.get(j), 2);
                if (d < distance) {
                    series = i;
                    distance = d;
                    minx = xlist.get(j);
                    miny = ylist.get(j);

                }
            }
        }
        return series;
    }

    public void addXYSeries(double[] xvals, double[] yvals) {
        dataset.addXYSeries(xvals, yvals);
        updateSpline(splinePairs.size());
        fireDatasetChanged();
    }

    public void setXYSeries(int series, double[] xvals, double[] yvals) {
        dataset.setXYSeries(series, xvals, yvals);
        updateSpline(series);
        fireDatasetChanged();
    }


    public int getItemCount(int seriesNum) {
        DoubleArrayList dlist = getSpline(seriesNum, DOMAIN);
        return dlist.size();
    }

    public double getXValue(int series, int item) {
        return getSpline(series, DOMAIN).getQuick(item);

    }

    public double getYValue(int series, int item) {
        return getSpline(series, RANGE).getQuick(item);
    }

    public double[][] getYSplineData() {
        double[][] data = new double[getSeriesCount()][];
        for (int i = 0; i < splinePairs.size(); i++) {
            Pair p = (Pair) splinePairs.get(i);
            DoubleArrayList ylist = (DoubleArrayList) p.right();
            ylist.trimToSize();
            data[i] = ylist.elements();
        }

        return data;
    }

    public void setXValue(int series, int item, double value) {
        
        dataset.setXValue(series, item, value);
        updateSpline(series);
        fireDatasetChanged();

    }


    public void setYValue(int series, int item, double value) {
        dataset.setYValue(series, item, value);
        updateSpline(series);
        fireDatasetChanged();
    }

    public void setYBaseLine(int series, double base, double max) {
        dataset.setYBaseLine(series, base, max);
        updateSpline(series);
        fireDatasetChanged();
    }

    public void multiplyYConstant(int series, double constant, double min, double max) {
        dataset.multiplyYConstant(series, constant, min, max);
        updateSpline(series);
        fireDatasetChanged();
    }

    public void addYConstant(int series, double constant, double min, double max) {
        dataset.addYConstant(series, constant, min, max);
        updateSpline(series);
        fireDatasetChanged();
    }

    public void addXYValue(int series, double xvalue, double yvalue) {
        dataset.addXYValue(series, xvalue, yvalue);
        updateSpline(series);
        fireDatasetChanged();
    }

    private void updateSpline(int series) {
        DoubleArrayList xlist = getSeries(series, DOMAIN);
        DoubleArrayList ylist = getSeries(series, RANGE);

        BSpline bspline = new CatmullRomSpline(xlist, ylist);
        DoubleArrayList[] splines = bspline.evaluateSpline(numSamples);

        if (series == splinePairs.size())
            splinePairs.add(new Pair(splines[0], splines[1]));
        else
            splinePairs.set(series, new Pair(splines[0], splines[1]));
    }

    public void printSplineData() {
        for (int i = 0; i < this.getSeriesCount(); i++) {
            DoubleArrayList xlist = getSpline(i, DOMAIN);
            DoubleArrayList ylist = getSpline(i, RANGE);

        }
    }

    public int getSeriesCount() {
        /**@todo: Override this com.brainflow.chart.DynamicXYDataset method*/
        return dataset.getSeriesCount();
    }

    public void setSeriesNames(String[] parm1) {
        /**@todo: Override this com.brainflow.chart.DynamicXYDataset method*/
        dataset.setSeriesNames(parm1);
    }

    public String getSeriesName(int series) {
        return dataset.getSeriesName(series);
    }


}