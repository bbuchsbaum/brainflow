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
public class CubicInterpolationToggleCommand extends BrainFlowToggleCommand {

    public CubicInterpolationToggleCommand() {
        super("toggle-interp-cubic");
    }

    protected void handleSelection(boolean b) throws ToggleVetoException {
        if (b) {
            ImageView view = getSelectedView();
            if (view != null && view.getScreenInterpolation() != InterpolationType.CUBIC) {
                view.setScreenInterpolation(InterpolationType.CUBIC);
            }
        }
    }

    public void viewSelected(ImageView view) {
        if (view.getScreenInterpolation() == InterpolationType.CUBIC) {

            super.setSelected(true);

        }

    }
}