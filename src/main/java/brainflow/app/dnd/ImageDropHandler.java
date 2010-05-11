package brainflow.app.dnd;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jun 27, 2008
 * Time: 12:12:14 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImageDropHandler extends TransferHandler {

    protected List<DataFlavor> dropFlavors;

    public ImageDropHandler() {
        initDataFlavors();
    }

    public List<DataFlavor> getDataFalvors() {
        return dropFlavors;
    }

    protected void initDataFlavors() {
        try {
            DataFlavor loadableImageFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=brainflow.image.io.IImageSource");

            DataFlavor imageLayerFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=brainflow.core.layer.ImageLayer");

            dropFlavors = new ArrayList<DataFlavor>();
            dropFlavors.add(loadableImageFlavor);
            dropFlavors.add(imageLayerFlavor);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public abstract void dispatchOnObject(Object obj, TransferSupport support);

    public boolean canImport(TransferSupport support) {

        DataFlavor[] flavors = support.getDataFlavors();
      
        for (int i = 0; i < flavors.length; i++) {

            Class c = flavors[i].getRepresentationClass();
            for (DataFlavor f : dropFlavors) {
                if (f.getRepresentationClass().isAssignableFrom(c)) {
                    return true;
                } else {
                    
                }
            }

        }

        return false;
    }

    public boolean importData(TransferSupport support) {

        try {
            if (canImport(support)) {
                try {
                    for (DataFlavor flavor : dropFlavors) {
                        if (support.getTransferable().isDataFlavorSupported(flavor)) {
                            Object obj = support.getTransferable().getTransferData(flavor);
                            dispatchOnObject(obj, support);
                        }

                    }

                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }


        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return true;

    }


}
