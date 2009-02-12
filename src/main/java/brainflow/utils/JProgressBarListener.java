package brainflow.utils;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: Nov 24, 2004
 * Time: 1:33:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class JProgressBarListener implements ProgressListener {

    JProgressBar progBar;
    JDialog dialog;

    public JProgressBarListener(JProgressBar _progBar) {
        progBar = _progBar;
    }

    public JProgressBarListener(JProgressBar _progBar, JDialog _dialog) {
        progBar = _progBar;
        dialog = _dialog;
    }

    public void setValue(int val) {
        progBar.setValue(val);
    }

    public void setMinimum(int val) {
        progBar.setMinimum(val);
    }

    public void setMaximum(int val) {
        progBar.setMaximum(val);
    }

    public void setString(String message) {
        progBar.setString(message);
    }

    public void setIndeterminate(boolean b) {
        progBar.setIndeterminate(b);
    }

    public void finished() {
        if (dialog != null) {
            dialog.setVisible(false);
        }     
    }


}
