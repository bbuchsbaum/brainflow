/*
 * ColorBarForm.java
 *
 * Created on July 13, 2006, 12:45 PM
 */

package brainflow.application.presentation.controls;

import brainflow.colormap.*;
import brainflow.chart.XAxis;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideSplitButton;

import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.*;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

import java.awt.*;

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
        cbar = new ColorBarWithAxis();
        colorMenu = new JideSplitButton("Select Map");

        buildGUI();
    }

    public ColorBarForm() {
        colorMap = new LinearColorMap2(0, 255, ColorTable.SPECTRUM);
        colorBar = colorMap.createColorBar();
        colorBar.setBorder(BorderFactory.createEtchedBorder());
        cbar = new ColorBarWithAxis();
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


  

    class ColorBarWithAxis extends JPanel {
        XAxis axis = new XAxis(0, 255);


        JPanel axispanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
               super.paintComponent(g);
                //axis.setYoffset(0);

                axis.draw((Graphics2D)g, getBounds());
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(256, 20);
            }
        };


        public ColorBarWithAxis() {
            setBorder(new EmptyBorder(0,0,0,0));
            setLayout(new BorderLayout());
            axis.setXoffset(0);
            axis.setYoffset(0);
            add(colorBar, BorderLayout.CENTER);
            add(axispanel, BorderLayout.SOUTH);
        }

        public void updateAxis(double min, double max) {
            axis.setMin(min);
            axis.setMax(max);
            repaint();


        }

        
    }


}
