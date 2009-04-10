package brainflow.app.actions;

import brainflow.core.layer.AbstractLayer;
import brainflow.core.layer.ImageLayerProperties;
import brainflow.core.ImageView;
import brainflow.core.ClipRange;
import brainflow.core.IClipRange;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 19, 2007
 * Time: 12:25:25 PM
 */
public class DecreaseContrastCommand extends BrainFlowCommand {

    private double percMultiplier = .07;


    public DecreaseContrastCommand() {
        super("decrease-contrast");
    }

    protected void handleExecute() {

        ImageView view = getSelectedView();
        if (view != null) {
            int idx = view.getSelectedLayerIndex();
            AbstractLayer layer = view.getModel().get(idx);
            decrementContrast(layer);

        }

    }


    private void decrementContrast(AbstractLayer layer) {
        ImageLayerProperties props = layer.getImageLayerProperties();
        IClipRange clipRange = props.getClipRange();
        double highClip = clipRange.getHighClip();
        double lowClip = clipRange.getLowClip();

        double distance = highClip - lowClip;
        double increment = (percMultiplier * distance) / 2;

        double newHighClip = Math.min(highClip + increment, layer.getImageLayerProperties().getColorMap().getMaximumValue());
        double newLowClip = Math.max(lowClip - increment, layer.getImageLayerProperties().getColorMap().getMinimumValue());

        if (newLowClip >= newHighClip) {
            newLowClip = newHighClip - .0001;
        }

        newHighClip = Math.min(newHighClip, layer.getImageLayerProperties().getColorMap().getMaximumValue());
        newLowClip = Math.max(newLowClip, layer.getImageLayerProperties().getColorMap().getMinimumValue());


        double max = clipRange.getMax();
        double min = clipRange.getMin();
        props.clipRange.set(new ClipRange(min, max, newLowClip, newHighClip));


    }


}
