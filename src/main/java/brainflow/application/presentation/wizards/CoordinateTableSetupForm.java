package brainflow.application.presentation.wizards;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jidesoft.combobox.ColorComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 15, 2007
 * Time: 3:25:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateTableSetupForm extends JPanel {


    private FormLayout layout;

    private JSpinner tableSizeSpinner;

    private JSpinner opacitySpinner;

    private JSpinner radiusSpinner;


    private ColorComboBox colorChooser;

    public CoordinateTableSetupForm() {
        buildGUI();
    }

    private void buildGUI() {

        layout = new FormLayout("6dlu, l:p, 12dlu, 1dlu, l:max(p;80dlu), 3dlu", "6dlu, p, 6dlu, p, 6dlu, p, 6dlu, p, 6dlu, p, 6dlu");
        CellConstraints cc = new CellConstraints();
        layout.addGroupedColumn(4);
        setLayout(layout);

        tableSizeSpinner = new JSpinner();
        add(new JLabel("Number of Coordinates:"), cc.xy(2,2));
        add(tableSizeSpinner, cc.xyw(4,2,2));

        opacitySpinner = new JSpinner();
        add(new JLabel("Default Opacity:"), cc.xy(2,4));
        add(opacitySpinner, cc.xyw(4,4,2));

        radiusSpinner = new JSpinner();
        add(new JLabel("Default Radius:"), cc.xy(2,6));
        add(radiusSpinner, cc.xyw(4,6,2));


        colorChooser = new MyColorComboBox();
        add(new JLabel("Default Color:"), cc.xy(2,8));
        add(colorChooser, cc.xyw(4,8,2));


    }

    public JSpinner getTableSizeSpinner() {
        return tableSizeSpinner;
    }

    public JSpinner getOpacitySpinner() {
        return opacitySpinner;
    }

    public ColorComboBox getColorChooser() {
        return colorChooser;
    }

    public JSpinner getRadiusSpinner() {
        return radiusSpinner;
    }

    public class MyColorComboBox extends ColorComboBox {

        public MyColorComboBox() {
            this.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        firePropertyChange("selectedColor", null, (Color)e.getItem());

                    }
                }
            });
        }

        public void setSelectedColor(Color color) {
            Color old = getSelectedColor();
            super.setSelectedColor(color);

         
            firePropertyChange("selectedColor", old, getSelectedColor());
        }





       
    }
}
