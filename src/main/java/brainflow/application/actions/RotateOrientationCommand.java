package brainflow.application.actions;

import brainflow.core.ImageView;
import brainflow.image.anatomy.Anatomy3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:42:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class RotateOrientationCommand extends BrainFlowCommand {

    public RotateOrientationCommand() {
        super("rotate-orientation");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();
        if (view != null) {
            Anatomy3D nextorient;

            if (view.getSelectedPlot().getDisplayAnatomy().isAxial()) {
                nextorient = Anatomy3D.getCanonicalSagittal();
            } else if (view.getSelectedPlot().getDisplayAnatomy().isSagittal()) {
                nextorient = Anatomy3D.getCanonicalCoronal();
            } else if (view.getSelectedPlot().getDisplayAnatomy().isCoronal()) {
                nextorient = Anatomy3D.getCanonicalAxial();
            } else {
                return;
            }

            view.getPlotLayout().setDisplayAnatomy(nextorient);
        }


       
    }


}