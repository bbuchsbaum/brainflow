package brainflow.app.actions;

import brainflow.core.ImageView;
import brainflow.image.anatomy.VoxelLoc3D;
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

        view.cursorPos.set(new VoxelLoc3D(vox[0], vox[1], vox[2], view.getModel().getImageSpace()));
        //float[] coords = view.getModel().getImageSpace().indexToWorld(vox);
        //BrainPoint3D ap = new BrainPoint3D(view.getModel().getImageSpace().getMapping().getWorldAnatomy(), coords[0], coords[1], coords[2]);
        //view.worldCursorPos.set(ap);
        //view.cursorX.set(ap.getValue());
        //view.cursorY.set(ap.getY());
        //view.cursorZ.set(ap.getZ());
    }
}
