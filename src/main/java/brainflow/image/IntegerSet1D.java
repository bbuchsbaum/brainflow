package brainflow.image;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class IntegerSet1D extends ProbeSet {

    public IntegerSet1D() {
    }

    int xbegin;
    int xend;

    int xstepSize;


    int[] xsamples;

    public IntegerSet1D(IntegerSet1D set) {
        xsamples = (int[]) set.xsamples.clone();
        xbegin = set.xbegin;
        xend = set.xend;
        xstepSize = set.xstepSize;
    }

    public IntegerSet1D(int _xbegin, int _xend) {

        xbegin = _xbegin;
        xend = _xend;

        xsamples = new int[Math.abs(xend - xbegin) + 1];

        if (xend < xbegin)
            xstepSize = -1;
        else
            xstepSize = 1;


        xsamples[0] = xbegin;
        xsamples[xsamples.length - 1] = xend;

        initSamples();

    }


    private void initSamples() {
        int xval = xbegin + xstepSize;
        for (int i = 1; i < xsamples.length - 1; i++) {
            xsamples[i] = xval;
            xval = xval + xstepSize;
        }

    }

    public IntegerSet1D reverse() {
        int[] nsamples = new int[xsamples.length];
        for (int i = 0; i < nsamples.length; i++) {
            nsamples[i] = xsamples[xsamples.length - i - 1];
        }

        return new IntegerSet1D(nsamples[0], nsamples[nsamples.length - 1]);

    }

    public int first() {
        return xsamples[0];
    }

    public int last() {
        return xsamples[xsamples.length - 1];
    }

    public int[] getSamples() {
        return xsamples;
    }


    public int size() {
        return xsamples.length;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<xsamples.length; i++) {
            sb.append(xsamples[i] + " ");
        }

        return sb.toString();
    }

}