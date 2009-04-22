package brainflow.app.presentation.controls;

import brainflow.chart.XAxis;
import brainflow.colormap.AbstractColorBar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
* User: Brad
* Date: Apr 19, 2009
* Time: 5:37:20 PM
* To change this template use File | Settings | File Templates.
*/
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


    public ColorBarWithAxis(AbstractColorBar colorBar) {
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
