/*
 * AbstractImageFilter.java
 *
 * Created on March 23, 2003, 12:40 PM
 */

package brainflow.image.operations;

import brainflow.image.data.BasicImageData;
import brainflow.image.data.IImageData;
import brainflow.image.iterators.ImageIterator;
import brainflow.utils.DataType;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Bradley
 */
public abstract class AbstractImageFilter implements ImageFilter {

    protected List<IImageData> sources = new LinkedList<IImageData>();

    protected DataType outputDataType = null;

    /**
     * Creates a new instance of AbstractImageFilter
     */
    public AbstractImageFilter() {
    }

    public void addInput(IImageData data) {
        if (outputDataType == null)
            outputDataType = data.getDataType();
        sources.add(data);
    }

    protected ImageIterator[] getSourceIterators() {
        ImageIterator[] iters = new ImageIterator[sources.size()];
        for (int i = 0; i < iters.length; i++) {
            iters[i] = ((BasicImageData) sources.get(i)).iterator();
        }
        return iters;
    }

    protected List<IImageData> getSources() {
        return sources;
    }

    public abstract IImageData getOutput();

    public void setInput(int i, IImageData _data) {
        if (sources.size() == 0 && i == 0) {
            sources.add(_data);
        } else {
            sources.set(i, _data);
        }
        if (outputDataType == null)
            outputDataType = _data.getDataType();
    }

    public void setOutputDataType(DataType dtype) {
        outputDataType = dtype;
    }

    public DataType getOutputDataType() {
        return outputDataType;
    }

}
