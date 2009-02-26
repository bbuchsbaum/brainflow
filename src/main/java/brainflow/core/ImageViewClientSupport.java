package brainflow.core;


import brainflow.image.space.IImageSpace;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;

import javax.swing.event.ListDataEvent;

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

    private DisplayModelListener listener = new DisplayModelListener();

    private LayerSelectionListener layerSelectionListener = new LayerSelectionListener();

    //private ListSelectionListener listSelectionListener;

    public ImageViewClientSupport(ImageView view, final ImageViewClient client) {
        this.client = client;
        this.view = view;
        this.view.displayModel.get().addImageDisplayModelListener(listener);



        BeanContainer.get().addListener(view.displayModel.get().getListSelection(), layerSelectionListener);
        BeanContainer.get().addListener(this.view.displayModel, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                IImageDisplayModel old = (IImageDisplayModel)oldValue;
                old.removeImageDisplayModelListener(listener);
                BeanContainer.get().removeListener(old.getListSelection(), layerSelectionListener);
                
            }
        });

    }


    public IImageDisplayModel getModel() {
        return view.displayModel.get();
    }

    public ImageLayer getSelectedLayer() {
        return view.displayModel.get().getSelectedLayer();
    }

    public int getSelectedIndex() {
        return view.displayModel.get().getSelectedIndex();
    }

    class LayerSelectionListener implements PropertyListener {
        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            ImageLayer layer = (ImageLayer) newValue;
            client.selectedLayerChanged(layer);

        }


    }


    class DisplayModelListener implements ImageDisplayModelListener {
        public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {

        }

        public void intervalAdded(ListDataEvent e) {
            client.layerContentsChanged(e);
        }

        public void contentsChanged(ListDataEvent e) {
            client.layerContentsChanged(e);
        }

        public void intervalRemoved(ListDataEvent e) {
            client.layerContentsChanged(e);
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
