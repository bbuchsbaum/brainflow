package brainflow.core;

import brainflow.image.io.IImageDataSource;

import javax.swing.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Feb 16, 2010
 * Time: 8:33:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageBrowser extends JPanel {

    private JList dataSourceList;

    private List<IImageDataSource> sources;

    private OrthoImageView imageView;

    public ImageBrowser(List<IImageDataSource> sources) {
        this.sources = sources;
    }

    private void initGui() {

    }

    private void initModel() {
        
    }
}
