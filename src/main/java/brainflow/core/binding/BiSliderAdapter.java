package brainflow.core.binding;

import net.java.dev.properties.binding.swing.adapters.SwingAdapter;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.RProperty;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import brainflow.gui.BiSlider;
import brainflow.gui.NumberRangeModel;
import brainflow.core.IClipRange;
import brainflow.utils.NumberUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 13, 2008
 * Time: 2:20:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class BiSliderAdapter extends SwingAdapter<IClipRange, BiSlider> implements ChangeListener {

    protected void bindListener(BaseProperty<IClipRange> clipRangeBaseProperty, BiSlider component) {
        component.addChangeListener(this);
    }

    protected void unbindListener(BaseProperty<IClipRange> clipRangeBaseProperty, BiSlider component) {
        component.removeChangeListener(this);
    }

    protected void updateUI(IClipRange newValue) {
        if (newValue != null) {
               getComponent().getModel().setRangeProperties(newValue.getLowClip(), newValue.getHighClip(), newValue.getMin(), newValue.getMax(), false);

        }

    }


    public void stateChanged(ChangeEvent e) {
        NumberRangeModel model = getComponent().getModel();
        RProperty<IClipRange> prop = (RProperty<IClipRange>) getProperty();
        IClipRange clip = prop.get();
        IClipRange crange = clip.newClipRange(model.getMin(), model.getMax(), model.getLowValue(), model.getHighValue());
         callWhenUIChanged(crange);

        if (!NumberUtils.equals(crange.getLowClip(), model.getLowValue(), .0001) || !NumberUtils.equals(crange.getHighClip(), model.getHighValue(), .0001)) {
            updateUI(crange);
        }

    }


    protected Class getType() {

        return IClipRange.class;

    }


    protected Class getComponentType() {

        return BiSlider.class;

    }

}
