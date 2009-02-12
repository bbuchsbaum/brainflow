package brainflow.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 25, 2008
 * Time: 3:07:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiSelectToggleBar extends JComponent implements ListDataListener {


    private ListModel model;

    private EventListenerList listeners = new EventListenerList();

    private List<JToggleButton> buttonList;

    private SortedSet<Integer> selectedIndices = new TreeSet<Integer>();

    private ButtonSelectionListener buttonListener;




    public MultiSelectToggleBar(List items) {
        DefaultListModel lm = new DefaultListModel();

        for (Object obj : items) {
            lm.addElement(obj);
        }


        setModel(lm);



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
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i) == item) {
                return true;
            }
        }

        return false;

    }

    private int indexOf(Object item) {
        int ret = -1;
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i) == item) {
                return i;
            }
        }

        return ret;
    }

    private void fireListSelectionEvent() {
        ListSelectionListener[] lsl = listeners.getListeners(ListSelectionListener.class);
        for (ListSelectionListener l : lsl) {
            if (selectedIndices.size() == 0) {
                l.valueChanged(new ListSelectionEvent(this, -1, -1, false));
            } else {
                l.valueChanged(new ListSelectionEvent(this, selectedIndices.first(), selectedIndices.last(), false));
            }
        }
    }

    public void setModel(ListModel _model) {
        if (model != null) {
            model.removeListDataListener(this);
        }


        model = _model;
        init();
        layoutButtons();
    }

    private boolean isControlDown = false;

    private void layoutButtons() {
        if (getComponentCount() > 0) {
            removeAll();
        }

        buttonList = new ArrayList<JToggleButton>();
        buttonListener = new ButtonSelectionListener();
       // int selindex = getSelectedIndex();

        for (int i = 0; i < model.getSize(); i++) {

            Object obj = model.getElementAt(i);

            JToggleButton button = new JToggleButton(obj.toString());

            buttonList.add(button);

            button.addItemListener(buttonListener);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isControlDown()) {
                        isControlDown = true;
                    } else {
                        isControlDown = false;
                    }
                }
            });
            add(button);
        }


        //if (selindex >= 0) {
        //    buttonList.get(selindex).setSelected(true);
        //}


        revalidate();

    }

    private void updateButtons() {
        for (int i=0; i<buttonList.size(); i++) {
            JToggleButton button = buttonList.get(i);
            if (selectedIndices.contains(i) && !button.isSelected()) {
                button.setSelected(true);
            } else if (!selectedIndices.contains(i) && button.isSelected()) {
                button.setSelected(false);
            }
        }
    }

    public void intervalAdded(ListDataEvent e) {
        layoutButtons();
    }

    public void intervalRemoved(ListDataEvent e) {
        layoutButtons();
    }

    public void contentsChanged(ListDataEvent e) {
        layoutButtons();
    }

    private void init() {

        model.addListDataListener(this);


    }

    public void addSelectedIndex(int index) {
        if (index >= model.getSize() || index < 0) {
            throw new IllegalArgumentException("illegal index : " + index);
        }

        selectedIndices.add(index);
        updateButtons();

        fireListSelectionEvent();


    }

    public void removeSelectedIndex(int index) {
        if (index >= model.getSize() || index < 0) {
            throw new IllegalArgumentException("illegal index : " + index);
        }

        selectedIndices.add(index);
        updateButtons();

        fireListSelectionEvent();


    }


    public void setSelectedIndices(List<Integer> indices) {
        TreeSet<Integer> other = new TreeSet<Integer>(indices);

        if (selectedIndices.equals(other)) {
            return;
        }

        selectedIndices.clear();

        for (Integer idx : other) {
            if (idx < 0 || idx >= model.getSize()) {
                throw new IllegalArgumentException("index " + idx + " out of bounds");
            }

            selectedIndices.add(idx);
        }

        updateButtons();

        fireListSelectionEvent();

    }

    public void setSelectedIndex(int index) {
        if (index >= model.getSize() || index < 0) {
            throw new IllegalArgumentException("illegal index : " + index);
        }

        selectedIndices.clear();
        selectedIndices.add(index);
        updateButtons();

        fireListSelectionEvent();

    }

    public List<Integer> getSelectedIndices() {
        List<Integer> ret = new ArrayList<Integer>();
        Iterator<Integer> iter = selectedIndices.iterator();

        while(iter.hasNext()) {
            ret.add(iter.next());
        }

        return ret;
    }

    public List<?> getSelectedItems() {
        List<Object> items = new ArrayList<Object>();

        for (Integer selectedIndice : selectedIndices) {
            items.add(model.getElementAt(selectedIndice));
        }

        return items;


    }


    public void setSelectedItem(Object item) {
       int i = indexOf(item);
       if (i == -1) {
           throw new IllegalArgumentException("item not member of list, cannot select it : " + item);
       }

        selectedIndices.clear();
        selectedIndices.add(i);
        updateButtons();
        
        fireListSelectionEvent();


    }


    class ButtonSelectionListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {

            AbstractButton button = (AbstractButton) e.getSource();
            System.out.println("state change : " + e.getStateChange());
            if (e.getStateChange() == ItemEvent.SELECTED) {
                
                System.out.println("item selected!");
                int selIdx = buttonList.indexOf(button);

                if (isControlDown) {
                    addSelectedIndex(selIdx);
                } else {
                    setSelectedIndex(selIdx);
                }


            } else {
                System.out.println("item not selected " + e);
            }
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel bar = new JPanel();

        MultiSelectToggleBar tbar = new MultiSelectToggleBar(Arrays.asList("1", "2", "3"));
        bar.add(tbar);
        frame.add(bar, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        tbar.setSelectedItem("1");


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
