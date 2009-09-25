package brainflow.app.toplevel;

import brainflow.core.ImageView;
import brainflow.core.BrainFlowException;
import brainflow.core.ImageViewModel;
import brainflow.core.layer.ImageLayer3D;
import brainflow.image.io.BrainIO;
import brainflow.image.io.MemoryImageDataSource;
import brainflow.image.data.IImageData3D;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 29, 2009
 * Time: 5:42:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class LabelTester extends JPanel {

    ImageView view;

    public LabelTester(ImageView view) {
        this.view = view;
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        try {
            IImageData3D image = (IImageData3D)BrainIO.readNiftiImage("src/main/groovy/testdata/cohtrend_GLT#0_Tstat.nii");
            ImageLayer3D layer = ImageLayerFactory.createImageLayer(new MemoryImageDataSource(image));
            ImageView view = ImageViewFactory.createAxialView(new ImageViewModel("view", layer));

            jf.add(new LabelTester(view));
            jf.pack();
            jf.setVisible(true);
            
        } catch(BrainFlowException e) {
            e.printStackTrace();
        }


    }
}
