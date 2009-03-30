/*
 * OpacityPanel.java
 *
 * Created on July 12, 2006, 1:16 PM
 */

package brainflow.app.presentation.controls;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

/**
 * @author buchs
 */
public class RenderingParamsForm extends JPanel {

    private JLabel opacityLabel;

    private JSlider opacitySlider;

    private JSlider smoothingSlider;

    private JLabel opacityValueLabel;

    private JLabel smoothingLabel;

    private JLabel smoothingValueLabel;

    private JComboBox interpolationChoices;

    private JLabel interpolationLabel;

    private FormLayout layout;

    /**
     * Creates new form OpacityPanel
     */
    public RenderingParamsForm() {
        buildGUI();
    }

    public JComboBox getInterpolationChoices() {
        return interpolationChoices;
    }

    public JLabel getInterpolationLabel() {
        return interpolationLabel;
    }

    public JLabel getOpacityValueLabel() {
        return opacityValueLabel;
    }

    public JSlider getOpacitySlider() {
        return opacitySlider;
    }

    public JSlider getSmoothingSlider() {
        return smoothingSlider;
    }

    public JLabel getSmoothingLabel() {
        return smoothingLabel;
    }

    public JLabel getSmoothingValueLabel() {
        return smoothingValueLabel;
    }

    public JLabel getOpacityLabel() {
        return opacityLabel;
    }

    private void buildGUI() {
        layout = new FormLayout("6dlu, p, p, 6dlu:g, 6dlu", "8dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 6dlu, p, 8dlu");
        setLayout(layout);
        CellConstraints cc = new CellConstraints();

        opacityLabel = new JLabel("Opacity: ");
        opacitySlider = new JSlider(JSlider.HORIZONTAL);
        opacityValueLabel = new JLabel("");

        smoothingLabel = new JLabel("Smoothing Radius: ");
        smoothingSlider = new JSlider(JSlider.HORIZONTAL);
        //smoothingSlider.setValue(0);
        smoothingValueLabel = new JLabel("");

        add(opacityLabel, cc.xy(2, 2));
        add(opacityValueLabel, cc.xy(3, 2));
        add(opacitySlider, cc.xywh(2, 4, 3, 1));

        add(smoothingLabel, cc.xy(2, 6));
        add(smoothingValueLabel, cc.xy(3, 6));
        add(smoothingSlider, cc.xywh(2, 8, 3, 1));

        interpolationLabel = new JLabel("Interpolation: ");
        add(interpolationLabel, cc.xy(2, 10));

        interpolationChoices = new JComboBox();
        add(interpolationChoices, cc.xywh(2, 12, 3, 1));


    }

}