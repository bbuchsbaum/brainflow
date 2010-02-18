package brainflow.app.presentation.controls;

import brainflow.colormap.ColorTableCellRenderer;
import com.jidesoft.combobox.ColorComboBox;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideScrollPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 8, 2010
 * Time: 7:58:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class ColorSequenceBuilder extends JPanel {

    private JideTable colorTable = new CellStyleTable();

    private ColorComboBox colorSelector = new ColorComboBox();

   // private DefaultListModel colorListModel = new DefaultListModel();

    DefaultTableModel colorTableModel = new DefaultTableModel();


    public ColorSequenceBuilder() {

        CellEditorManager.initDefaultEditor();
        CellRendererManager.initDefaultRenderer();
        ObjectConverterManager.initDefaultConverter();
        colorTable.setDefaultEditor(Color.class, (TableCellEditor) CellEditorManager.getEditor(Color.class));
        
        setLayout(new MigLayout());


        add(new JScrollPane(colorTable), "grow, width 100:200:500, height 100:250:, wrap");
        add(colorSelector, "growx, wrap");



        colorTableModel = createTableModel();
        colorTable.setModel(colorTableModel);



        colorSelector.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == ColorComboBox.PROPERTY_SELECTED_ITEM) {
                    Color clr = (Color)evt.getNewValue();
                    //colorListModel.addElement(clr);
                    colorTableModel.addRow(new Object[] { clr });
                }
            }
        });



        colorTable.setDefaultRenderer(Color.class,
                new ColorCellRenderer());


    }

    private DefaultTableModel createTableModel() {
        DefaultTableModel colorTableModel = new DefaultTableModel() {
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return Color.class;
                    }
                };
                colorTableModel.addColumn("Colors");
        return colorTableModel;


    }

    public void clear() {
        colorTableModel = createTableModel();
        colorTable.setModel(colorTableModel);
    }

    public java.util.List<Color> getColorSequence() {
        java.util.List<Color> ret = new ArrayList<Color>();
        for (int i=0; i<colorTableModel.getRowCount(); i++) {
            ret.add((Color)colorTableModel.getValueAt(i,0));
        }

        return ret;
    }


    public static void main(String[] args) {
        
        LookAndFeelFactory.installJideExtension();
        JFrame jf = new JFrame();
        jf.add(new ColorSequenceBuilder());
        jf.pack();
        jf.setVisible(true);
    }
}
