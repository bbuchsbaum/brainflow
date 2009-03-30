package brainflow.app.actions;

import brainflow.app.toplevel.ImageViewFactory;
import brainflow.core.ImageView;
import brainflow.core.OrthoPlotLayout;
import brainflow.core.IBrainCanvas;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 9, 2007
 * Time: 10:30:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateVerticalOrthogonalCommand extends BrainFlowCommand {

    public CreateVerticalOrthogonalCommand() {
        super("create-ortho-vertical");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {

            ImageView sview = ImageViewFactory.createOrthogonalView(view, OrthoPlotLayout.ORIENTATION.VERTICAL);
            IBrainCanvas canvas = getSelectedCanvas();

            if (canvas != null) {
                canvas.addImageView(sview);
            }
        }

    }



}