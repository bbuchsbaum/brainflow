/*
 * OpenAnalyzeAction.java
 *
 * Created on February 25, 2003, 3:34 PM
 */

package brainflow.application.actions;


import javax.swing.*;
import java.awt.*;

import com.pietschy.command.ActionCommand;

/**
 * @author Bradley
 */
public class ExitApplicationCommand extends ActionCommand {


    /**
     * Creates a new instance of OpenAnalyzeAction
     */
    public ExitApplicationCommand() {
        super("exit-application");

    }

    protected void handleExecute() {
        Window window = getInvokerWindow();
        JFrame frame = (JFrame)JOptionPane.getFrameForComponent(window);
        frame.setVisible(false);
        frame.dispose();
        System.exit(0);
    }

    
}



