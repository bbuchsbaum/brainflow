package brainflow.app.actions;

import brainflow.core.ImageView;
import brainflow.core.annotations.AxisLabelAnnotation;
import com.pietschy.command.toggle.ToggleVetoException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 28, 2007
 * Time: 11:52:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToggleAxisLabelCommand extends BrainFlowToggleCommand {

    public static final String COMMAND_ID = "toggle-axis-label";

    public ToggleAxisLabelCommand() {
        super(ToggleAxisLabelCommand.COMMAND_ID);
    }

    protected void handleSelection(boolean b) throws ToggleVetoException {
        ImageView view = getSelectedView();
        if (view != null) {
            AxisLabelAnnotation annot = (AxisLabelAnnotation) view.getAnnotation(view.getSelectedPlot(), AxisLabelAnnotation.ID);
            if (annot != null) {
                if (b) {
                    annot.setVisible(true);
                } else {
                    annot.setVisible(false);

                }
            } else {
                if (b) {
                    annot = new AxisLabelAnnotation();
                    annot.setVisible(true);
                    view.setAnnotation(AxisLabelAnnotation.ID, annot);
                }

            }

        }
    }



}
