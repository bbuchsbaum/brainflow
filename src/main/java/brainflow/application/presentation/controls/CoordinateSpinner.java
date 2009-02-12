package brainflow.application.presentation.controls;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyleRange;

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

    private FormLayout layout;

    public CoordinateSpinner() {
        xlabel = new JLabel("X: ");
        ylabel = new JLabel("Y: ");
        zlabel = new JLabel("Z: ");

        xspinner = new JSpinner(new SpinnerNumberModel());

        yspinner = new JSpinner(new SpinnerNumberModel());
        
        zspinner = new JSpinner(new SpinnerNumberModel());

        layout = new FormLayout("8dlu, l:p, 2dlu, 30dlu:g, 5dlu, l:p, 2dlu, 30dlu:g, 5dlu, l:p, 2dlu, 30dlu:g, 8dlu", "8dlu, pref, 2dlu, pref, 8dlu");
        setLayout(layout);



        CellConstraints cc = new CellConstraints();

        xspinnerHeader = new StyledLabel("X");
        xspinnerHeader.setEnabled(false);
        xspinnerHeader.addStyleRange(new StyleRange(Font.BOLD, Color.GRAY));

        yspinnerHeader = new StyledLabel("Y");
        yspinnerHeader.addStyleRange(new StyleRange(Font.BOLD, Color.GRAY));
        yspinnerHeader.setEnabled(false);

        zspinnerHeader = new StyledLabel("Z");
        zspinnerHeader.addStyleRange(new StyleRange(Font.BOLD, Color.GRAY));
        zspinnerHeader.setEnabled(false);

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
