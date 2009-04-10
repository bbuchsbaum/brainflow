package brainflow.app.actions;

import brainflow.core.IImageDisplayModel;
import brainflow.core.ImageView;
import brainflow.core.ImageViewModel;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.LayerList;

import java.util.Collections;
import java.util.List;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 18, 2007
 * Time: 12:35:48 PM
 */
public class RotateLayersCommand extends BrainFlowCommand {

    public RotateLayersCommand() {
        super("rotate-layers");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null && view.getModel().size() > 1) {
            ImageViewModel model = view.getModel();

            List<ImageLayer3D> list = model.cloneList();
            Collections.rotate(list, 1);
            view.setModel(new ImageViewModel(model.getName(), list));
            //model.swapLayers(model.getNumLayers() -1, );
        }
    }
}
