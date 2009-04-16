package brainflow.colormap;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Apr 29, 2005
 * Time: 10:25:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class ColorIntervalEditor extends JComponent {

    private static final int ICON_WIDTH = 25;

    private static final int ICON_HEIGHT = 18;

    private ImageIcon colorIcon;

    private ColorInterval interval;

    private JFormattedTextField lowField;

    private JFormattedTextField highField;

    private boolean isEditable = false;

    public ColorIntervalEditor(ColorInterval _interval) {
        interval = _interval;
        colorIcon = createColorIcon();
        setupLayout();
    }

    private void setupLayout() {
        FormLayout layout = new FormLayout("2dlu, l:max(p;25dlu), 5dlu, max(p;15dlu), 5dlu, max(p;15dlu), 5dlu, max(p;15dlu), 5dlu, max(p;15dlu), 2dlu", "2dlu, p, 4dlu, p, 4dlu, p, 2dlu");
        layout.addGroupedColumn(4);
        layout.addGroupedColumn(6);
        layout.addGroupedColumn(8);
        layout.addGroupedColumn(10);
        setLayout(layout);
        CellConstraints cc = new CellConstraints();

        JLabel redLabel = new JLabel("R");
        redLabel.setToolTipText("Red");
        add(redLabel, cc.xy(4, 2));

        JLabel greenLabel = new JLabel("G");
        greenLabel.setToolTipText("Green");
        add(greenLabel, cc.xy(6, 2));

        JLabel blueLabel = new JLabel("B");
        blueLabel.setToolTipText("Blue");

        add(blueLabel, cc.xy(8, 2));

        JLabel alphaLabel = new JLabel("A");
        alphaLabel.setToolTipText("Alpha/Opacity");
        add(alphaLabel, cc.xy(10, 2));

        add(new JideButton(colorIcon), cc.xy(2, 4));
        add(new JLabel(" Range: "), cc.xy(2, 6));
        add(new JLabel("" + interval.getRed()), cc.xy(4, 4));
        add(new JLabel("" + interval.getGreen()), cc.xy(6, 4));
        add(new JLabel("" + interval.getBlue()), cc.xy(8, 4));
        add(new JLabel("" + interval.getAlpha()), cc.xy(10, 4));

        NumberFormat format = DecimalFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);

        lowField = new JFormattedTextField(format);
        highField = new JFormattedTextField(format);

        lowField.setValue(interval.getMinimum());
        highField.setValue(interval.getMaximum());

        if (!isEditable) {
            lowField.setEditable(false);
            highField.setEditable(false);
        }

        add(lowField, cc.xywh(4, 6, 3, 1));
        //add(new JLabel(JideIconsFactory.getImageIcon(JideIconsFactory.Arrow.RIGHT)), cc.xy(6,6));
        add(highField, cc.xywh(8, 6, 3, 1));


    }


    private ImageIcon createColorIcon() {
        BufferedImage bimg = new BufferedImage(ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2 = bimg.createGraphics();
        g2.setColor(interval.getColor());
        g2.fillRect(0, 0, ICON_WIDTH, ICON_HEIGHT);

        return new ImageIcon(bimg);

    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
        lowField.setEditable(editable);
        highField.setEditable(editable);
    }

    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "7.YTcWgxxjx1xjSnUqG:U1ldgGetfRn1");

        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2003_STYLE);
        } catch (Exception e) {
            Logger.getAnonymousLogger().severe("Could not load Look and Feel, aborting");
        }


        ColorInterval ival = new ColorInterval(new OpenClosedInterval(0, 100), Color.RED);
        JFrame frame = new JFrame();
        ColorIntervalEditor editor = new ColorIntervalEditor(ival);

        frame.add(editor, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }


}
