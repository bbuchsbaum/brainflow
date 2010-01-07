package brainflow.utils;

import brainflow.gui.FileExplorer;
import org.apache.commons.vfs.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 12, 2009
 * Time: 7:16:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileObjectMatcher {

    private FileObject rootDir;

    private int recursiveDepth = 1;

    private String regex;


    public FileObjectMatcher(FileObject rootDir, String regex, int recursiveDepth) {
        this.recursiveDepth = recursiveDepth;
        this.regex = regex;
        this.rootDir = rootDir;
    }

    public List<FileObject> matchFiles() {

        List<FileObject> matchList = new ArrayList<FileObject>();
        try {


            FileObject[] results = rootDir.findFiles(new FileSelector() {
                @Override
                public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {

                    if (fileSelectInfo.getFile().getType() == FileType.FILE) {
                         return fileSelectInfo.getFile().getName().getBaseName().matches(regex);
                    } else {
                        return false;
                    }

                }

                @Override
                public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
                    return fileSelectInfo.getDepth() > recursiveDepth;
                }
            });

            matchList.addAll(Arrays.asList(results));

        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }

        return matchList;


    }

    public static void main(String[] args) {
        try {
            FileObject fobj = VFS.getManager().resolveFile("c:/javacode/javalibs");
            List<FileObject> results = new FileObjectMatcher(fobj, "s.*jar", 1).matchFiles();

            for (FileObject f : results) {
                System.out.println("base name: " + f.getName().getBaseName());

            }
        } catch(FileSystemException e) {
            e.printStackTrace();
        }
    }


}
