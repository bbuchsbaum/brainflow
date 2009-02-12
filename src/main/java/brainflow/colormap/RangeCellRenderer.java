package brainflow.colormap;

import brainflow.utils.IRange;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 13, 2006
 * Time: 1:01:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class RangeCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    private NumberFormat format = NumberFormat.getNumberInstance();

    public RangeCellRenderer() {
        super();

    }

    public Component getTableCellRendererComponent(
            JTable table, Object range,
            boolean isSelected, boolean hasFocus,
            int row, int column) {


        IRange curRange = (IRange) range;
        Component c = super.getTableCellRendererComponent(table, range, isSelected, hasFocus, row, column);
        setText(format.format(curRange.getMin()) + ", " + format.format(curRange.getMax()));
        return c;
    }
}
