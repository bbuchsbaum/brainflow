package brainflow.app.presentation.controls;


import com.jidesoft.combobox.ColorComboBox;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 12, 2007
 * Time: 8:35:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairForm extends JPanel {


    private ColorComboBox colorChooser;

    private JCheckBox visibleBox = new JCheckBox("Crosshair Visible");

    private JSpinner lineWidthSpinner;

    private JSpinner lineLengthSpinner;

    private JSpinner gapSpinner;


    public CrosshairForm() {
        buildGUI();

    }

    private void buildGUI() {
        MigLayout layout = new MigLayout();
        setLayout(layout);
        colorChooser = new ColorComboBox();

        lineWidthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        gapSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1, .1));
        lineLengthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1, .1));

        add(visibleBox, "span 2, wrap 18px");

        add(new JLabel("Line Color:"));
        add(colorChooser, "gap left 30, width 100:120:200, wrap 15px");

        add(new JLabel("Line Width:"));
        add(lineWidthSpinner, "gap left 30, width 60::, growx, wrap 15px");

        add(new JLabel("Gap:"));
        add(gapSpinner, "gap left 30, width 60::, growx, wrap 15px");

        add(new JLabel("Length:"));
        add(lineLengthSpinner, "gap left 30, width 60::, growx");

        colorChooser.setSelectedColor(Color.GREEN);
        colorChooser.setColorValueVisible(false);


    }


 /*   private void _buildGUI() {


        FormLayout layout = new FormLayout("2dlu, l:p, 20dlu, r:p:g, 3dlu, 2dlu", "6dlu, p, 12dlu, p, 12dlu, p, 12dlu, p, 12dlu, p, 12dlu");
        setLayout(layout);


        CellConstraints cc = new CellConstraints();

        colorChooser = new ColorComboBox();

        lineWidthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        gapSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1, .1));
        lineLengthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1, .1));


        add(visibleBox, cc.xyw(2, 2, 3));

        add(new JLabel("Line Color:"), cc.xy(2, 4));
        add(colorChooser, cc.xyw(4, 4, 2));

        add(new JLabel("Line Width:"), cc.xy(2, 6));
        add(lineWidthSpinner, cc.xyw(4, 6, 2));

        add(new JLabel("Gap:"), cc.xy(2, 8));
        add(gapSpinner, cc.xyw(4, 8, 2));

        add(new JLabel("Length:"), cc.xy(2, 10));
        add(lineLengthSpinner, cc.xyw(4, 10, 2));

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        applyButton = new JButton("Apply");

        add(ButtonBarFactory.buildRightAlignedBar(okButton, cancelButton, applyButton),
                cc.xyw(2,12, 4));

        //colorChooser.setSelectedColor(Color.GREEN);
       // colorChooser.setColorValueVisible(false);


    } */




    public ColorComboBox getColorChooser() {
        return colorChooser;
    }

    public JCheckBox getVisibleBox() {
        return visibleBox;
    }

    public JSpinner getLineWidthSpinner() {
        return lineWidthSpinner;
    }

    public JSpinner getGapSpinner() {
        return gapSpinner;
    }


    public JSpinner getLineLengthSpinner() {
        return lineLengthSpinner;
    }

    public static void main(String[] args) throws Exception {

        JFrame jf = new JFrame();
        jf.add(new CrosshairForm());
        jf.setVisible(true);
        jf.pack();
    }
}
