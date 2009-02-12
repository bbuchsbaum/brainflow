package brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: Nov 24, 2004
 * Time: 1:30:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProgressAdapter implements ProgressListener {

    public void setValue(int val) {};
    public void setMinimum(int val) {};
    public void setMaximum(int val) {};
    public void setString(String message) {};
    public void setIndeterminate(boolean b) {};
    public void finished() {};


}