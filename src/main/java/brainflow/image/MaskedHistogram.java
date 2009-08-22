package brainflow.image;

import brainflow.image.data.IImageData;
import brainflow.image.data.IMaskedData;
import brainflow.image.iterators.ValueIterator;
import brainflow.math.ArrayUtils;
import cern.colt.list.DoubleArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 9, 2009
 * Time: 12:36:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskedHistogram extends Histogram {

    private IMaskedData mask;

    public MaskedHistogram(IImageData _data, IMaskedData mask, int _numBins) {
        super(_data, _numBins);
        this.mask = mask;
    }

    public DoubleArrayList computeBins() {
        if (computed)
            return binList;

        maxValue = getData().maxValue();
        minValue = getData().minValue();

        int[] bins = new int[getNumBins()];

        binSize = (maxValue - minValue) / getNumBins();

        ValueIterator iter = getData().iterator();

        while (iter.hasNext()) {
            if (!mask.isTrue(iter.index())) {
                continue;
            }

            double val = iter.next();


            int nbin = (int) ((val - minValue) / binSize);
            if (nbin >= bins.length)
                nbin = bins.length - 1;

            bins[nbin]++;
        }

        computed = true;
        binList = new DoubleArrayList(ArrayUtils.castToDoubles(bins));
        computeBinIntervals(binSize);
        binList.trimToSize();
        return binList;
    }

}
