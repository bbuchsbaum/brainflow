package brainflow.app.actions;

import brainflow.core.ImageView;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 30, 2007
 * Time: 6:58:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SetPreserveAspectCommand extends ToggleCommand {


    public static final String COMMAND_ID = "preserve-aspect";

    private ImageView view;

    public SetPreserveAspectCommand(ImageView view) {
        super(SetPreserveAspectCommand.COMMAND_ID);
        this.view = view;
        setSelected(view.isPreserveAspect());

        
    }

    protected void handleSelection(boolean b) throws ToggleVetoException {
        if (view.isPreserveAspect() != b)
            view.setPreserveAspect(b);
    }


}
