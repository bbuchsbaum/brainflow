package brainflow.app.actions;

import brainflow.image.io.IImageSource;
import brainflow.app.toplevel.DataSourceManager;
import brainflow.app.toplevel.BrainFlow;
import com.jidesoft.dialog.JideOptionPane;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 6, 2006
 * Time: 8:22:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveDataSourceCommand extends BrainFlowCommand {

    public static final String SELECTED_DATASOURCE = "selected_datasource";

    public RemoveDataSourceCommand() {
        getDefaultFace(true).setText("Remove");
    }

    protected void handleExecute() {
       IImageSource dataSource = (IImageSource) getParameter(SELECTED_DATASOURCE);
        if (dataSource != null) {
            if (BrainFlow.get().isShowing(dataSource)) {
                String message = "The selected image " + dataSource.getStem() + " is currently being viewed. Are you sure you would like to remove it?";
                int ret = JideOptionPane.showConfirmDialog(super.getSelectedCanvas().getComponent(), message);
                if (ret == JideOptionPane.OK_OPTION) {
                    DataSourceManager.get().requestRemoval(dataSource);
                }
            } else {
                DataSourceManager.get().requestRemoval(dataSource);
            }
        }

    }

    
}
