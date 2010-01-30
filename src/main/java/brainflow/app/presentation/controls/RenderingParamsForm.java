/*
 * OpacityPanel.java
 *
 * Created on July 12, 2006, 1:16 PM
 */

package brainflow.app.presentation.controls;


import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

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

    private LayoutManager layout;

    /**
     * Creates new form OpacityPanel
     */
    public RenderingParamsForm() {
        buildGUI();
    }

   // public JComboBox getInterpolationChoices() {
  //      return interpolationChoices;
  //  }

    //public JLabel getInterpolationLabel() {
    //    return interpolationLabel;
    //}

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
        layout = new MigLayout("", "[][grow]", "[][]");
        setLayout(layout);
        opacityLabel = new JLabel("Opacity: ");
        opacitySlider = new JSlider(JSlider.HORIZONTAL);
        opacityValueLabel = new JLabel("");

        smoothingLabel = new JLabel("Smoothing Radius: ");
        smoothingSlider = new JSlider(JSlider.HORIZONTAL);
        //smoothingSlider.setValue(0);
        smoothingValueLabel = new JLabel("");

        add(opacityLabel);
        add(opacityValueLabel,"wrap");
        add(opacitySlider, "width 80:250:400, growx, span 2, wrap");

        add(smoothingLabel);
        add(smoothingValueLabel,"wrap");
        add(smoothingSlider, "width 80:250:400, growx, span 2, wrap");

        //interpolationLabel = new JLabel("Interpolation: ");
        //add(interpolationLabel);

        //interpolationChoices = new JComboBox();
        //add(interpolationChoices, "growx, span 2");

    }

   

}