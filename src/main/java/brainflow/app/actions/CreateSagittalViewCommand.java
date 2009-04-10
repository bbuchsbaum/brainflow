package brainflow.app.actions;

import brainflow.app.toplevel.ImageViewFactory;
import brainflow.app.toplevel.DisplayManager;
import brainflow.core.ImageView;
import brainflow.core.IBrainCanvas;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:42:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateSagittalViewCommand extends BrainFlowCommand {

    public CreateSagittalViewCommand() {
        super("create-sagittal");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {

            ImageView sview = ImageViewFactory.createSagittalView(view);

            IBrainCanvas canvas = getSelectedCanvas();

            if (canvas != null) {
                //canvas.addImageView(sview);
                 DisplayManager.getInstance().displayView(sview);
                canvas.getImageCanvasModel().yoke(sview, view);
            }
        }


    }


}