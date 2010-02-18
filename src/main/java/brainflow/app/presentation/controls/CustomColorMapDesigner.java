package brainflow.app.presentation.controls;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.AutoResizingTextArea;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSplitButton;
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
import java.text.NumberFormat;
import java.util.List;

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

    private IndexColorModel selectedModel = ColorTable.COLOR_MAPS.get("Spectrum");

    private AutoResizingTextArea inputArea = new AutoResizingTextArea("", 4, 10, 10);
    //private JTextArea inputArea = new JTextArea("");

    private JPanel colorBarPanel = new JPanel();

    private JCheckBox invertColorCheckBox = new JCheckBox("invert colors");

    private boolean invertColors = false;

    private JLabel gradientSelectionLabel = new JLabel("Auto Gradient:");



    private JideSplitButton colorMapSelector;

    private JideSplitButton generateValues = new JideSplitButton("generate");

    private JFormattedTextField minValueField = new JFormattedTextField(NumberFormat.getNumberInstance());

    private JFormattedTextField maxValueField = new JFormattedTextField(NumberFormat.getNumberInstance());

    private JFormattedTextField autoGap = new JFormattedTextField(NumberFormat.getNumberInstance());

    private JButton clearButton = new JButton("Clear");

    private JideButton interpRightButton = new JideButton("extrapolate next");

    private JideSplitButton autoInterpButton = new JideSplitButton("auto generate");


    //private JComboBox sequenceFunction = new JComboBox();


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


        colorMapSelector = createColorMapSelector();

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
        add(colorMapSelector, "width 100:150:200");

        add(new JLabel("from:"), "gap left 60");
        add(minValueField);
        add(new JLabel("to:"));
        add(maxValueField, "wrap 15px");

        add(colorBarPanel, "grow, wrap");
        add(invertColorCheckBox, "align right, wrap");


        add(new TitledSeparator("Color Map Boundaries"), "growx, wrap 13px");
        JScrollPane textPane = new JScrollPane(inputArea);
        add(textPane, "grow, wrap 8px");

        
        add(clearButton, "split 5");

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/control_play_blue.png"));
        interpRightButton.setIcon(icon);
        icon = new ImageIcon(getClass().getClassLoader().getResource("icons/control_fastforward_blue.png"));
        autoInterpButton.setIcon(icon);

        add(interpRightButton, "gap left 60");
        add(autoInterpButton);
        add(new JLabel("auto gap: "));
        add(autoGap, "wrap, width 40:50:60");

        autoGap.setValue(valueRange.getInterval()/10.0);
       

        //add(defineGradientButton, "gap left 40, wrap");

        initTextListener();
        invertColorCheckBox.setSelected(false);
        invertColorCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                invertColors = invertColorCheckBox.isSelected();
                updateColorBar(parseValues(inputArea.getText()), selectedModel);
            }

        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputArea.setText("");
                lastValidInput = "";
            }


        });

        autoInterpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = true;
                while(success) {
                    success = advanceValue();
                }
            }
        });

        interpRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                advanceValue();
            }
        });




    }

    private void setSelectedModel(IndexColorModel model) {
        selectedModel = model;
    }



    /*private JComboBox createComboBox() {
        String[] names = new String[ColorTable.COLOR_MAPS.size()];
        ColorTable.COLOR_MAPS.keySet().toArray(names);
        gradientSelection = new JComboBox(names);
        gradientSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedModel = ColorTable.COLOR_MAPS.get(gradientSelection.getSelectedItem());
                updateColorBar(parseValues(inputArea.getText()), selectedModel);

            }
        });

        return gradientSelection;


    }*/

    private JideSplitButton createColorMapSelector() {
        String[] names = new String[ColorTable.COLOR_MAPS.size()];
        ColorTable.COLOR_MAPS.keySet().toArray(names);
        colorMapSelector = new JideSplitButton(names[0]);
        final JidePopup popup = new JidePopup();
        final ColorSequenceBuilder sequenceBuilder = new ColorSequenceBuilder();


        colorMapSelector.add(new AbstractAction("Custom Colors") {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorMapSelector.setText("Custom Colors");

                popup.getContentPane().setLayout(new BorderLayout());


                popup.add(sequenceBuilder, BorderLayout.CENTER);
                popup.setOwner(colorMapSelector);
                popup.setResizable(true);
                popup.setMovable(true);

                ButtonPanel pane = new ButtonPanel(SwingConstants.LEFT);

                JButton ok = new JButton("OK");
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        popup.hidePopupImmediately();
                        IndexColorModel model = ColorTable.createIndexColorModel(sequenceBuilder.getColorSequence());
                        setSelectedModel(model);
                        updateColorBar(parseValues(inputArea.getText()), selectedModel);
                    }
                });

                pane.add(ok, ButtonPanel.AFFIRMATIVE_BUTTON);
                pane.add(new JButton("Cancel"), ButtonPanel.CANCEL);
                pane.setBorder(BorderFactory.createEmptyBorder(8, 6, 8, 2));


                popup.add(pane, BorderLayout.SOUTH);
                popup.showPopup();

                /*ColorSequenceBuilder builder = new ColorSequenceBuilder();
                ButtonPanel pane = new ButtonPanel(SwingConstants.LEFT);
                pane.add(new JButton("OK"), ButtonPanel.AFFIRMATIVE_BUTTON);
                pane.add(new JButton("Cancel"), ButtonPanel.CANCEL);

                JDialog dialog = new JDialog();
                dialog.add(builder, BorderLayout.CENTER);
                dialog.add(pane, BorderLayout.SOUTH);
                dialog.pack();
                dialog.setVisible(true); */


            }
        });

         for (final String map : names) {
             Action colorAction = new AbstractAction(map, ColorTable.createImageIcon(ColorTable.COLOR_MAPS.get(map), 30, 12)) {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     colorMapSelector.setText(map);
                     selectedModel = ColorTable.COLOR_MAPS.get(map);
                     updateColorBar(parseValues(inputArea.getText()), selectedModel);
                 }
             };
             colorMapSelector.add(colorAction);
         }

       

        return colorMapSelector;

    }

    private ColorBarWithAxis createColorBar(IColorMap map) {
        AbstractColorBar cbar = map.createColorBar();
        cbar.setPreferredSize(new Dimension(350, 125));
        cbar.setBorder(BorderFactory.createEtchedBorder());
        return new ColorBarWithAxis(cbar);

    }


    //@Override
    //public Dimension getPreferredSize() {
    //    return new Dimension(600, 400);
    //}

    private void updateColorBar(List<Double> values, IndexColorModel model) {
        colorMap = createMap(valueRange, values, model);
        colorBarPanel.removeAll();
        colorBarPanel.add(createColorBar(colorMap), BorderLayout.CENTER);
        revalidate();
    }

    private void initTextListener() {
        inputArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                System.out.println("insert update");
                processInput(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                System.out.println("remove update");
                processInput(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                System.out.println("change update");
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
                    advanceValue();
                }


            }

            @Override
            public void keyReleased(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }

    private boolean advanceValue() {
        int caretPos = inputArea.getCaretPosition();

        if (inputArea.getText().trim().length() <= 0) {
            return interpolateRight();
        }

        inputArea.setText(trimText(inputArea.getText()));

        System.out.println("caret pos " + caretPos);
        System.out.println("text length " + inputArea.getText().length());
        System.out.println("last char " + inputArea.getText().charAt(inputArea.getText().length() - 1));
        String text = trimText(inputArea.getText());

        if (caretPos >= 0 && caretPos >= inputArea.getText().length() && inputArea.getText().charAt(inputArea.getText().length() - 1) == ' ') {
            return interpolateRight();
        } else if (caretPos == inputArea.getText().length()) {
            List<Double> ret = parseValues(inputArea.getText());
            if (isMonotonic(ret)) {
                updateInputSequence(ret);
                return interpolateRight();

            }
        }

        System.out.println("cannot next");

        return false;
    }


    private boolean setNextValue(double vnext) {
        double vlast = valueRange.getMin();

        if (inputSequence.size() > 0) {
            vlast = inputSequence.get(inputSequence.size() - 1);
        }
        
        if (vnext == vlast) {
            Toolkit.getDefaultToolkit().beep();
            return false;
        }

        inputArea.setText(inputArea.getText().trim() + " " + vnext + " ");
        return true;
    }


    private boolean interpolateRight() {
        if (inputSequence.size() == 0) {

            double diff = ((Number)autoGap.getValue()).doubleValue();
            double vnext = valueRange.getMin() + diff;
             if (vnext < valueRange.getMax())
                return setNextValue(vnext);
        }
        if (inputSequence.size() == 1) {
            double vlast = inputSequence.get(0);
            double diff = ((Number)autoGap.getValue()).doubleValue();
            double vnext = vlast + diff;
            if (vnext < valueRange.getMax())
                return setNextValue(vnext);
        } else if (inputSequence.size() >= 2) {
            double vlast = inputSequence.get(inputSequence.size() - 1);
            //double vpen = inputSequence.get(inputSequence.size() - 2);
            double diff = ((Number)autoGap.getValue()).doubleValue();
            double vnext = vlast + diff;
            if (vnext < valueRange.getMax())
               return setNextValue(vnext);
        }

        System.out.println("cannot interpolate right");
        System.out.println("inputSequence is " + Arrays.toString(inputSequence.toArray()));
        return false;

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

    private void updateInputSequence(List<Double> sequence) {
        System.out.println("updating input sequence");
        if (isMonotonic(sequence)) {
            System.out.println("");
            inputSequence = sequence;
            System.out.println("input sequence is " + Arrays.toString(inputSequence.toArray()));
            updateColorBar(inputSequence, selectedModel);
            lastValidInput = inputArea.getText();
        } else {
            editError("invalid input: number sequence must be increasing");

        }

    }

    private String trimText(String text) {
        if (text.length() > 2) {
            if (text.substring(text.length() - 1).matches("\\s")) {
                return text.trim() + " ";
            }
        }

        return text;
    }

    private void processInput(DocumentEvent e) {
        int endpos = e.getDocument().getEndPosition().getOffset();

        String text = trimText(inputArea.getText());




        System.out.println("-->" + text);
        if (text.length() == 0) {
            updateInputSequence(new ArrayList<Double>());
        } else if (text.length() >= 2 && text.substring(text.length() - 1).equals(" ")) {
            try {
                List<Double> ret = parseValues(text);
                System.out.println("numbers: " + Arrays.toString(ret.toArray()));
                if (!inBounds(ret)) {
                    editError(ret.get(ret.size() - 1) + " outside intensity range : " + valueRange);
                    return;
                }

                updateInputSequence(ret);

            } catch (NumberFormatException ex) {
                editError("\"invalid input: failed to parse number sequence");


            }
        } //else {
          //  List<Double> ret = parseValues(text);
          //  if (!ret.equals(inputSequence)) {

          //  }
        //}

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

    private IColorMap createMap(IRange range, List<Double> interior, IndexColorModel model) {
        System.out.println("interior.size() " + interior.size());
        double[] boundaries = new double[interior.size() + 2];
        System.arraycopy(toDoubleArray(interior), 0, boundaries, 1, interior.size());
        boundaries[0] = range.getMin();
        boundaries[boundaries.length - 1] = range.getMax();


        if (invertColors) {
            return new DiscreteColorMap(ColorTable.createColorGradient(ColorTable.invert(model), boundaries.length - 1), boundaries);
        } else {
            //todo this should perform true interpolation between discrete items, especially when oversampling color map.
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
            LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
            //UIManager.setLookAndFeel(new SyntheticaStandardLookAndFeel());
        //} //catch (UnsupportedLookAndFeelException e) {
          //  e.printStackTrace();
        } catch (Exception e) {
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
