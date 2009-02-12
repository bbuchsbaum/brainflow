package brainflow.application.presentation.binding;

import brainflow.gui.ToggleBar;

import net.java.dev.properties.binding.swing.adapters.SwingAdapter;
import net.java.dev.properties.BaseProperty;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 31, 2008
 * Time: 10:41:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToggleBarAdapterX extends SwingAdapter<Integer, ToggleBar> implements ListSelectionListener {


    protected void bindListener(BaseProperty<Integer> integerBaseProperty, ToggleBar component) {
        component.addListSelectionListener(this);
    }

    protected void unbindListener(BaseProperty<Integer> integerBaseProperty, ToggleBar component) {
        component.removeListSelectionListener(this);

    }


    protected Class getType() {
        return Integer.class;
    }

    protected Class getComponentType() {
        return ToggleBar.class;
    }

    protected void updateUI(Integer newValue) {

        getComponent().setSelectedIndex(newValue);

    }

    public void valueChanged(ListSelectionEvent e) {
        callWhenUIChanged(getComponent().getSelectedIndex());
    }

    protected boolean isSelectionBind() {
        return true;
    }


}
