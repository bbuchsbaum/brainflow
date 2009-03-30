package brainflow.app.presentation.controls;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import com.jidesoft.hints.ListDataIntelliHints;
import com.jidesoft.swing.FolderChooser;
import com.jidesoft.swing.SelectAllUtils;
import com.jidesoft.dialog.JideOptionPane;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 24, 2007
 * Time: 10:51:57 AM
 */
public class SnapshotForm extends JPanel {

    private RenderedImage snapShot;

    private JTextField fileNameField;

    private JComboBox filePathComboBox;

    private JComboBox imageFormatComboBox;

    private JLabel snapShotLabel;

    private List<String> recentDirectories = new ArrayList<String>();

    private JButton browseButton = new JButton("Browse");

    private JButton saveButton = new JButton("Save");

    private JButton cancelButton = new JButton("Cancel");

    private ListDataIntelliHints fileHints;

    private String[] imageFormats;

    private String formatSelection = ".png";


    private Action saveAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    private Action cancelAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    public SnapshotForm(RenderedImage snapShot) {
        this.snapShot = snapShot;
        String userDir = System.getProperty("user.home");
        recentDirectories.add(userDir);
        initFormats();
        buildGUI();
    }

    private void initFormats() {
        imageFormats = ImageIO.getWriterFileSuffixes();
    }

    public Action getSaveAction() {
        return saveAction;
    }

    public void setSaveAction(Action saveAction) {
        this.saveAction = saveAction;
    }

    public Action getCancelAction() {
        return cancelAction;
    }

    public void setCancelAction(Action cancelAction) {
        this.cancelAction = cancelAction;
    }

    private void buildGUI() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        WritableRaster raster = WritableRaster.createWritableRaster(snapShot.getSampleModel(), snapShot.getData().getDataBuffer(), new Point(0,0));

        ImageIcon icon = new ImageIcon(new BufferedImage(snapShot.getColorModel(), raster, false, null));

        JPanel tp = new JPanel();
        snapShotLabel = new JLabel(icon);

        tp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Image Snapshot"), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        JPanel bp = new JPanel();
        FormLayout layout = new FormLayout("5dlu, l:p, 3dlu, l:max(60dlu;p):g, 1dlu, 3dlu, p, 8dlu", "8dlu, p, 10dlu, p, 10dlu, p, 18dlu, p, 8dlu");
        CellConstraints cc = new CellConstraints();


        bp.setLayout(layout);


        imageFormatComboBox = new JComboBox(imageFormats);
        JLabel formatLabel = new JLabel("Image Format: ");

        bp.add(formatLabel, cc.xy(2, 2));
        bp.add(imageFormatComboBox, cc.xyw(4, 2, 2));

        JLabel fileNameLabel = new JLabel("File Name: ");
        bp.add(fileNameLabel, cc.xy(2, 4));

        fileNameField = new JTextField();
        bp.add(fileNameField, cc.xyw(4, 4, 2));


        JLabel filePathLabel = new JLabel("File Path: ");
        bp.add(filePathLabel, cc.xy(2, 6));

        filePathComboBox = new JComboBox(recentDirectories.toArray());
        filePathComboBox.setEditable(true);
        bp.add(filePathComboBox, cc.xyw(4, 6, 2));

        //add(saveButton, cc.xy(7, 4));
        bp.add(browseButton, cc.xy(7, 6));


        ButtonBarBuilder builder = new ButtonBarBuilder();
        //builder.setHAlignment(CellConstraints.RIGHT);
        builder.addGridded(saveButton);
        builder.addRelatedGap();
        builder.addGridded(cancelButton);
        //builder.setBorder(BorderFactory.createRaisedBevelBorder()); 
        bp.add(builder.getPanel(), cc.xyw(2, 8, 3));

        JScrollPane jsp = new JScrollPane(snapShotLabel);
        jsp.setPreferredSize(new Dimension((int)Math.min(600, snapShotLabel.getPreferredSize().getWidth()),400));
        tp.add(jsp, BorderLayout.CENTER);
        add(tp);
        add(bp);

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelAction.actionPerformed(e);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String path = filePathComboBox.getSelectedItem().toString();
                File ofile = new File(path + File.separatorChar + fileNameField.getText() + "." + imageFormatComboBox.getSelectedItem().toString());

                FileImageOutputStream stream = null;
                try {
                    stream = new FileImageOutputStream(ofile);
                    ImageIO.write(snapShot, imageFormatComboBox.getSelectedItem().toString(), stream);

                } catch(IOException ex) {
                    JideOptionPane.showMessageDialog(SnapshotForm.this, "Error writing snapshot image to file : " + ofile);
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch(IOException ex2) {}
                        
                    }
                }


                saveAction.actionPerformed(e);



            }
        });

        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String path = filePathComboBox.getSelectedItem().toString();
                File fdir = new File(path);

                FolderChooser chooser;

                if (fdir.isDirectory()) {
                    chooser = new FolderChooser(fdir);
                } else {
                    chooser = new FolderChooser(recentDirectories.get(0));
                }

                int ret = chooser.showOpenDialog(SnapshotForm.this);
                if (ret == FolderChooser.APPROVE_OPTION) {
                    String npath = chooser.getSelectedFile().getPath();
                    filePathComboBox.addItem(npath);
                    filePathComboBox.setSelectedItem(npath);
                    updateHints();

                }


            }
        });

        SelectAllUtils.install(fileNameField);
        fileHints = new ListDataIntelliHints(fileNameField, getHintsForPath(filePathComboBox.getSelectedItem().toString()));
        fileHints.setCaseSensitive(false);


        filePathComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                checkValid();
            }

        });

        fileNameField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void keyReleased(KeyEvent e) {
                checkValid();
            }

            public void keyPressed(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }


        });

        checkValid();


    }

    private void updateHints() {
        String path = filePathComboBox.getSelectedItem().toString();
        String[] hints = getHintsForPath(path);
        fileHints.setCompletionList(hints);

    }

    private void checkValid() {
        if (isValidFile()) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }


    private String[] getHintsForPath(String path) {
        File pathFile = new File(path);
        String[] fileNames = pathFile.list();
        return fileNames;
    }

    public boolean isValidFile() {
        File fdir = new File(filePathComboBox.getSelectedItem().toString());
        if (fdir.isDirectory() && (fileNameField.getText().length() > 0)) {
            return true;
        } else {
            return false;
        }
    }

    public String getFilePath() {
        String path = filePathComboBox.getSelectedItem().toString();
        String fileName = fileNameField.getText();

        return path + File.separatorChar + fileName;
    }


    public RenderedImage getSnapShot() {
        return snapShot;
    }

    public JTextField getFileNameField() {
        return fileNameField;
    }

    public JComboBox getFilePathComboBox() {
        return filePathComboBox;
    }

    public JLabel getSnapShotLabel() {
        return snapShotLabel;
    }

    public List<String> getRecentDirectories() {
        return recentDirectories;
    }

    public JButton getBrowseButton() {
        return browseButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public ListDataIntelliHints getFileHints() {
        return fileHints;
    }

    public static void main(String[] args) {
        try {

            //UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
            BufferedImage bimg = ImageIO.read(ClassLoader.getSystemResource("resources/data/axial_slice.png"));
            SnapshotForm form = new SnapshotForm(bimg);


            String[] formats = ImageIO.getWriterFileSuffixes();
            
            JFrame frame = new JFrame();
            frame.add(form, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
