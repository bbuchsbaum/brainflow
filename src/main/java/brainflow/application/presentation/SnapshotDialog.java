package brainflow.application.presentation;

import brainflow.application.presentation.controls.SnapshotForm;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 24, 2007
 * Time: 3:58:09 PM
 */
public class SnapshotDialog extends JDialog {


    private SnapshotForm form;

    private RenderedImage snapshot;

    public SnapshotDialog(Frame owner, boolean modal, RenderedImage snapshot) {
        super(owner, modal);
        this.snapshot = snapshot;
        buildGUI();
    }

    public SnapshotDialog(RenderedImage snapshot) {
        this.snapshot = snapshot;
        buildGUI();
    }

    private void buildGUI() {
        setLayout(new BorderLayout());
        form = new SnapshotForm(snapshot);
        add(form, BorderLayout.CENTER);

        //ButtonPanel bp = new ButtonPanel();
        //bp.setAlignment(1);
        //bp.addButton(new JButton("Dismiss"));
        //add(bp, BorderLayout.SOUTH);

    }


    public static void main(String[] args) {
        try {

            BufferedImage bimg = ImageIO.read(ClassLoader.getSystemResource("resources/data/axial_slice.png"));
            SnapshotDialog form = new SnapshotDialog(bimg);        
            form.setVisible(true);
            form.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class SaveAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        }
    }


}
