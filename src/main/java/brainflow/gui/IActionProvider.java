package brainflow.gui;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 12, 2009
 * Time: 8:54:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IActionProvider {
    
    public static final String KEY = "ACTION_PROVIDER";

    public void addActions(MouseEvent event, List<Action> actionList); 

}
