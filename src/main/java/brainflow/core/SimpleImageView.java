package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.group.GroupBuilder;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import com.pietschy.command.toggle.ToggleGroup;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Feb 6, 2009
 * Time: 5:16:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleImageView extends ImageView {


    private CommandContainer commandContainer;

    private ToggleCommand switchSagittal = new ToggleCommand() {
        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isSagittal()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalSagittal());
            }
        }

    };

    private ToggleCommand switchAxial = new ToggleCommand() {
        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isAxial()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalAxial());
            }
        }

    };

    private ToggleCommand switchCoronal = new ToggleCommand() {
        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (b && !getPlotLayout().getDisplayAnatomy().isCoronal()) {
                getPlotLayout().setDisplayAnatomy(Anatomy3D.getCanonicalCoronal());
            }
        }


    };

    public SimpleImageView(IImageDisplayModel imodel, Anatomy3D displayAnatomy) {
        super(imodel);


        initPlotLayout(new SimplePlotLayout(this, displayAnatomy));
        commandContainer = new CommandContainer();
        commandContainer.bind(this);
        
        if (displayAnatomy.isAxial()) {
            switchAxial.setSelected(true);
        } else if (displayAnatomy.isCoronal()) {
            switchCoronal.setSelected(true);
        } else {
            switchSagittal.setSelected(true);
        }

        initToolBar();
    }



    protected void initToolBar() {


        switchSagittal.getDefaultFace(true).setText("");
        switchSagittal.getDefaultFace().setIcon(new ImageIcon(getClass().getResource("sagit_16.png")));
        switchSagittal.bind(commandContainer);


        switchAxial.getDefaultFace(true).setText("");
        switchAxial.getDefaultFace().setIcon(new ImageIcon(getClass().getResource("axial_16.png")));
        switchAxial.bind(commandContainer);


        switchCoronal.getDefaultFace(true).setText("cor");
        switchCoronal.bind(commandContainer);
        switchCoronal.getDefaultFace().setIcon(new ImageIcon(getClass().getResource("coronal_16.png")));

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
