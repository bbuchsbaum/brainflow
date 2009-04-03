package brainflow.image.axis;

import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.AnatomicalDirection;
import brainflow.image.anatomy.AnatomicalPoint1D;

import java.util.Arrays;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Jan 30, 2004
 * Time: 11:30:47 AM
 * To change this template use Options | File Templates.
 */
public class ImageAxis extends CoordinateAxis {


    protected int samples;

    protected int[] sampleArray;

    protected double spacing;


    public ImageAxis() {
        super(AnatomicalAxis.LEFT_RIGHT);
        samples = 1;
        sampleArray = new int[1];
        spacing = 1;
    }

    public ImageAxis(ImageAxis src) {
        super(src.getAnatomicalAxis(), src.getRange());

        setAnatomicalAxis(src.getAnatomicalAxis());
        samples = src.samples;
        spacing = src.spacing;

    }

    public ImageAxis(AxisRange range, double spacing) {
        super(range.getAnatomicalAxis(), range);
        this.samples = (int) (range.getInterval() / spacing);
        this.spacing = spacing;

    }


    public ImageAxis(double begin, double end, AnatomicalAxis _axis, int _samples) {
        super(_axis, new AxisRange(_axis, begin, end));
        samples = _samples;
        spacing = getRange().getInterval() / samples;
    }

    public ImageAxis(AnatomicalAxis _axis, double begin, double _spacing, int _samples) {
        super(_axis, new AxisRange(_axis, begin, begin+ _spacing*_samples));
        samples = _samples;
        spacing = _spacing;

    }

    public ImageAxis flip() {
        return new ImageAxis(getRange().getBeginning().getValue(), getRange().getEnd().getValue(), getAnatomicalAxis().getFlippedAxis(), samples);
    }


    public ImageAxis anchorAxis(AnatomicalDirection adir, double value) {
        if (adir == getAnatomicalAxis().getMaxDirection()) {
            return new ImageAxis(new AxisRange(getAnatomicalAxis(), value - getRange().getInterval(), value), spacing);
        } else if (adir == getAnatomicalAxis().getMinDirection()) {
            return new ImageAxis(new AxisRange(getAnatomicalAxis(), value, value + getRange().getInterval()), spacing);
        } else {
            throw new ImageAxis.IncompatibleAxisException("cannot anchor to axis direction: " + adir + " for axis " + getAnatomicalAxis());
        }
    }

    public ImageAxis matchAxis(AnatomicalAxis aaxis) {
        if (aaxis == getAnatomicalAxis()) {
            return this;
        } else if (aaxis == getAnatomicalAxis().getFlippedAxis()) {
            return flip();
        }

        throw new ImageAxis.IncompatibleAxisException("cannot match axes: " + aaxis + " and " + getAnatomicalAxis());

    }

    public ImageAxis matchAxis(ImageAxis src) {
        if (src.getAnatomicalAxis() == getAnatomicalAxis()) {
            return src;
        } else if (src.getAnatomicalAxis() == getAnatomicalAxis().getFlippedAxis()) {
            return flip();
        }

        throw new ImageAxis.IncompatibleAxisException("cannot match axes: " + src.getAnatomicalAxis() + " and " + getAnatomicalAxis());
    }






    public int getNumSamples() {
        return samples;
    }

    public final int nearestSample(double pt) {
        int idx = (int) (Math.abs(pt - getRange().getMinimum()) / spacing);
        idx = Math.max(idx, 0);
        idx = Math.min(idx, getNumSamples() - 1);
        return getSamples()[idx];
    }

    public final int nearestSample(AnatomicalPoint1D pt) {
        assert pt.getAnatomy().sameAxis(getAnatomicalAxis());


        if (pt.getAnatomy() == getAnatomicalAxis().getFlippedAxis()) {
            pt = pt.mirrorPoint(this);
        }

        int idx = (int) (Math.abs(pt.getValue() - getRange().getMinimum()) / spacing);
        idx = Math.max(idx, 0);
        idx = Math.min(idx, getNumSamples() - 1);
        return getSamples()[idx];
    }

