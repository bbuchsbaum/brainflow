package brainflow.core.layer;

import brainflow.core.layer.AbstractLayer;
import brainflow.core.IImageDisplayModel;

import javax.swing.event.ListDataListener;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 8, 2007
 * Time: 7:46:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMaskList {

    public List<? extends AbstractLayer> getCongruentLayers(IImageDisplayModel model);

    public IMaskItem getLastItem();

    public IMaskItem getFirstItem();

    public IMaskItem getMaskItem(int index);

    public void removeMaskItem(int idx);

    public int indexOf(IMaskItem item);

    public Iterator<? extends IMaskItem> iterator();

    public int size();

    public void addListDataListener(ListDataListener listener);
    
    public void removeListDataListener(ListDataListener listener);
    //public void addMask(IMaskItem item);

    public IMaskItem dupMask();


}
