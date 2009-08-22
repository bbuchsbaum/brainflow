package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.core.annotations.IAnnotation;
import com.pietschy.command.ActionCommand;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.group.GroupBuilder;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import com.pietschy.command.toggle.ToggleGroup;

import javax.swing.*;
import java.util.logging.Logger;
import java.util.HashMap;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Feb 8, 2009
 * Time: 11:31:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class OrthoImageView extends ImageView {

    private OrthoPlotLayout plotLayout;

    private Anatomy3D leadAnatomy = Anatomy3D.getCanonicalAxial();

    private OrthoPlotLayout.ORIENTATION orientation;

    private CommandContainer commandContainer = new CommandContainer();


    protected class SwitchSagittal extends ToggleCommand {

        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isSagittal()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalSagittal());
            }
        }

    }



    protected class SwitchAxial extends ToggleCommand {
        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isAxial()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalAxial());
            }
        }

    }



    protected class SwitchCoronal extends ToggleCommand {
        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isCoronal()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalCoronal());
            }
        }


    }


    private ToggleCommand switchAxial = new SwitchAxial();

    private ToggleCommand switchCoronal = new SwitchCoronal();

    private ToggleCommand switchSagittal = new SwitchSagittal();


    public OrthoImageView(ImageViewModel imodel, OrthoPlotLayout.ORIENTATION orientation) {
        super(imodel);    //To change body of overridden methods use File | Settings | File Templates.
        this.orientation = orientation;
        init();
    }

    public OrthoImageView(ImageViewModel imodel, Anatomy3D leadAnatomy, OrthoPlotLayout.ORIENTATION orientation) {
        super(imodel);    //To change body of overridden methods use File | Settings | File Templates.
        this.leadAnatomy = leadAnatomy;
        this.orientation = orientation;
        init();
    }

    private void init() {

        layoutPlots();
        initDisplayAnatomy(leadAnatomy);

        initToolBar();
        layoutPlots();


        commandContainer.bind(this);


    }

    protected void layoutPlots() {
        plotLayout = createPlotLayout(leadAnatomy);
        initPlotLayout(plotLayout, new HashMap<String, IAnnotation>());

    }

    public OrthoPlotLayout getPlotLayout() {
        return plotLayout;
    }

    protected OrthoPlotLayout createPlotLayout(Anatomy3D displayAnatomy) {
        plotLayout = new OrthoPlotLayout(this, displayAnatomy, orientation);
        return plotLayout;
    }

    protected void initDisplayAnatomy(Anatomy3D displayAnatomy) {
        if (displayAnatomy.isAxial()) {
            switchAxial.setSelected(true);
        } else if (displayAnatomy.isCoronal()) {
            switchCoronal.setSelected(true);
        } else if (displayAnatomy.isSagittal()) {
            switchSagittal.setSelected(true);
        } else {
            Logger.getAnonymousLogger().severe("DisplayAnatomy not one of : [Axial, Coronal, Sagittal] -- this should never happen");
        }

    }


    protected void initCommand(ActionCommand command, String text, String iconpath) {
        command.getDefaultFace(true).setText(text);
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconpath));
        command.getDefaultFace().setIcon(icon);
        command.bind(commandContainer);

    }


    protected void initToolBar() {
        ToggleCommand switchAxial = new SwitchAxial();
        ToggleCommand switchCoronal = new SwitchCoronal();
        ToggleCommand switchSagittal = new SwitchSagittal();

        initCommand(switchAxial, "", "icons/axial_16.png");
        initCommand(switchCoronal, "", "icons/coronal_16.png");
        initCommand(switchSagittal, "", "icons/sagit_16.png");

        ToggleGroup switchGroup = new ToggleGroup();

        switchGroup.getDefaultFace(true);
        switchGroup.setEmptySelectionAllowed(false);
        switchGroup.setExclusive(true);

        GroupBuilder builder = switchGroup.getBuilder();

        builder.add(switchAxial);
        builder.add(switchCoronal);
        builder.add(switchSagittal);

        builder.applyChanges();
        switchGroup.bind(commandContainer);
        add(switchGroup.createToolBar(), BorderLayout.NORTH);


    }


}
