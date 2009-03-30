package brainflow.app;

import brainflow.image.data.IImageData;
import brainflow.image.io.IImageDataSource;
import brainflow.utils.ProgressListener;
import com.jidesoft.dialog.JideOptionPane;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2007
 * Time: 10:44:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageProgressDialog extends SwingWorker<IImageData, Integer> implements ProgressListener {


    private int min;

    private int max;

    private int value;

    private String message = "";

    private IImageDataSource loadable;

    private JProgressBar progressBar;

    private JLabel progressLabel;

    private JDialog dialog;


    private Component parent;


    public ImageProgressDialog(Component _parent) {
        parent = _parent;
        buildGUI();
    }

    public ImageProgressDialog(IImageDataSource _loadable, Component _parent) {
        loadable = _loadable;
        parent = _parent;
        buildGUI();
    }


    public IImageDataSource getDataSource() {
        return loadable;
    }

    public void setDataSource(IImageDataSource loadable) {
        this.loadable = loadable;
    }

    private void buildGUI() {
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressLabel = new JLabel("Percent Complete: " + progressBar.getValue() + "%");


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(progressLabel);
        panel.add(Box.createVerticalStrut(3));
        panel.add(progressBar);
        panel.add(Box.createVerticalStrut(3));
        panel.add(new JLabel("Loading Image: " + loadable.getImageInfo().getImageLabel()));
        
        JideOptionPane optionPane = new JideOptionPane(panel, JOptionPane.INFORMATION_MESSAGE, 0, null, new Object[]{
                new JButton("Cancel")});

        optionPane.setTitle("Loading Image ");

        String details = ("Details are as Follows");
        optionPane.setDetails(details);

        dialog = optionPane.createDialog(parent, "Loading Image");
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.pack();


    }

    public JDialog getDialog() {
        return dialog;

    }


    protected void process(List<Integer> chunks) {
        for (Integer i : chunks) {
            double perc =  i / getMax();
            int prog = (int) (perc * 100f);
            progressBar.setValue(prog);
            progressLabel.setText("Percent Complete: " + prog + "%");

        }
    }

    protected void done() {
        dialog.setVisible(false);
        dialog.dispose();

    }

    protected IImageData doInBackground() throws BrainFlowException {
        return loadable.load(this);
    }




    public void setValue(int val) {
        value = val;
        publish(val);

    }


    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void setMinimum(int val) {
        min = val;
    }

    public void setMaximum(int val) {
        max = val;
    }


    public void setString(String _message) {
        message = _message;

    }



    public void setIndeterminate(boolean b) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void finished() {
        //
    }
}
