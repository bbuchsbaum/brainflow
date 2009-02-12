package brainflow.chart;

import brainflow.image.Histogram;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 9, 2008
 * Time: 10:22:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class LogHistogramDataset extends HistogramDatasetX {

    public LogHistogramDataset(Histogram _histogram) {
        super(_histogram);
        double[] bins = getBins();
        for (int i=0; i<bins.length;i++){
            bins[i] = Math.log(bins[i]);
        }

    }
}
