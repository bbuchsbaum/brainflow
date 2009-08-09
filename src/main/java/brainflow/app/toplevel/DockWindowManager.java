package brainflow.app.toplevel;

import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.event.DockableFrameListener;
import com.jidesoft.docking.event.DockableFrameEvent;
import com.jidesoft.swing.JideMenu;
import brainflow.app.actions.ActivateDockableFrameCommand;
import com.pietschy.command.ActionCommand;
import com.pietschy.command.face.Face;

import javax.swing.*;
import java.util.HashMap;



/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 27, 2007
 * Time: 10:09:21 PM
 * To change this template use File | Settings | File Templates.
 */

class DockWindowManager implements DockableFrameListener {

    private HashMap<String, DockableFrame> windowMap = new HashMap<String, DockableFrame>();

    private JMenu dockMenu = new JideMenu("Window");


    public DockWindowManager() {
        dockMenu.setMnemonic('W');
    }

    public static DockWindowManager getInstance() {
        return (DockWindowManager) SingletonRegistry.REGISTRY.getInstance("brainflow.app.toplevel.DockWindowManager");
    }

    public DockableFrame createDockableFrame(String title, String iconLocation, int state, int side) {

        DockableFrame dframe = new DockableFrame(title,
                new ImageIcon(getClass().getClassLoader().getResource(iconLocation)));


        dframe.getContext().setInitMode(state);
        dframe.getContext().setInitSide(side);
        dframe.getContext().setInitIndex(0);
        dframe.getContext().setHidable(true);
        

       
        dockMenu.add(createCommand(dframe, title).createMenuItem());


        windowMap.put(title, dframe);
        return dframe;

    }

    private ActionCommand createCommand(DockableFrame dframe, String title) {
        ActionCommand command = new ActivateDockableFrameCommand(dframe);
        //command.getDefaultFace(true).setIcon(dframe.getContext().getIcon());
        Face menuFace = command.getFace(Face.MENU, true);
        menuFace.setExtendsContext(Face.DEFAULT);
        //menuFace.setIcon( dframe.getIcon());
        menuFace.setText(title);

        return command;

    }

    public DockableFrame createDockableFrame(String title, String iconLocation, int state, int side, int index) {

        DockableFrame dframe = new DockableFrame(title,
                new ImageIcon(getClass().getClassLoader().getResource(iconLocation)));


        dframe.getContext().setInitMode(state);
        dframe.getContext().setInitSide(side);
        dframe.getContext().setInitIndex(index);

        dframe.addDockableFrameListener(this);
        //dframe.getContext().setHidable(false);

        windowMap.put(title, dframe);
        dockMenu.add(createCommand(dframe, title).createMenuItem());

       
        return dframe;

    }


    public JMenu getDockMenu() {
        return dockMenu;
    }

    public DockableFrame getDockableFrame(String title) {
        return windowMap.get(title);
    }


    public void dockableFrameAdded(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameRemoved(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameShown(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameHidden(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameDocked(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameFloating(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameAutohidden(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameAutohideShowing(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameActivated(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameDeactivated(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameTabShown(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameTabHidden(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameMaximized(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameRestored(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
