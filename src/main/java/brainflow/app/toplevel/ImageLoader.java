package brainflow.app.toplevel;

import brainflow.image.data.IImageData;
import brainflow.image.io.IImageDataSource;
import brainflow.utils.ProgressListener;

import javax.swing.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 5, 2009
 * Time: 9:29:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLoader extends SwingWorker<IImageData, Integer> {

    public static String PROGRESS_MESSAGE = "progress_message";

    private IImageDataSource dataSource;

    public ImageLoader(IImageDataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected IImageData doInBackground() throws Exception {
        return dataSource.load(new ProgressListener() {

            int min;

            int max;


            public void setValue(int val) {
                setProgress((int) Math.round((double) val / (max - min) * 100));
            }

            public void setMinimum(int val) {
                min = val;
            }

            public void setMaximum(int val) {
                max = val;
            }

            public void setString(String message) {
                ImageLoader.this.firePropertyChange("message", "nil", message);
            }

            public void setIndeterminate(boolean b) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void finished() {
                setProgress(100);
            }
        });

    }


    @Override
    protected void done() {
        try {
             IImageData result = get();
        } catch (InterruptedException e) {
            
        } catch (CancellationException e) {
            // get() throws CancellationException if background task was canceled

        } catch (ExecutionException e) {

        }


    }
}
