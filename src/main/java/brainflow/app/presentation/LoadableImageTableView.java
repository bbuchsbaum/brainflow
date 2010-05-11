package brainflow.app.presentation;

import brainflow.image.io.IImageSource;
import brainflow.app.actions.RemoveDataSourceCommand;
import brainflow.app.services.DataSourceStatusEvent;
import brainflow.gui.AbstractPresenter;
import com.jidesoft.grid.*;
import com.jidesoft.swing.NullButton;
import com.jidesoft.swing.NullJideButton;
import com.jidesoft.swing.NullLabel;
import com.jidesoft.swing.NullPanel;
import com.pietschy.command.ActionCommand;
import org.apache.commons.vfs.FileSystemException;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import javax.imageio.ImageIO;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import net.java.balloontip.CustomBalloonTip;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.ModernBalloonStyle;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 6, 2006
 * Time: 3:10:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadableImageTableView extends AbstractPresenter implements EventSubscriber {



    private HierarchicalTable table;

    private ImageTableModel imageTableModel;

    private List<IImageSource> imageList = new ArrayList<IImageSource>();

    private ActionCommand removeCommand = new RemoveDataSourceCommand();

    public LoadableImageTableView() {
        EventBus.subscribe(DataSourceStatusEvent.class, this);
    }

    public JComponent getComponent() {
        if (table == null) {
            table = createTable();
        }

        return table;
    }

    // create property table
    private HierarchicalTable createTable() {
        imageTableModel = new ImageTableModel();
        table = new HierarchicalTable() {


            public TableModel getStyleModel() {
                return imageTableModel; // designate it as the style model
            }
        };
        table.setModel(imageTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        table.setHierarchicalColumn(-1);
        table.setSingleExpansion(true);
        table.setName("Available Images");
        table.setShowGrid(false);
        table.setRowHeight(24);
        table.getTableHeader().setPreferredSize(new Dimension(0, 0));
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setMaxWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setMaxWidth(100);
        table.getColumnModel().getColumn(0).setCellRenderer(new LoadableImageCellRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setComponentFactory(new HierarchicalTableComponentFactory() {
            public Component createChildComponent(HierarchicalTable table, Object value, int row) {
                if (value instanceof IImageSource) {
                    return new ImageInfoPanel((IImageSource) value);
                }
                return null;
            }

            public void destroyChildComponent(HierarchicalTable table, Component component, int row) {
            }
        });


        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    table.expandRow(row);
                }
            }
        });
        return table;
    }


    public void onEvent(Object evt) {
        DataSourceStatusEvent event = (DataSourceStatusEvent) evt;
        DataSourceStatusEvent.EventID id = event.getEventID();


        switch (id) {
            case IMAGE_LOADED:
                break;
            case IMAGE_REGISTERED:
                imageList.add(event.getLoadableImage());
                imageTableModel.update();
                break;
            case IMAGE_REMOVED:
                int idx = imageList.indexOf(event.getLoadableImage());
                imageList.remove(idx);
                imageTableModel.fireTableRowsDeleted(idx, idx);
                imageTableModel.update();
                break;
            case IMAGE_UNLOADED:
                break;
        }
    }

    private String convertBytesToString(long bytes) {
        String ret;

        if (bytes >= 1048576) {
            // we're dealing with megabytes
            double mb = bytes / 1048576.0;
            ret = NumberFormat.getNumberInstance().format(mb) + "MB";
        } else if (bytes >= 1024) {
            double kb = bytes / 1024.0;
            ret = NumberFormat.getNumberInstance().format(kb) + "KB";
        } else {
            ret = NumberFormat.getNumberInstance().format(bytes) + " bytes";
        }

        return ret;

    }


    class ImageInfoPanel extends JPanel {
        IImageSource limg;

        public ImageInfoPanel(IImageSource _limg) {
            limg = _limg;
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(2, 2, 3, 2));
            add(createTextPanel());
            add(createControlPanel(), BorderLayout.AFTER_LINE_ENDS);
            setBackground(UIManager.getColor("Table.selectionBackground"));
            setForeground(UIManager.getColor("Table.selectionForeground"));
        }

        JComponent createTextPanel() {
            NullPanel panel = new NullPanel(new GridLayout(4, 1, 5, 1));
            //panel.add(new NullLabel(limg.getStem(), getSnapshot(limg, SMALL_ICON_WIDTH, SMALL_ICON_HEIGHT), JLabel.LEADING));
            panel.add(new NullLabel(limg.getStem(), JLabel.LEADING));

            final NullJideButton supportButton = new NullJideButton("");
            supportButton.setHorizontalAlignment(SwingConstants.LEADING);
            supportButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            panel.add(supportButton);
            panel.add(new NullPanel());

            panel.add(createInfoButton());
            return panel;
        }

        private String infoString() {
            table.getSelectedRow();
            StringBuilder builder = new StringBuilder();
            builder.append("<html>");
            builder.append("Name: " + limg.getStem());
            builder.append("<br>");
            builder.append("Format: " + limg.getFileFormat());
            builder.append("<br>");
            builder.append("Dim: " + limg.getImageInfo().getVolumeDim());
            builder.append("<br>");
            builder.append("Spacing: " + limg.getImageInfo().getSpacing());
            builder.append("<br>");
            builder.append("Anatomy: " + limg.getImageInfo().getAnatomy());
            builder.append("<br>");
            builder.append("Origin: " + limg.getImageInfo().getOrigin());
            builder.append("<br>");
            builder.append("Scaling: " + limg.getImageInfo().getScaleFactor());
            builder.append("</html>");

            return builder.toString();
        }

        private BalloonTip createBalloon(JComponent comp) {
            return new CustomBalloonTip(comp,

                    infoString(),
                    new Rectangle(comp.getWidth(), 0, 0, 0),
                    new ModernBalloonStyle(3, 3, Color.WHITE, Color.WHITE.darker(), Color.GRAY),
                    BalloonTip.Orientation.RIGHT_ABOVE,
                    BalloonTip.AttachLocation.EAST,
                    10, 10,
                    true);


        }

        private NullJideButton createInfoButton() {
            final NullJideButton propertiesButton = new NullJideButton("More Info");
            propertiesButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            propertiesButton.setHorizontalAlignment(SwingConstants.LEADING);

            URL url = getClass().getClassLoader().getResource("icons/information.png");
            ImageIcon icon = null;
            try {
                icon = new ImageIcon(ImageIO.read(url));

            } catch (IOException e) {
                e.printStackTrace();
            }


            propertiesButton.setIcon(icon);
            propertiesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    createBalloon(propertiesButton).setVisible(true);
                }
            });

            return propertiesButton;

        }

        JComponent createControlPanel() {

            NullPanel panel = new NullPanel(new GridLayout(5, 2, 5, 0));
            panel.add(new NullLabel("Size", NullLabel.TRAILING));
            NullJideButton sizeButton = new NullJideButton(convertBytesToString(getImageSize(limg)));
            sizeButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            sizeButton.setHorizontalAlignment(SwingConstants.TRAILING);
            //sizeButton.addActionListener(new ClickAction(program, "Size", sizeButton));
            panel.add(sizeButton);

            panel.add(new NullLabel("Data Type", NullLabel.TRAILING));
            NullJideButton dataTypeButton = new NullJideButton(limg.getImageInfo().getDataType().toString());
            dataTypeButton.setHorizontalAlignment(SwingConstants.TRAILING);
            dataTypeButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            //dataTypeButton.addActionListener(new ClickAction(program, "Used", dataTypeButton));
            panel.add(dataTypeButton);

            panel.add(new NullLabel("Dimensions", NullLabel.TRAILING));
            final NullJideButton dimButton = new NullJideButton(limg.getImageInfo().getVolumeDim().toString());
            dimButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            dimButton.setHorizontalAlignment(SwingConstants.TRAILING);
            panel.add(dimButton);

            panel.add(new NullLabel("Showing", NullLabel.TRAILING));
            final NullJideButton showingButton = new NullJideButton("true");
            showingButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            showingButton.setHorizontalAlignment(SwingConstants.TRAILING);
            //dimButton.addActionListener(new ClickAction(program, "Last Used On", dimButton));
            panel.add(showingButton);

            NullButton loadButton = new NullButton("Load");
            //loadButton.addActionListener(new ClickAction(program, "Change", changeButton));
            panel.add(loadButton);

            NullButton removeButton = new NullButton("Remove");
            removeCommand.putParameter(RemoveDataSourceCommand.SELECTED_DATASOURCE, limg);

            removeButton.addActionListener(removeCommand.getActionAdapter());
            panel.add(removeButton);
            return panel;
        }
    }


    class LoadableImageCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof IImageSource) {
                IImageSource limg = (IImageSource) value;
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, limg.getStem(), isSelected, hasFocus, row, column);
                //label.setIcon(getSnapshot(limg, SMALL_ICON_WIDTH, SMALL_ICON_HEIGHT));
                return label;
            } else {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }
    }

    private long getImageSize(IImageSource limg) {

        try {
            return limg.getDataFile().getContent().getSize();
        } catch (FileSystemException e) {
            // log, publish error.
            throw new RuntimeException(e);
            ///


        }
    }


    class ImageTableModel extends AbstractTableModel implements HierarchicalTableModel, StyleModel {

        CellStyle cellStyle = new CellStyle();

        public ImageTableModel() {
            cellStyle.setHorizontalAlignment(SwingConstants.TRAILING);
        }

        public int getRowCount() {
            return imageList.size();
        }

        public int getColumnCount() {
            return 3;
        }

        public void update() {
            fireTableDataChanged();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            IImageSource limg = imageList.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return limg;
                case 1:
                    return "Size";
                case 2:
                    return convertBytesToString(getImageSize(limg));

            }
            return "";
        }


        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public boolean hasChild(int row) {
            return true;
        }

        public boolean isExpandable(int row) {
            return true;
        }

        public boolean isHierarchical(int row) {
            return false;
        }

        public Object getChildValueAt(int row) {
            return imageList.get(row);
        }

        public boolean isCellStyleOn() {
            return true;
        }


        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (columnIndex == 2) {
                return cellStyle;
            }
            return null;
        }
    }

}
