/*
 * LinearSet1D.java
 *
 * Created on May 12, 2003, 3:19 PM
 */

package brainflow.image;
import brainflow.utils.*;


/**
 *
 * @author  Bradley
 */
public class LinearSet1D extends ProbeSet {
    
    double[] samples;
    double stepSize;

    /** Creates a new instance of LinearSet1D */
    public LinearSet1D(double begin, double end, int steps) {
        samples = new double[steps];
        //stepSize = (end-begin)/(steps-1);
        //samples[0] = begin;
        //samples[samples.length-1] = end;
        stepSize = (end-begin)/steps;
        initSamples(begin, end);
        //initSamples();
    }
    
    private void initSamples(double begin, double end) {
        double[] vals = new double[samples.length+1];
        vals[0] = begin;
        vals[vals.length-1] = end;
        for (int i=1; i<vals.length-1; i++) {
            vals[i] = vals[i-1] + stepSize;
        }

        for (int i=0; i<samples.length; i++) {
            samples[i] = (vals[i] + vals[i+1])/2;
        }
        //double val = samples[0];
        //for (int i=1; i<samples.length-1; i++) {
        //    val = val+stepSize;
        //    samples[i] = val;
        //}
    }
    
    public double[] getSamples() {
        return (double[])samples.clone();
    }
    
    public double getSample(int idx) {
        return samples[idx];
    }
    
    public int length() {
        return samples.length;
    }
    
    public Range getBounds() {
        return new Range(samples[0], samples[samples.length-1]);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("number of samples: " + samples.length + "\n");
        sb.append("stepSize: " + stepSize + "\n");

        for (int i=0; i<samples.length; i++) {
            sb.append(samples[i] + " ");
        }

        return sb.toString();
    }

}
