package brainflow.application.actions;

import brainflow.core.ImageView;
import brainflow.display.InterpolationType;
import com.pietschy.command.toggle.ToggleVetoException;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 12, 2007
 * Time: 10:31:56 PM
 */
public class NearestInterpolationToggleCommand extends BrainFlowToggleCommand {

    public NearestInterpolationToggleCommand() {
        super("toggle-interp-nearest");
    }

    protected void handleSelection(boolean b) throws ToggleVetoException {
        if (b) {
            ImageView view = getSelectedView();
            if (view != null && view.getScreenInterpolation() != InterpolationType.NEAREST_NEIGHBOR) {
                view.setScreenInterpolation(InterpolationType.NEAREST_NEIGHBOR);
            }
        }


    }

    public void viewSelected(ImageView view) {
        if (view.getScreenInterpolation() == InterpolationType.NEAREST_NEIGHBOR) {
            setSelected(true);
        }

    }
}