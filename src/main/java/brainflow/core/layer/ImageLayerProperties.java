package brainflow.core.layer;

import brainflow.colormap.ColorTable;
import brainflow.colormap.IColorMap;
import brainflow.colormap.LinearColorMap2;
import brainflow.display.*;
import brainflow.utils.IRange;
import brainflow.utils.Range;
import brainflow.core.ClipRange;
import brainflow.core.IClipRange;
import net.java.dev.properties.Property;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.container.ObservableIndexed;
import net.java.dev.properties.container.ObservableWrapper;

import java.awt.image.IndexColorModel;
import java.io.Serializable;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */


public class ImageLayerProperties implements Serializable {



    public final ObservableProperty<IColorMap> colorMap = ObservableProperty.create();


    public final IndexedProperty<InterpolationType> interpolationSet = new ObservableIndexed<InterpolationType>(
            InterpolationType.NEAREST_NEIGHBOR, InterpolationType.LINEAR, InterpolationType.CUBIC);


    public final Property<InterpolationType> interpolationType = ObservableProperty.create(InterpolationType.LINEAR);

    public final Property<Integer> interpolationSelection = new ObservableWrapper.ReadWrite<Integer>(interpolationType) {
        public Integer get() {
            return interpolationSet.get().indexOf(interpolationType.get());
        }

        public void set(Integer integer) {       
            InterpolationType itype = interpolationSet.get(integer);
            if (itype != null) {
                interpolationType.set(itype);
            } else {
                throw new IllegalArgumentException("Illegal index " + integer + " for interpolation type");
            }
        }
    };

    public final Property<Boolean> visible = new ObservableProperty<Boolean>(true) {
        public void set(Boolean aBoolean) {
           super.set(aBoolean);

        }
    };

    public final Property<Double> opacity = ObservableProperty.create(1.0);

    public final Property<Double> smoothingRadius = ObservableProperty.create(0.0);

    public final ObservableProperty<IClipRange> thresholdRange = ObservableProperty.create();

    public final ObservableProperty<IClipRange> clipRange = ObservableProperty.create();


    public ImageLayerProperties(ImageLayerProperties props) {
        BeanContainer.bind(this);
        visible.set(props.visible.get());
        opacity.set(props.opacity.get());
        smoothingRadius.set(props.smoothingRadius.get());
        thresholdRange.set(props.thresholdRange.get());
        //clipRange.set(props.clipRange.get());
        init(props.colorMap.get(), new Range(props.clipRange.get().getMin(), props.clipRange.get().getMax()));
        //clipRange.set(props.clipRange.get());
        //colorMap.set(props.colorMap.get());



    }


    public ImageLayerProperties(IRange _dataRange) {
        BeanContainer.bind(this);
        IColorMap imap = new LinearColorMap2(_dataRange.getMin(), _dataRange.getMax(), ColorTable.GRAYSCALE);
        init(imap, _dataRange);
    }


    public ImageLayerProperties(IndexColorModel _icm, IRange _dataRange) {
        BeanContainer.bind(this);
        IColorMap imap = new LinearColorMap2(_dataRange.getMin(), _dataRange.getMax(), _icm);
        init(imap, _dataRange);
    }


    private void init(IColorMap map, IRange dataRange) {
        colorMap.set(map);

        IClipRange clip = new ClipRange(dataRange.getMin(), dataRange.getMax(), map.getLowClip(), map.getHighClip());
        clipRange.set(clip);

        //todo this automatically resets threshold range ...
        IClipRange tclip = new ClipRange(dataRange.getMin(), dataRange.getMax(), 0, 0);
        thresholdRange.set(tclip);

        


    }

   

    public double getSmoothingRadius() {
        return smoothingRadius.get();
    }

    public boolean isVisible() {
        return visible.get();
    }

    public IClipRange getClipRange() {
        return clipRange.get();
    }


    public IClipRange getThresholdRange() {
        return thresholdRange.get();
    }

    public float getOpacity() {
        return opacity.get().floatValue();
    }

    public IColorMap getColorMap() {
        return colorMap.get();
    }

    public InterpolationType getInterpolation() {
        return interpolationType.get();
    }


}