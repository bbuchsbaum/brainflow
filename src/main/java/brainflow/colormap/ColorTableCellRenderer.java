package brainflow.colormap;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 13, 2006
 * Time: 1:00:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorTableCellRenderer extends JLabel implements TableCellRenderer {

    private Border unselectedBorder = null;

    private Border selectedBorder = null;

    private boolean isBordered = true;

    private static final Map<Color, ImageIcon> colorIconMap = new HashMap<Color, ImageIcon>();

    public ColorTableCellRenderer(boolean isBordered) {
        super();
        this.isBordered = isBordered;
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object color, boolean isSelected, boolean hasFocus,
            int row, int column) {


        //setBackground((Color) color);

        ImageIcon icon = colorIconMap.get(color);
        if (icon == null) {
            icon = ColorTable.createImageIcon((Color)color, 75, 25);
            colorIconMap.put((Color)color, icon);
        }
        setIcon(icon);


        if (isBordered) {
            if (isSelected) {
                if (selectedBorder == null) {
                    selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5,
                            table.getSelectionBackground());
                }
                setBorder(selectedBorder);
            } else {
                if (unselectedBorder == null) {
                    unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5,
                            table.getBackground());
                }
                setBorder(unselectedBorder);
            }
        }
        return this;
    }
}
