package brainflow.app.presentation;

import brainflow.colormap.*;
import brainflow.core.layer.ImageLayer;
import brainflow.core.ImageView;
import brainflow.core.ImageViewModel;
import brainflow.utils.ResourceLoader;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.swing.JideBoxLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 13, 2006
 * Time: 5:29:18 PM
 * To change this template use File | Settings | File Templates.
 */


public class ColorMapTablePresenter extends BrainFlowPresenter {


    private ColorMapTable colorTable;

    private JSpinner segmentSpinner;

    private JPanel form;


    public ColorMapTablePresenter() {
        colorTable = new ColorMapTable(new LinearColorMap2(0, 255, ColorTable.SPECTRUM));
        JScrollPane scrollPane = new JScrollPane(colorTable.getComponent());


        Box topPanel = Box.createHorizontalBox();
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));


        segmentSpinner = new JSpinner(new SpinnerNumberModel());

        segmentSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Number n = (Number) segmentSpinner.getValue();
                IColorMap cmap = colorTable.getModel().setTableSize(n.intValue());

                colorTable.getComponent().getSelectionModel().clearSelection();

                ImageViewModel model = getSelectedView().getModel();
                model.getSelectedLayer().getLayerProps().colorMap.set(cmap);


            }
        });


        topPanel.add(new JLabel("Color Segments: "));
        topPanel.add(segmentSpinner);

        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.setSizeConstraint(ButtonPanel.NO_LESS_THAN);

        JButton applyButton = new JButton("Apply");
        JButton resetButton = new JButton("Cancel");

        buttonPanel.addButton(applyButton, ButtonPanel.AFFIRMATIVE_BUTTON);
        buttonPanel.addButton(resetButton, ButtonPanel.CANCEL_BUTTON);


        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 8, 2));

        form = new JPanel();
        form.setLayout(new JideBoxLayout(form, JideBoxLayout.Y_AXIS));
        form.add(createToolBar(), JideBoxLayout.FIX);
        form.add(topPanel, JideBoxLayout.FIX);
        form.add(scrollPane, JideBoxLayout.VARY);
        form.add(buttonPanel, JideBoxLayout.FLEXIBLE);

        applyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ImageView view = getSelectedView();

                if (view != null) {
                    // !!! ?? color map already may be set and therefore property change event never fired?
                    ImageLayer layer = getSelectedView().getModel().getSelectedLayer();
                    layer.getLayerProps().colorMap.set(colorTable.getEditedColorMap());
                }
            }
        });

        initSegmentSpinner();

        TableUtils.autoResizeAllColumns(colorTable.getComponent());

    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.add(new SelectAllAction());
        toolBar.add(new EqualBinsAction());
        toolBar.add(new ColorGradientAction());

        toolBar.setOpaque(false);
        return toolBar;
    }

    private IColorMap getColorMap() {
        if (getSelectedView() != null) {
            return getSelectedView().getSelectedLayer().getLayerProps().getColorMap();
        }
        return null;
    }

    private void initSegmentSpinner() {

        IColorMap cmap = getColorMap();
        if (cmap != null) {
            segmentSpinner.setEnabled(true);
            segmentSpinner.setModel(new SpinnerNumberModel(cmap.getMapSize(), 1, 256, 1));
        } else {
            segmentSpinner.setEnabled(false);
        }


    }

    public void allViewsDeselected() {

    }

    public void viewSelected(ImageView view) {
        IColorMap cmap = getSelectedView().getModel().getSelectedLayer().getLayerProps().getColorMap();
        setColorMap(cmap);

    }



    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        if (oldModel.getSelectedLayer() != newModel.getSelectedLayer()) {
            viewSelected(view);
        }
    }

    public void setColorMap(IColorMap cmap) {

        initSegmentSpinner();
        colorTable.setColorMap(cmap);


    }

    public JComponent getComponent() {
        return form;
    }


    class ColorGradientAction extends AbstractAction {


        public ColorGradientAction() {
            try {
                putValue(Action.SMALL_ICON, new ImageIcon(ImageIO.read(ResourceLoader.getResource("icons/problemview.gif"))));
                putValue(Action.SHORT_DESCRIPTION, "Adds a color gradient to selected region.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void actionPerformed(ActionEvent actionEvent)  {
            IColorMap colorMap = colorTable.getEditedColorMap();
            if (colorMap instanceof DiscreteColorMap) {
                JTable table = colorTable.getComponent();
                int[] rows = table.getSelectedRows();
                DiscreteColorMap cmap = (DiscreteColorMap) colorMap;
                if (rows.length == 0) {
                    // no selection
                    // equalize all
                    rows = new int[2];
                    rows[0] = 0;
                    rows[1] = cmap.getMapSize() - 1;
                }
                // assuming contiguous


                int r1 = rows[0];
                int r2 = rows[rows.length - 1];
                java.util.List<Color> clist = ColorTable.createColorGradient(cmap.getInterval(r1).getColor(), cmap.getInterval(r2).getColor(), r2 - r1 + 1);
                for (int i = r1; i < (r2 + 1); i++) {
                    cmap.setColor(i, clist.get(i - r1));
                }

                colorTable.getModel().fireTableDataChanged();
                DefaultListSelectionModel model = new DefaultListSelectionModel();
                model.setSelectionInterval(rows[0], rows[rows.length - 1]);
                table.setSelectionModel(model);

            } else {
                //todo this is not professional

            }

        }


        public boolean shouldBeEnabled() {

            return false;
        }
    }

    class SelectAllAction extends AbstractAction {

        public SelectAllAction() {
            try {
                putValue(Action.SMALL_ICON, new ImageIcon(ImageIO.read(ResourceLoader.getResource("icons/lprio_tsk.gif"))));
                //putValue(Action.SMALL_ICON, new ImageIcon(ImageIO.read(ResourceLoader.getResource("icons/logical_package_obj.gif"))));

                putValue(Action.SHORT_DESCRIPTION, "Select All");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void actionPerformed(ActionEvent actionEvent)  {
            JTable table = colorTable.getComponent();
            DefaultListSelectionModel model = new DefaultListSelectionModel();
            model.setSelectionInterval(0, table.getRowCount() - 1);
            table.setSelectionModel(model);

        }


    }


    class EqualBinsAction extends AbstractAction {


        public EqualBinsAction() {
            try {
                putValue(Action.SMALL_ICON, new ImageIcon(ImageIO.read(ResourceLoader.getResource("icons/stkfrm_obj.gif"))));
                putValue(Action.SHORT_DESCRIPTION, "make the size of all bin intervals equal");
            } catch (IOException e) {
                //TODO log e
                throw new RuntimeException(e);
            }
        }

        public void actionPerformed(ActionEvent actionEvent)  {
            IColorMap colorMap = colorTable.getEditedColorMap();
            if (colorMap instanceof DiscreteColorMap) {
                JTable table = colorTable.getComponent();

                int[] rows = table.getSelectedRows();
                DiscreteColorMap cmap = (DiscreteColorMap) colorMap;
                if (rows.length == 0) {
                    // no selection
                    // equalize all
                    rows = new int[2];
                    rows[0] = 0;
                    rows[1] = cmap.getMapSize() - 1;
                }
                // assuming contiguous


                if (rows[rows.length - 1] > rows[0]) {
                    assert rows[rows.length - 1] < cmap.getMapSize();

                    cmap.equalizeIntervals(rows[0], rows[rows.length - 1]);
                    colorTable.getModel().fireTableDataChanged();
                    DefaultListSelectionModel model = new DefaultListSelectionModel();
                    model.setSelectionInterval(rows[0], rows[rows.length - 1]);
                    table.setSelectionModel(model);
                }

            }
        }


        public boolean shouldBeEnabled() {
            
            return false;
        }
    }


}
