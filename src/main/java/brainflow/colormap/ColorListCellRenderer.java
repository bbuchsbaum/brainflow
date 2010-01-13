package brainflow.colormap;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 8, 2010
 * Time: 8:16:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class ColorListCellRenderer extends DefaultListCellRenderer {

    private static final Map<Color, ImageIcon> colorIconMap = new HashMap<Color, ImageIcon>();

    @Override
    public Component getListCellRendererComponent(JList list, Object color, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel)super.getListCellRendererComponent(list,color,index,isSelected,cellHasFocus);
        ImageIcon icon = colorIconMap.get(color);

        int width = Math.max(list.getWidth(), 200);
        if (icon == null) {
            icon = ColorTable.createImageIcon((Color)color, width, label.getPreferredSize().height);
            colorIconMap.put((Color)color, icon);
        }

        label.setText("");
        label.setIcon(icon);

        return label;

    }
}
