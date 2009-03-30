package brainflow.app.presentation.wizards;

import brainflow.gui.AbstractPresenter;
import brainflow.image.data.CoordinateSet3D;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.CellEditorManager;
import com.jidesoft.grid.CellRendererManager;
import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.grid.TableUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 15, 2007
 * Time: 6:04:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateTableView extends AbstractPresenter {


    private CellStyleTable table;

    private CoordinateTableSetupInfo info;

    private CoordinateTableModel tableModel;

    public CoordinateTableView(CoordinateTableSetupInfo _info) {

        info = _info;

        tableModel = new CoordinateTableModel();
        table = new CellStyleTable(tableModel);
        CellEditorManager.initDefaultEditor();
        CellRendererManager.initDefaultRenderer();
        ObjectConverterManager.initDefaultConverter();

        table.setColumnSelectionAllowed(true);

        table.setRowHeight(18);
        table.setColumnAutoResizable(true);
        table.setDefaultEditor(Color.class, (TableCellEditor) CellEditorManager.getEditor(Color.class));
        table.setDefaultRenderer(Color.class, CellRendererManager.getRenderer(Color.class));
        //table.setAutoResizeMode(JideTable.AUTO_RESIZE_ALL_COLUMNS);
        TableUtils.autoResizeAllColumns(table);

        //table.setDefaultRenderer(Color.class,
        //        new ColorCellRenderer(true));


    }

    public JComponent getComponent() {

        return table;
    }

    class CoordinateTableModel extends AbstractTableModel {

        CoordinateSet3D ctable;


        private String[] names = {"X", "Y", "Z", "Value", "Size", "Opacity", "Color"};

        List<String> columnNames;


        public CoordinateTableModel() {
            ctable = new CoordinateSet3D(info.getCoordinateSpace(), new double[info.getTableSize()][3], info.getDefaultValue(), info.getDefaultRadius());
            columnNames = Arrays.asList(names);
        }

        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Double.class;  // zero
                case 1:
                    return Double.class;  // zero
                case 2:
                    return Double.class;  // one
                case 3:
                    return Double.class;  // value
                case 4:
                    return Double.class;  // radius
                case 5:
                    return Double.class; // opacity
                case 6:
                    return Color.class;  // color
                default:
                    throw new AssertionError();
            }

        }

        public int getRowCount() {
            return ctable.getRows();
        }

        public int getColumnCount() {
            return 7;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        public int findColumn(String columnName) {
            return columnNames.indexOf(columnName);

        }

        @Override
        public String getColumnName(int column) {
            return columnNames.get(column);

        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ctable.getXCoordinate(rowIndex);
                case 1:
                    return ctable.getYCoordinate(rowIndex);
                case 2:
                    return ctable.getZCoordinate(rowIndex);
                case 3:
                    return ctable.getValue(rowIndex);
                case 4:
                    return ctable.getRadius(rowIndex);
                case 5:
                    return info.getDefaultOpacity();
                case 6:
                    return info.getDefaultColor();
                default:
                    throw new AssertionError();
            }


        }
    }
}
