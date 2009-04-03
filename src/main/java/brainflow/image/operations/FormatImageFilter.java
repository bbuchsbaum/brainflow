/*
 * MeanImageFilter.java
 *
 * Created on March 23, 2003, 12:45 PM
 */

package brainflow.image.operations;

import brainflow.image.data.BasicImageData;
import brainflow.image.data.IImageData;
import brainflow.image.data.AbstractImageData;
import brainflow.image.iterators.ImageIterator;

import java.util.List;

/**
 * @author Bradley
 */
public class FormatImageFilter extends AbstractImageFilter {

    /**
     * Creates a new instance of MeanImageFilter
     */
    public FormatImageFilter() {
    }

    public IImageData getOutput() {
        List sources = getSources();

        BasicImageData first = (BasicImageData) sources.get(0);
        if (first == null) return null;

        AbstractImageData opdata = BasicImageData.create(first.getImageSpace(), outputDataType);
        ImageIterator opiter = opdata.iterator();

        ImageIterator[] iters = getSourceIterators();

        while (opiter.hasNext()) {
            double result = iters[0].next();
            opiter.set(result);
            opiter.advance();
        }

        return opdata;
    }
}
        
        
        
        
        
        
        
        
 