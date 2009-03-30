package brainflow.app.presentation.controls;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 15, 2007
 * Time: 1:23:25 PM
 */
public class ColorBarAnnotationForm extends JPanel {

    private FormLayout layout;


    private JCheckBox visibleBox = new JCheckBox("Color Bar Visible");

    private JSpinner barLengthSpinner;

    private JSpinner barSizeSpinner;

    private JSpinner marginSpinner;

    private JComboBox orientationComboBox;


    public ColorBarAnnotationForm() {
        buildGUI();
    }

    private void buildGUI() {


        layout = new FormLayout("2dlu, l:p, 20dlu, r:p:g, 3dlu, 2dlu", "6dlu, p, 12dlu, p, 12dlu, p, 12dlu, p, 12dlu, p, 12dlu");
        setLayout(layout);


        CellConstraints cc = new CellConstraints();


        barLengthSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 1, .05));
        barSizeSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 100, 5));
        marginSpinner = new JSpinner(new SpinnerNumberModel(20, 2, 50, 1));
        orientationComboBox = new JComboBox(new String[]{"Vertical", "Horizontal"});


        add(visibleBox, cc.xyw(2, 2, 3));

        add(new JLabel("Orientation:"), cc.xy(2, 4));
        add(orientationComboBox, cc.xyw(4, 4, 2));

        add(new JLabel("Margin:"), cc.xy(2, 6));
        add(marginSpinner, cc.xyw(4, 6, 2));

        add(new JLabel("Bar Length:"), cc.xy(2, 8));
        add(barLengthSpinner, cc.xyw(4, 8, 2));

        add(new JLabel("Bar Size:"), cc.xy(2, 10));
        add(barSizeSpinner, cc.xyw(4, 10, 2));


    }


    public JCheckBox getVisibleBox() {
        return visibleBox;
    }

    public JSpinner getBarLengthSpinner() {
        return barLengthSpinner;
    }

    public JSpinner getBarSizeSpinner() {
        return barSizeSpinner;
    }

    public JSpinner getMarginSpinner() {
        return marginSpinner;
    }

    public JComboBox getOrientationComboBox() {
        return orientationComboBox;
    }
}
