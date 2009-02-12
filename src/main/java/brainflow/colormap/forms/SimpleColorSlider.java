package brainflow.colormap.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 11, 2007
 * Time: 4:25:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleColorSlider extends JPanel implements ChangeListener {

    private FormLayout layout;


    private JSlider redSlider;
    private JSlider greenSlider;
    private JSlider blueSlider;
    private JSlider alphaSlider;

    private JFormattedTextField redValueField;
    private JFormattedTextField greenValueField;
    private JFormattedTextField blueValueField;
    private JFormattedTextField alphaValueField;


    private SimpleColorSlider.ColorPanel swatch;

    private Color color = Color.WHITE;

    public SimpleColorSlider() {

        layout = new FormLayout(

                "6dlu, l:75dlu:g, 3dlu, 5dlu, l:max(p;25dlu), 8dlu",
                "4dlu, p, 3dlu, p, 6dlu, p, 3dlu, p, 6dlu, p, 3dlu, p, 6dlu, p, 3dlu, p, 6dlu, 20dlu, 3dlu");


        setLayout(layout);
        CellConstraints cc = new CellConstraints();

        setLayout(layout);

        redSlider = new JSlider(0, 255, color.getRed());
        greenSlider = new JSlider(0, 255, color.getGreen());
        blueSlider = new JSlider(0, 255, color.getBlue());
        alphaSlider = new JSlider(0, 255, color.getAlpha());


        add(new JLabel("Red:"), cc.xy(2, 2));
        add(new JLabel("Green:"), cc.xy(2, 6));
        add(new JLabel("Blue:"), cc.xy(2, 10));
        add(new JLabel("Alpha:"), cc.xy(2, 14));

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumIntegerDigits(3);

        redValueField = new JFormattedTextField(format);
        redValueField.setText(color.getRed() + " ");
        greenValueField = new JFormattedTextField(format);
        greenValueField.setText(color.getGreen() + " ");
        blueValueField = new JFormattedTextField(format);
        blueValueField.setText(color.getBlue() + " ");
        alphaValueField = new JFormattedTextField(format);
        alphaValueField.setText(color.getAlpha() + " ");

        add(redValueField, cc.xy(5, 4));
        add(greenValueField, cc.xy(5, 8));
        add(blueValueField, cc.xy(5, 12));
        add(alphaValueField, cc.xy(5, 16));


        add(redSlider, cc.xywh(2, 4, 2, 1));
        add(greenSlider, cc.xywh(2, 8, 2, 1));
        add(blueSlider, cc.xywh(2, 12, 2, 1));
        add(alphaSlider, cc.xywh(2, 16, 2, 1));

        swatch = new SimpleColorSlider.ColorPanel();
        swatch.setBorder(BorderFactory.createEtchedBorder());
        add(swatch, cc.xywh(2, 18, 2, 2));


        redSlider.addChangeListener(this);
        blueSlider.addChangeListener(this);
        greenSlider.addChangeListener(this);
        alphaSlider.addChangeListener(this);


    }

    private void updateSliders() {
        redSlider.setValue(color.getRed());
        greenSlider.setValue(color.getGreen());
        blueSlider.setValue(color.getBlue());
        //alphaSpinner.setValue(color.getAlpha());
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color _color) {

        if (!_color.equals(color)) {
            color = _color;
            updateSliders();
            swatch.repaint();
        }
    }


    public JSlider getRedSlider() {
        return redSlider;
    }

    public JSlider getGreenSlider() {
        return greenSlider;
    }

    public JSlider getBlueSlider() {
        return blueSlider;
    }

    public JSlider getAlphaSlider() {
        return alphaSlider;
    }

    public JTextField getRedValueField() {
        return redValueField;
    }

    public JTextField getGreenValueField() {
        return greenValueField;
    }

    public JTextField getBlueValueField() {
        return blueValueField;
    }

    public JTextField getAlphaValueField() {
        return alphaValueField;
    }

    public void stateChanged(ChangeEvent e) {
        int red = redSlider.getValue();
        int green = greenSlider.getValue();
        int blue = blueSlider.getValue();
        //int alpha = ((Number)alphaSpinner.value()).intValue();

        //setColor(new Color(red,green,blue,alpha));
        setColor(new Color(red, green, blue));
    }


    class ColorPanel extends JPanel {

        protected void paintComponent(Graphics g) {
            //Graphics2D g2 = (Graphics2D)g;
            Color c = g.getColor();
            g.setColor(color);
            Insets insets = getInsets();
            //g.fillRect(insets.left, insets.top, getWidth() - (insets.left + insets.right) , getHeight() - (insets.top + insets.bottom) );
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(c);
        }
    }

    public static void main(String[] args) throws Exception {

        JFrame jf = new JFrame();
        jf.add(new SimpleColorSlider());
        jf.pack();
        jf.setVisible(true);
    }
}
