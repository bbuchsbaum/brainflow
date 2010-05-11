package brainflow.app.dnd;

import brainflow.app.toplevel.BrainFlow;
import brainflow.core.ImageView;
import brainflow.core.ImageViewModel;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.image.io.IImageSource;

import java.awt.*;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 19, 2004
 * Time: 4:32:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewTransferHandler extends ImageDropHandler {


    // need to make it so that different handler can "register"
    final static Logger log = Logger.getLogger(ImageViewTransferHandler.class.getName());


    public ImageViewTransferHandler() {
        super();
    }

    private void importDataSource(IImageSource dsource, TransferSupport support) {
        Component c = support.getComponent();

        if (c instanceof ImageView) {
            ImageView view = (ImageView) c;
            
            BrainFlow.get().loadAndDisplay(dsource, view);
        }

    }

    private void importImageLayer(ImageLayer layer, TransferSupport support) {
        Component c = support.getComponent();

        if (c instanceof ImageView) {
            ImageView view = (ImageView) c;

            ImageViewModel model = view.getModel();

            ImageLayer3D layeradd = (ImageLayer3D)layer;
      
            if (model.contains(layeradd)) {
                layeradd = new ImageLayer3D(layeradd);
            }

            model.add(layeradd);
            model.layerSelection.set(model.indexOf(layeradd));
        }

    }


    public void dispatchOnObject(Object obj, TransferSupport support) {
        if (obj instanceof IImageSource) {
            importDataSource((IImageSource) obj, support);
        } else if (obj instanceof ImageLayer) {
            importImageLayer((ImageLayer) obj, support);
        }
    }
}
