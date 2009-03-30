package brainflow.app.dnd;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jun 26, 2008
 * Time: 10:27:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class DnDUtils {

    

    public static Transferable createTransferable(final Object obj)  {
        final String localObject = DataFlavor.javaJVMLocalObjectMimeType +
                ";class=" + obj.getClass().getCanonicalName();

        final DataFlavor flavor;

        try {
            flavor = new DataFlavor(localObject);
        } catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        return new Transferable() {
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{flavor};
            }

            public boolean isDataFlavorSupported(DataFlavor oflavor) {
                if (oflavor.getRepresentationClass().isAssignableFrom(obj.getClass())) return true;
                return false;
            }

            public DataFlavor getDataFlavor() {
                return flavor;
            }

            public Object getTransferData(DataFlavor oflavor)  {
                try {
                    if (isDataFlavorSupported(oflavor)) {
                        return obj;
                    } else {
                        return null;
                    }
                } catch(Exception ex) {
                    throw new RuntimeException(ex);
                }

            }

            
        };
    }

    public static void main(String[] args) {
        try {
            Transferable trans = DnDUtils.createTransferable("hello");
            String obj = (String)trans.getTransferData(trans.getTransferDataFlavors()[0]);
        
        } catch(Exception  e) {
            e.printStackTrace();
        }

    }
}
