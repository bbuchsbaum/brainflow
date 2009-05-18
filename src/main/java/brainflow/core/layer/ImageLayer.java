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
import brainflow.image.data.ImageData;
import brainflow.image.space.IImageSpace;
import brainflow.utils.Range;
import brainflow.utils.IRange;
import brainflow.core.layer.AbstractLayer;
import brainflow.core.layer.ImageLayerProperties;
import brainflow.core.ClipRange;
import brainflow.core.IClipRange;


/**
 * @author buchs
 */


public abstract class ImageLayer<T extends IImageSpace> extends AbstractLayer {


    private IImageDataSource dataSource;


    public ImageLayer(ImageLayer layer) {
        super(layer.getLabel() + "*", layer.getImageLayerProperties());
        this.dataSource = layer.getDataSource();

    }

    public ImageLayer(String name, ImageLayer layer) {
        super(name, layer.getImageLayerProperties());
        this.dataSource = layer.getDataSource();

    }

    public ImageLayer(IImageDataSource dataSource, IRange range) {
        super(dataSource.getImageInfo().getImageLabel(), new ImageLayerProperties(ColorTable.GRAYSCALE, range));
        this.dataSource = dataSource;

        if (dataSource.isLoaded()) {
            initClip();
        }

    }

    public ImageLayer(String name, IImageDataSource dataSource) {
        super(name, new ImageLayerProperties(ColorTable.GRAYSCALE, new Range(0, 255)));
        this.dataSource = dataSource;

        if (dataSource.isLoaded()) {
            initClip();
            //IClipRange clip = new ClipRange(getData().minValue(), getData().maxValue(), getData().minValue(), getData().maxValue());
            //getImageLayerProperties().getClipRange().setLowClip(getData().minValue());
            //getImageLayerProperties().getClipRange().setHighClip(getData().maxValue());
            //getImageLayerProperties().clipRange.set(clip);

        }


    }

    public ImageLayer(IImageDataSource dataSource) {
        this(dataSource.getImageInfo().getImageLabel(), dataSource);

    }

    public ImageLayer(IImageDataSource dataSource, ImageLayerProperties _properties) {
        super(dataSource.getImageInfo().getImageLabel(), _properties);
        this.dataSource = dataSource;

        //todo need to clone properties
        if (dataSource.isLoaded()) {
            IClipRange clip = getImageLayerProperties().getClipRange();


            IClipRange newclip = clip.newClipRange(getData().minValue(), getData().maxValue(), clip.getLowClip(), clip.getHighClip());
            _properties.clipRange.set(newclip);
            _properties.colorMap.set(_properties.colorMap.get().newClipRange(
                    newclip.getLowClip(), newclip.getHighClip(), newclip.getMin(), newclip.getMax()));

            IClipRange thresh = _properties.getThresholdRange();
            _properties.thresholdRange.set(thresh.newClipRange(newclip.getMin(), newclip.getMax(), thresh.getLowClip(), thresh.getHighClip()));

        }


    }

    private void initClip() {
        ImageLayerProperties props = getImageLayerProperties();
        IClipRange clip = getImageLayerProperties().getClipRange();

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

    public String getLabel() {
        return getName();
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
