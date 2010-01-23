package brainflow.core;

import brainflow.colormap.HistogramColorBar;
import brainflow.core.binding.CoordinateToIndexConverter2;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.core.annotations.IAnnotation;
import brainflow.image.space.Axis;
import com.jidesoft.swing.JideBoxLayout;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.ActionCommand;
import com.pietschy.command.group.GroupBuilder;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import com.pietschy.command.toggle.ToggleGroup;
import net.java.dev.properties.binding.swing.adapters.SwingBind;
import net.java.dev.properties.container.BeanContainer;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Feb 6, 2009
 * Time: 5:16:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleImageView extends ImageView {


    private CommandContainer commandContainer = new CommandContainer();

    private JSlider slider = new JSlider(JSlider.HORIZONTAL);

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

    public SimpleImageView(ImageViewModel imodel, Anatomy3D displayAnatomy) {
        super(imodel);

        BeanContainer.bind(this);
        commandContainer.bind(this);

        this.displayAnatomy = displayAnatomy;

        layoutPlots();

        initDisplayAnatomy(displayAnatomy);
              
        initToolBar();

        initSlider();
    }

    protected void layoutPlots() {
        plotLayout = createPlotLayout(displayAnatomy);
        initPlotLayout(plotLayout, new HashMap<String, IAnnotation>());

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

    protected void bindSlider() {
        CoordinateToIndexConverter2 kconv = new CoordinateToIndexConverter2(this.worldCursorPos, getModel().getImageSpace(), Axis.Z_AXIS);
        SwingBind.get().bind(kconv, slider);

    }

    protected void initSlider() {
        int max = getModel().getImageSpace().getDimension(Axis.Z_AXIS)-1;

        slider = new JSlider(JSlider.HORIZONTAL, 0, max, max/2);
        /*JXLayer<JSlider> sliderLayer = new JXLayer<JSlider>(slider);
        AbstractLayerUI<JSlider> layerUI = new AbstractLayerUI<JSlider>() {
             @Override
             protected void paintLayer(Graphics2D g2, JXLayer<JSlider> l) {
                 super.paintLayer(g2, l);
                 double w = slider.getBounds().getWidth();
                 double h = slider.getBounds().getHeight();
                 g2.drawString("hello",(int)(w/2.0), (int)(h/2.0));
             }
         };

        sliderLayer.setUI(layerUI);


        add(sliderLayer, BorderLayout.SOUTH);   */



        add(slider, BorderLayout.SOUTH);
        bindSlider();        


    }
}
