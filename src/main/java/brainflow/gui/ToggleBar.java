package brainflow.gui;

import com.jidesoft.swing.JideToggleButton;
import com.jidesoft.swing.JideButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 29, 2008
 * Time: 7:32:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ToggleBar extends JComponent implements ListDataListener {



    private ComboBoxModel model = new DefaultComboBoxModel();

    private EventListenerList listeners = new EventListenerList();


    private List<JideToggleButton> buttonList;

    private ButtonSelectionListener buttonListener;

    private ButtonGroup buttonGroup = new ButtonGroup();


    public ToggleBar(List items) {
        DefaultComboBoxModel dm = new DefaultComboBoxModel();

        for (Object obj : items) {
            dm.addElement(obj);
        }

        setModel(dm);



        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);

        setBorder(new EmptyBorder(0, 0, 0, 0));
        
        init();
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        listeners.add(ListSelectionListener.class, listener);
    }

    public void removeListSelectionListener(ListSelectionListener listener) {
        listeners.remove(ListSelectionListener.class, listener);
    }

    private boolean contains(Object item) {
        for (int i=0; i<model.getSize(); i++) {
            if (model.getElementAt(i) == item) {
                return true;
            }
        }

        return false;

    }

    private int indexOf(Object item) {
        int ret = -1;
        for (int i=0; i<model.getSize(); i++) {
            if (model.getElementAt(i) == item) {
                return i;
            }
        }

        return ret;
    }

    private void fireListSelectionEvent() {
        ListSelectionListener[] lsl = listeners.getListeners(ListSelectionListener.class);
        for (ListSelectionListener l : lsl) {
            l.valueChanged(new ListSelectionEvent(this, indexOf(model.getSelectedItem()), indexOf(model.getSelectedItem()), false));
        }
    }

    public void setModel(ComboBoxModel _model) {
        model.removeListDataListener(this);
        this.model = _model;
        init();
        layoutButtons();
    }

    private void layoutButtons() {
        if (getComponentCount() > 0) {
            removeAll();
        }

        buttonList = new ArrayList<JideToggleButton>();
        buttonListener = new ButtonSelectionListener();
        int selindex = getSelectedIndex();

        for (int i = 0; i < model.getSize(); i++) {

            Object obj = model.getElementAt(i);

            JideToggleButton button = new JideToggleButton(obj.toString());
            button.setButtonStyle(JideButton.TOOLBAR_STYLE);
       
            buttonList.add(button);
            buttonGroup.add(button);
            button.addItemListener(buttonListener);
            add(button);
        }



        if (selindex >= 0) {
            buttonList.get(selindex).setSelected(true);
        }
        

        revalidate();

    }



    @Override
    public void intervalAdded(ListDataEvent e) {
       layoutButtons();
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        layoutButtons();
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        layoutButtons();
    }

    private void init() {


        model.addListDataListener(this);
        

    }



    public void setSelectedIndex(int index) {
        Object obj = model.getElementAt(index);
        if (obj != getSelectedItem()) {
            setSelectedItem(obj);
        }

    }

    public int getSelectedIndex() {
        return indexOf(model.getSelectedItem());
    }

    public Object getSelectedItem() {
        return model.getSelectedItem();
    }


    public void setSelectedItem(Object item) {
        if (item == getSelectedItem()) return;

        if (contains(item)) {
            
            model.setSelectedItem(item);
            buttonList.get(indexOf(getSelectedItem())).setSelected(true);


            fireListSelectionEvent();
        }

    }


    class ButtonSelectionListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {

            AbstractButton button = (AbstractButton) e.getSource();
            if (e.getStateChange() == ItemEvent.SELECTED) {

                int selIdx = buttonList.indexOf(button);
                setSelectedItem(model.getElementAt(selIdx));
                //ToggleBar.this.fireListSelectionEvent();
               
            }
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel bar = new JPanel();
       
        ToggleBar tbar = new ToggleBar(Arrays.asList("aaaa", "BBBB", "CCCCC"));
        bar.add(tbar);
        frame.add(bar, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
        tbar.setSelectedItem("CCCCC");
       

    }

    /*public void setSelectedItem(Object anObject) {
        Object oldSelection = selectedItemReminder;
        Object objectToSelect = anObject;
        if (oldSelection == null || !oldSelection.equals(anObject)) {

            if (anObject != null && !isEditable()) {
                // For non editable combo boxes, an invalid selection
                // will be rejected.
                boolean found = false;
                for (int i = 0; i < dataModel.getSize(); i++) {
                    Object element = dataModel.getElementAt(i);
                    if (anObject.equals(element)) {
                        found = true;
                        objectToSelect = element;
                        break;
                    }
                }
                if (!found) {
                    return;
                }
            }

            // Must toggle the state of this flag since this method
            // call may result in ListDataEvents being fired.
            selectingItem = true;
            dataModel.setSelectedItem(objectToSelect);
            selectingItem = false;

            if (selectedItemReminder != dataModel.getSelectedItem()) {
                // in case a users implementation of ComboBoxModel
                // doesn't fire a ListDataEvent when the selection
                // changes.
                selectedItemChanged();
            }
        }
        fireActionEvent();
    }  */


}
