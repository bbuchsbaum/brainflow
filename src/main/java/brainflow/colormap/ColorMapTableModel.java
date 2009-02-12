package brainflow.colormap;

import brainflow.utils.IRange;
import brainflow.utils.Range;

import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 13, 2006
 * Time: 12:13:48 PM
 * To change this template use File | Settings | File Templates.
 */


public class ColorMapTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Range", "Color", "Opacity"};


    private DiscreteColorMap colorMap;
    private IColorMap sourceMap;

    public ColorMapTableModel(IColorMap _sourceMap) {
        sourceMap = _sourceMap;
        colorMap = new DiscreteColorMap(sourceMap);
    }


    public DiscreteColorMap getEditedColorMap() {
        return colorMap;
    }

    public IColorMap getSourceColorMap() {
        return sourceMap;
    }


    public IColorMap setTableSize(int newSize) {
        int oldSize = colorMap.getMapSize();
        if (newSize < oldSize) {
            colorMap = colorMap.shrink(oldSize - newSize);
            fireTableStructureChanged();
        } else {
            colorMap = colorMap.grow(newSize - oldSize);
            fireTableStructureChanged();

            //colorMap.setMapSize(newSize);
            //fireTableStructureChanged();
            //fireTableDataChanged();
        }

        return colorMap;
    }


    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return colorMap.getMapSize();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        ColorInterval interval = colorMap.getInterval(row);
        if (col == 0) {
            return new Range(interval.getMinimum(), interval.getMaximum());
        } else if (col == 1) {
            return interval.getColor();
        } else if (col == 2) {
            return interval.getAlpha();
        } else {
            throw new IllegalArgumentException();
        }

    }

    /*
    * JTable uses this method to determine the default renderer/
    * editor for each cell.  If we didn'three implement this method,
    * then the last column would contain text ("true"/"false"),
    * rather than a check box.
    */
    public Class getColumnClass(int c) {
        if (c == 0) {
            return IRange.class;
        } else if (c == 1) {
            return Color.class;
        } else if (c == 2) {
            return Integer.class;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /*
    * Don'three need to implement this method unless your table's
    * editable.
    */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.

        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        ColorInterval interval = colorMap.getInterval(row);
        if (col == 0) {
            IRange range = (IRange) value;
            Interval current = colorMap.getInterval(row);
            if (current == null || (range.getMin() > current.getMaximum()) || (range.getMax() < current.getMinimum())) {
                return;
            }

            colorMap.replaceInterval(row, range.getMin(), range.getMax(), interval.getColor());
            fireTableCellUpdated(row, 0);

            //
        } else if (col == 1) {
            Color clr = (Color) value;
            colorMap.setColor(row, clr);
            fireTableCellUpdated(row, col);

        } else if (col == 2) {
            int opacity = Math.min((Integer) value, 255);
            opacity = Math.max(0, opacity);
            Color newColor = new Color(interval.getRed(), interval.getGreen(), interval.getBlue(), opacity);
            colorMap.setColor(row, newColor);
            fireTableCellUpdated(row, col);
            fireTableCellUpdated(row, 2);
        }


    }

    public void clear() {

        fireTableDataChanged();
    }

    private void printDebugData() {

    }
}
