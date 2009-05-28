package brainflow.core.binding;

import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.ImageLayerListener;
import brainflow.core.layer.ImageLayerListenerImpl;
import brainflow.core.layer.ImageLayerEvent;
import brainflow.core.ImageViewModel;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.ObservableIndexed;
import net.java.dev.properties.container.BeanContainer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 9, 2009
 * Time: 4:13:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class WrappedImageViewModel {


    public final IndexedProperty<ImageLayer3D> listModel = ObservableIndexed.create();


    public final IndexedProperty<Integer> visibleSelection = new ObservableIndexed<Integer>() {
        @Override
        public void set(List<Integer> integers) {
            
            if (!integers.equals(get())) {
                super.set(integers);
                updateVisiblity();
            }
        }



        
    };



    private ImageViewModel model;

    private ImageLayerListener visListener;

    public WrappedImageViewModel(ImageViewModel model) {
        BeanContainer.bind(this);

        this.model = model;

        listModel.set(model.cloneList());
        visibleSelection.set(model.visibleLayers());

        visListener = new ImageLayerListenerImpl() {

            public void visibilityChanged(ImageLayerEvent event) {
                List<Integer> vlist = event.getModel().visibleLayers();
                if (!vlist.equals(visibleSelection.get())) {
                    visibleSelection.set(vlist);
                }
            }
        };

        model.addImageLayerListener(visListener);
    }

    public Property<Integer> layerSelection() {
        return model.layerSelection;
    }

    private void updateVisiblity() {
        for (int i=0; i<model.size(); i++) {
            if (model.isVisible(i) && !visibleSelection.get().contains(i)) {
                model.setVisible(i, false);
            } else if (!model.isVisible(i) && visibleSelection.get().contains(i)) {
                model.setVisible(i, true);
            }
        }
    }


}