    public final double valueOf(AnatomicalPoint1D sample) {
        assert sample.getAnatomy().sameAxis(getAnatomicalAxis());

        if (sample.getAnatomy() == getAnatomicalAxis().getFlippedAxis()) {
            sample = sample.mirrorPoint(this);
        }

        double relpos = sample.getValue() * spacing + spacing / 2f;
        return relpos + getRange().getMinimum();
    }


    public final AnatomicalPoint1D valueOf(int sample) {
        double relpos = sample * spacing + spacing / 2f;
        return new AnatomicalPoint1D(getAnatomicalAxis(), relpos + getRange().getMinimum());
    }

    public int[] getSampleArray() {
        if (sampleArray == null) {
            sampleArray = new int[samples];
            for (int i = 0; i < samples; i++) {
                sampleArray[i] = i;
            }
        }

        return sampleArray.clone();

    }

    protected int[] getSamples() {
        if (sampleArray == null) {
            sampleArray = new int[samples];
            for (int i = 0; i < samples; i++) {
                sampleArray[i] = i;
            }
        }

        return sampleArray;
    }

    public double getSpacing() {
        return spacing;
    }

    public boolean contains(double pt) {
        return getRange().contains(pt);
    }


    public final double gridPosition(double pt) {
        //todo double check to make sure this is right
        return ((pt - getRange().getMinimum()) / spacing) -1;
    }

    public final double gridToReal(double gridpt) {
        //todo double check to make sure this is right
        return gridpt * spacing + getRange().getMinimum();
    }



    public double taxi(double current, double step, AnatomicalDirection adir) {
        assert (adir == getAnatomicalAxis().getMaxDirection()) || (adir == getAnatomicalAxis().getMinDirection());

        double ret = 0;
        if (adir == getAnatomicalAxis().getMaxDirection()) {
            ret = current + step;
        } else if (adir == getAnatomicalAxis().getMinDirection()) {
            ret = current - step;
        }

        return ret;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ImageAxis imageAxis = (ImageAxis) o;

        if (samples != imageAxis.samples) return false;
        if (Double.compare(imageAxis.spacing, spacing) != 0) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + samples;
        result = 31 * result + (sampleArray != null ? Arrays.hashCode(sampleArray) : 0);
        temp = spacing != +0.0d ? Double.doubleToLongBits(spacing) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(" anatomical axis: " + getAnatomicalAxis().toString()).append("\n")
                .append(" axis range: " + getRange().toString()).append("\n")
                .append(" samples: " + samples).append("\n")
                .append(" spacing: " + spacing);

        return sb.toString();

    }

    /*private class FlippedImageAxis extends ImageAxis {

       public FlippedImageAxis(ImageAxis unflipped) {
           range = unflipped.getRange().flip();
           samples = unflipped.getNumSamples();
           spacing = unflipped.getSpacing();
           axis = unflipped.getAnatomicalAxis().getFlippedAxis();
           sampleArray = unflipped.getSampleArray();
           reverseSamples();
       }

       private void reverseSamples() {
           int[] nsamples = new int[sampleArray.length];
           for (int i = sampleArray.length - 1; i > -1; i--) {
               nsamples[sampleArray.length - i - 1] = sampleArray[i];
           }

           sampleArray = nsamples;
       }

       public int[] getSampleArray() {
           return sampleArray.clone();
       }

       protected int[] getSamples() {
           return sampleArray;
       }

   } */


    public static class IncompatibleAxisException extends IllegalArgumentException {
        public IncompatibleAxisException() {
            super();
        }

        public IncompatibleAxisException(String message) {
            super(message);
        }

    }


    public static void main(String[] args) {
        int[] i1 = new int[]{1, 2, 3};
        int[] i2 = i1.clone();
        i2[0] = 55;


    }


}
