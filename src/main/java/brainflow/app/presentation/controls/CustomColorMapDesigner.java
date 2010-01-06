package brainflow.app.presentation.controls;

import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.swing.AutoResizingTextArea;
import com.jidesoft.swing.TitledSeparator;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.IndexColorModel;
import java.util.*;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.NumberFormat;
import java.util.List;

import brainflow.utils.IRange;
import brainflow.utils.Range;
import brainflow.colormap.DiscreteColorMap;
import brainflow.colormap.IColorMap;
import brainflow.colormap.ColorTable;
import brainflow.colormap.AbstractColorBar;
import brainflow.colormap.forms.ColorBarWithAxis;
import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;
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
    //private JTextArea inputArea = new JTextArea("");

    private JPanel colorBarPanel = new JPanel();

    private JCheckBox invertColorCheckBox = new JCheckBox("invert colors");

    private boolean invertColors = false;

    private JLabel gradientSelectionLabel = new JLabel("Auto Gradient:");

    private JComboBox gradientSelection;

    private JFormattedTextField minValueField = new JFormattedTextField(NumberFormat.getNumberInstance());

    private JFormattedTextField maxValueField = new JFormattedTextField(NumberFormat.getNumberInstance());

    private JButton clearButton = new JButton("Clear");

    private JComboBox sequenceFunction = new JComboBox();

    private JSpinner numSegments = new JSpinner(new SpinnerNumberModel(10, 1, 255, 1));

    private String lastValidInput = "";

    private List<Double> inputSequence = new ArrayList<Double>();


    public CustomColorMapDesigner() {
        buildGUI();
    }

    public CustomColorMapDesigner(IRange valueRange) {
        this.valueRange = valueRange;
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
        add(gradientSelectionLabel, "split 6");
        add(gradientSelection);
        add(new JLabel("from:"), "gap left 30");
        add(minValueField);
        add(new JLabel("to:"));
        add(maxValueField, "wrap 15px");

        add(colorBarPanel, "grow, wrap");
        add(invertColorCheckBox, "align right, wrap");


        add(new TitledSeparator("Color Sequence Editor"), "growx, wrap 13px");
        JScrollPane textPane = new JScrollPane(inputArea);
        add(textPane, "grow, wrap 8px");
        add(clearButton, "split 6");

        add(sequenceFunction, "gap left 50, growx");
        add(new JLabel("Segments: "), "gap left 10");
        add(numSegments);
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

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputArea.setText("");
                lastValidInput = "";
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

    private void updateColorBar(List<Double> values) {
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

        inputArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    int caretPos = inputArea.getCaretPosition();
                    if (caretPos == inputArea.getText().length() && inputArea.getText().charAt(inputArea.getText().length() - 1) == ' ') {
                        interpolateRight();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }


    private void interpolateRight() {
        if (inputSequence.size() >= 2) {
            double vlast = inputSequence.get(inputSequence.size() - 1);
            double vpen = inputSequence.get(inputSequence.size() - 2);
            double diff = vlast - vpen;
            double vnext = Math.min(vlast + diff, valueRange.getMax());
            if (vnext == vlast) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            inputArea.setText(inputArea.getText() + " " + vnext + " ");
        }

    }

    private boolean inBounds(List<Double> vals) {
        if (vals.size() == 0) return true;

        double val = vals.get(vals.size() - 1);
        if (val < valueRange.getMin() || val > valueRange.getMax()) {
            return false;
        } else {
            return true;
        }
    }

    private void editError(String message) {
        JideOptionPane.showMessageDialog(this, message);
        inputArea.setEnabled(false);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                inputArea.setText(lastValidInput);
                inputArea.setEnabled(true);
            }
        });

    }

    private void processInput(DocumentEvent e) {
        int endpos = e.getDocument().getEndPosition().getOffset();

        String text = inputArea.getText();
        System.out.println("-->" + text);
        if (text.length() >= 2 && text.substring(text.length() - 1).equals(" ")) {
            try {
                List<Double> ret = parseValues(inputArea.getText());
                if (!inBounds(ret)) {
                    editError(ret.get(ret.size() - 1) + " outside intensity range : " + valueRange);
                    return;
                }
                if (isMonotonic(ret)) {
                    inputSequence = ret;
                    updateColorBar(ret);
                    lastValidInput = text;
                } else {
                    editError("invalid input: number sequence must be increasing");

                }
            } catch (NumberFormatException ex) {
                editError("\"invalid input: failed to parse number sequence");


            }
        }

    }


    private List<Double> parseValues(String text) throws NumberFormatException {
        StringTokenizer tokenizer = new StringTokenizer(text, ", ");
        Double[] ret = new Double[tokenizer.countTokens()];

        int i = -0;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            ret[i] = Double.parseDouble(token);
            i++;

        }

        return Arrays.asList(ret);
    }

    private boolean isMonotonic(List<Double> vals) {
        if (vals.size() <= 1) return true;
        double last = vals.get(0);

        for (int i = 1; i < vals.size(); i++) {
            if (vals.get(i) <= last) return false;
            last = vals.get(i);
        }

        return true;
    }

    private double[] toDoubleArray(List<Double> vals) {
        double[] ret = new double[vals.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = vals.get(i);
        }

        return ret;

    }

    private IColorMap createMap(IRange range, List<Double> interior) {
        double[] boundaries = new double[interior.size() + 2];
        System.arraycopy(toDoubleArray(interior), 0, boundaries, 1, interior.size());
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

    public IColorMap getColorMap() {
        return colorMap;
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
            UIManager.setLookAndFeel(new SyntheticaStandardLookAndFeel());
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
