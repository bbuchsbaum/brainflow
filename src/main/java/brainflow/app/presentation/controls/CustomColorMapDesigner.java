package brainflow.app.presentation.controls;

import com.jidesoft.swing.AutoResizingTextArea;
import com.jidesoft.swing.TitledSeparator;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.image.IndexColorModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.NumberFormat;

import brainflow.utils.IRange;
import brainflow.utils.Range;
import brainflow.colormap.DiscreteColorMap;
import brainflow.colormap.IColorMap;
import brainflow.colormap.ColorTable;
import brainflow.colormap.AbstractColorBar;
import brainflow.colormap.forms.ColorBarWithAxis;
import net.miginfocom.swing.MigLayout;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 16, 2009
 * Time: 7:53:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomColorMapDesigner extends JPanel {

    private IRange valueRange = new Range(0, 255);

    private IColorMap colorMap = new DiscreteColorMap(Arrays.asList(Color.RED), Arrays.asList(0d, 255d));

    private AutoResizingTextArea inputArea = new AutoResizingTextArea("", 4, 10, 10);

    private JPanel colorBarPanel = new JPanel();

    private JCheckBox invertColorCheckBox = new JCheckBox("invert colors");

    private boolean invertColors = false;

    private JLabel gradientSelectionLabel = new JLabel("Auto Gradient:");

    private JComboBox gradientSelection;

    private JFormattedTextField minValueField = new JFormattedTextField(NumberFormat.getNumberInstance());

    private JFormattedTextField maxValueField = new JFormattedTextField(NumberFormat.getNumberInstance());


    public CustomColorMapDesigner() {
        buildGUI();
    }

    private void buildGUI() {
        MigLayout layout = new MigLayout();
        setLayout(layout);

        gradientSelection = createComboBox();

        minValueField.setColumns(6);
        maxValueField.setColumns(6);
        minValueField.setEditable(false);
        maxValueField.setEditable(false);

        minValueField.setValue(valueRange.getMin());
        maxValueField.setValue(valueRange.getMax());

        colorBarPanel.setLayout(new BorderLayout());
        colorBarPanel.add(createColorBar(colorMap), BorderLayout.CENTER);
        colorBarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        add(new TitledSeparator("Color Bar"), "growx, wrap 13px");

        add(colorBarPanel, "grow, wrap");
        add(invertColorCheckBox, "align right, wrap");
        add(new TitledSeparator("Color Sequence Editor"), "growx, wrap 13px");
        add(new JScrollPane(inputArea), "growx, wrap 8px");
        add(gradientSelectionLabel, "split 6");
        add(gradientSelection);
        add(new JLabel("from:"), "gap left 30");
        add(minValueField);
        add(new JLabel("to:"));
        add(maxValueField, "wrap");

        //add(defineGradientButton, "gap left 40, wrap");

        initTextListener();
        invertColorCheckBox.setSelected(false);
        invertColorCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                invertColors = invertColorCheckBox.isSelected();
                updateColorBar(parseValues(inputArea.getText()));
            }

        });

    }

    private JComboBox createComboBox() {
        String[] names = new String[ColorTable.COLOR_MAPS.size()];
        ColorTable.COLOR_MAPS.keySet().toArray(names);
        gradientSelection = new JComboBox(names);
        gradientSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateColorBar(parseValues(inputArea.getText()));

            }
        });

        return gradientSelection;


    }

    private ColorBarWithAxis createColorBar(IColorMap map) {
        AbstractColorBar cbar = map.createColorBar();
        cbar.setPreferredSize(new Dimension(350, 125));
        cbar.setBorder(BorderFactory.createEtchedBorder());
        return new ColorBarWithAxis(cbar);

    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 400);
    }

    private void updateColorBar(double[] values) {
        colorMap = createMap(valueRange, values);
        colorBarPanel.removeAll();
        colorBarPanel.add(createColorBar(colorMap), BorderLayout.CENTER);
        revalidate();
    }

    private void initTextListener() {
        inputArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                processInput(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                processInput(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                processInput(e);
            }
        });
    }

    private void processInput(DocumentEvent e) {
        int endpos = e.getDocument().getEndPosition().getOffset();
        System.out.println("endpos = " + endpos);
        String text = inputArea.getText();
        System.out.println("-->" + text);
        if (text.length() >= 2 && text.substring(text.length() - 1).equals(" ")) {
            try {
                double[] ret = parseValues(inputArea.getText());
                if (isMonotonic(ret)) {
                    updateColorBar(ret);
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

    }


    private double[] parseValues(String text) throws NumberFormatException {
        StringTokenizer tokenizer = new StringTokenizer(text, ", ");
        double[] ret = new double[tokenizer.countTokens()];

        int i = -0;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            ret[i] = Double.parseDouble(token);
            i++;

        }

        return ret;
    }

    private boolean isMonotonic(double[] vals) {
        double last = vals[0];
        if (vals.length == 0) return true;
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] <= last) return false;
            last = vals[i];
        }

        return true;
    }

    private IColorMap createMap(IRange range, double[] interior) {
        double[] boundaries = new double[interior.length + 2];
        System.arraycopy(interior, 0, boundaries, 1, interior.length);
        boundaries[0] = range.getMin();
        boundaries[boundaries.length - 1] = range.getMax();

        IndexColorModel model = ColorTable.COLOR_MAPS.get(gradientSelection.getSelectedItem().toString());
        System.out.println("model size : " + model.getMapSize());
        if (invertColors) {
            System.out.println("inverting colors");
            return new DiscreteColorMap(ColorTable.createColorGradient(ColorTable.invert(model), boundaries.length - 1), boundaries);
        } else {

            return new DiscreteColorMap(ColorTable.createColorGradient(model, boundaries.length - 1), boundaries);
        }
    }


    private boolean isLegal(String text) {

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));

        try {
            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                if (tokenizer.ttype == StreamTokenizer.TT_WORD) {
                    System.out.println("illegal token " + tokenizer.sval);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }


        return true;


    }


    /*class ColorBarPanel extends JPanel {

        ColorBarWithAxis colorBar;

        ColorBarPanel(ColorBarWithAxis colorBar) {
            this.colorBar = colorBar;
        }

        public ColorBarWithAxis getColorBar() {
            return colorBar;
        }

        public void setColorBar(ColorBarWithAxis colorBar) {
            this.colorBar = colorBar;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            colorBar.paint(g);
        }
    } */


    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
        try {
            UIManager.setLookAndFeel(new de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        LookAndFeelFactory.installJideExtension();
        CustomColorMapDesigner designer = new CustomColorMapDesigner();
        JFrame jf = new JFrame();
        jf.add(designer, BorderLayout.CENTER);
        jf.setSize(600, 600);
        jf.setVisible(true);

        //jf.pack();
    }


}
