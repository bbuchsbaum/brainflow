package brainflow.gui;

import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideBoxLayout;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 25, 2009
 * Time: 2:59:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileObjectRegexTest extends JPanel {

    //needs to search in unexpanded nodes

    private JSpinner depthSpinner = new JSpinner();

    private JTextField regexField = new JTextField();

    private JLabel regexLabel = new JLabel("Pattern: ");

    private JButton findButton = new JButton("Search");

    private JideSplitPane splitPane;

    private JList fileList = new JList();

    private FileExplorer explorer;

    private int recursiveDepth = 1;


    public FileObjectRegexTest() throws FileSystemException {
        explorer = new FileExplorer(VFS.getManager().resolveFile("c:/javacode/googlecode/brainflow/src/main/resources/data"));

        setLayout(new BorderLayout());
        Box box = Box.createHorizontalBox();
        box.add(regexLabel);
        box.add(regexField);
        box.add(findButton);

        depthSpinner.setMaximumSize(new Dimension(50, 200));
        depthSpinner.setModel(new SpinnerNumberModel(recursiveDepth,0,5,1));

        Box box2 = Box.createHorizontalBox();
        box2.add(new JLabel("Search Depth: "));
        box2.add(depthSpinner);

        JPanel treePanel = new JPanel();
        treePanel.setLayout(new BorderLayout());
        //treePanel.add(box, BorderLayout.NORTH);
        treePanel.add(new JScrollPane(explorer.getComponent()), BorderLayout.CENTER);

        depthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                recursiveDepth = ((Number)depthSpinner.getValue()).intValue();
                updateFileList();
            }
        });

        regexField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(Arrays.toString(explorer.getSelectedNodes().toArray()));
            }
        });

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFileList();

            }
        });

        splitPane = new JideSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
        splitPane.setProportionalLayout(true);
        splitPane.add(treePanel, JideBoxLayout.FLEXIBLE);
        splitPane.add(new JScrollPane(fileList), JideBoxLayout.VARY);

        add(splitPane, BorderLayout.CENTER);
        add(box, BorderLayout.NORTH);
        add(box2, BorderLayout.SOUTH);

    }

    private int numMatches(String[] parts, String[] otherParts) {
        int count=0;
        for (int j=parts.length-1, k=otherParts.length-1; j>= 0 && k >=0; j--, k--) {
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
        int start= parts.length - numParts;
        StringBuffer ret = new StringBuffer();
        for (int i=start; i<parts.length; i++) {
            ret.append("/");
            ret.append(parts[i]);

        }

        return ret.toString();

    }

    private String unambiguousIdentifier(int index, List<FileObject> fileList) {

        FileObject candidate = fileList.get(index);
        String[] parts = candidate.getName().getPath().split("/");
        if (fileList.size() == 1) {
            return parts[parts.length-1];           
        }

        int maxParts = 0;

        for (int i = 0; i < fileList.size(); i++) {
            if (i == index) continue;
            FileObject other = fileList.get(i);
            String[] otherParts = other.getName().getPath().split("/");
            maxParts = Math.max(maxParts, numMatches(parts, otherParts));
        }

        return joinParts(parts, maxParts+1);
        

    }

    private List<FileObject> matchChildren(FileExplorer.FileObjectNode node, String regex, List<FileObject> fileList, int depth) {
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

        System.out.println("filelist.size() " + fileList.size());

        return fileList;

    }

    private void updateFileList() {
        String regexPattern = regexField.getText();
        if (!regexPattern.startsWith(".*")) {
            regexPattern = ".*" + regexPattern;
        }

        List<FileExplorer.FileObjectNode> fileNodes = explorer.getSelectedNodes();
        List<FileObject> matchList = new ArrayList<FileObject>();
        System.out.println("fileNodes.size() " + fileNodes.size());
        for (FileExplorer.FileObjectNode node : fileNodes) {
            matchChildren(node, regexPattern, matchList, 0);

        }

        System.out.println("matchList.size()" + matchList.size());

        DefaultListModel listModel = new DefaultListModel();
        for (int i=0; i<matchList.size(); i++) {
            listModel.addElement(unambiguousIdentifier(i, matchList));
   
        }


        fileList.setModel(listModel);

    }

    private static Pattern globToRegexPattern(
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

        return Pattern.compile(t.toString());
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            JFrame jf = new JFrame();
            jf.add(new FileObjectRegexTest(), BorderLayout.CENTER);
            jf.pack();
            jf.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


}
