package brainflow.application.actions;

import brainflow.application.presentation.SnapshotDialog;
import brainflow.core.ImageView;

import javax.swing.*;
import java.awt.image.RenderedImage;

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
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {
            RenderedImage image = view.captureImage();
            SnapshotDialog dialog = new SnapshotDialog(JOptionPane.getFrameForComponent(getSelectedCanvas().getComponent()), true, image);
            dialog.pack();
            dialog.setVisible(true);


        }
    }
}
