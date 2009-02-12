package brainflow.gui;

import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jidesoft.swing.JideToggleButton;
import com.jidesoft.swing.SplitButtonGroup;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 28, 2005
 * Time: 10:57:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class ButtonList extends JComponent implements ListDataListener {


    private ListSelectionModel selectionModel;

    private ListModel dataModel;

    private ListSelectionListener selectionListener;

    private List<AbstractButton> buttons = new ArrayList<AbstractButton>();

    private ButtonGroup buttonGroup = new SplitButtonGroup();

    private ButtonFactory buttonFactory;


    public ButtonList(ListModel dataModel, ButtonFactory buttonFactory) {
        if (dataModel == null) {
            throw new IllegalArgumentException("dataModel must be non null");
        }

        // Register with the ToolTipManager so that tooltips from the
        // renderer show through.
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.registerComponent(this);


        this.dataModel = dataModel;
        this.buttonFactory = buttonFactory;

        dataModel.addListDataListener(this);
        selectionModel = createSelectionModel();

        selectionListener = new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {

                int idx = selectionModel.getMinSelectionIndex();
                buttonGroup.setSelected(buttons.get(idx).getModel(), true);

            }
        };

        selectionModel.addListSelectionListener(selectionListener);

        initComponents();
    }

    protected void initComponents() {

        int selIndex = selectionModel.getMinSelectionIndex();

        removeAll();
        buttons.clear();

        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        setLayout(layout);

        for (int i = 0; i < dataModel.getSize(); i++) {

            AbstractButton button = buttonFactory.createButton(dataModel.getElementAt(i));

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AbstractButton ab = (AbstractButton) e.getSource();
                    int index = buttons.indexOf(ab);
                    selectionModel.setSelectionInterval(index, index);
                    ButtonList.this.fireSelectionValueChanged(index, index, false);
                }
            });


            buttonGroup.add(button);
            if (i == selIndex) {
                buttonGroup.setSelected(button.getModel(), true);
            }
            buttons.add(button);
            add(button);
        }


        validate();

    }


    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }

    public int getSelectedIndex() {
        return selectionModel.getMinSelectionIndex();
    }


    public void setSelectionModel(ListSelectionModel selectionModel) {
        if (selectionModel == null) {
            throw new IllegalArgumentException("selectionModel must be non null");
        }

        /* Remove the forwarding ListSelectionListener from the old
         * selectionModel, and add it to the new zero, if necessary.
         */


        if (selectionListener != null) {
            this.selectionModel.removeListSelectionListener(selectionListener);
            selectionModel.addListSelectionListener(selectionListener);
        }

        ListSelectionModel oldValue = this.selectionModel;

        this.selectionModel = selectionModel;

        //int idx = selectionModel.getMinSelectionIndex();


        firePropertyChange("selectionModel", oldValue, selectionModel);
    }


    protected void fireSelectionValueChanged(int firstIndex, int lastIndex,
                                             boolean isAdjusting) {
        Object[] listeners = listenerList.getListenerList();
        ListSelectionEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListSelectionListener.class) {
                if (e == null) {
                    e = new ListSelectionEvent(this, firstIndex, lastIndex,
                            isAdjusting);
                }
                ((ListSelectionListener) listeners[i + 1]).valueChanged(e);
            }
        }
    }

    public void intervalAdded(ListDataEvent e) {
        initComponents();
    }

    public void intervalRemoved(ListDataEvent e) {
        initComponents();
    }

    public void contentsChanged(ListDataEvent e) {
        initComponents();
    }


    private class ListSelectionHandler implements ListSelectionListener, Serializable {
        public void valueChanged(ListSelectionEvent e) {
            fireSelectionValueChanged(e.getFirstIndex(),
                    e.getLastIndex(),
                    e.getValueIsAdjusting());
        }
    }


    public void addListSelectionListener(ListSelectionListener listener) {
        if (selectionListener == null) {
            selectionListener = new ListSelectionHandler();
            getSelectionModel().addListSelectionListener(selectionListener);
        }

        listenerList.add(ListSelectionListener.class, listener);
    }


    public void removeListSelectionListener(ListSelectionListener listener) {
        listenerList.remove(ListSelectionListener.class, listener);
    }


    public ListSelectionListener[] getListSelectionListeners() {
        return (ListSelectionListener[]) listenerList.getListeners(
                ListSelectionListener.class);
    }


    protected ListSelectionModel createSelectionModel() {
        return new DefaultListSelectionModel();
    }

    public static void main(String[] args) {
        JFrame jf1 = new JFrame("hello");
        JToolBar toolbar = new JToolBar();

        //final BrainCanvasModel canvasModel = new BrainCanvasModel();
        //canvasModel.addImageView(new SimpleImageView(new ImageDisplayModel("View 1")));
        //canvasModel.addImageView(new SimpleImageView(new ImageDisplayModel("View 2")));
        //canvasModel.addImageView(new SimpleImageView(new ImageDisplayModel("View 3")));

        final DefaultListModel canvasModel = new DefaultListModel();
        canvasModel.addElement("A1");
        canvasModel.addElement("A2");
        canvasModel.addElement("A3");


        SelectionInList tmp = new SelectionInList(canvasModel);

        ButtonFactory factory = new ButtonFactory() {
            public AbstractButton createButton(Object elem) {
                return new JideToggleButton(elem.toString());
            }

        };
        ButtonList blist = new ButtonList(tmp, factory);


        blist.setSelectionModel(new SingleListSelectionAdapter(tmp.getSelectionIndexHolder()));

        toolbar.add(blist);


        JPanel panel = new JPanel();
        final JTextField field = new JTextField();
        field.setColumns(15);
        JButton jb = new JButton("Add");

        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String newtext = field.getText();
                canvasModel.addElement(newtext);
            }
        });

        panel.add(new JLabel("Type text here: "));
        panel.add(field);
        panel.add(jb);


        JPanel mainPanel = new JPanel();
        JList list = new JList(tmp);
        list.setSelectionModel(new SingleListSelectionAdapter(tmp.getSelectionIndexHolder()));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panel, BorderLayout.NORTH);
        mainPanel.add(list, BorderLayout.CENTER);

        jf1.add(toolbar, BorderLayout.NORTH);
        jf1.add(mainPanel, BorderLayout.CENTER);
        jf1.setSize(800, 800);
        jf1.setVisible(true);


    }


}
