/*
* BrainFrame.java
*
* Created on April 23, 2003, 2:14 PM
*/

package brainflow.app;

import com.jidesoft.docking.DefaultDockableHolder;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.action.DefaultDockableBarHolder;
import com.jidesoft.action.DockableBarDockableHolderPanel;
import com.jidesoft.action.DefaultDockableBarDockableHolder;

import java.awt.*;
import java.util.logging.Logger;


/**
 * @author Bradley
 */


public class BrainFrame extends DefaultDockableBarDockableHolder {


    private static Logger log = Logger.getLogger(BrainFrame.class.getName());

    public BrainFrame() {
        super("BrainFlow");

    }

    
   
    public static void main(String[] args) {
        log.info("verifying license");
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFrame", "7.YTcWgxxjx1xjSnUqG:U1ldgGetfRn1");

        BrainFrame bf = new BrainFrame();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        bf.setLocationRelativeTo(null);
       // bf.setSize((int) screenSize.getWidth()-10, (int) screenSize.getHeight() - 10);

        log.info("showing frame");
        bf.setVisible(true);

    }


}

