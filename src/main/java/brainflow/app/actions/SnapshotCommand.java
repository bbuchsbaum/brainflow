package brainflow.app.actions;

import brainflow.app.presentation.SnapshotDialog;
import brainflow.core.ImageView;

import javax.swing.*;
import java.awt.image.RenderedImage;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Oct 16, 2007
 * Time: 8:24:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class SnapshotCommand extends BrainFlowCommand {

    public static final String COMMAND_ID = "create-snapshot";

    public SnapshotCommand() {
        super(SnapshotCommand.COMMAND_ID);
        setEnabled(false);
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {
            RenderedImage image = view.captureImage();
            final SnapshotDialog dialog = new SnapshotDialog(JOptionPane.getFrameForComponent(getSelectedCanvas().getComponent()), true, image);
            dialog.setLocation((int)(this.getSelectedCanvas().getComponent().getWidth()/2.0),
                        (int)(this.getSelectedCanvas().getComponent().getHeight()/2.0));

            dialog.getForm().setCancelAction(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    //todo take logic out of SanpShotForm
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            });

            dialog.getForm().setSaveAction(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            });


            dialog.pack();
            dialog.setVisible(true);


        }
    }

    @Override
    public void viewSelected(ImageView view) {
        this.setEnabled(true);
    }

    @Override
    public void allViewsDeselected() {
        this.setEnabled(false);
    }
}
