/*
 * ColorBarForm.java
 *
 * Created on July 13, 2006, 12:45 PM
 */

package brainflow.colormap.forms;

import brainflow.colormap.*;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideSplitButton;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author buchs
 */
public class ColorBarForm extends javax.swing.JPanel {

    private IColorMap colorMap;

    private AbstractColorBar colorBar;

    private ColorBarWithAxis cbar;

    private JideSplitButton colorMenu;

    private JCheckBox invertColors;

    private JSpinner numColors;

    private MigLayout layout;

    /**
     * Creates new form ColorBarForm
     */
    public ColorBarForm(IColorMap _colorMap) {
        colorMap = _colorMap;
        colorBar = colorMap.createColorBar();
        colorBar.setBorder(BorderFactory.createEtchedBorder());
        cbar = new ColorBarWithAxis(colorBar);
        colorMenu = new JideSplitButton("Select Map");
        invertColors = new JCheckBox("invert", false);
        numColors = new JSpinner(new SpinnerNumberModel(1,1, 256, 1));
        buildGUI();
    }

    public ColorBarForm() {
        colorMap = new LinearColorMap2(0, 255, ColorTable.SPECTRUM);
        colorBar = colorMap.createColorBar();
        colorBar.setBorder(BorderFactory.createEtchedBorder());
        cbar = new ColorBarWithAxis(colorBar);
        colorMenu = new JideSplitButton("Select Map");
        invertColors = new JCheckBox("invert", false);
        numColors = new JSpinner(new SpinnerNumberModel(1,1, 256, 1));
        buildGUI();

    }


    public JideSplitButton getColorMenu() {
        return colorMenu;
    }

    public void setColorMap(IColorMap colorMap) {
        colorBar = colorMap.createColorBar();
        cbar.updateColorBar(colorBar);
        cbar.updateAxis(colorMap.getMinimumValue(), colorMap.getMaximumValue());
    }


    private void buildGUI() {

        layout = new MigLayout();
       
        setLayout(layout);

        add(colorMenu);
        add(new JLabel("# of colors"), "align right, gap left 30");
        add(numColors, "width 40:60:80, wrap");
        //add(invertColors, "align right, wrap");
        add(cbar, "span 3, height 65");
        //add(invertColors, "span 3, align right");

    }


}
