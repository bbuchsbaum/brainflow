package brainflow.colormap.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 11, 2007
 * Time: 4:25:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleColorEditor extends JPanel implements ChangeListener {

    private FormLayout layout;


    private JSpinner redSpinner;

    private JSpinner greenSpinner;

    private JSpinner blueSpinner;

    private ColorPanel swatch;

    private Color color = Color.WHITE;

    public SimpleColorEditor() {
        layout = new FormLayout("8dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 8dlu", "5dlu, p, 5dlu, p:g, 20dlu, 5dlu");
        CellConstraints cc = new CellConstraints();

        setLayout(layout);

        redSpinner = new JSpinner(new SpinnerNumberModel(color.getRed(), 0, 255, 1));
        greenSpinner = new JSpinner(new SpinnerNumberModel(color.getGreen(), 0, 255, 1));
        blueSpinner = new JSpinner(new SpinnerNumberModel(color.getBlue(), 0, 255, 1));


        add(new JLabel("R:"), cc.xy(2, 2));
        add(new JLabel("G:"), cc.xy(6, 2));
        add(new JLabel("B:"), cc.xy(10, 2));
        //add(new JLabel("A:"), cc.xy(14, 2));

        add(redSpinner, cc.xy(4, 2));
        add(greenSpinner, cc.xy(8, 2));
        add(blueSpinner, cc.xy(12, 2));
        //add(alphaSpinner, cc.xy(16, 2));

        swatch = new ColorPanel();
        swatch.setBorder(BorderFactory.createEtchedBorder());
        add(swatch, cc.xywh(2, 4, 11, 2));


        redSpinner.addChangeListener(this);
        blueSpinner.addChangeListener(this);
        greenSpinner.addChangeListener(this);
        //alphaSpinner.addChangeListener(this);


    }

    private void updateSpinners() {
        redSpinner.setValue(color.getRed());
        greenSpinner.setValue(color.getGreen());
        blueSpinner.setValue(color.getBlue());
        //alphaSpinner.setValue(color.getAlpha());
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color _color) {

        if (!_color.equals(color)) {
            color = _color;
            updateSpinners();
            swatch.repaint();
        }
    }

    public void stateChanged(ChangeEvent e) {
        int red = ((Number) redSpinner.getValue()).intValue();
        int green = ((Number) greenSpinner.getValue()).intValue();
        int blue = ((Number) blueSpinner.getValue()).intValue();
        //int alpha = ((Number)alphaSpinner.value()).intValue();

        //setColor(new Color(red,green,blue,alpha));
        setColor(new Color(red, green, blue));
    }


    class ColorPanel extends JPanel {

        protected void paintComponent(Graphics g) {
            //Graphics2D g2 = (Graphics2D)g;
            Color c = g.getColor();
            g.setColor(color);
            Insets insets = this.getInsets();
            g.fillRect(insets.left, insets.top, getWidth() - (insets.right + insets.left), getHeight() - (insets.top + insets.bottom));
            g.setColor(c);
        }
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.add(new SimpleColorEditor());
        jf.pack();
        jf.setVisible(true);
    }
}
