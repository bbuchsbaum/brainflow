package brainflow.application.presentation;

import brainflow.colormap.*;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;
import com.jidesoft.combobox.ColorComboBox;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.IndexColorModel;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 1, 2007
 * Time: 3:19:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorGradientEditor extends JPanel {

    public static final String ONE_COLOR_GRADIENT = "ONE_COLOR";

    public static final String TWO_COLOR_GRADIENT = "TWO_COLOR";


    public static final String GRADIENT_SETTING_PROPERTY = "gradientSetting";

    private ColorComboBox colorComboOne;

    private ColorComboBox colorComboTwo;

    private IColorMap colorMap;

    private AbstractColorBar colorBar;


    private Color colorOne = Color.RED;

    private Color colorTwo = Color.GREEN;

    private String gradientSetting = ONE_COLOR_GRADIENT;

    private JRadioButton oneColorButton = new JRadioButton();

    private JRadioButton twoColorButton = new JRadioButton();

   // private PresentationModel choiceModel;

    //private ExecutorService threadService = Executors.newSingleThreadExecutor();


    public ColorGradientEditor() {
        colorMap = new ConstantColorMap(0, 255, colorOne);
        initGUI();


    }


    public ColorGradientEditor(double min, double max) {
        colorMap = new ConstantColorMap(min, max, colorOne);
        initGUI();


    }


    private void initGUI() {
        colorComboOne = new ColorComboBox();
        colorComboOne.setSelectedColor(colorOne);

        colorComboTwo = new ColorComboBox();
        colorComboTwo.setSelectedColor(colorTwo);

    //    choiceModel = new PresentationModel(this);
    //    ValueModel gradientValue = choiceModel.getModel(ColorGradientEditor.GRADIENT_SETTING_PROPERTY);
     //   oneColorButton = BasicComponentFactory.createRadioButton(gradientValue, ColorGradientEditor.ONE_COLOR_GRADIENT, "One Color");
     //   twoColorButton = BasicComponentFactory.createRadioButton(gradientValue, ColorGradientEditor.TWO_COLOR_GRADIENT, "Two Color");


        initBindings();
        initLayout();

    }

    private void initBindings() {

        colorComboOne.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                colorOne = colorComboOne.getSelectedColor();
                updateColorMap();
            }
        });

        colorComboTwo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                colorTwo = colorComboTwo.getSelectedColor();
                updateColorMap();
            }
        });

        oneColorButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                gradientSetting = ONE_COLOR_GRADIENT;
                updateColorMap();
            }
        });

        twoColorButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                gradientSetting = TWO_COLOR_GRADIENT;
                updateColorMap();
            }
        });



    }


    private void initLayout() {

        JPanel mainPanel = new JPanel();
        FormLayout layout = new FormLayout("l:p:g, 12dlu, l:p, 3dlu, p:g");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, mainPanel);
        layout.setColumnGroups(new int[][]{{2, 4}});
        builder.setLineGapSize(Sizes.dluX(8));

        builder.appendSeparator("Preview");

        colorBar = colorMap.createColorBar();
        colorBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        builder.append(colorBar, 5);
        builder.appendSeparator("Colors");
        builder.nextLine();
        builder.setDefaultDialogBorder();
        builder.append(oneColorButton);

        builder.append("Color 1:", colorComboOne);
        builder.nextLine();
        builder.append(twoColorButton);

        builder.append("Color 2: ", colorComboTwo);


        setLayout(new BorderLayout());
        add(builder.getPanel(), BorderLayout.CENTER);


    }

    public IColorMap getColorMap() {
        return colorMap;
    }

    public String getGradientSetting() {
        return gradientSetting;
    }

    private void updateColorMap() {
        if (gradientSetting == ColorGradientEditor.TWO_COLOR_GRADIENT) {
            IndexColorModel icm = ColorTable.createIndexColorModel(ColorTable.createColorGradient(colorOne, colorTwo, 256));
            colorMap = new LinearColorMap2(colorMap.getMinimumValue(), colorMap.getMaximumValue(), icm);
            colorBar.setColorMap(colorMap);
        } else {
            IndexColorModel icm = ColorTable.createConstantMap(colorOne);
            colorMap = new LinearColorMap2(colorMap.getMinimumValue(), colorMap.getMaximumValue(), icm);
            colorBar.setColorMap(new LinearColorMap2(0, 100, icm));

        }

    }

    public void setGradientSetting(String gradientSetting) {
        String oldSetting = getGradientSetting();
        this.gradientSetting = gradientSetting;

        if (gradientSetting == ColorGradientEditor.TWO_COLOR_GRADIENT) {
            colorComboTwo.setEnabled(true);
        } else {
            colorComboTwo.setEnabled(false);

        }

        updateColorMap();
        firePropertyChange(ColorGradientEditor.GRADIENT_SETTING_PROPERTY, oldSetting, getGradientSetting());


    }


    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");

        try {

            //UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceModerateLookAndFeel());

            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE);

        } catch (Exception e) {
            Logger.getAnonymousLogger().severe("Error Loading LookAndFeel, exiting");
            e.printStackTrace();
            System.exit(-1);

        }
        JFrame frame = new JFrame();
        ColorGradientEditor editor = new ColorGradientEditor();
        frame.add(editor);
        frame.pack();
        frame.setVisible(true);
    }
}
