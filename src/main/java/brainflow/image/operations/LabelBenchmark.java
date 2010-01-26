package brainflow.image.operations;

import brainflow.core.BF;
import brainflow.core.BrainFlowException;
import brainflow.image.data.*;
import brainflow.image.io.BrainIO;
import brainflow.utils.StopWatch;
import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 23, 2010
 * Time: 4:37:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class LabelBenchmark {

    IImageData3D data;

    double threshold;

    int radius;

    public LabelBenchmark(IImageData3D data, double threshold, int radius) {
        this.data = data;
        this.threshold = threshold;
        this.radius = radius;


    }

    public long start() {
        StopWatch watch = new StopWatch();
        watch.start("label " + threshold + " = " + threshold + " " + " radius = " + radius);
        IMaskedData3D mask = new
                MaskedData3D(data, new MaskPredicate() {
            @Override
            public final boolean mask(double value) {
                return value > (threshold);

            }
        });

        ComponentLabeler labeler = new ComponentLabeler(mask, radius);
        labeler.label();
        return watch.stopAndReport("label " + threshold + " = " + threshold + " " + " radius = " + radius).duration;

    }



    public static void main(String[] args) {
        try {
            IImageData3D data = (IImageData3D)BrainIO.readNiftiImage(BF.getDataURL("cohtrend_GLT#0_Tstat.nii"));
            double thresh = 0;
            List<Integer> radii = Arrays.asList( 8,12,16,24,32);

            Map<Integer, DoubleArrayList> map = new HashMap<Integer, DoubleArrayList>();
            for (int i =0; i<30; i++) {
                for (int j : radii) {
                    LabelBenchmark mark = new LabelBenchmark(data, thresh, j);
                    double res = mark.start()/1000000.0;
                    if (map.containsKey(j)) {
                        DoubleArrayList ret = map.get(j);
                        ret.add(res);
                    } else {
                        DoubleArrayList list = new DoubleArrayList();
                        list.add(res);
                        map.put(j, list);
                    }
                }
            }

            for (int j : radii) {
                DoubleArrayList ret = map.get(j);
                System.out.println("mean " + j + ": " + Descriptive.median(ret));

            }





        } catch (BrainFlowException e) {
            e.printStackTrace();
        }
    }
}
