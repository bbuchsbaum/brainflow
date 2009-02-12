/*
 * FileListEditor.java
 *
 * Created on April 28, 2003, 1:48 PM
 */

package brainflow.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;


/**
 * @author Bradley
 */
public class FileListEditor extends JPanel implements DNDComponentInterface {

    DNDList fileList;
    JScrollPane listPane;
    JToolBar toolbar;
    String lastDir;

    JButton addButton;
    JButton removeButton;
    JButton shiftUpButton;
    JButton shiftDownButton;

    DefaultListModel listModel = new DefaultListModel();

    FilenameFilter filter;
    ArrayList files = new ArrayList();


    public static String FILE_ADDED_PROPERTY = "File Added";
    public static String FILE_REMOVED_PROPERTY = "File Remove";
    public static String FILE_DOUBLE_CLICKED_PROPERTY = "File Double Clicked";

    /**
     * Creates a new instance of FileListEditor
     */
    public FileListEditor() {
        init();
    }

    public FileListEditor(String startDir) {
        lastDir = startDir;
        init();
    }

    public void setFilenameFilter(FilenameFilter _filter) {
        filter = _filter;
    }

    public File[] getSelectedFiles() {
        int[] idx = fileList.getSelectedIndices();
        File[] sfiles = new File[idx.length];
        for (int i = 0; i < sfiles.length; i++) {
            sfiles[i] = (File) files.get(idx[i]);
        }

        return sfiles;
    }


    public static void main(String[] args) {

        JFrame f = new JFrame();
        JInternalFrame jif = new JInternalFrame("Internal Frame", true, true);
        jif.getContentPane().add(new FileListEditor());
        jif.pack();
        jif.setVisible(true);
        jif.setResizable(false);
        f.getContentPane().add(jif);
        f.pack();
        f.setVisible(true);
    }

