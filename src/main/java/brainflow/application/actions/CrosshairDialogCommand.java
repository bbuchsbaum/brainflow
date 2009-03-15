package brainflow.application.actions;

import brainflow.application.presentation.CrosshairPresenter;
import brainflow.core.ImageView;
import brainflow.core.annotations.CrosshairAnnotation;
import com.jgoodies.forms.factories.ButtonBarFactory;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 13, 2007
 * Time: 12:59:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairDialogCommand extends BrainFlowCommand {

    private static final Logger log = Logger.getLogger(CrosshairDialogCommand.class.getName());

    private JDialog dialog;


    public CrosshairDialogCommand() {


    }

    protected void handleExecute() {

        ImageView view = getSelectedView();


        if (view != null) {
            final CrosshairAnnotation icross = (CrosshairAnnotation) view.getAnnotation(view.getSelectedPlot(), CrosshairAnnotation.ID);
            final CrosshairAnnotation safeCopy = (CrosshairAnnotation) icross.safeCopy();

            if (icross != null) {
                CrosshairPresenter presenter = new CrosshairPresenter(icross);
                Container c = JOptionPane.getFrameForComponent(view);


                final JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        dialog.setVisible(false);
                        dialog.dispose();
                    }
                });

                final JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        //view.setAnnotation(safeCopy);
                        icross.setLinePaint(safeCopy.getLinePaint());
                        icross.setLineWidth(safeCopy.getLineWidth());
                        icross.setGap(safeCopy.getGap());
                        icross.setVisible(safeCopy.isVisible());


                        dialog.setVisible(false);
                        dialog.dispose();
                    }
                });


                final JButton applyButton = new JButton("Apply");


                dialog = new JDialog(JOptionPane.getFrameForComponent(view));
                dialog.setLayout(new BorderLayout());


                Point p = c.getLocation();


                JPanel mainPanel = new JPanel();
                mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 8, 15, 8));
                mainPanel.setLayout(new BorderLayout());
                JPanel buttonPanel = ButtonBarFactory.buildRightAlignedBar(okButton, cancelButton, applyButton);

                mainPanel.add(presenter.getComponent(), BorderLayout.CENTER);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);

                dialog.add(mainPanel, BorderLayout.CENTER);
                dialog.pack();
                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                dialog.setLocation((int) (p.getX() + c.getWidth() / 2f), (int) (p.getY() + c.getHeight() / 2f));
                dialog.setVisible(true);
            }
        }

    }


}