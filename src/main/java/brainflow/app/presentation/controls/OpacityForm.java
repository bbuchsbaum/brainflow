/*
 * OpacityPanel.java
 *
 * Created on July 12, 2006, 1:16 PM
 */

package brainflow.app.presentation.controls;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;

/**
 * @author buchs
 */
public class OpacityForm extends javax.swing.JPanel {

    private javax.swing.JLabel opacityLabel;

    private javax.swing.JSlider opacitySlider;

    private javax.swing.JLabel valueLabel;

    private javax.swing.JCheckBox visibleCheckBox;

    private FormLayout layout;

    /**
     * Creates new form OpacityPanel
     */
    public OpacityForm() {
        buildGUI();
    }

    public JLabel getValueLabel() {
        return valueLabel;
    }

    public JSlider getOpacitySlider() {
        return opacitySlider;
    }

    public JCheckBox getVisibleCheckBox() {
        return visibleCheckBox;
    }


    private void buildGUI() {
        layout = new FormLayout("6dlu, p, p, 6dlu:g, 6dlu", "10dlu, p, 3dlu, p, 10dlu");
        setLayout(layout);
        CellConstraints cc = new CellConstraints();

        opacityLabel = new JLabel("Opacity: ");
        opacitySlider = new JSlider(JSlider.HORIZONTAL);
        valueLabel = new JLabel("");
        visibleCheckBox = new JCheckBox("visible");

        add(opacityLabel, cc.xy(2,2));
        add(valueLabel, cc.xy(3,2));
        add(opacitySlider, cc.xywh(2, 4, 3, 1));

    }

}
