package brainflow.app.presentation;

import brainflow.app.BrainFlowProject;
import brainflow.app.dnd.DnDUtils;
import brainflow.app.dnd.ImageDropHandler;
import brainflow.app.toplevel.*;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.*;
import brainflow.image.space.IImageSpace;
import brainflow.image.io.IImageDataSource;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.ListDataListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.datatransfer.Transferable;
import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2007
 * Time: 4:54:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectTreeView extends ImageViewPresenter implements MouseListener, MouseMotionListener {

    private BrainFlowProject project;

    private DefaultTreeModel treeModel;

    private ProjectNode rootNode;

    private JTree tree;

    public ProjectTreeView(BrainFlowProject _project) {
        project = _project;
        rootNode = new ProjectNode(project);

        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();

                Object sel = path.getLastPathComponent();
                if (sel instanceof ImageViewModelNode) {
                    ImageViewModelNode node = (ImageViewModelNode) sel;
                    IBrainCanvas canvas = BrainFlow.get().getSelectedCanvas();
                    if (canvas.getSelectedView().getModel() != node.getModel()) {
                        List<ImageView> views = canvas.getViews();
                        for (ImageView v : views) {
                            if (v.getModel() == node.getModel()) {
                                canvas.setSelectedView(v);
                                break;
                            }
                        }
                    }

                }

            }
        });

        initDnD();

    }

    private void initDnD() {
        // todo hackalicious
        tree.setDragEnabled(true);

        tree.addMouseListener(this);
        tree.addMouseMotionListener(this);


        TransferHandler handler = new ImageDropHandler() {


            private void importDataSource(IImageDataSource dsource, TransferSupport support) {
                //todo this logic does not belong in TreeView class

                if (!dsource.isLoaded()) {
                    BrainFlow.get().load(dsource);
                }

                Point p = support.getDropLocation().getDropPoint();
                TreePath path = tree.getClosestPathForLocation(p.x, p.y);

                ImageViewModel model = findParentModel(path);


                if (model != null) {
                    ImageLayer sel = getSelectedLayerNode(path);

                    if (sel != null) {
                        //todo hack cast
                        int i = model.indexOf((ImageLayer3D)sel);
                        assert i >= 0;
                        model.insert(i+1, ImageLayerFactory.createImageLayer(dsource));
                    } else {
                        model.add(ImageLayerFactory.createImageLayer(dsource));
                    }


                }

                
                project.addDataSource(dsource);

            }



            private void importImageLayer(ImageLayer3D layer, TransferSupport support) {
                Component c = support.getComponent();

                if (c == tree) {
                    Point p = support.getDropLocation().getDropPoint();
                    TreePath path = tree.getClosestPathForLocation(p.x, p.y);
                    ImageViewModel model = findParentModel(path);

                    if (model != null && !model.contains(layer)) {
                        model.add(layer);
                        model.layerSelection.set(model.indexOf(layer));
                    } else {
                        //todo drop layer in correct location rather than just adding it to end ....
                    }
                }

            }


            public void dispatchOnObject(Object obj, TransferSupport support) {
                if (obj instanceof IImageDataSource) {
                    importDataSource((IImageDataSource) obj, support);
                } else if (obj instanceof ImageLayer3D) {
                    importImageLayer((ImageLayer3D) obj, support);
                }
            }

            public int getSourceActions(JComponent c) {
                return TransferHandler.COPY;
            }

            protected Transferable createTransferable(JComponent c) {

                Transferable ret = null;
                if (c instanceof JTree) {
                    JTree tree = (JTree) c;
                    TreePath path = tree.getSelectionPath();
                    Object[] opath = path.getPath();


                    if (opath.length > 0) {
                        Object obj = opath[opath.length - 1];
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
                        if (node.isLeaf()) {
                            Object layer = node.getUserObject();
                            if (layer instanceof ImageLayer) {
                                ImageLayer ilayer = (ImageLayer) layer;
                                ret = DnDUtils.createTransferable(ilayer);
                            }
                        } else {
                            if (node instanceof ImageViewModelNode) {
                                ImageViewModelNode modelNode = (ImageViewModelNode)node;
                                ret = DnDUtils.createTransferable(modelNode.getModel());

                            }
                        }

                    }


                }

                return ret;


            }
        };

        tree.setTransferHandler(handler);
    }

    private ImageViewModel findParentModel(Point p) {
        TreePath path = tree.getClosestPathForLocation(p.x, p.y);
        return findParentModel(path);
    }

    private ImageViewModel findParentModel(TreePath path) {
        Object[] obj = path.getPath();

        if (obj.length == 0) return null;

        for (int i=obj.length-1; i>=0; i--) {
            Object node = obj[i];
            if (node instanceof ImageViewModelNode) {
                return ((ImageViewModelNode)node).getModel();
            }
        }

        return null;

    }


    private ImageLayer getSelectedLayerNode(TreePath path) {
        Object[] obj = path.getPath();

        if (obj.length == 0) return null;

        Object node= obj[obj.length-1];
        if (node instanceof ImageLayerNode) {
            return ((ImageLayerNode)node).getUserObject();
        } else {
            return null;
        }
    }



    public void viewSelected(ImageView view) {
        //TreePath path = new TreePath(view.getModel());
        //tree.setS

    }

    @Override
    protected void layerIntervalAdded(ListDataEvent event) {

    }

    @Override
    protected void layerIntervalRemoved(ListDataEvent event) {


    }

    @Override
    public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {

    }

    @Override
    public void allViewsDeselected() {

    }

    public JComponent getComponent() {
        return tree;
    }

    private MouseEvent firstMouseEvent = null;

    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mousePressed(MouseEvent e) {
        firstMouseEvent = e;
        e.consume();
    }

    public void mouseDragged(MouseEvent e) {
        if (firstMouseEvent != null) {
            e.consume();
            int dx = Math.abs(e.getX() - firstMouseEvent.getX());
            int dy = Math.abs(e.getY() - firstMouseEvent.getY());
            //Arbitrarily define a 5-pixel shift as the
            //official beginning of a drag.
            if (dx > 1 || dy > 1) {

                //This is a drag, not a click.
                JComponent c = (JComponent) e.getSource();
                //Tell the transfer handler to initiate the drag.
                TransferHandler handler = c.getTransferHandler();
                handler.exportAsDrag(c, firstMouseEvent, -1);
                firstMouseEvent = null;
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseReleased(MouseEvent e) {
        firstMouseEvent = null;
    }

    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    class ProjectNode extends DefaultMutableTreeNode implements BrainFlowProjectListener {

        private BrainFlowProject project;

        public ProjectNode(BrainFlowProject _project) {
            super(_project);

            project = _project;

            project.addListDataListener(this);

            Iterator<ImageViewModel> iter = project.iterator();

            while (iter.hasNext()) {
                add(new ImageViewModelNode(iter.next()));
            }
        }

        public void modelAdded(BrainFlowProjectEvent event) {
            add(new ImageViewModelNode(event.getModel()));
            treeModel.nodesWereInserted(this, new int[]{getChildCount() - 1});

        }

        public void modelRemoved(BrainFlowProjectEvent event) {
            Enumeration en = children();
            while (en.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
                if (node.getUserObject() == event.getModel()) {
                    remove(node);
                }
            }

        }

        public void intervalAdded(BrainFlowProjectEvent event) {
        }

        public void contentsChanged(BrainFlowProjectEvent event) {
        }

        public void intervalRemoved(BrainFlowProjectEvent event) {

        }


        public boolean isLeaf() {
            return false;
        }


    }

    class ImageLayerNode extends DefaultMutableTreeNode {

        ImageLayer layer;

        public ImageLayerNode(ImageLayer layer) {
            super(layer);
            this.layer = layer;

        }

        public ImageLayer getUserObject() {
            return layer;
        }
    }

    class ImageViewModelNode extends DefaultMutableTreeNode {

        private ImageViewModel model;

        private ImageModelListener listener = new ImageModelListener(this);

        public ImageViewModelNode(ImageViewModel _model) {
            super(_model);
            model = _model;
            model.addListDataListener(listener);

            //model.addImageDisplayModelListener(listener);

            for (int i = 0; i < model.size(); i++) {
                ImageLayer layer = model.get(i);
                add(new ImageLayerNode(layer));
                treeModel.nodesWereInserted(ImageViewModelNode.this, new int[]{ImageViewModelNode.this.getChildCount() - 1});
            }
        }

        public ImageViewModel getUserObject() {
            return model;
        }

        public ImageViewModel getModel() {
            return model;
        }

        @Override
        public void remove(int childIndex) {
            super.remove(childIndex);
            System.out.println("removing child Index");
        }

        @Override
        public TreeNode getChildAt(int index) {
            return super.getChildAt(index);    //To change body of overridden methods use File | Settings | File Templates.
        }



        public boolean isLeaf() {
            return false;
        }


    }

    class ImageModelListener implements ListDataListener {

        private ImageViewModelNode node;

        ImageModelListener(ImageViewModelNode node) {
            this.node = node;

        }

        public void intervalAdded(ListDataEvent e) {
            //todo need to check for multiple additions
            int idx = e.getIndex0();
            ImageLayer layer = node.getModel().get(idx);
            node.add(new ImageLayerNode(layer));
            treeModel.nodesWereInserted(node, new int[]{node.getChildCount() - 1});
        }

        public void intervalRemoved(ListDataEvent e) {
            //todo need to check for multiple additions
            int idx = e.getIndex0();
            Object layerNode = node.getChildAt(idx);
            node.remove(idx);
            treeModel.nodesWereRemoved(node, new int[] { idx }, new Object[] { layerNode});
            treeModel.nodeChanged(node);
            
        }


        public void contentsChanged(ListDataEvent e) {
            // todo implement ...

        }




    }

}
