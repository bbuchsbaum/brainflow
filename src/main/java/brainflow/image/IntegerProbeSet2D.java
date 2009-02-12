package brainflow.image;

import java.awt.Rectangle;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class IntegerProbeSet2D extends ProbeSet {


    IntegerSet1D xset;
    IntegerSet1D yset;

    public IntegerProbeSet2D(int _xbegin, int _xend, int _ybegin, int _yend) {
        xset = new IntegerSet1D(_xbegin, _xend);
        yset = new IntegerSet1D(_ybegin, _yend);
    }

    public IntegerProbeSet2D(IntegerSet1D _xset, IntegerSet1D _yset) {
        xset = _xset;
        yset = _yset;
    }

    public int[] getXSamples() {
        return xset.getSamples();
    }

    public int[] getYSamples() {
        return yset.getSamples();
    }

    public IntegerSet1D getXSet() {
        return xset;
    }

    public IntegerSet1D getYSet() {
        return yset;
    }

    public Rectangle getBounds() {
        return new Rectangle(xset.first(), yset.first(), xset.size(), yset.size());
    }

    public int size() {
        return xset.size() * yset.size();
    }





}