package brainflow.app.actions;

import brainflow.core.ImageView;
import brainflow.image.anatomy.BrainPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import com.jidesoft.dialog.JideOptionPane;

import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 6, 2008
 * Time: 1:13:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoToVoxelCommand extends BrainFlowCommand {

    public GoToVoxelCommand() {
        super("goto-voxel");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();
        int[] vox = new int[3];
        if (view != null) {
            String res = JideOptionPane.showInputDialog("Enter voxel coordinates (i j k): ");
            StringTokenizer tokenizer = new StringTokenizer(res, " ");


            int count=0;
            while (tokenizer.hasMoreTokens() && count < 3) {
                String t = tokenizer.nextToken();
                try {
                    vox[count] = Integer.parseInt(t);
                    count++;
                } catch (NumberFormatException e) {
                    //todo report error on status bar?
                    return;
                }

            }
        }

        float[] coords = view.getModel().getImageSpace().indexToWorld(vox);
     
        BrainPoint3D ap = new BrainPoint3D((Anatomy3D)view.getModel().getImageSpace().getAnatomy(), coords[0], coords[1], coords[2]);
        view.cursorX.set(ap.getX());
        view.cursorY.set(ap.getY());
        view.cursorZ.set(ap.getZ());
    }
}
