package brainflow.app.actions;

import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import brainflow.image.anatomy.Anatomy3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 3, 2009
 * Time: 11:01:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class SwitchOrientation {

    //image view probably shouldn't have reference to thse commands

    public static interface SwitchAnatomy {
        public void selectAnatomy(boolean b);
    }

    //public ToggleCommand createAxialSwitch() {

    //}



    public static class SwitchSagittal extends ToggleCommand {

        public SwitchSagittal() {
        }

        protected void handleSelection(boolean b) throws ToggleVetoException {
            //if (b && !getPlotLayout().getDisplayAnatomy().isSagittal()) {
            //    getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalSagittal());
           // }
        }

    };
}
