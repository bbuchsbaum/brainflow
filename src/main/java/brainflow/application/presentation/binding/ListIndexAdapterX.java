package brainflow.application.presentation.binding;

import net.java.dev.properties.binding.swing.adapters.SwingAdapter;
import net.java.dev.properties.BaseProperty;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import com.jidesoft.swing.CheckBoxList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 1, 2008
 * Time: 8:28:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class ListIndexAdapterX extends SwingAdapter<Integer, CheckBoxList> implements ListSelectionListener {

    @Override
    protected void bindListener(BaseProperty<Integer> property, CheckBoxList cmp) {
         cmp.addListSelectionListener(this);
    }


    protected void unbindListener(BaseProperty<Integer> property, CheckBoxList cmp) {
        cmp.removeListSelectionListener(this);
    }

    
    protected void updateUI(Integer newValue) {
        getComponent().setSelectedIndex(newValue);
    }

    public void valueChanged(ListSelectionEvent e) {
        callWhenUIChanged(getComponent().getSelectedIndex());
    }

    protected Class getType() {
        return Integer.class;
    }

    protected Class getComponentType() {
        return CheckBoxList.class;
    }

    protected boolean isSelectionBind() {
        return true;
    }
}
