/*
 * ImageLayer.java
 *
 * Created on June 29, 2006, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.core.layer;

import brainflow.image.io.IImageDataSource;
import brainflow.colormap.ColorTable;
import brainflow.image.data.IImageData;
import brainflow.image.space.IImageSpace;
import brainflow.utils.Range;
import brainflow.utils.IRange;
import brainflow.core.layer.AbstractLayer;
import brainflow.core.layer.LayerProps;
import brainflow.core.IClipRange;


/**
 * @author buchs
 */


public abstract class ImageLayer<T extends IImageSpace> extends AbstractLayer {


    private IImageDataSource dataSource;


    public ImageLayer(ImageLayer layer) {
        super(layer.getName() + "*", layer.getLayerProps());
        this.dataSource = layer.getDataSource();

    }

    public ImageLayer(String name, ImageLayer layer) {
        super(name, layer.getLayerProps());
        this.dataSource = layer.getDataSource();

    }

    public ImageLayer(IImageDataSource dataSource, IRange range) {
        super(dataSource.getImageInfo().getImageLabel(), new LayerProps(ColorTable.GRAYSCALE, range));
        this.dataSource = dataSource;

        if (dataSource.isLoaded()) {
            initClip();
        }

    }

    public ImageLayer(String name, IImageDataSource dataSource) {
        super(name, new LayerProps(ColorTable.GRAYSCALE, new Range(0, 255)));
        this.dataSource = dataSource;

        if (dataSource.isLoaded()) {
            initClip();
            //IClipRange clip = new ClipRange(getData().minValue(), getData().maxValue(), getData().minValue(), getData().maxValue());
            //getLayerProps().getClipRange().setLowClip(getData().minValue());
            //getLayerProps().getClipRange().setHighClip(getData().maxValue());
            //getLayerProps().clipRange.set(clip);

        }


    }

    public ImageLayer(IImageDataSource dataSource) {
        this(dataSource.getImageInfo().getImageLabel(), dataSource);
    }

    public ImageLayer(IImageDataSource dataSource, LayerProps _props) {
        super(dataSource.getImageInfo().getImageLabel(), _props);
        this.dataSource = dataSource;

        //todo need to clone properties
        if (dataSource.isLoaded()) {
            IClipRange clip = getLayerProps().getClipRange();


            IClipRange newclip = clip.newClipRange(getData().minValue(), getData().maxValue(), clip.getLowClip(), clip.getHighClip());
            _props.clipRange.set(newclip);
            _props.colorMap.set(_props.colorMap.get().newClipRange(
                    newclip.getLowClip(), newclip.getHighClip(), newclip.getMin(), newclip.getMax()));

            IClipRange thresh = _props.getThresholdRange();
            _props.thresholdRange.set(thresh.newClipRange(newclip.getMin(), newclip.getMax(), thresh.getLowClip(), thresh.getHighClip()));

        }


    }

    private void initClip() {
        LayerProps props = getLayerProps();
        IClipRange clip = getLayerProps().getClipRange();

        IClipRange newclip = clip.newClipRange(clip.getMin(), clip.getMax(), clip.getLowClip(), clip.getHighClip());
        props.clipRange.set(newclip);
        props.colorMap.set(props.colorMap.get().newClipRange(
                newclip.getLowClip(), newclip.getHighClip(), newclip.getMin(), newclip.getMax()));


    }


    public IImageData getData() {
        return dataSource.getData();
    }

    public IImageDataSource getDataSource() {
        return dataSource;
    }



    public T getCoordinateSpace() {
        return (T) dataSource.getData().getImageSpace();
    }

    public double getMaxValue() {
        return dataSource.getData().maxValue();
    }

    public double getMinValue() {
        return dataSource.getData().minValue();
    }


    public String toString() {
        return getName();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /*public boolean equals(Object o) {
       if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;

       ImageLayer that = (ImageLayer) o;

       if (!dataSource.equals(that.dataSource)) return false;

       return true;
   } */


}
