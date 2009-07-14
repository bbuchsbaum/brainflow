package brainflow.app.actions;

import brainflow.core.layer.AbstractLayer;
import brainflow.core.layer.LayerProps;
import brainflow.core.ImageView;
import brainflow.core.ClipRange;
import brainflow.core.IClipRange;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 19, 2007
 * Time: 12:25:25 PM
 */
public class IncreaseContrastCommand extends BrainFlowCommand {

    private double percMultiplier = .07;


    public IncreaseContrastCommand() {
        super("increase-contrast");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();
        if (view != null) {
            AbstractLayer layer = view.getSelectedLayer();
            incrementContrast(layer);

        }

    }


    private void incrementContrast(AbstractLayer layer) {
        LayerProps props = layer.getLayerProps();
        IClipRange clip = props.getClipRange();

        double highClip = clip.getHighClip();
        double lowClip = clip.getLowClip();

        double distance = highClip - lowClip;
        double increment = (percMultiplier * distance) / 2;

        double newHighClip = highClip - increment;
        double newLowClip = lowClip + increment;

        if (newLowClip >= newHighClip) {
            newLowClip = newHighClip - .0001;
        }

        newHighClip = Math.min(newHighClip, layer.getLayerProps().getColorMap().getMaximumValue());
        newLowClip = Math.max(newLowClip, layer.getLayerProps().getColorMap().getMinimumValue());

        double max = clip.getMax();
        double min = clip.getMin();
        props.clipRange.set(new ClipRange(min, max, newLowClip, newHighClip));
        

    }
}
