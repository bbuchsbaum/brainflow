package brainflow.app.dnd;

import brainflow.core.layer.AbstractLayer;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 12, 2007
 * Time: 3:16:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractLayerTransferable implements Transferable {

    private String localObject = DataFlavor.javaJVMLocalObjectMimeType +
            ";class=brainflow.core.layer.AbstractLayer";


    private DataFlavor abstractLayerFlavor = null;

    private DataFlavor[] flavors = new DataFlavor[1];

    private AbstractLayer layer;

    public AbstractLayerTransferable(AbstractLayer layer) {
        try {
            DataFlavor flavor = new DataFlavor(localObject);
            flavors[0] = flavor;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.layer = layer;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
         if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }

        return layer;

    }

    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavors[0].equals(flavor)) {
            return true;
        } else {
            return false;
        }
    }

    
}
