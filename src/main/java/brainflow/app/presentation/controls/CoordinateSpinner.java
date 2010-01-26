package brainflow.app.presentation.controls;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyleRange;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 13, 2008
 * Time: 7:08:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateSpinner extends JPanel {

    private JSpinner xspinner;

    private JSpinner yspinner;

    private JSpinner zspinner;

    private JLabel xlabel;

    private JLabel ylabel;

    private JLabel zlabel;

    private StyledLabel xspinnerHeader;

    private StyledLabel yspinnerHeader;

    private StyledLabel zspinnerHeader;

    private LayoutManager layout;

    public CoordinateSpinner() {
        xlabel = new JLabel("X: ");
        ylabel = new JLabel("Y: ");
        zlabel = new JLabel("Z: ");

        xspinner = new JSpinner(new SpinnerNumberModel());
        yspinner = new JSpinner(new SpinnerNumberModel());
        zspinner = new JSpinner(new SpinnerNumberModel());

        xspinnerHeader = createLabel("X");
        yspinnerHeader = createLabel("Y");
        zspinnerHeader = createLabel("Z");

        layout2();


    }

    private void layout2() {
        layout = new MigLayout();
        setLayout(layout);
        add(xspinnerHeader, "cell 1 0");
        add(yspinnerHeader, "cell 3 0");
        add(zspinnerHeader, "cell 5 0");
        add(xlabel, "cell 0 1");
        add(ylabel, "cell 2 1");
        add(zlabel, "cell 4 1");
        add(xspinner, "cell 1 1, width 40:60:, growx");
        add(yspinner, "cell 3 1, width 40:60:, growx");
        add(zspinner, "cell 5 1, width 40:60:, growx");

    }

    private void layout1() {
        layout = new FormLayout("8dlu, l:p, 2dlu, 30dlu:g, 5dlu, l:p, 2dlu, 30dlu:g, 5dlu, l:p, 2dlu, 30dlu:g, 8dlu", "8dlu, pref, 2dlu, pref, 8dlu");
        setLayout(layout);



        CellConstraints cc = new CellConstraints();



        add(xspinnerHeader, cc.xy(4, 2));
        add(yspinnerHeader, cc.xy(8, 2));
        add(zspinnerHeader, cc.xy(12, 2));


        add(xlabel, cc.xy(2, 4));
        add(xspinner, cc.xy(4, 4));

        add(ylabel, cc.xy(6, 4));
        add(yspinner, cc.xy(8, 4));


        add(zlabel, cc.xy(10, 4));
        add(zspinner, cc.xy(12, 4));

    }

    private StyledLabel createLabel(String lab) {
        StyledLabel label = new StyledLabel(lab);
        label.setEnabled(false);
        label.addStyleRange(new StyleRange(Font.BOLD, Color.GRAY));
        return label;
    }

    public StyledLabel getXspinnerHeader() {
        return xspinnerHeader;
    }


    public StyledLabel getYspinnerHeader() {
        return yspinnerHeader;
    }


    public StyledLabel getZspinnerHeader() {
        return zspinnerHeader;
    }


    public JSpinner getXspinner() {
        return xspinner;
    }

    public JSpinner getYspinner() {
        return yspinner;
    }

    public JSpinner getZspinner() {
        return zspinner;
    }

    public JLabel getYlabel() {
        return ylabel;
    }

    public JLabel getXlabel() {
        return xlabel;
    }

    public JLabel getZlabel() {
        return zlabel;
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.add(new CoordinateSpinner(), BorderLayout.CENTER);

        jf.pack();
        jf.setVisible(true);
    }
}
