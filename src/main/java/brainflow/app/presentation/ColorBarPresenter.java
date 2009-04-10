/*
 * ColorBarPresenter.java
 *
 * Created on July 13, 2006, 2:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package brainflow.app.presentation;


import brainflow.app.actions.DesignColorMapCommand;
import brainflow.app.actions.SelectColorMapCommand;
import brainflow.app.presentation.controls.ColorBarForm;

import brainflow.app.toplevel.ResourceManager;
import brainflow.colormap.ColorTable;
import brainflow.colormap.IColorMap;
import brainflow.colormap.LinearColorMap2;
import brainflow.core.ImageView;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import com.jidesoft.swing.JideSplitButton;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.image.IndexColorModel;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;

/**
 * @author buchs
 */
public class ColorBarPresenter extends ImageViewPresenter {

    private static final Logger log = Logger.getLogger(ColorBarPresenter.class.getName());

    private ColorBarForm form;

    private IColorMap colorMap;

    private List<Action> actions;

    private PropertyListener colorMapListener;

    /**
     * Creates a new instance of ColorBarPresenter
     */


    public ColorBarPresenter() {
        super();
        colorMap = new LinearColorMap2(0, 255, ColorTable.GRAYSCALE);
        init();


    }

    public ColorBarPresenter(IColorMap _colorMap) {
        super();
        colorMap = _colorMap;
        init();

    }

    private void init() {
        form = new ColorBarForm(colorMap);
        List<Action> actions = createColorMapActions();
        JideSplitButton colorMenu = form.getColorMenu();


        Iterator<Action> iter = actions.iterator();

        while (iter.hasNext()) {
            colorMenu.add(iter.next());
        }

        colorMenu.add(new GradientColorAction("Solid Color ..."));


        colorMapListener = new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {

                IColorMap cmap = (IColorMap) newValue;
                
                form.setColorMap(cmap);
            }
        };
    }

    public void viewDeselected(ImageView view) {
        BeanContainer.get().removeListener(view.getSelectedLayer().getImageLayerProperties().colorMap, colorMapListener);
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void viewSelected(final ImageView view) {
        form.setColorMap(view.getSelectedLayer().getImageLayerProperties().colorMap.get());

        BeanContainer.get().addListener(view.getSelectedLayer().getImageLayerProperties().colorMap, colorMapListener);

    }

    @Override
    public void viewModelChanged(ImageView view) {
        viewSelected(view);
    }

    @Override
    protected void layerSelected(ImageLayer3D layer) {
        form.setColorMap(layer.getImageLayerProperties().colorMap.get());
        BeanContainer.get().addListener(layer.getImageLayerProperties().colorMap, colorMapListener);
    }

    @Override
    protected void layerDeselected(ImageLayer3D layer) {
       BeanContainer.get().removeListener(getSelectedView().getSelectedLayer().getImageLayerProperties().colorMap, colorMapListener);
    }

    public JComponent getComponent() {
        return form;
    }

    public List<Action> createColorMapActions() {
        Map<String, IndexColorModel> maps = ResourceManager.getInstance().getColorMaps();
        actions = new ArrayList<Action>();
        Iterator<String> iter = maps.keySet().iterator();

        while (iter.hasNext()) {
            String name = iter.next();
            IndexColorModel icm = maps.get(name);
            SelectColorMapCommand action = new SelectColorMapCommand(name,
                    ColorTable.createImageIcon(icm, 30, 12), icm);


            actions.add(action.getActionAdapter());


        }

        DesignColorMapCommand command = new DesignColorMapCommand();
        command.getDefaultFace(true).setText("Custom Colors...");
        Action designAction = command.getActionAdapter();

        actions.add(designAction);

        return actions;
    }

    class SolidColorPanel extends JPanel {
        JCheckBox intensityCheckBox = new JCheckBox("ramp intensity");

        JColorChooser chooser = new JColorChooser();

        JPanel southPanel = new JPanel();

        public SolidColorPanel() {
            setLayout(new BorderLayout());

            chooser.setBorder(BorderFactory.createEtchedBorder());
            add(chooser, BorderLayout.CENTER);

            southPanel.setLayout(new JideBoxLayout(southPanel, JideBoxLayout.X_AXIS));
            southPanel.add(intensityCheckBox, JideBoxLayout.FIX);
            southPanel.setBorder(BorderFactory.createEtchedBorder());

            add(southPanel, BorderLayout.SOUTH);

        }

        public Color getColor() {
            return chooser.getColor();
        }

        public boolean getIntensityRamp() {
            return intensityCheckBox.isSelected();
        }
    }


    class GradientColorAction extends AbstractAction {


        public GradientColorAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            ImageView view = getSelectedView();
            int layer = view.getModel().getSelectedIndex();

            IColorMap oldMap = view.getModel().get(layer).
                    getImageLayerProperties().getColorMap();

            ColorGradientEditor chooser = new ColorGradientEditor(oldMap.getMinimumValue(), oldMap.getMaximumValue());

            String[] options = {
                    "OK",
                    "Cancel",

            };


            int result = JOptionPane.showOptionDialog(
                    getComponent(),                             // the parent that the dialog blocks
                    chooser,                                  // the dialog message array
                    "Create Color Map",                 // the title of the dialog window
                    JOptionPane.OK_CANCEL_OPTION,                 // option type
                    JOptionPane.INFORMATION_MESSAGE,            // message type
                    null,                                       // optional icon, use null to use the default icon
                    options,                                    // options string array, will be made into buttons
                    options[0]                                  // option that should be made into a default button
            );


            if (result == 0) {


                IColorMap newMap = chooser.getColorMap();

                view.getModel().get(layer).
                        getImageLayerProperties().colorMap.set(newMap);
            }


        }
    }


}
