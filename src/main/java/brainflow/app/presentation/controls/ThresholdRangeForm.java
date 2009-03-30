package brainflow.app.presentation.controls;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 8, 2007
 * Time: 9:52:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThresholdRangeForm extends JPanel {


    private FormLayout layout;

    private JSlider slider1;

    private JSlider slider2;

    private JLabel sliderLabel1;

    private JLabel sliderLabel2;


    private JFormattedTextField valueField1;

    private JFormattedTextField valueField2;

    private JCheckBox inclusiveCheckBox;

    private JCheckBox symmetricalCheckBox;


    public ThresholdRangeForm() {
        buildGUI();
    }

    public JSlider getSlider1() {
        return slider1;
    }

    public JSlider getSlider2() {
        return slider2;
    }


    public JLabel getSliderLabel1() {
        return sliderLabel1;
    }

    public JLabel getSliderLabel2() {
        return sliderLabel2;
    }


    public JFormattedTextField getValueField1() {
        return valueField1;
    }

    public JFormattedTextField getValueField2() {
        return valueField2;
    }

    public JCheckBox getInclusiveCheckBox() {
        return inclusiveCheckBox;
    }

    public void setInclusiveCheckBox(JCheckBox inclusiveCheckBox) {
        this.inclusiveCheckBox = inclusiveCheckBox;
    }

    public JCheckBox getSymmetricalCheckBox() {
        return symmetricalCheckBox;
    }

    public void setSymmetricalCheckBox(JCheckBox symmetricalCheckBox) {
        this.symmetricalCheckBox = symmetricalCheckBox;
    }

    private void buildGUI() {

        //layout = new FormLayout("6dlu, p, 3dlu, max(p;40dlu), p:g, 5dlu, 3dlu", "6dlu, p, 3dlu, p, 8dlu, p, 3dlu, p, 6dlu");

        layout = new FormLayout("6dlu, p, 3dlu, max(p;40dlu), p:g, 5dlu, 3dlu",
                "6dlu, p, 3dlu, p, 8dlu, p, 3dlu, p, 8dlu, p, 6dlu");
        setLayout(layout);

        slider1 = new JSlider();
        slider2 = new JSlider();

        sliderLabel1 = new JLabel("X:");
        sliderLabel2 = new JLabel("Y:");

        NumberFormat format = NumberFormat.getInstance();

        format.setMaximumFractionDigits(2);
        format.setMinimumIntegerDigits(1);


        valueField1 = new JFormattedTextField(format);
        valueField1.setText("   ");
        valueField2 = new JFormattedTextField(format);
        valueField2.setText("   ");

        CellConstraints cc = new CellConstraints();

        add(slider1, cc.xywh(2, 4, 4, 1));
        add(slider2, cc.xywh(2, 8, 4, 1));

        add(sliderLabel1, cc.xy(2, 2));
        add(sliderLabel2, cc.xy(2, 6));

        add(valueField1, cc.xy(4, 2));
        add(valueField2, cc.xy(4, 6));

        //inclusiveCheckBox = new JCheckBox("inclusive");

        //add(inclusiveCheckBox, cc.xyw(2, 10, 3));

        //symmetricalCheckBox = new JCheckBox("symmetrical");
        //add(symmetricalCheckBox, cc.xy(5, 10));


    }


}



