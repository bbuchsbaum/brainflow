package brainflow.image.operations;

import brainflow.image.data.BasicImageData;
import brainflow.image.data.IImageData;
import brainflow.image.data.AbstractImageData;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.iterators.ValueIterator;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: May 2, 2005
 * Time: 12:44:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThresholdImageFilter extends AbstractImageFilter {


    private double threshold = 0;

    public ThresholdImageFilter() {

    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }


    public IImageData getOutput() {
        List sources = getSources();
        if (sources.size() == 0)
            throw new RuntimeException("ThresholdImageFilter requires zero or more source images");

        BasicImageData first = (BasicImageData) sources.get(0);


        AbstractImageData opdata = BasicImageData.create(first.getImageSpace(), outputDataType);
        ValueIterator opiter = opdata.valueIterator();

        ImageIterator[] iters = getSourceIterators();

        while (opiter.hasNext()) {
            double val = iters[0].next();
            if (val > threshold) {
                //opiter.set(val);
            }

            opiter.advance();
        }

        return opdata;


    }
}
