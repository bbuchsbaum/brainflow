package brainflow.application.dnd;

import brainflow.application.toplevel.*;
import brainflow.core.ImageView;
import brainflow.core.IBrainCanvas;
import brainflow.core.IImageDisplayModel;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.image.io.IImageDataSource;

import java.awt.*;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 19, 2004
 * Time: 4:32:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrainCanvasTransferHandler extends ImageDropHandler {


    // need to make it so that different handler can "register"
    private static final Logger log = Logger.getLogger(BrainCanvasTransferHandler.class.getName());


    public BrainCanvasTransferHandler() {
        super();


    }


    private void importDataSource(IImageDataSource dsource, TransferSupport support) {
        Component c = support.getComponent();

        if (c instanceof IBrainCanvas) {
            Point p = support.getDropLocation().getDropPoint();

            IBrainCanvas canvas = (IBrainCanvas) c;
            ImageView view = canvas.whichView(c, p);

            if (view != null) {
                BrainFlow.get().loadAndDisplay(dsource, view);
            } else {

                BrainFlow.get().loadAndDisplay(dsource);
            }

        }

    }

    private void importImageLayer(ImageLayer layer, TransferSupport support) {
        Component c = support.getComponent();
        if (c instanceof IBrainCanvas) {
            Point p = support.getDropLocation().getDropPoint();


            IBrainCanvas canvas = (IBrainCanvas) c;
            ImageView view = canvas.whichView(c, p);

            if (view == null) {
                //todo hack cast
                IImageDisplayModel model = ProjectManager.getInstance().createDisplayModel((ImageLayer3D)layer, true);
                DisplayManager.getInstance().displayView(ImageViewFactory.createAxialView(model));
            } else {

                // todo hack cast
                ImageLayer3D layeradd = (ImageLayer3D) layer;
                IImageDisplayModel model = view.getModel();

                if (model.containsLayer(layeradd)) {
                    //System.out.println("COPYING LAYER");
                    layeradd = new ImageLayer3D(layeradd);

                }


                model.addLayer(layeradd);


            }


        }


    }

    @Override
    public void dispatchOnObject(Object obj, TransferSupport support) {
        if (obj instanceof IImageDataSource) {
            importDataSource((IImageDataSource) obj, support);
        } else if (obj instanceof ImageLayer) {
            importImageLayer((ImageLayer) obj, support);

        }

    }


    public static void main(String[] args) {

    }


}


