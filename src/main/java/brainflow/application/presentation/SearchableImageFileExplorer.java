package brainflow.application.presentation;

import brainflow.gui.AbstractPresenter;
import com.jidesoft.list.ListModelWrapper;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.tree.FilterableTreeModel;
import com.jidesoft.tree.QuickTreeFilterField;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.xduke.xswing.DataTipManager;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

import org.apache.commons.vfs.VFS;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 7:30:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchableImageFileExplorer extends AbstractPresenter {

    private JPanel mainPanel = new JPanel(new BorderLayout(6, 6));

    private ImageFileExplorer explorer;


    public SearchableImageFileExplorer(ImageFileExplorer explorer) {
        this.explorer = explorer;
        init();
    }

    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel());
            LookAndFeelFactory.installDefaultLookAndFeel();
            ImageFileExplorer explorer = new ImageFileExplorer(VFS.getManager().resolveFile("C:/javacode"));
            SearchableImageFileExplorer sexplorer = new SearchableImageFileExplorer(explorer);

            JFrame frame = new JFrame();
            frame.add(sexplorer.getComponent(), BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TreePath getPathForNode(TreeNode current) {
        // Get node depth
        int depth = 0;
        for (TreeNode node = current; node != null; node.getParent())
            depth++;
        // Construct node path
        // First scan helped us, now we can directly allocate array of exact
        // size => no extra objects created (be kind to your local gc, it has
        // hard job cleaning that mess already), no collection reverse.
        // Price is doubling path construction time.
        // But in many situations you know the depth already. In such case
        // Only code below applies and time complexity is O(depth) not
        // O(2*depth)
        TreeNode[] path = new TreeNode[depth];
        for (TreeNode node = current; node != null; node.getParent())
            path[--depth] = node; // reverse fill array
        return new TreePath(path);

    }

    private void init() {
        JPanel quickSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        final TreeModel treeModel = explorer.getTreeModel();
        final QuickTreeFilterField field = new QuickTreeFilterField(treeModel) {
            protected FilterableTreeModel createDisplayTreeModel(TreeModel treeModel) {
                return new FilterableTreeModel(treeModel) {
                    @Override
                    protected void configureListModelWrapper(ListModelWrapper wrapper, Object node) {

                        if (node instanceof DefaultMutableTreeNode) {
                            //ImageFileExplorer.LazyNode inode = (ImageFileExplorer.LazyNode) node;
                            DefaultMutableTreeNode inode = (DefaultMutableTreeNode) node;
                            boolean expanded = explorer.getJTree().isExpanded(new TreePath(inode.getPath()));
                            if (inode.isLeaf() || expanded) {
                                super.configureListModelWrapper(wrapper, node);
                            }

                        } 
                    }

                    
                };
            }
        };


        field.setSearchingDelay(200);
        quickSearchPanel.add(field);


        JPanel treePanel = new JPanel(new BorderLayout(2, 2));


        field.setTree(explorer.getJTree());
        SearchableUtils.installSearchable(field.getTree());

        JTree tree = field.getTree();
        tree.setModel(field.getDisplayTreeModel());
        DataTipManager.get().register(tree);


        treePanel.add(explorer.getComponent());

        mainPanel.add(treePanel);
        mainPanel.add(quickSearchPanel, BorderLayout.BEFORE_FIRST_LINE);


    }

    public JComponent getComponent() {
        return mainPanel;
    }

    private String getFilterStatus(TreeModel displayModel, TreeModel originalModel) {
        int count = displayModel.getChildCount(displayModel.getRoot());
        String text = count + " out of " + originalModel.getChildCount(originalModel.getRoot()) + " files";
        return text;
    }

}
