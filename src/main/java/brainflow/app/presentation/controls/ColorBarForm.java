/*
 * ColorBarForm.java
 *
 * Created on July 13, 2006, 12:45 PM
 */

package brainflow.app.presentation.controls;

import brainflow.colormap.*;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideSplitButton;

import javax.swing.*;

/**
 * @author buchs
 */
public class ColorBarForm extends javax.swing.JPanel {

    private IColorMap colorMap;

    private AbstractColorBar colorBar;

    private ColorBarWithAxis cbar;

    private JideSplitButton colorMenu;

    private FormLayout layout;

    /**
     * Creates new form ColorBarForm
     */
    public ColorBarForm(IColorMap _colorMap) {
        colorMap = _colorMap;
        colorBar = colorMap.createColorBar();
        colorBar.setBorder(BorderFactory.createEtchedBorder());
        cbar = new ColorBarWithAxis(colorBar);
        colorMenu = new JideSplitButton("Select Map");

        buildGUI();
    }

    public ColorBarForm() {
        colorMap = new LinearColorMap2(0, 255, ColorTable.SPECTRUM);
        colorBar = colorMap.createColorBar();
        colorBar.setBorder(BorderFactory.createEtchedBorder());
        cbar = new ColorBarWithAxis(colorBar);
        colorMenu = new JideSplitButton("Select Map");

        buildGUI();

    }


    public JideSplitButton getColorMenu() {
        return colorMenu;
    }

    public void setColorMap(IColorMap colorMap) {
        colorBar.setColorMap(colorMap);
        cbar.updateAxis(colorMap.getMinimumValue(), colorMap.getMaximumValue());
    }


    private void buildGUI() {

        layout = new FormLayout("5px, l:max(100dlu;p):g, 3dlu, 6dlu", "3dlu, p, 3dlu, max(35dlu;p), 3dlu");
        CellConstraints cc = new CellConstraints();
        setLayout(layout);

        add(colorMenu, cc.xy(2, 2));
        add(cbar, cc.xywh(2, 4, 2, 2));

    }


}
