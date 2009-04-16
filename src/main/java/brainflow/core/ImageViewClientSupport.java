package brainflow.core;


import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 29, 2007
 * Time: 10:32:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewClientSupport {

    private ImageViewClient client;

    private ImageView view;

    private LayerSelectionListener layerSelectionListener = new LayerSelectionListener();

    public ImageViewClientSupport(ImageView view, final ImageViewClient client) {
        this.client = client;
        this.view = view;



        BeanContainer.get().addListener(view.getModel().layerSelection, layerSelectionListener);


    }


    public ImageViewModel getModel() {
        return view.getModel();
    }

    public ImageLayer3D getSelectedLayer() {
        return view.getSelectedLayer();
    }

    public int getSelectedIndex() {
        return view.getSelectedLayerIndex();
    }

    class LayerSelectionListener implements PropertyListener {
        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            ImageLayer layer = (ImageLayer) newValue;
            client.selectedLayerChanged(layer);

        }


    }




    public static void main(String[] args) {
        ImageLayer3D layer = null; //(ImageLayer3D)TestUtils.quickLayer("icbm452_atlas_probability_gray.hdr");
        IImageDisplayModel model = new ImageDisplayModel("junk");
        model.addLayer(layer);
        
        ImageLayer3D layer2 = null;//(ImageLayer3D)TestUtils.quickLayer("icbm452_atlas_probability_white.hdr");
        model.addLayer(layer2);
        model.setSelectedIndex(1);


    }
}
