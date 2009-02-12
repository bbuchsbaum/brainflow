package brainflow.application.presentation;

import brainflow.colormap.ColorTable;
import brainflow.core.IImageDisplayModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Apr 26, 2004
 * Time: 11:20:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class ColorMapChooser extends JPanel implements ActionListener, ListSelectionListener {

    public static final String EXIT_ACTION_PROPERTY = "EXIT";
    public static final String APPLY_ACTION_PROPERTY = "APPLY";
    public static final String CHOOSE_COLOR_ACTION_PROPERTY = "CHOOSE";


    private IImageDisplayModel dset;
    private IndexColorModel[] cmodels;


    private JList mapList;
    private JList solidList;
    private DefaultListModel mapListModel = new DefaultListModel();
    private DefaultListModel solidListModel = new DefaultListModel();

    private JPanel listPanel = new JPanel();

    private JComboBox layerChooser = new JComboBox();
    private JButton applyButton;
    private JButton exitButton;
    private JButton chooseColorButton;
    private JButton createMapButton;

    //private ColorBar currentColorBar;


    private IndexColorModel selectedModel = null;
    private JList selectedList = null;

    public ColorMapChooser(IImageDisplayModel _dset, IndexColorModel[] _cmodels) {
        dset = _dset;
        cmodels = _cmodels;
        initGui();

    }

    public static void main(String[] args) {

    }


    public IImageDisplayModel getImageDisplaySet() {
        return dset;
    }

    private void initGui() {


        solidList = new JList();
        solidList.setCellRenderer(new ColorItemRenderer());
        solidListModel.add(0, "red");
        solidListModel.add(1, "blue");
        solidListModel.add(2, "green");
        solidListModel.add(3, "yellow");
        solidListModel.add(4, "orange");
        solidListModel.add(5, "cyan");
        solidListModel.add(6, "pink");
        solidListModel.add(7, "gray");
        solidListModel.add(8, "magenta");

        solidList.addListSelectionListener(this);


        solidList.setModel(solidListModel);

        for (int i = 0; i < cmodels.length; i++) {
            mapListModel.add(i, cmodels[i]);
        }
        mapList = new JList();
        mapList.setModel(mapListModel);
        mapList.setCellRenderer(new ColorBarItemRenderer());
        mapList.addListSelectionListener(this);

        JPanel buttonPanel = new JPanel();
        applyButton = new JButton("Apply");
        exitButton = new JButton("Exit");
        chooseColorButton = new JButton("Choose Color");
        createMapButton = new JButton("Create Map");

        applyButton.addActionListener(this);
        exitButton.addActionListener(this);
        chooseColorButton.addActionListener(this);
        createMapButton.addActionListener(this);


        buttonPanel.add(applyButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(chooseColorButton);
        buttonPanel.add(createMapButton);

        //DisplayProperties dprops = dset.getDisplayProperties(dset.getSelectedLayer());
        //currentColorBar = new ColorBar(dprops.getColorModel().getWrappedModel());
        //currentColorBar = new ColorBar();
        //currentColorBar.setColorModel(cmodels[0].getWrappedModel());


        FormLayout layout = new FormLayout("3dlu, max(70dlu;p):g, 4dlu, max(70dlu;p):g, 12dlu, max(50dlu;p):g, 12dlu",
                "6dlu, p, 3dlu, p, max(50dlu;p):g, 3dlu");
        listPanel.setLayout(layout);
        layout.setColumnGroups(new int[][]{{2, 4}});
        CellConstraints cc = new CellConstraints();
        listPanel.add(new JLabel("Solid Colors"), cc.xy(4, 2));
        listPanel.add(new JLabel("Color Maps"), cc.xy(2, 2));
        listPanel.add(new JScrollPane(solidList), cc.xywh(4, 4, 1, 2));
        listPanel.add(new JScrollPane(mapList), cc.xywh(2, 4, 1, 2));
        listPanel.add(layerChooser, cc.xy(6, 4));
//        listPanel.add(currentColorBar, cc.xy(6,5));

        //listPanel.add(buttonPanel, cc.xywh(1,6, 7,2));

        setLayout(new BorderLayout());
        add(listPanel, "Center");
        add(buttonPanel, "South");
    }

    public void addSolidColor(Color c) {
        String cstr = "custom" + "#" + c.getRed() + "#" + c.getGreen() + "#" + c.getBlue();
        solidListModel.addElement(cstr);
    }


    public IndexColorModel getSelectedMap() {
        if (selectedList == mapList) {
            int selectedModel = mapList.getSelectedIndex();
            return cmodels[selectedModel];
        } else if (selectedList == solidList) {

            String colstr = (String) solidList.getSelectedValue();
            Color c = ColorMapChooser.lookupColor(colstr);
            IndexColorModel icm = ColorTable.createConstantMap(c);
            return icm;
        }

        return null;
    }

    public int getSelectedLayer() {
        return layerChooser.getSelectedIndex();
    }

    public void actionPerformed(ActionEvent e) {
        JButton jb = (JButton) e.getSource();
        if (jb == exitButton) {
            firePropertyChange(ColorMapChooser.EXIT_ACTION_PROPERTY, -1, 1);
        } else if (jb == applyButton) {

            firePropertyChange(ColorMapChooser.APPLY_ACTION_PROPERTY, -1, 1);
        } else if (jb == chooseColorButton) {
            firePropertyChange(ColorMapChooser.CHOOSE_COLOR_ACTION_PROPERTY, -1, 1);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        JList src = (JList) e.getSource();
        if (src != selectedList) {
            if (src == solidList) {
                int idx = mapList.getSelectedIndex();
                mapList.removeSelectionInterval(idx, idx);
            } else {
                int idx = solidList.getSelectedIndex();
                solidList.removeSelectionInterval(idx, idx);

            }
        }

        selectedList = src;
    }


    private static Color parseColor(String cstr) {
        StringTokenizer tokenizer = new StringTokenizer(cstr, "#");
        assert tokenizer.countTokens() == 3;

        String[] rgb = new String[3];

        //throws away "custom"
        tokenizer.nextToken();
        ////

        for (int i = 0; i < 3; i++) {
            String str = tokenizer.nextToken();
            rgb[i] = str;
        }

        return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    }

    public static Color lookupColor(String cstr) {
        if (cstr.equalsIgnoreCase("red")) {
            return Color.RED;
        } else if (cstr.equalsIgnoreCase("blue")) {
            return Color.BLUE;
        } else if (cstr.equalsIgnoreCase("green")) {
            return Color.GREEN;
        } else if (cstr.equalsIgnoreCase("yellow")) {
            return Color.YELLOW;
        } else if (cstr.equalsIgnoreCase("orange")) {
            return Color.ORANGE;
        } else if (cstr.equalsIgnoreCase("cyan")) {
            return Color.CYAN;
        } else if (cstr.equalsIgnoreCase("magenta")) {
            return Color.MAGENTA;
        } else if (cstr.equalsIgnoreCase("gray")) {
            return Color.GRAY;
        } else if (cstr.equalsIgnoreCase("pink")) {
            return Color.PINK;
        } else {
            return parseColor(cstr);
        }

    }


    class ColorItemRenderer extends DefaultListCellRenderer {

        HashMap iconMap = new HashMap();
        private int SWATCH_WIDTH = 16;
        private int SWATCH_HEIGHT = 16;


        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean hasFocus) {
            JLabel label =
                    (JLabel) super.getListCellRendererComponent(list,
                            value,
                            index,
                            isSelected,
                            hasFocus);


            String colstr = label.getText();
            ImageIcon icon = (ImageIcon) iconMap.get(colstr);
            if (icon == null) {
                BufferedImage bimg = new BufferedImage(SWATCH_WIDTH, SWATCH_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g2d = (Graphics2D) bimg.createGraphics();
                Color c = ColorMapChooser.lookupColor(label.getText());
                g2d.setPaint(c);
                g2d.fillRect(0, 0, SWATCH_WIDTH, SWATCH_HEIGHT);
                icon = new ImageIcon(bimg);
                iconMap.put(value, icon);
            }

            label.setIcon(icon);
            return label;

        }
    }


    class ColorBarItemRenderer extends DefaultListCellRenderer {

        HashMap iconMap = new HashMap();
        private int SWATCH_WIDTH = 40;
        private int SWATCH_HEIGHT = 16;


        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean hasFocus) {
            JLabel label =
                    (JLabel) super.getListCellRendererComponent(list,
                            value,
                            index,
                            isSelected,
                            hasFocus);

            // *** //
            String mapname = label.getText();
            ImageIcon icon = (ImageIcon) iconMap.get(mapname);
            if (icon == null) {
                //ColorBar cbar = new ColorBar();
                //cbar.setSize(SWATCH_WIDTH, SWATCH_HEIGHT);
                //BufferedImage bimg = new BufferedImage(SWATCH_WIDTH, SWATCH_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
                //IndexColorModel cmodel = (IndexColorModel) value;
                //cbar.setColorModel(cmodel);
                //cbar.paintComponent(bimg.createGraphics());

                //label.setText("");

                //icon = new ImageIcon(bimg);
                //iconMap.put(mapname, icon);
            }

            label.setIcon(icon);
            return label;

        }


    }


}



