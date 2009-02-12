/*
 * AnalyzeFileNameFilter.java
 *
 * Created on April 29, 2003, 9:21 AM
 */

package brainflow.image.io;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Bradley
 */
public class AnalyzeFileFilter extends FileFilter {

    /**
     * Creates a new instance of AnalyzeFileNameFilter
     */
    public AnalyzeFileFilter() {
    }


    public boolean accept(File pathname) {
        if (pathname.getAbsolutePath().endsWith(".hdr"))
            return true;
        return false;
    }


    public String getDescription() {
        return "Analyze 7.5 images";
    }

}
