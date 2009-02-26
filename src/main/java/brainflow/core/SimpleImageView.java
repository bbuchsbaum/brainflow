package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.ActionCommand;
import com.pietschy.command.group.GroupBuilder;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import com.pietschy.command.toggle.ToggleGroup;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Feb 6, 2009
 * Time: 5:16:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleImageView extends ImageView {


    private CommandContainer commandContainer = new CommandContainer();

    protected class SwitchSagittal extends ToggleCommand {

        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isSagittal()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalSagittal());
            }
        }

    };

    protected class SwitchAxial extends ToggleCommand  {
        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isAxial()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalAxial());
            }
        }

    };

     protected class SwitchCoronal extends ToggleCommand  {
        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isCoronal()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalCoronal());
            }
        }


    };


    private ToggleCommand switchAxial = new SwitchAxial();

    private ToggleCommand switchCoronal = new SwitchCoronal();

    private ToggleCommand switchSagittal = new SwitchSagittal();

    private Anatomy3D displayAnatomy;

    private ImagePlotLayout plotLayout;

    public SimpleImageView(IImageDisplayModel imodel, Anatomy3D displayAnatomy) {
        super(imodel);
        this.displayAnatomy = displayAnatomy;

        layoutPlots();
        initDisplayAnatomy(displayAnatomy);
              
        commandContainer.bind(this);

        initToolBar();
    }

    protected void layoutPlots() {
        plotLayout = createPlotLayout(displayAnatomy);
        resetPlotLayout(plotLayout);

    }

    public SimplePlotLayout getPlotLayout() {
        return (SimplePlotLayout)plotLayout;
    }

    protected ImagePlotLayout createPlotLayout(Anatomy3D displayAnatomy) {
       return new SimplePlotLayout(this, displayAnatomy);

    }

    protected CommandContainer getCommandContainer() {
        return commandContainer;
    }

    protected void initDisplayAnatomy(Anatomy3D displayAnatomy) {
        if (displayAnatomy.isAxial()) {
            switchAxial.setSelected(true);
        } else if (displayAnatomy.isCoronal()) {
            switchCoronal.setSelected(true);
        } else if (displayAnatomy.isSagittal()){
            switchSagittal.setSelected(true);
        } else {
            Logger.getAnonymousLogger().severe("DisplayAnatomy not one of : [Axial, Coronal, Sagittal] -- this should never happen");
        }

    }


    protected void initCommand(ActionCommand command, String text, String iconpath) {
        command.getDefaultFace(true).setText(text);
        ImageIcon icon = new ImageIcon(getClass().getResource(iconpath));
        command.getDefaultFace().setIcon(icon);
        command.bind(commandContainer);

    }



    protected void initToolBar() {
        ToggleCommand switchAxial = new SwitchAxial();
        ToggleCommand switchCoronal = new SwitchCoronal();
        ToggleCommand switchSagittal = new SwitchSagittal();
        initCommand(switchAxial, "", "axial_16.png");
        initCommand(switchCoronal, "", "coronal_16.png");
        initCommand(switchSagittal, "", "sagit_16.png");

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
