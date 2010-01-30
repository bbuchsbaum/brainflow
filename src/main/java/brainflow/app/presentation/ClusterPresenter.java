package brainflow.app.presentation;

import brainflow.app.presentation.controls.ClusterTreeView;
import brainflow.app.toplevel.ImageViewFactory;
import brainflow.core.ImageView;
import brainflow.core.ImageViewModel;
import brainflow.core.layer.ImageLayer3D;
import brainflow.image.anatomy.SpatialLoc3D;
import brainflow.image.data.ClusterSet;
import brainflow.math.Index3D;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.pane.FloorTabbedPane;
import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.InfiniteProgressPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

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

    private JPanel contentPane;

    private DefaultOverlayable overlayPanel;

    private InfiniteProgressPanel progressPanel = new InfiniteProgressPanel() {
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }
    };

    public ClusterPresenter() {
        this.clusterViewPanes = new FloorTabbedPane();
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(clusterViewPanes, BorderLayout.CENTER);

        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.LEFT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        buttonPanel.addButton(clusterize);
        buttonPanel.addButton(showCluster);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        initProgressOverlay();

        clusterize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ImageLayer3D layer = getSelectedLayer();

                SwingWorker<ClusterSet, Object> worker = new SwingWorker<ClusterSet, Object>() {
                    @Override
                    protected ClusterSet doInBackground() throws Exception {
                        ClusterSet cset = layer.clusterProperty.get().computeCluster();
                        return cset;
                    }

                    @Override
                    protected void done() {
                        try {
                            ClusterTreeView treeView = (ClusterTreeView) clusterViewPanes.getComponentAt(ClusterPresenter.this.getSelectedView().getSelectedLayerIndex());
                            treeView.setClusterSet(get());
                            hideProgressSignal();
                        } catch (ExecutionException e) {
                            hideProgressSignal();
                            throw new RuntimeException(e);

                        }
                        catch (InterruptedException e2) {
                            hideProgressSignal();
                        }


                    }
                };

                startProgressSignal();
                worker.execute();



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

    private void startProgressSignal() {
        overlayPanel.setOverlayVisible(true);
       
        progressPanel.start();


    }

    private void hideProgressSignal() {
        progressPanel.stop();
        overlayPanel.setOverlayVisible(false);

    }

    private void initProgressOverlay() {
        overlayPanel = new DefaultOverlayable(contentPane);
        overlayPanel.addOverlayComponent(progressPanel);
        overlayPanel.setOverlayVisible(false);
        overlayPanel.setOverlayLocation(contentPane, SwingConstants.CENTER);
        progressPanel.stop();
    }

    @Override
    public void viewSelected(ImageView view) {
        layoutTabs(view);

        int selIndex = view.getModel().getSelectedIndex();
        clusterViewPanes.setSelectedIndex(selIndex);
        //contentPane.revalidate();

    }

    private void layoutTabs(ImageView view) {
        clusterViewPanes.removeAll();
        for (final ImageLayer3D layer : view.getModel()) {
            final ClusterTreeView treeView = new ClusterTreeView(layer.clusterProperty.get().getClusterSet());
            clusterViewPanes.addTab(layer.getName(), treeView);
            treeView.getClusterTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                  int selrow = treeView.getClusterTable().getSelectedRow();
                    if (selrow >= 0) {
                        ClusterTreeView.ClusterRow crow = (ClusterTreeView.ClusterRow) treeView.getTableModel().getRowAt(selrow);
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
        return overlayPanel;
    }
}
