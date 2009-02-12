/*
 * AnalyzeFileNameFilter.java
 *
 * Created on April 29, 2003, 9:21 AM
 */

package brainflow.image.io;
//import javax.swing.filechooser.*;
import java.io.*;
/**
 *
 * @author  Bradley
 */
public class GeFileFilter implements FileFilter {
    
    /** Creates a new instance of GeFileFilter */
    public GeFileFilter() {
    }
    
    
    public boolean accept(File pathname) {
        String name = pathname.getName();
        if ( name.matches("I.\\d\\d\\d") ) 
            return true;
        else 
            return false;
    }    
    
   
    
    public String getDescription() {
        return "GE IFiles";
    }
    
}
