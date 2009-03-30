package brainflow.app.presentation.controls;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 1:44:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TripleSliderForm extends JPanel {

    private FormLayout layout;

    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;

    private JLabel sliderLabel1;
    private JLabel sliderLabel2;
    private JLabel sliderLabel3;

    private JFormattedTextField valueField1;
    private JFormattedTextField valueField2;
    private JFormattedTextField valueField3;

    public TripleSliderForm() {
        buildGUI();

    }


    public JSlider getSlider1() {
        return slider1;
    }

    public JSlider getSlider2() {
        return slider2;
    }

    public JSlider getSlider3() {
        return slider3;
    }

    public JLabel getSliderLabel1() {
        return sliderLabel1;
    }

    public JLabel getSliderLabel2() {
        return sliderLabel2;
    }

    public JLabel getSliderLabel3() {
        return sliderLabel3;
    }

    public JFormattedTextField getValueLabel1() {
        return valueField1;
    }

    public JFormattedTextField getValueLabel2() {
        return valueField2;
    }

    public JFormattedTextField getValueLabel3() {
        return valueField3;
    }

    private void buildGUI() {
        layout = new FormLayout("6dlu, l:min(75dlu;p):g, 3dlu, 5dlu, l:max(p;30dlu), 3dlu, 3dlu", "5dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 5dlu");
        setLayout(layout);

        slider1 = new JSlider();
        slider2 = new JSlider();
        slider3 = new JSlider();

        sliderLabel1 = new JLabel("X:");
        sliderLabel2 = new JLabel("Y:");
        sliderLabel3 = new JLabel("Z:");


        NumberFormat format = NumberFormat.getInstance();

        //format.setMaximumFractionDigits(1);
        //format.setMinimumIntegerDigits(2);

        valueField1 = new JFormattedTextField(format);
        valueField1.setText("0000");
        valueField2 = new JFormattedTextField(format);
        valueField2.setText("0000");
        valueField3 = new JFormattedTextField(format);
        valueField3.setText("0000");

        CellConstraints cc = new CellConstraints();


        add(slider1, cc.xywh(2, 4, 2, 1));
        add(slider2, cc.xywh(2, 8, 2, 1));
        add(slider3, cc.xywh(2, 12, 2, 1));

        add(sliderLabel1, cc.xy(2, 2));
        add(sliderLabel2, cc.xy(2, 6));
        add(sliderLabel3, cc.xy(2, 10));

        add(valueField1, cc.xywh(5, 4, 2, 1));
        add(valueField2, cc.xywh(5, 8, 2, 1));
        add(valueField3, cc.xywh(5, 12, 2, 1));
        layout.addGroupedColumn(5);

        //layout.addGroupedRow(2);


    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.add(new TripleSliderForm(), BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }


}
