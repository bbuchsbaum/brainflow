package brainflow.application.presentation;

import brainflow.application.BrainFlowProject;
import brainflow.application.dnd.DnDUtils;
import brainflow.application.dnd.ImageDropHandler;
import brainflow.application.toplevel.BrainFlowProjectEvent;
import brainflow.application.toplevel.BrainFlowProjectListener;
import brainflow.application.toplevel.BrainFlow;
import brainflow.core.layer.ImageLayer;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.IBrainCanvas;
import brainflow.core.ImageView;
import brainflow.core.IImageDisplayModel;
import brainflow.core.ImageDisplayModelListener;
import brainflow.image.space.IImageSpace;
import brainflow.image.io.IImageDataSource;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
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
                if (sel instanceof ImageDisplayModelNode) {
                    ImageDisplayModelNode node = (ImageDisplayModelNode) sel;
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
                Component c = support.getComponent();


            }



            private void importImageLayer(ImageLayer layer, TransferSupport support) {
                Component c = support.getComponent();

                if (c == tree) {
                    Point p = support.getDropLocation().getDropPoint();
                    TreePath path = tree.getClosestPathForLocation(p.x, p.y);
                    IImageDisplayModel model = findParentModel(path);
                    if (model != null) {
                        model.addLayer(new ImageLayer3D((ImageLayer3D) layer));
                    }
                }

            }


            public void dispatchOnObject(Object obj, TransferSupport support) {
                if (obj instanceof IImageDataSource) {
                    importDataSource((IImageDataSource) obj, support);
                } else if (obj instanceof ImageLayer) {
                    importImageLayer((ImageLayer) obj, support);
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

                        }

                    }


                }

                return ret;


            }
        };

        tree.setTransferHandler(handler);
    }

    private IImageDisplayModel findParentModel(Point p) {
        TreePath path = tree.getClosestPathForLocation(p.x, p.y);
        return findParentModel(path);
    }

    private IImageDisplayModel findParentModel(TreePath path) {
        Object[] obj = path.getPath();

        if (obj.length == 0) return null;

        for (int i=obj.length-1; i>=0; i--) {
            Object node = obj[i];
            if (node instanceof ImageDisplayModelNode) {
                return ((ImageDisplayModelNode)node).getModel();
            }
        }

        return null;

    }


    public void viewSelected(ImageView view) {
        //TreePath path = new TreePath(view.getModel());
        //tree.setS

    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
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

            Iterator<IImageDisplayModel> iter = project.iterator();

            while (iter.hasNext()) {
                add(new ImageDisplayModelNode(iter.next()));
            }
        }

        public void modelAdded(BrainFlowProjectEvent event) {
            add(new ImageDisplayModelNode(event.getModel()));
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

    class ImageDisplayModelNode extends DefaultMutableTreeNode {

        private IImageDisplayModel model;

        private ImageModelListener listener = new ImageModelListener(this);

        public ImageDisplayModelNode(IImageDisplayModel _model) {
            super(_model);
            model = _model;

            model.addImageDisplayModelListener(listener);

            for (int i = 0; i < model.getNumLayers(); i++) {
                ImageLayer layer = model.getLayer(i);
                add(new ImageLayerNode(layer));
                treeModel.nodesWereInserted(ImageDisplayModelNode.this, new int[]{ImageDisplayModelNode.this.getChildCount() - 1});


            }
        }

        public IImageDisplayModel getUserObject() {
            return model;
        }

        public IImageDisplayModel getModel() {
            return model;
        }


        public boolean isLeaf() {
            return false;
        }


    }

    class ImageModelListener implements ImageDisplayModelListener {

        private ImageDisplayModelNode node;

        ImageModelListener(ImageDisplayModelNode node) {
            this.node = node;
        }

        public void intervalAdded(ListDataEvent e) {
            int idx = e.getIndex0();
            ImageLayer layer = node.getModel().getLayer(idx);
            node.add(new ImageLayerNode(layer));
            treeModel.nodesWereInserted(node, new int[]{node.getChildCount() - 1});
        }

        public void intervalRemoved(ListDataEvent e) {
            int idx = e.getIndex0();
            node.remove(idx);

        }


        public void contentsChanged(ListDataEvent e) {

        }


        public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

    }

}
