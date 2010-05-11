package brainflow.gui;

import brainflow.utils.ResourceLoader;
import org.apache.commons.vfs.*;


import javax.swing.*;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Aug 20, 2004
 * Time: 11:16:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileExplorer extends AbstractPresenter {


    //protected static FileSystemView fsv = FileSystemView.getFileSystemView();

    private static Logger log = Logger.getLogger(FileExplorer.class.getName());

    private List<FileObject> rootList = new ArrayList<FileObject>();

    protected JTree fileTree;

    protected DefaultTreeModel treeModel;

    private ImageIcon folderIcon = new ImageIcon(ResourceLoader.getResource("icons/folder.png"));

    private ImageIcon folderOpenIcon = new ImageIcon(ResourceLoader.getResource("icons/folderOpen.png"));

    private ImageIcon leafIcon = new ImageIcon(ResourceLoader.getResource("icons/intf_obj.gif"));

    protected FileSelector selector;

    public FileExplorer(FileObject _rootObject) {
        rootList.add(_rootObject);

        selector = new FileSelector() {
            @Override
            public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
                if (fileSelectInfo.getDepth() == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
                if (fileSelectInfo.getDepth() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        init();
    }


    public FileExplorer(FileObject _rootObject, FileSelector _selector) {
        rootList.add(_rootObject);

        selector = _selector;
        init();
    }


    public JTree getJTree() {
        return fileTree;
    }


    public void addTreeSelectionListener(TreeSelectionListener tsl) {
        fileTree.addTreeSelectionListener(tsl);
    }

    public void addTreeExpansionListener(TreeExpansionListener tel) {
        fileTree.addTreeExpansionListener(tel);
    }


    private void init() {


        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("File Systems");
        treeModel = new DefaultTreeModel(rootNode);


        fileTree = new JTree(treeModel);
        fileTree.setCellRenderer(new FileTreeCellRenderer());


        for (int i=0; i<rootList.size(); i++) {
            if (i == 0) {
                addFileRoot(rootList.get(i), true);
            } else {
                addFileRoot(rootList.get(i), false);
            }
        }


        fileTree.setDragEnabled(true);
       
        fileTree.scrollPathToVisible(new TreePath(rootNode.getPath()));
        

    }


    public void addFileRoot(FileObject fobj, boolean select) {
        DefaultMutableTreeNode node = createTreeNode(fobj);

        MutableTreeNode root = (MutableTreeNode) treeModel.getRoot();
        treeModel.insertNodeInto(node, root,
                root.getChildCount());

        if (select) {
            fileTree.setSelectionPath(new TreePath(node.getPath()));
        }
        fileTree.scrollPathToVisible(new TreePath(node.getPath()));
       

    }

    protected DefaultMutableTreeNode createTreeNode(FileObject fobj) {
        return new FileObjectNode(fobj, selector);
    }


    public JComponent getComponent() {
        return fileTree;
    }

    public List<FileObjectNode> getSelectedNodes() {

        int[] selRows = fileTree.getSelectionModel().getSelectionRows();
        if (selRows ==null) {
            return Collections.emptyList();
        }
        
        //fileTree.getPathForRow()

        //TreePath path = fileTree.getSelectionModel().getSelectionPath();

        //if (path == null) {
        //    return null;
        //}

        List<FileObjectNode> nodes = new ArrayList<FileObjectNode>();

        //Object[] obj = path.getPath();
        //for (Object nodeobj : obj) {
        //    if (nodeobj instanceof FileObjectNode) {
        //        nodes.add((FileObjectNode) nodeobj);
        //    }
        //}

        for (int i : selRows) {
            TreePath path = fileTree.getPathForRow(i);
            Object[] opath = path.getPath();
            if (opath != null && opath.length > 0)
                nodes.add((FileObjectNode) opath[opath.length-1]);
        }
        
        return nodes;
    }


    public static class FileObjectNode extends DefaultMutableTreeNode {

        private boolean areChildrenDefined = false;


        private boolean leaf;

        private FileObject fileObject;

        private FileSelector selector;

        public FileObjectNode(FileObject _fobj, FileSelector _selector) {
            fileObject = _fobj;
            selector = _selector;

            try {
                if (fileObject.getType() == FileType.FOLDER) {
                    leaf = false;
                } else {
                    leaf = true;
                }
            } catch (FileSystemException e) {
                throw new RuntimeException("error accessing file type of file object: " + fileObject, e);
            }

        }

        public FileObject getFileObject() {
            return fileObject;
        }


        public boolean isLeaf() {
            return leaf;
        }

        public int getChildCount() {
            if (!areChildrenDefined)
                defineChildNodes();
            return (super.getChildCount());
        }

        @Override
        public Enumeration children() {
            if (!areChildrenDefined) {
                defineChildNodes();
            }
            return super.children();
        }

        private void defineChildNodes() {

            areChildrenDefined = true;

            try {

                FileObject[] children = fileObject.findFiles(selector);

                for (int i = 0; i < children.length; i++) {
                    add(new FileObjectNode(children[i], selector));
                }
            } catch (FileSystemException e) {
                e.printStackTrace();
            }
        }

        public String toString() {
            if (this == getRoot()) {
                try {
                    URI uri = new URI(fileObject.getName().getURI());

                    return uri.getHost() + ":" + uri.getPath();
                } catch (URISyntaxException e) {
                    return fileObject.getName().getURI();
                }
            } else
                return fileObject.getName().getBaseName();
        }
    }


    class CustomFileSelector implements FileSelector {


        public CustomFileSelector() {

        }

        /**
         * Determines if a file or folder should be selected.
         */
        public boolean includeFile(final FileSelectInfo fileInfo)
                throws FileSystemException {

            if (fileInfo.getDepth() == 0)
                return false;

            else
                return true;
        }

        /**
         * Determines whether a folder should be traversed.
         */
        public boolean traverseDescendents(final FileSelectInfo fileInfo) {
            if (fileInfo.getDepth() == 0) {
                return true;
            } else
                return false;
        }
    }


    private class FileTreeCellRenderer extends DefaultTreeCellRenderer {
        /**
         * Icon cache to speed the rendering.
         */
        private Map<String, Icon> iconCache = new HashMap<String, Icon>();

        /**
         * Root name cache to speed the rendering.
         */
        private Map<FileObject, String> rootNameCache = new HashMap<FileObject, String>();

        /*
        * (non-Javadoc)
        *
        * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
        *      java.lang.Object, boolean, boolean, boolean, int, boolean)
        */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {

            FileObjectNode ftn;

            if (value instanceof FileObjectNode) {
                ftn = (FileObjectNode) value;
            } else {
                return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            }


            FileObject file = ftn.getFileObject();
            String filename = "";
            Icon icon = null;

            try {
                if (file != null) {


                    if (file.equals(file.getFileSystem().getRoot())) {
                        // long start = System.currentTimeMillis();
                        filename = this.rootNameCache.get(file);
                        //if (filename == null) {
                        //    filename = fsv.getSystemDisplayName(file);
                        //    this.rootNameCache.put(file, filename);
                        //}
                        // long end = System.currentTimeMillis();
                    } else {
                        filename = file.getName().getBaseName();
                    }

                    if (FileType.FOLDER == file.getType()) {
                        icon = folderIcon;
                    }
                }


                JLabel result = (JLabel) super.getTreeCellRendererComponent(tree,
                        filename, sel, expanded, leaf, row, hasFocus);
                if (icon != null) {
                    result.setIcon(icon);

                }

                return result;


            } catch (Exception e) {
                log.severe(e.getMessage());
                throw new RuntimeException(e);
            }


        }
    }

    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel());
            FileExplorer explorer = new FileExplorer(VFS.getManager().resolveFile("C:/javacode"), new FileSelector() {
                public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
                    return true;
                }

                public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
                    if (fileSelectInfo.getDepth() == 0) {
                        return true;
                    } else
                        return false;

                }
            });


            JFrame frame = new JFrame();
            frame.add(explorer.getComponent(), BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}