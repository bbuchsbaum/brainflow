package brainflow.app.presentation;

import brainflow.app.presentation.controls.ClusterTreeView;
import brainflow.app.toplevel.ImageViewFactory;
import brainflow.core.ImageView;
import brainflow.core.ImageViewModel;
import brainflow.core.SimpleImageView;
import brainflow.core.layer.ImageLayer3D;
import brainflow.image.anatomy.SpatialLoc3D;
import brainflow.image.data.ClusterSet;
import brainflow.math.Index3D;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.grid.Row;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pane.FloorTabbedPane;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 23, 2010
 * Time: 12:07:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterPresenter extends BrainFlowPresenter {

    private FloorTabbedPane clusterViewPanes;

    private JButton clusterize = new JButton("Clusterize");

    private JButton showCluster = new JButton("View Clusters");

    private JPanel panel;

    public ClusterPresenter() {
        this.clusterViewPanes = new FloorTabbedPane();
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(clusterViewPanes, BorderLayout.CENTER);

        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.addButton(clusterize);
        buttonPanel.addButton(showCluster);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        clusterize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageLayer3D layer = getSelectedLayer();
                ClusterSet cset = layer.clusterProperty.get().computeCluster();

                ClusterTreeView treeView = (ClusterTreeView) clusterViewPanes.getComponentAt(ClusterPresenter.this.getSelectedView().getSelectedLayerIndex());
                treeView.setClusterSet(cset);


            }
        });

        showCluster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 ImageLayer3D layer = getSelectedLayer();
                ClusterSet cset = layer.clusterProperty.get().computeCluster();
                ImageView view = ImageViewFactory.createAxialView(layer.clusterProperty.get().getLabels());
                JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(showCluster));
                dialog.add(view, BorderLayout.CENTER);
                dialog.pack();
                dialog.setVisible(true);
            }
        });


    }

    @Override
    public void viewSelected(ImageView view) {
        layoutTabs(view);

        int selIndex = view.getModel().getSelectedIndex();
        clusterViewPanes.setSelectedIndex(selIndex);
        //panel.revalidate();

    }

    private void layoutTabs(ImageView view) {
        clusterViewPanes.removeAll();
        for (final ImageLayer3D layer : view.getModel()) {
            final ClusterTreeView treeView = new ClusterTreeView(layer.clusterProperty.get().getClusterSet());
            clusterViewPanes.addTab(layer.getName(), treeView);
            treeView.getClusterTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getLastIndex() >= 0) {
                        int actualRow = TableModelWrapperUtils.getRowAt(treeView.getClusterTableModel(), e.getFirstIndex());
                        int modelidx = treeView.getClusterTable().convertRowIndexToModel(e.getFirstIndex());
                        int viewidx = treeView.getClusterTable().convertRowIndexToView(e.getFirstIndex());
                        int selrow = treeView.getClusterTable().getSelectedRow();

                        ClusterTreeView.ClusterRow crow = (ClusterTreeView.ClusterRow)treeView.getTableModel().getRowAt(e.getLastIndex());
                        Index3D voxel = crow.getCluster().getExtremeVoxel();
               

                        float[] wpos = layer.getData().getImageSpace().indexToWorld(voxel.i1(), voxel.i2(), voxel.i3());
                        getSelectedView().worldCursorPos.set(new SpatialLoc3D(layer.getData().getImageSpace().getMapping().getWorldAnatomy(), wpos[0], wpos[1], wpos[2]));

                    }
                }
            });

        }

        clusterViewPanes.revalidate();

    }

    @Override
    protected void layerChangeNotification() {
        layoutTabs(getSelectedView());
    }

    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
        layoutTabs(view);
    }

    @Override
    public void allViewsDeselected() {
        clusterViewPanes.removeAll();
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }
}
