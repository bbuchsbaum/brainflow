package brainflow.gui;

import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.basic.ThemePainter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;

import de.javasoft.plaf.synthetica.SyntheticaWhiteVisionLookAndFeel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Nov 9, 2008
 * Time: 1:25:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionDialog {



    private Throwable throwable;

    private JTextArea textArea;

    private JPanel textPanel;

    private JScrollPane scrollPane;

    private JButton saveButton = new JButton("Save");

    private JButton exitButton = new JButton("Shutdown");


    public ExceptionDialog(Throwable throwable) {

        this.throwable = throwable;

        textArea = new JTextArea();
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        throwable.printStackTrace(out);
        // Add string to end of text area
        textArea.append(sw.toString());
        textArea.setRows(10);

        scrollPane = new JScrollPane(textArea);

        ButtonPanel bp = new ButtonPanel(SwingConstants.LEFT);
        bp.addButton(saveButton);
        bp.addButton(exitButton);
        bp.setBorder(new EmptyBorder(5, 1, 1, 1));

        textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(scrollPane, BorderLayout.CENTER);
        textPanel.add(bp, BorderLayout.SOUTH);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.getRootFrame().dispose();
                System.exit(1);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileName = "error_" + new Date().getTime() + ".txt";

                JFileChooser chooser = new JFileChooser();
                chooser.setSelectedFile(new File(fileName));
                int ret = chooser.showSaveDialog(JOptionPane.getRootFrame());

                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter(f);
                        ExceptionDialog.this.throwable.printStackTrace(new PrintWriter(writer));

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error saving file " + f);
                    } finally {
                        if (writer != null) {
                            try {
                                writer.close();
                            } catch (IOException exx) {   }

                        }
                    }

                }


            }
        });


    }


    public JDialog createDialog(Component parent) {
        JideOptionPane optionPane = new JideOptionPane("Click \"Details\" button to see more information ... ", JOptionPane.ERROR_MESSAGE, JideOptionPane.CLOSE_OPTION);
        optionPane.setTitle("An " + throwable.getClass().getName() + " occurred in BrainFlow : " + throwable.getMessage());


        optionPane.setDetails(textPanel);
        JDialog dialog = optionPane.createDialog(parent, "Warning");
        dialog.setResizable(true);
        dialog.pack();
  
        return dialog;

    }


    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
        try {
            UIManager.setLookAndFeel(new SyntheticaWhiteVisionLookAndFeel());


            LookAndFeelFactory.installJideExtension();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LookAndFeelFactory.UIDefaultsCustomizer uiDefaultsCustomizer = new LookAndFeelFactory.UIDefaultsCustomizer() {
            public void customize(UIDefaults defaults) {
                ThemePainter painter = (ThemePainter) UIDefaultsLookup.get("Theme.painter");
                defaults.put("OptionPaneUI", "com.jidesoft.plaf.basic.BasicJideOptionPaneUI");

                defaults.put("OptionPane.showBanner", Boolean.TRUE); // show banner or not. default is true
                //defaults.put("OptionPane.bannerIcon", JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50));
                defaults.put("OptionPane.bannerFontSize", 13);
                defaults.put("OptionPane.bannerFontStyle", Font.BOLD);
                defaults.put("OptionPane.bannerMaxCharsPerLine", 60);
                defaults.put("OptionPane.bannerForeground", painter != null ? painter.getOptionPaneBannerForeground() : null);  // you should adjust this if banner background is not the default gradient paint
                defaults.put("OptionPane.bannerBorder", null); // use default border

                // set both bannerBackgroundDk and // set both bannerBackgroundLt to null if you don't want gradient
                defaults.put("OptionPane.bannerBackgroundDk", painter != null ? painter.getOptionPaneBannerDk() : null);
                defaults.put("OptionPane.bannerBackgroundLt", painter != null ? painter.getOptionPaneBannerLt() : null);
                defaults.put("OptionPane.bannerBackgroundDirection", Boolean.TRUE); // default is true

                // optionally, you can set a Paint object for BannerPanel. If so, the three UIDefaults related to banner background above will be ignored.
                defaults.put("OptionPane.bannerBackgroundPaint", null);

                defaults.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder(6, 6, 6, 6));
                defaults.put("OptionPane.buttonOrientation", SwingConstants.RIGHT);
            }
        };
        uiDefaultsCustomizer.customize(UIManager.getDefaults());


        Throwable thr = new Exception();
        ExceptionDialog ed = new ExceptionDialog(thr);
        JFrame f = new JFrame();
        f.setSize(600, 600);
        f.setVisible(true);

        ed.createDialog(f).setVisible(true);


    }


}
