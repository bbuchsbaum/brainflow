package brainflow.math;

import cern.colt.list.DoubleArrayList;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class BSpline {

    private DoubleArrayList ypoints;

    private DoubleArrayList xpoints;

    private DoubleArrayList extendedXpoints;

    private DoubleArrayList extendedYpoints;

    private int[] sampleCount;

    public BSpline(DoubleArrayList _xpoints, DoubleArrayList _ypoints) {
        xpoints = _xpoints;
        ypoints = _ypoints;
        extendedXpoints = (DoubleArrayList) xpoints.clone();
        extendedYpoints = (DoubleArrayList) ypoints.clone();

        extendedXpoints.beforeInsert(0, xpoints.get(0));
        extendedXpoints.beforeInsert(0, xpoints.get(0));
        extendedYpoints.beforeInsert(0, ypoints.get(0));
        extendedYpoints.beforeInsert(0, ypoints.get(0));
        extendedXpoints.add(xpoints.get(xpoints.size() - 1));
        extendedYpoints.add(ypoints.get(ypoints.size() - 1));
    }

    public DoubleArrayList[] evaluateSpline(int numSamples) {

        int splineSamples = numSamples - 2;
        double t = xpoints.get(0);
        double end = xpoints.get(xpoints.size() - 1);
        double step = (end - t) / (splineSamples - 1);

        DoubleArrayList xout = new DoubleArrayList();
        DoubleArrayList yout = new DoubleArrayList();

        sampleCount = new int[xpoints.size()];

        for (int i = 0; i < splineSamples; i++, t += step) {
            int index = nearestIndex(xpoints, t);
            sampleCount[index]++;
        }

        t = xpoints.get(0);
        for (int i = 0; i < sampleCount.length; i++)
            for (int j = 1; j <= sampleCount[i]; j++) {
                double[] pt = p(i + 2, (double) j / sampleCount[i]);
                xout.add(pt[0]);
                yout.add(pt[1]);
            }

        xout.beforeInsert(0, xpoints.get(0));
        xout.add(xpoints.get(xpoints.size() - 1));

        yout.beforeInsert(0, ypoints.get(0));
        yout.add(ypoints.get(ypoints.size() - 1));


        return new DoubleArrayList[]{xout, yout};


    }


    private int nearestIndex(DoubleArrayList list, double value) {
        double minDistance = Math.abs(list.get(0) - value);
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            double distance = Math.abs(list.get(i) - value);
            if (distance < minDistance) {
                index = i;
                minDistance = distance;
            }
        }

        return index;
    }

    public double b(int i, double t) {
        switch (i) {
            case -2:
                return (((-t + 3) * t - 3) * t + 1) / 6;
            case -1:
                return (((3 * t - 6) * t) * t + 4) / 6;
            case 0:
                return (((-3 * t + 3) * t + 3) * t + 1) / 6;
            case 1:
                return (t * t * t) / 6;
        }
        return 0; //we only get here if an invalid i is specified
    }

    public double[] p(int i, double t) {
        float px = 0;
        float py = 0;
        double[] pt = new double[2];
        for (int j = -2; j <= 1; j++) {
            px += b(j, t) * extendedXpoints.getQuick(i + j);
            py += b(j, t) * extendedYpoints.getQuick(i + j);
        }
        pt[0] = px;
        pt[1] = py;
        return pt;
    }


}