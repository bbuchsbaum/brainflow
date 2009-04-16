package brainflow.colormap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 3, 2007
 * Time: 6:12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscreteColorMap2Test extends JPanel {

    DiscreteColorMap map;

    JFormattedTextField index;
    JFormattedTextField value;

    public DiscreteColorMap2Test() {
        LinearColorMap2 cmap = new LinearColorMap2(0, 255, ColorTable.resampleMap(ColorTable.SPECTRUM, 20));

        final DiscreteColorMap tmp = new DiscreteColorMap(cmap);
        add(tmp.createColorBar(), BorderLayout.CENTER);
        index = new JFormattedTextField(NumberFormat.getIntegerInstance());
        index.setText("" + 12);
        value = new JFormattedTextField(NumberFormat.getNumberInstance());
        value.setText("" + tmp.getInterval(12).getMaximum());


        value.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                double num = ((Number)value.getValue()).doubleValue();
                ColorInterval ival = tmp.getInterval(12);
                tmp.replaceInterval(12, ival.getMinimum(), num, ival.getColor());
            }
        });

        JPanel jp = new JPanel();

        jp.add(new JLabel("Index: "));

        jp.add(index);

        jp.add(new JLabel("Value: "));
        jp.add(value);

        add(jp, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        DiscreteColorMap2Test test = new DiscreteColorMap2Test();
        jf.add(test, BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }
}
