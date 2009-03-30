package brainflow.app.presentation.binding;

import net.java.dev.properties.binding.swing.adapters.SwingAdapter;
import net.java.dev.properties.binding.Adapter;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.util.Utils;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.List;
import java.util.ArrayList;

import com.jidesoft.swing.CheckBoxList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 8, 2008
 * Time: 7:05:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class CheckBoxListAdapter extends SwingAdapter<List<Integer>, CheckBoxList> implements ListSelectionListener {


    protected void bindListener(BaseProperty<List<Integer>> objectBaseProperty, CheckBoxList component) {
        component.getCheckBoxListSelectionModel().addListSelectionListener(this);
    }

    protected void unbindListener(BaseProperty<List<Integer>> objectBaseProperty, CheckBoxList component) {
        component.getCheckBoxListSelectionModel().removeListSelectionListener(this);
    }

    protected void bindUI(BaseProperty<List<Integer>> property, CheckBoxList component) {


        component.putClientProperty("SelectionProperty2", property);

        component.putClientProperty("SelectionAdapter2", this);


        bindListener(property, component);

    }

     protected SwingAdapter<List<Integer>, CheckBoxList> getBoundSelectionAdapter(CheckBoxList component) {

        return (SwingAdapter<List<Integer>, CheckBoxList>)component.getClientProperty("SelectionAdapter2");

    }


    /**
     * Unbinds the listeners in the subclass appropriately
     */

    protected void unbindUI(BaseProperty<List<Integer>> property, CheckBoxList component) {

        component.putClientProperty("SelectionProperty2", null);

        component.putClientProperty("SelectionAdapter2", null);

        unbindListener(property, component);

    }

    public static void unbindComponent(JComponent c) {


        Adapter a = (Adapter)c.getClientProperty("SelectionAdapter2");

        if(a != null) {

            a.unbind(c);

        }

    }

    protected Class getType() {
        return List.class;
    }

    protected Class getComponentType() {
        return CheckBoxList.class;
    }

    protected void updateUI(List<Integer> newValue) {
        getComponent().setCheckBoxListSelectedIndices((int[]) Utils.asArray((IndexedProperty<Integer>) getProperty(), Integer.TYPE));

    }

    public void valueChanged(ListSelectionEvent e) {
        callWhenUIChanged((List<Integer>) Utils.addToCollection(getComponent().getCheckBoxListSelectedIndices(),
                new ArrayList<Integer>()));

    }

    protected boolean isSelectionBind() {
        return true;
    }
}