    private void init() {
        fileList = new DNDList(this);
        fileList.setModel(listModel);
        addButton = new JButton();
        removeButton = new JButton();
        listPane = new JScrollPane(fileList);

        try {
            addButton.setIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("resources/icons/add.png"))));
            addButton.addActionListener(new AddAction());
            removeButton.setIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("resources/icons/remove.png"))));
            removeButton.setEnabled(false);
            removeButton.addActionListener(new RemoveAction());

            shiftUpButton = new JButton();
            shiftDownButton = new JButton();
            shiftUpButton.setIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("resources/icons/up.png"))));
            shiftDownButton.setIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("resources/icons/down.png"))));
            shiftUpButton.setEnabled(false);
            shiftDownButton.setEnabled(false);
            shiftUpButton.addActionListener(new ShiftUpAction());
            shiftDownButton.addActionListener(new ShiftDownAction());
        } catch (Exception e) {
            e.printStackTrace();
        }

        toolbar = new JToolBar(JToolBar.HORIZONTAL);
        toolbar.add(addButton);
        toolbar.add(removeButton);
        toolbar.addSeparator();
        toolbar.addSeparator();

        toolbar.add(shiftUpButton);
        toolbar.add(shiftDownButton);


        setLayout(new BorderLayout());
        add(toolbar, "North");
        add(listPane, "Center");

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                FileListEditor local = FileListEditor.this;
                int clicks = e.getClickCount();
                if (clicks >= 2) {
                    int idx = local.fileList.getSelectedIndex();
                    if (idx >= 0)
                        local.firePropertyChange(FileListEditor.FILE_DOUBLE_CLICKED_PROPERTY, "old", "new");
                }
            }

        };

        MouseMotionListener mouseMotionListener = new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                JList list = (JList) e.getSource();
                int index = list.locationToIndex(e.getPoint());
                if (index >= 0) {
                    File f = (File) FileListEditor.this.files.get(index);
                    list.setToolTipText(f.getAbsolutePath());
                } else {
                    list.setToolTipText("");
                }
            }

            public void mouseDragged(MouseEvent e) {
            }
        };

        fileList.addMouseMotionListener(mouseMotionListener);
        fileList.addMouseListener(mouseListener);

    }

    public Dimension getPreferredSize() {
        return new Dimension(250, 300);
    }

    public void addElement(int idx, Object s) {
        int oidx = listModel.indexOf(s);

        if (oidx < 0) {
            File nfile = new File(s.toString());
            if (!nfile.exists()) return;
            files.add(nfile);
            listModel.addElement(s);
        } else {
            File f = (File) files.get(oidx);
            files.remove(oidx);
            listModel.removeElement(s);
            listModel.add(idx, s);
            files.add(idx, f);
        }
    }

    public void removeElement(Object s) {
        listModel.removeElement(s);
    }

    class AddAction extends javax.swing.AbstractAction {
        public AddAction() {
            putValue(Action.NAME, "Add");
            putValue(Action.SHORT_DESCRIPTION, "Add File To List");
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = null;


            String lastDir = FileListEditor.this.lastDir;
            if (lastDir == null)
                chooser = new JFileChooser();
            else
                chooser = new JFileChooser(lastDir);

            chooser.setMultiSelectionEnabled(true);

            int option = chooser.showOpenDialog(JOptionPane.getFrameForComponent(chooser));

            if (option == JFileChooser.APPROVE_OPTION) {
                File[] f = chooser.getSelectedFiles();
                for (int i = 0; i < f.length; i++) {
                    files.add(f[i]);
                    String fileName = f[i].getName();
                    FileListEditor.this.listModel.addElement(fileName);
                }
                if (f.length > 0) {
                    FileListEditor.this.lastDir = f[0].getParentFile().getAbsolutePath();
                    FileListEditor.this.removeButton.setEnabled(true);
                }
            }

            if (FileListEditor.this.fileList.getModel().getSize() >= 2) {
                FileListEditor.this.shiftUpButton.setEnabled(true);
                FileListEditor.this.shiftDownButton.setEnabled(true);
            }


        }

    }

    class RemoveAction extends javax.swing.AbstractAction {
        public RemoveAction() {
            putValue(Action.NAME, "Remove");
            putValue(Action.SHORT_DESCRIPTION, "Remove File From List");
        }

        public void actionPerformed(ActionEvent e) {
            JList list = FileListEditor.this.fileList;
            int[] idx = list.getSelectedIndices();
            if (idx.length == 0)
                return;

            for (int i = 0; i < idx.length; i++) {
                files.remove(idx[i]);
                FileListEditor.this.listModel.remove(idx[i]);
            }

            if (idx[0] >= list.getModel().getSize()) {
                list.setSelectedIndex(list.getModel().getSize() - 1);
            } else {
                list.setSelectedIndex(idx[0]);
            }

            if (list.getModel().getSize() == 0)
                removeButton.setEnabled(false);
            if (list.getModel().getSize() < 2) {
                FileListEditor.this.shiftUpButton.setEnabled(false);
                FileListEditor.this.shiftDownButton.setEnabled(false);
            }


        }
    }

    class ShiftUpAction extends javax.swing.AbstractAction {
        public ShiftUpAction() {
            putValue(Action.NAME, "Move Up");
            putValue(Action.SHORT_DESCRIPTION, "Move up zero slot");
        }

        public void actionPerformed(ActionEvent e) {
            JList list = FileListEditor.this.fileList;
            int idx = list.getSelectedIndex();
            if (idx < 1) return;

            Object data1 = list.getModel().getElementAt(idx - 1);
            Object data2 = list.getModel().getElementAt(idx);

            FileListEditor.this.listModel.set(idx - 1, data2);
            FileListEditor.this.listModel.set(idx, data1);
            list.setSelectedIndex(idx - 1);
        }

    }


    class ShiftDownAction extends javax.swing.AbstractAction {
        public ShiftDownAction() {
            putValue(Action.NAME, "Move Down");
            putValue(Action.SHORT_DESCRIPTION, "Move down zero slot");
        }

        public void actionPerformed(ActionEvent e) {
            JList list = FileListEditor.this.fileList;
            int idx = list.getSelectedIndex();
            if (idx < 0 || idx >= (list.getModel().getSize() - 1)) return;

            Object data1 = list.getModel().getElementAt(idx + 1);
            Object data2 = list.getModel().getElementAt(idx);

            FileListEditor.this.listModel.set(idx + 1, data2);
            FileListEditor.this.listModel.set(idx, data1);
            list.setSelectedIndex(idx + 1);
        }

    }


}
