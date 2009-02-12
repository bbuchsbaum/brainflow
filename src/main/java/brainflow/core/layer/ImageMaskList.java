package brainflow.core.layer;

import brainflow.image.data.*;
import brainflow.image.operations.BooleanOperation;
import brainflow.core.layer.AbstractLayer;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.IMaskList;
import brainflow.core.layer.IMaskItem;
import brainflow.core.IImageDisplayModel;
import brainflow.core.layer.MaskItem;
import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 10, 2007
 * Time: 3:02:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageMaskList implements IMaskList {

    private List<ImageMaskItem> maskItems = new ArrayList<ImageMaskItem>();

    private ImageMaskItem root;

    private ExtendedPropertyChangeSupport support = new ExtendedPropertyChangeSupport(this);

    private PropertyChangeListener propertyListener = new MaskItemChangeListener();

    private List<ListDataListener> listDataListeners = new ArrayList<ListDataListener>();


    public ImageMaskList(final ImageLayer layer) {
        // todo here is the bug

        //final ClipRange threshhold = layer.getImageLayerProperties().thresholdRange.get();


        root = new ImageMaskItem(layer, new MaskPredicate() {
            public boolean mask(double value) {
               return !layer.getImageLayerProperties().thresholdRange.get().contains(value);
            }
        }, 0);

        maskItems.add(root);

        root.addPropertyChangeListener(propertyListener);

    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);

    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }


    public void addListDataListener(ListDataListener listener) {
        if (!listDataListeners.contains(listener))
            listDataListeners.add(listener);
    }

    public void removeListDataListener(ListDataListener listener) {
        listDataListeners.remove(listener);
    }

    private int firstItemWithinGroup(int group) {
        for (int i = 0; i < size(); i++) {
            if (getMaskItem(i).getGroup() == group) {
                return i;
            }
        }

        throw new AssertionError("assertion failed in mathod: firstItemWithinGroup");
    }


    private int lastItemWithinGroup(int group) {
        for (int i = 0; i < size(); i++) {
            if (getMaskItem(i).getGroup() > group) {
                return i - 1;
            }
        }

        throw new AssertionError("assertion failed in mathod: firstItemWithinGroup");
    }


    private IMaskedData3D makeGroupNode(List<ImageMaskItem> itemList, IMaskedData3D node, int current, int group) {
        if (current == itemList.size()) return node;

        IMaskItem item = itemList.get(current);
        if (item.getGroup() > group) {
            return node;
        } else {
            return makeGroupNode(itemList, new BooleanMaskNode3D(node, new MaskedData3D((IImageData3D) (item.getSource().getData()),
                    //todo hacked a cast here...
                    item.getPredicate()), (BooleanOperation)item.getOperation()), current + 1, item.getGroup());
        }


    }


    private IMaskedData3D makeTreeNode(List<IMaskedData3D> groupList, IMaskedData3D node, int current) {

        if (current == groupList.size()) {
            return node;
        } else {
            IMaskItem item = getMaskItem(current);
            IImageData3D data = (IImageData3D) item.getSource().getDataSource();
            BooleanMaskNode3D newNode = new BooleanMaskNode3D(node, new MaskedData3D(data, item.getPredicate()),
                    (BooleanOperation)item.getOperation());

            return makeTreeNode(groupList, newNode, current + 1);


        }

    }


    public IMaskedData3D composeMask(boolean lazy) {
        if (this.size() == 1) {
            IMaskItem item = getFirstItem();
            //item.getSource().
            return new MaskedData3D((IImageData3D) (item.getSource().getData()), item.getPredicate());
        } else {

            List<IMaskedData3D> groupList = new ArrayList<IMaskedData3D>();
            int numGroups = getLastItem().getGroup();
            int group = getFirstItem().getGroup();
            IMaskedData3D startNode = new MaskedData3D((IImageData3D) (getFirstItem().getSource().getData()), getFirstItem().getPredicate());

            int itemNum = 0;
            while (group <= numGroups) {
                itemNum = firstItemWithinGroup(group);
                groupList.add(makeGroupNode(maskItems, startNode, itemNum++, group));
                group++;
            }

            return makeTreeNode(groupList, groupList.get(0), 1);


        }

    }


    public List<ImageLayer> getCongruentLayers(IImageDisplayModel model) {
        List<ImageLayer> list = new ArrayList<ImageLayer>();
        for (int i = 0; i < model.getNumLayers(); i++) {
            AbstractLayer layer = model.getLayer(i);
            if (layer instanceof ImageLayer) {
                ImageLayer il = (ImageLayer) layer;
                if (root.getSource().getData().getImageSpace().sameGeometry(il.getData().getImageSpace())) {
                    list.add(il);
                }
            }
        }

        return list;
    }

    public boolean isCongruent(AbstractLayer layer) {
        if (root == null) return false;
        if (layer instanceof ImageLayer) {
            ImageLayer ilayer = (ImageLayer) layer;
            if (root.getSource().getData().getImageSpace().sameGeometry(ilayer.getData().getImageSpace())) {
                return true;
            }
        }

        return false;
    }

    public int indexOf(IMaskItem item) {
        return maskItems.indexOf(item);
    }

    public Iterator<ImageMaskItem> iterator() {
        return maskItems.iterator();
    }

    public ImageMaskItem getLastItem() {
        if (maskItems.size() == 0) {
            throw new IllegalStateException("ImageMaskList is empty, cannot access last item");
        }
        return maskItems.get(maskItems.size() - 1);
    }

    public ImageMaskItem getFirstItem() {
        if (maskItems.size() == 0) {
            throw new IllegalStateException("ImageMaskList is empty, cannot access first item");
        }

        return maskItems.get(0);
    }

    public ImageMaskItem getMaskItem(int index) {
        if (maskItems.size() == 0) {
            throw new IllegalStateException("ImageMaskList is empty, cannot access " + index + "th item");
        }
        return maskItems.get(index);
    }

    public void removeMaskItem(int idx) {
        if (idx == 0) throw new IllegalArgumentException("Cannot remove root item form ImageMaskList");

        MaskItem item = maskItems.get(idx);
        item.removePropertyChangeListener(propertyListener);
        maskItems.remove(idx);
        fireItemRemoved(idx);
    }

    public int size() {
        return maskItems.size();
    }


    public void fireItemAdded(int index) {
        for (ListDataListener listener : listDataListeners) {
            listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index));
        }
    }

    public void fireItemRemoved(int index) {
        for (ListDataListener listener : listDataListeners) {
            listener.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index));
        }
    }

    public IMaskItem dupMask() {
        //ImageMaskItem item = new ImageMaskItem(getLastItem().getSource(),
        //        getLastItem().getPredicate().copy(),
        //        getLastItem().getGroup(), Operations.AND, false);

        //addMask(item);
        //return item;
        return null;

    }

    public void addMask(ImageMaskItem item) {
        if (item.getGroup() > (getLastItem().getGroup() + 1)) {
            throw new IllegalArgumentException("Illegal Group number for MaskItem : " + item + " " + item.getGroup());
        }

        if (item.getGroup() < (getLastItem().getGroup())) {
            throw new IllegalArgumentException("Illegal Group number for MaskItem : " + item + " " + item.getGroup());
        }

        if (!isCongruent(item.getSource())) {
            throw new IllegalArgumentException("Argument has different geomtry than this mask group");
        }

        if (maskItems.size() == 0) {
            root = item;
        }

        maskItems.add(item);

        item.addPropertyChangeListener(propertyListener);
        fireItemAdded(maskItems.size() - 1);

    }


    class MaskItemChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            ImageMaskItem item = (ImageMaskItem) evt.getSource();
            int index = maskItems.indexOf(item);
            support.fireIndexedPropertyChange(evt.getPropertyName(), index, evt.getOldValue(), evt.getNewValue());
        }
    }


}
