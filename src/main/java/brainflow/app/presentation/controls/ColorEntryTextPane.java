package brainflow.app.presentation.controls;

import com.jidesoft.swing.AutoResizingTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Nov 6, 2009
 * Time: 7:55:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class ColorEntryTextPane extends JPanel {

    private JList inputList = new JList();

    private DefaultListModel listModel = new DefaultListModel();

    private JTextField inputField = new JTextField(12);

    public ColorEntryTextPane() {

        setLayout(new BorderLayout());
        inputList.setMinimumSize(new Dimension(50, 200));
        inputList.setModel(listModel);
        add(inputList, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText();
                try {
                    Color.getColor(text, 33)
                    Color c = Color.decode(text);
                    listModel.addElement(c);
                } catch (Throwable t) {
                    t.printStackTrace();
                }

            }
        });


    }

    private boolean isRGBTriplet(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, ", ");
        int i = -0;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            i++;

        }

    }


    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.add(new JScrollPane(new ColorEntryTextPane()), BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }
}
