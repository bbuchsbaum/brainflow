package brainflow.core.layer;

import brainflow.image.data.ClusterSet;
import brainflow.image.data.Data;
import brainflow.image.data.IImageData3D;
import brainflow.image.operations.LabelComponents;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.operations.ComponentLabeler;
import brainflow.utils.DataType;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 23, 2010
 * Time: 12:40:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterProperty {

    private ClusterSet clusterSet;

    private ImageLayer3D layer;

    private boolean computed = false;

    private IImageData3D labels;

    public ClusterProperty(ImageLayer3D layer) {
        this.layer = layer;
        clusterSet = new ClusterSet();

        BeanContainer.get().addListener(layer.maskProperty, new PropertyListener() {
            @Override
            public void propertyChanged(BaseProperty baseProperty, Object o, Object o1, int i) {
                computed = false;
                clusterSet = new ClusterSet();
            }
        });
    }

    public ClusterSet getClusterSet() {
        return clusterSet;
    }

    public ImageLayer3D getLayer() {
        return layer;
    }

    public boolean isComputed() {
        return computed;
    }

    public IImageData3D getLabels() {
        if (labels == null) computeCluster();
        return labels;
    }

    public ClusterSet computeCluster() {
        //ComponentLabeler labeler = new ComponentLabeler(layer.getMaskProperty().buildMask(), 12);
        LabelComponents labeler = new LabelComponents(layer.getMaskProperty().buildMask(), Data.createWriter(layer.getData(), DataType.INTEGER), 12,1);
      
        labeler.labelComponents();
        labels = labeler.getLabelledComponents();
        clusterSet = new ClusterSet(labeler.getLabelledComponents(),layer.getData());
        return clusterSet;

    }
}
