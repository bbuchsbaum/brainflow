package brainflow.colormap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 13, 2006
 * Time: 1:02:29 PM
 * To change this template use File | Settings | File Templates.
 */ /*
* The editor button that brings up the dialog.
* We extend DefaultCellEditor for convenience,
* even though it mean we have to create a dummy
* check box.  Another approach would be to copy
* the implementation of TableCellEditor methods
* from the source code for DefaultCellEditor.
*/

public class ColorCellEditor extends DefaultCellEditor {


    private Color currentColor = null;

    public ColorCellEditor(JButton b) {
        super(new JCheckBox()); //Unfortunately, the constructor
        //expects a check box, combo box,
        //or text field.
        editorComponent = b;
        setClickCountToStart(1); //This is usually 1 or 2.

        //Must do this so that editing stops when appropriate.
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }

    public Object getCellEditorValue() {
        return currentColor;
    }

    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        ((JButton) editorComponent).setText(value.toString());
        currentColor = (Color) value;
        return editorComponent;
    }
}
