package brainflow.app.presentation.controls;

import brainflow.gui.FileExplorer;
import com.jidesoft.combobox.ListComboBox;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.list.DualList;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideComboBox;
import com.jidesoft.swing.JideSplitButton;
import com.jidesoft.swing.JideSplitPane;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 3, 2010
 * Time: 9:21:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileObjectGroupSelector extends JPanel {


    private JSpinner depthSpinner = new JSpinner();

    private JTextField regexField = new JTextField();

    private JLabel regexLabel = new JLabel("Pattern Filter: ");

    //private JButton findButton = new JButton("Search");

    private JComboBox searchType = new JComboBox((new Object[]{GLOB, REGEX}));

    private FileExplorer explorer;

    private JideSplitPane splitPane;

    private JList fileList = new JList();

    private JTextField rootField = new JTextField();

    private FileObject rootFolder;

    private static final String GLOB = "glob";
    private static final String REGEX = "regex";


    private int recursiveDepth = 1;

    public FileObjectGroupSelector(FileObject root) throws FileSystemException {
        if (root.getType() != FileType.FOLDER) {
            throw new IllegalArgumentException("root file must be a directory");
        }

        rootFolder = root;
        explorer = new FileExplorer(rootFolder);
        explorer.getJTree().setRootVisible(false);
        explorer.getJTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);


        setLayout(new BorderLayout());

        Box topPanel = new Box(BoxLayout.X_AXIS);
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));
        topPanel.add(regexLabel);
        topPanel.add(regexField);
        topPanel.add(Box.createHorizontalStrut(4));
        topPanel.add(searchType);
        add(topPanel, BorderLayout.NORTH);

        //JPanel mainPanel = new JPanel();
        //MigLayout layout = new MigLayout("");
        // mainPanel.setLayout(layout);

        //add(regexLabel);
        //add(regexField, "gap left 0, growx");
        //add(findButton, "align right, wrap");

        //add(searchType, "wrap");

        //add(createSplitPane(), "span 3, grow, wrap");
        add(createSplitPane(), BorderLayout.CENTER);


        depthSpinner.setMaximumSize(new Dimension(50, 200));
        depthSpinner.setModel(new SpinnerNumberModel(recursiveDepth, 0, 5, 1));

        JPanel bottomPanel = new JPanel();
        MigLayout layout = new MigLayout("", "[][grow]", "[][]");
        bottomPanel.setLayout(layout);

        //bottomPanel.setBorder(BorderFactory.createEmptyBorder(4,12,8,8));
        bottomPanel.add(new JLabel("Root Folder: "));
        rootField.setEditable(false);
        rootField.setText(root.getName().getBaseName());
        bottomPanel.add(rootField, "wrap, width 150:150:150");
        bottomPanel.add(new JLabel("Search Depth: "), "gap top 8");

        bottomPanel.add(depthSpinner, "width 35:45:55, wrap");
        add(bottomPanel, BorderLayout.SOUTH);

        explorer.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                Object[] obj = path.getPath();
                Object lastNode = obj[obj.length - 1];
                if (lastNode instanceof FileExplorer.FileObjectNode) {
                    FileExplorer.FileObjectNode fnode = (FileExplorer.FileObjectNode) lastNode;
                    try {
                        if (fnode.getFileObject().getType() == FileType.FOLDER) {
                            rootField.setText(fnode.getFileObject().getName().getBaseName());
                            rootFolder = fnode.getFileObject();
                            updateFileList();
                        }
                    } catch (FileSystemException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });

        depthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                recursiveDepth = ((Number) depthSpinner.getValue()).intValue();

                updateFileList();
            }
        });

        regexField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFileList();
                System.out.println(Arrays.toString(explorer.getSelectedNodes().toArray()));
            }
        });




    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 600);
    }

    private JideSplitPane createSplitPane() {
        JPanel treePanel = new JPanel();
        treePanel.setLayout(new BorderLayout());
        treePanel.setMinimumSize(new Dimension(300, 100));
        treePanel.add(new JScrollPane(explorer.getComponent()), BorderLayout.CENTER);
        splitPane = new JideSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
        splitPane.setProportionalLayout(true);

        treePanel.setBorder(BorderFactory.createTitledBorder("File System"));
        splitPane.add(treePanel, JideBoxLayout.FLEXIBLE);

        JScrollPane jsp = new JScrollPane(fileList);
        jsp.setBorder(BorderFactory.createTitledBorder("Filter Selection"));
        splitPane.add(jsp, JideBoxLayout.VARY);
        splitPane.setProportions(new double[]{.55});
        return splitPane;


    }

    private int numMatches(String[] parts, String[] otherParts) {
        int count = 0;
        for (int j = parts.length - 1, k = otherParts.length - 1; j >= 0 && k >= 0; j--, k--) {
            if (parts[j].equals(otherParts[k])) {
                count++;
            } else {
                break;
            }
        }

        assert count <= parts.length;
        return count;

    }

    private String joinParts(String[] parts, int numParts) {
        int start = parts.length - numParts;
        StringBuffer ret = new StringBuffer();
        for (int i = start; i < parts.length; i++) {
            ret.append("/");
            ret.append(parts[i]);

        }

        return ret.toString();

    }

    private String unambiguousIdentifier(int index, java.util.List<FileObject> fileList) {

        FileObject candidate = fileList.get(index);
        String[] parts = candidate.getName().getPath().split("/");
        if (fileList.size() == 1) {
            return parts[parts.length - 1];
        }

        int maxParts = 0;

        for (int i = 0; i < fileList.size(); i++) {
            if (i == index) continue;
            FileObject other = fileList.get(i);
            String[] otherParts = other.getName().getPath().split("/");
            maxParts = Math.max(maxParts, numMatches(parts, otherParts));
        }

        return joinParts(parts, maxParts + 1);


    }

    private java.util.List<FileObject> matchChildren(FileExplorer.FileObjectNode node, String regex, java.util.List<FileObject> fileList, int depth) {
        if (depth > recursiveDepth) return fileList;

        if (node.isLeaf()) {
            String name = node.getFileObject().getName().getPath();
            if (name.matches(regex)) {

                fileList.add(node.getFileObject());

            }
        } else {
            Enumeration e = node.children();
            while (e.hasMoreElements()) {
                matchChildren((FileExplorer.FileObjectNode) e.nextElement(), regex, fileList, depth + 1);
            }
        }


        return fileList;

    }

    private void updateFileList() {

        String pattern = regexField.getText();
        if (pattern.equals("")) {
            return;
        }

        if (searchType.getSelectedItem() == GLOB) {
            pattern = FileObjectGroupSelector.globToRegexPattern(pattern);
        }

        if (!pattern.startsWith(".*")) {
            pattern = ".*" + pattern;
        }

        java.util.List<FileExplorer.FileObjectNode> fileNodes = explorer.getSelectedNodes();
        java.util.List<FileObject> matchList = new ArrayList<FileObject>();
        //System.out.println("fileNodes.size() " + fileNodes.size());
        for (FileExplorer.FileObjectNode node : fileNodes) {
            matchChildren(node, pattern, matchList, 0);

        }


        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < matchList.size(); i++) {
            listModel.addElement(unambiguousIdentifier(i, matchList));

        }


        fileList.setModel(listModel);

    }

    private static String globToRegexPattern(
            final String glob)
            throws PatternSyntaxException {
        /* Stack to keep track of the parser mode: */
        /* "--" : Base mode (first on the stack)   */
        /* "[]" : Square brackets mode "[...]"     */
        /* "{}" : Curly braces mode "{...}"        */
        final Deque<String> parserMode = new ArrayDeque<String>();
        parserMode.push("--"); // base mode

        final int globLength = glob.length();
        int index = 0; // index in glob

        /* equivalent REGEX expression to be compiled */
        final StringBuilder t = new StringBuilder();

        while (index < globLength) {
            char c = glob.charAt(index++);

            if (c == '\\') {
                /***********************
                 * (1) ESCAPE SEQUENCE *
                 ***********************/

                if (index == globLength) {
                    /* no characters left, so treat '\' as literal char */
                    t.append(Pattern.quote("\\"));
                } else {
                    /* read next character */
                    c = glob.charAt(index);
                    final String s = c + "";

                    if (("--".equals(parserMode.peek()) && "\\[]{}?*".contains(s)) ||
                            ("[]".equals(parserMode.peek()) && "\\[]{}?*!-".contains(s)) ||
                            ("{}".equals(parserMode.peek()) && "\\[]{}?*,".contains(s))) {
                        /* escape the construct char */
                        index++;
                        t.append(Pattern.quote(s));
                    } else {
                        /* treat '\' as literal char */
                        t.append(Pattern.quote("\\"));
                    }
                }
            } else if (c == '*') {
                /************************
                 * (2) GLOB PATTERN '*' *
                 ************************/

                /* create non-capturing group to match zero or more characters */
                t.append(".*");
            } else if (c == '?') {
                /************************
                 * (3) GLOB PATTERN '?' *
                 ************************/

                /* create non-capturing group to match exactly one character */
                t.append('.');
            } else if (c == '[') {
                /****************************
                 * (4) GLOB PATTERN "[...]" *
                 ****************************/

                /* opening square bracket '[' */
                /* create non-capturing group to match exactly one character */
                /* inside the sequence */
                t.append('[');
                parserMode.push("[]");

                /* check for negation character '!' immediately after */
                /* the opening bracket '[' */
                if ((index < globLength) &&
                        (glob.charAt(index) == '!')) {
                    index++;
                    t.append('^');
                }
            } else if ((c == ']') && "[]".equals(parserMode.peek())) {
                /* closing square bracket ']' */
                t.append(']');
                parserMode.pop();
            } else if ((c == '-') && "[]".equals(parserMode.peek())) {
                /* character range '-' in "[...]" */
                t.append('-');
            } else if (c == '{') {
                /****************************
                 * (5) GLOB PATTERN "{...}" *
                 ****************************/

                /* opening curly brace '{' */
                /* create non-capturing group to match one of the */
                /* strings inside the sequence */
                t.append("(?:(?:");
                parserMode.push("{}");
            } else if ((c == '}') && "{}".equals(parserMode.peek())) {
                /* closing curly brace '}' */
                t.append("))");
                parserMode.pop();
            } else if ((c == ',') && "{}".equals(parserMode.peek())) {
                /* comma between strings in "{...}" */
                t.append(")|(?:");
            } else {
                /*************************
                 * (6) LITERAL CHARACTER *
                 *************************/

                /* convert literal character to a regex string */
                t.append(Pattern.quote(c + ""));
            }
        }
        /* done parsing all chars of the source pattern string */

        /* check for mismatched [...] or {...} */
        if ("[]".equals(parserMode.peek()))
            throw new PatternSyntaxException("Cannot find matching closing square bracket ] in GLOB expression", glob, -1);

        if ("{}".equals(parserMode.peek()))
            throw new PatternSyntaxException("Cannot find matching closing curly brace } in GLOB expression", glob, -1);

        return t.toString();
    }

    public static void main(String[] args) {
        try {
            com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
            //com.jidesoft.plaf.LookAndFeelFactory.installDefaultLookAndFeel();
            //LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2007_STYLE);
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            JFrame jf = new JFrame();
            FileObjectGroupSelector selector = new FileObjectGroupSelector(VFS.getManager().resolveFile("c:/javacode"));

            ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.RIGHT);
            buttonPanel.setSizeConstraint(ButtonPanel.NO_LESS_THAN);

            JButton okButton = new JButton("OK");
            JButton resetButton = new JButton("Cancel");

            buttonPanel.addButton(okButton, ButtonPanel.AFFIRMATIVE_BUTTON);
            buttonPanel.addButton(resetButton, ButtonPanel.CANCEL_BUTTON);


            buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(selector, BorderLayout.CENTER);
            panel.add(buttonPanel, BorderLayout.SOUTH);
            panel.setMinimumSize(new Dimension(800, 100));
            jf.add(panel, BorderLayout.CENTER);
            jf.pack();
            jf.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


}
