package brainflow.image.axis;

import brainflow.utils.ToStringGenerator;
import brainflow.utils.NumberUtils;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.AnatomicalDirection;
import brainflow.image.anatomy.BrainPoint1D;


import java.util.logging.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public final class AxisRange implements Cloneable {

    private double begin;

    private double end;

    private AnatomicalAxis aaxis;

    private static final Logger log = Logger.getLogger(AxisRange.class.getName());

    public AxisRange(AnatomicalAxis _aaxis, double _begin, double _end) {
        assert _aaxis != null;
        assert (_begin <= _end) : "Error AxisRange: begin must be less than end";


        begin = _begin;
        end = _end;
        aaxis = _aaxis;
    }

    public AnatomicalAxis getAnatomicalAxis() {
        return aaxis;
    }

    public BrainPoint1D getCenter() {
        return new BrainPoint1D(aaxis, (begin + end) / 2);
        
    }

    public double getInterval() {
        return end - begin;
    }


    public double getMinimum() {
        return begin;
    }

    public double getMaximum() {
        return end;

    }

    public BrainPoint1D getBeginning() {
        return new BrainPoint1D(aaxis, begin);
    }

    public BrainPoint1D getEnd() {
        return new BrainPoint1D(aaxis, end);
    }

    public AxisRange flip() {
        return new AxisRange(aaxis.getFlippedAxis(), begin, end);
    }

    public AxisRange union(AxisRange other) {
        if (other.aaxis != aaxis) {
            throw new IllegalArgumentException("AxisRange.union(...): axes must have same Anatomical Direction");
        }

        double nbegin = Math.min(getBeginning().getValue(), other.getBeginning().getValue());
        double nend = Math.max(getEnd().getValue(), other.getEnd().getValue());
        return new AxisRange(aaxis, nbegin, nend);
    }


    public AxisRange grow(double proportion) {

        double growBy = proportion*getInterval();
       
        double nbegin = getBeginning().getValue() - (growBy/2);
        double nend = getEnd().getValue() + (growBy/2);
        return new AxisRange(aaxis, nbegin, nend);

    }



    public AxisRange shrink(double proportion) {
        if (proportion <= 0 || proportion >= 1) {
            throw new IllegalArgumentException("shrink factor must be greater than 0 and less than 1");
        }

        double shrinkBy = proportion*getInterval();
        double nbegin = getBeginning().getValue() + (shrinkBy/2);
        double nend = getEnd().getValue() - (shrinkBy/2);
        return new AxisRange(aaxis, nbegin, nend);
        

    }

    public double getBeginDisplacement(AxisRange other) {
        return other.begin - begin;

    }

    public double getEndDisplacement(AxisRange other) {
        return other.end - end;
    }

    public AxisRange translate(double val) {
        double nbegin, nend;
        nbegin = begin + val;
        nend = end + val;
        return new AxisRange(aaxis, nbegin, nend);

    }


    public BrainPoint1D getEdgePoint(AnatomicalDirection adir) {
        if (adir == aaxis.getMinDirection()) {
            return getBeginning();
        } else if (adir == aaxis.getMaxDirection()) {
            return getEnd();
        } else {
            throw new IllegalArgumentException("AxisRange.getEdgePoint: Supplied AnatomicalDirection " + adir + " is incorrect");
        }
    }

    public boolean contains(double val) {

        if ((val >= begin) && (val <= end)) {
            return true;
        }

        return false;
    }

    public Object clone() {
        return new AxisRange(aaxis, begin, end);
    }

    public int hashCode() {
        int result;
        long temp;
        temp = begin != +0.0d ? Double.doubleToLongBits(begin) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = end != +0.0d ? Double.doubleToLongBits(end) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (aaxis != null ? aaxis.hashCode() : 0);
        return result;
    }

    public boolean equals(Object other) {
        if (!(other instanceof AxisRange)) {
            return false;
        }
        AxisRange rhs = (AxisRange) other;

        if (!NumberUtils.equals(rhs.begin, begin, .001)) return false;
        if (!NumberUtils.equals(rhs.end, end, .001)) return false;
        if (!rhs.aaxis.equals(aaxis)) return false;

        return true;


    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("begin: " + begin).append(" end: " + end).append(" Anatomical Axis: " + aaxis);

        return sb.toString();
    }

    public String dump() {
        ToStringGenerator gen = new ToStringGenerator();
        return gen.generateToString(this);
    }


}
