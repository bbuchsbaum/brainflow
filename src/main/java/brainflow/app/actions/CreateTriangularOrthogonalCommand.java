package brainflow.app.actions;

import brainflow.app.toplevel.ImageViewFactory;
import brainflow.app.toplevel.BrainFlow;
import brainflow.core.ImageView;
import brainflow.core.OrthoImageView;
import brainflow.core.IBrainCanvas;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 9, 2007
 * Time: 10:30:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateTriangularOrthogonalCommand extends BrainFlowCommand {

    public CreateTriangularOrthogonalCommand() {
        super("create-ortho-triangular");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {

            ImageView sview = ImageViewFactory.createOrthogonalView(view, OrthoImageView.ORIENTATION.TRIANGULAR);
            IBrainCanvas canvas = getSelectedCanvas();

            if (canvas != null) {
                BrainFlow.get().displayView(sview);
                //canvas.addImageView(sview);
            }
        }

    }



}