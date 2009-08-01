package brainflow.app.presentation.controls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 24, 2009
 * Time: 6:36:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileRegexTest {

    // a tree table
    public FileRegexTest() {
        init();


    }


    public void init() {
        final JFrame frame = new JFrame();
        JButton jb = new JButton("open");

        frame.getContentPane().add(jb, BorderLayout.CENTER);

        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setAccessory(new JLabel("accessory!"));
                chooser.showOpenDialog(frame);
            }
        });

        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new FileRegexTest();

    }
}
