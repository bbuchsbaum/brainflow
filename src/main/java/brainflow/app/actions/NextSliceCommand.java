package brainflow.app.actions;

import brainflow.core.ImageView;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 29, 2007
 * Time: 12:36:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class NextSliceCommand extends BrainFlowCommand {

    public NextSliceCommand() {
        super("next-slice");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {
            view.getSliceController().nextSlice();
        }


    }


}
