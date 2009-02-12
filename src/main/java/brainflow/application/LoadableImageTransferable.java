package brainflow.application;

import brainflow.image.io.IImageDataSource;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 19, 2004
 * Time: 4:56:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadableImageTransferable implements Transferable {

    private String localObject = DataFlavor.javaJVMLocalObjectMimeType +
                                    ";class=brainflow.image.io.IImageDataSource";


    private DataFlavor loadableImageFlavor = null;

    private DataFlavor[] flavors = new DataFlavor[1];

    private IImageDataSource[] limg;

    public LoadableImageTransferable(IImageDataSource[] _limg) {
        try {
            DataFlavor flavor = new DataFlavor(localObject);
            flavors[0] = flavor;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        limg = _limg;

    }

    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavors[0].equals(flavor)) return true;
        return false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }

        return limg;

    }

    public static void main(String[] args) {
        LoadableImageTransferable t = new LoadableImageTransferable(null);
    }
}
