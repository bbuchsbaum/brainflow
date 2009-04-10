package brainflow.app.presentation;

import brainflow.core.ImageView;
import brainflow.core.ClipRange;
import brainflow.core.layer.ImageLayer;
import brainflow.app.presentation.controls.HistogramControl;
import brainflow.colormap.LinearColorMap2;
import brainflow.colormap.ColorTable;
import brainflow.colormap.IColorMap;
import brainflow.image.Histogram;
import brainflow.image.space.Space;
import brainflow.image.data.BasicImageData3D;
import brainflow.image.data.IImageData;
import brainflow.utils.DataType;
import brainflow.utils.Range;

import javax.swing.*;

import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.container.BeanContainer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 27, 2008
 * Time: 4:46:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistogramPresenter extends ImageViewPresenter {

    private HistogramControl control;

    private PropertyListener colorMapListener;

    private PropertyListener thresholdListener;


    private final int capacity = 8;



    private LinkedHashMap<IImageData, Histogram> cache = new LinkedHashMap<IImageData, Histogram>() {
        protected boolean removeEldestEntry(Map.Entry<IImageData, Histogram> eldest) {
             return size() > capacity;
        }
    };

    public HistogramPresenter() {
        control = new HistogramControl(new LinearColorMap2(0, 100, ColorTable.GRAYSCALE),
                new Histogram(new BasicImageData3D(Space.createImageSpace(2, 2, 2, 1, 1, 1), DataType.DOUBLE), 10),
                new Range(0,0));

        colorMapListener = new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                IColorMap cmap = (IColorMap)newValue;

                control.setColorMap(cmap);

            }
        };

        thresholdListener = new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ClipRange cr = (ClipRange)newValue;
                control.setOverlayRange(cr.getInnerRange());

            }
        };


    }

    private void updateHistogram() {
        IImageData data = getSelectedView().getSelectedLayer().getData();
        Histogram histo = cache.get(data);


        if (histo == null ) {
            int nbins = Math.min(getSelectedLayer().getImageLayerProperties().colorMap.get().getMapSize(), 30);
            histo = new Histogram(data, nbins);
            histo.ignoreRange(new Range(0, 0));
            cache.put(data, histo);

        }

        control.setHistogram(histo);
        control.setOverlayRange(getSelectedView().getSelectedLayer().getImageLayerProperties().thresholdRange.get().getInnerRange());
        control.setColorMap(getSelectedLayer().getImageLayerProperties().colorMap.get());

    }

    public void viewDeselected(ImageView view) {
        BeanContainer.get().removeListener(view.getSelectedLayer().getImageLayerProperties().colorMap, colorMapListener);
        BeanContainer.get().removeListener(view.getSelectedLayer().getImageLayerProperties().thresholdRange, thresholdListener);
    }

    public void viewSelected(ImageView view) {
        updateHistogram();
        BeanContainer.get().addListener(view.getSelectedLayer().getImageLayerProperties().colorMap, colorMapListener);
        BeanContainer.get().addListener(view.getSelectedLayer().getImageLayerProperties().thresholdRange, thresholdListener);
    }

    @Override
    public void viewModelChanged(ImageView view) {
        viewSelected(view);
    }

    protected void layerDeselected(ImageLayer layer) {
        BeanContainer.get().removeListener(layer.getImageLayerProperties().colorMap, colorMapListener);
        BeanContainer.get().removeListener(layer.getImageLayerProperties().thresholdRange, thresholdListener);

    }

    protected void layerSelected(ImageLayer layer) {
        updateHistogram();
        BeanContainer.get().addListener(layer.getImageLayerProperties().colorMap, colorMapListener);
        BeanContainer.get().addListener(layer.getImageLayerProperties().thresholdRange, thresholdListener);

    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getComponent() {
        return control;
    }
}
