package brainflow.app.presentation.controls;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.RangeSlider;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 18, 2006
 * Time: 12:32:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class TripleRangeSliderForm extends JPanel {


    private RangeSlider slider1;
    private RangeSlider slider2;
    private RangeSlider slider3;

    private JLabel sliderLabel1;
    private JLabel sliderLabel2;
    private JLabel sliderLabel3;


    private JTextField leftField1;
    private JTextField leftField2;
    private JTextField leftField3;

    private JTextField rightField1;
    private JTextField rightField2;
    private JTextField rightField3;


    private FormLayout layout;

    public TripleRangeSliderForm() {

        initGUI();

    }


    private void initGUI() {
        layout = new FormLayout("6dlu, l:p, 3dlu, l:max(12dlu;p), 125dlu, r:max(12dlu;p), 6dlu",
                "6dlu, p,1dlu, p, 6dlu, p,1dlu, p, 6dlu, p, 1dlu, p, 6dlu");

        slider1 = new RangeSlider();
        slider2 = new RangeSlider();
        slider3 = new RangeSlider();

        slider1.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        slider2.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        slider3.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        leftField1 = new JTextField(3);
        leftField2 = new JTextField(3);
        leftField3 = new JTextField(3);

        rightField1 = new JTextField(3);
        rightField2 = new JTextField(3);
        rightField3 = new JTextField(3);

        leftField1.setEditable(false);
        leftField2.setEditable(false);
        leftField3.setEditable(false);
        rightField1.setEditable(false);
        rightField2.setEditable(false);
        rightField3.setEditable(false);

        sliderLabel1 = new JLabel("X:");
        sliderLabel2 = new JLabel("Y:");
        sliderLabel3 = new JLabel("Z:");


        setLayout(layout);

        CellConstraints cc = new CellConstraints();
        add(sliderLabel1, cc.xy(2, 4));
        add(sliderLabel2, cc.xy(2, 8));
        add(sliderLabel3, cc.xy(2, 12));

        add(leftField1, cc.xy(4, 2));
        add(slider1, cc.xywh(4, 4, 3, 1));
        add(rightField1, cc.xy(6, 2));
        add(leftField2, cc.xy(4, 6));
        add(slider2, cc.xywh(4, 8, 3, 1));
        add(rightField2, cc.xy(6, 6));
        add(leftField3, cc.xy(4, 10));
        add(slider3, cc.xywh(4, 12, 3, 1));
        add(rightField3, cc.xy(6, 10));

    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.add(new TripleRangeSliderForm(), BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }

}
