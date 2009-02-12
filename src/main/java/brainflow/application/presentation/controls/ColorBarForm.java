/*
 * ColorBarForm.java
 *
 * Created on July 13, 2006, 12:45 PM
 */

package brainflow.application.presentation.controls;

import brainflow.colormap.*;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideSplitButton;

import javax.swing.border.EtchedBorder;

/**
 * @author buchs
 */
public class ColorBarForm extends javax.swing.JPanel {

    private IColorMap colorMap;

    private AbstractColorBar colorBar;

    //private ColorBarPlot colorBarPlot;

    private JideSplitButton colorMenu;

    private FormLayout layout;

    /**
     * Creates new form ColorBarForm
     */
    public ColorBarForm(IColorMap _colorMap) {
        colorMap = _colorMap;
        //colorBarPlot = new ColorBarPlot(colorMap);
        colorBar = colorMap.createColorBar();
        //colorBarPlot.setBorder(new EtchedBorder());
        colorMenu = new JideSplitButton("Select Map");

        buildGUI();
    }

    public ColorBarForm() {
        colorMap = new LinearColorMapDeprecated(0, 255, ColorTable.SPECTRUM);
        //colorBarPlot = new ColorBarPlot(colorMap);
        colorBar = colorMap.createColorBar();
        colorBar.setBorder(new EtchedBorder());
        //colorBarPlot.setBorder(new EtchedBorder());
        colorMenu = new JideSplitButton("Select Map");

        buildGUI();

    }


    public JideSplitButton getColorMenu() {
        return colorMenu;
    }

    public void setColorMap(IColorMap colorMap) {
        colorBar.setColorMap(colorMap);

    }


    private void buildGUI() {

        layout = new FormLayout("3dlu, l:max(100dlu;p):g, 3dlu, 6dlu", "3dlu, p, 3dlu, max(35dlu;p), 3dlu");
        //colorBar = colorMap.createColorBar();
        //colorBarPlot.setBorder(new EtchedBorder());
        //colorBarPlot.setBorder(new EmptyBorder(0,0,0,0));
        CellConstraints cc = new CellConstraints();
        setLayout(layout);

        add(colorBar, cc.xywh(2, 4, 2, 1));
        add(colorMenu, cc.xy(2, 2));

    }


}
