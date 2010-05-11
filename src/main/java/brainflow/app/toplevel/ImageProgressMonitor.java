package brainflow.app.toplevel;

import brainflow.image.io.IImageSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 5, 2009
 * Time: 10:19:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageProgressMonitor {

    private ProgressMonitor monitor;

    private IImageSource dataSource;

    private Component parentComponent;

    public ImageProgressMonitor(IImageSource dataSource, Component parent) {
        this.dataSource = dataSource;
        parentComponent = parent;
    }

    public IImageSource getDataSource() {
        return dataSource;
    }

    public void loadImage(final ActionListener callback) {
        monitor = new ProgressMonitor(parentComponent, "loading image " + dataSource.getStem(), "", 0, 100);
        monitor.setProgress(0);
        monitor.setMillisToDecideToPopup(250);

        ImageLoader loader = new ImageLoader(dataSource) {
            @Override
            protected void done() {
                callback.actionPerformed(new ActionEvent(ImageProgressMonitor.this, 1, "done"));
            }
        };

        loader.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getPropertyName().equals("progress")) {
                    // get the % complete from the progress event
                    // and set it on the progress monitor
                    int progress = (Integer)event.getNewValue();
                    monitor.setProgress(progress);
                } else if (event.getPropertyName().equals("message")){
                    monitor.setNote(event.getNewValue().toString());
                }


            }
        });


        loader.execute();

    }


}
