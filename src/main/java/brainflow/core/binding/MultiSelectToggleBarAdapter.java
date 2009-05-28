package brainflow.core.binding;

import brainflow.gui.MultiSelectToggleBar;
import net.java.dev.properties.binding.swing.adapters.SwingAdapter;
import net.java.dev.properties.BaseProperty;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 25, 2008
 * Time: 5:50:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiSelectToggleBarAdapter extends SwingAdapter<List<Integer>, MultiSelectToggleBar> implements ListSelectionListener {

    @Override
    protected void bindListener(BaseProperty<List<Integer>> property, MultiSelectToggleBar cmp) {
         cmp.addListSelectionListener(this);
    }


    protected void unbindListener(BaseProperty<List<Integer>> property, MultiSelectToggleBar cmp) {
        cmp.removeListSelectionListener(this);
    }


    protected void updateUI(List<Integer> newValue) {
        getComponent().setSelectedIndices(newValue);
    }

    public void valueChanged(ListSelectionEvent e) {
        callWhenUIChanged(getComponent().getSelectedIndices());


    }

    protected Class getType() {
        return List.class;
    }

    protected Class getComponentType() {
        return MultiSelectToggleBar.class;
    }

    protected boolean isSelectionBind() {
        return true;
    }


}
