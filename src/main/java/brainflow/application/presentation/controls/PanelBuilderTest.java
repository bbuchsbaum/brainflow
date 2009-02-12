package brainflow.application.presentation.controls;

import brainflow.colormap.ColorTable;
import brainflow.colormap.LinearColorBar;
import brainflow.colormap.LinearColorMapDeprecated;
import brainflow.gui.ColorIcon;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 15, 2007
 * Time: 11:34:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class PanelBuilderTest {

    private LinearColorMapDeprecated cmap = new LinearColorMapDeprecated(0, 255, ColorTable.SPECTRUM);


    public PanelBuilderTest() {
    }

    public static void main(String[] args) {


        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Custom Rows");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LinearColorBar cbar = new LinearColorBar(new LinearColorMapDeprecated(0, 255, ColorTable.SPECTRUM), SwingConstants.VERTICAL);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        cbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 2, 10, 2),
                BorderFactory.createEtchedBorder()));

        mainPanel.add(cbar, BorderLayout.WEST);
        JComponent panel = new PanelBuilderTest().buildPanel();
        mainPanel.add(new JScrollPane(panel), BorderLayout.CENTER);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);


    }


    public JComponent buildPanel() {

        FormLayout layout = new FormLayout(
                "l:max(p;24px), 3dlu, l:p, 3dlu, p:grow, 8dlu, l:p, 3dlu p:grow",
                "");

        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setRowGroupingEnabled(true);

        CellConstraints cc = new CellConstraints();

        for (int i = 0; i < 12; i++) {
            addRow(i, builder);
        }

        builder.appendSeparator();

        builder.append(new JLabel());
        builder.append(new JButton("Previous"), 3);
        builder.append(new JButton("Next"), 3);


        return builder.getPanel();

    }

    private void addRow(int index, DefaultFormBuilder builder) {
        builder.appendSeparator("Index " + (index + 1));
        JButton b = new JButton("");
        b.setPreferredSize(new Dimension(24, 24));
        b.setIcon(ColorIcon.createIcon(cmap.getInterval(index).getColor(), 16, 16));
        b.setActionCommand("" + index);
        b.setDefaultCapable(true);

        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = Integer.parseInt(e.getActionCommand());
                JColorChooser chooser = new JColorChooser();

                chooser.setColor(cmap.getInterval(index).getColor());
                JOptionPane.showMessageDialog((JButton) e.getSource(), chooser,
                        "Select Color", JOptionPane.PLAIN_MESSAGE);
            }
        });

        builder.append(b);
        builder.append("Min:", new JTextField());
        builder.append("Max:", new JTextField());
        builder.nextLine();

        double val = cmap.getInterval(index).getMaximum();
        double perc = (val - cmap.getMinimumValue()) / cmap.getRange().getInterval();
        JSlider slider = new JSlider(0, 1000, (int) (perc * 10.0));
        builder.append(slider, 9);


    }

}