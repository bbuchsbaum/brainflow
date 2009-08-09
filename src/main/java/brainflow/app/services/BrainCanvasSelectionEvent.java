package brainflow.app.services;

import org.bushe.swing.event.AbstractEventServiceEvent;
import brainflow.core.IBrainCanvas;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 2, 2009
 * Time: 8:59:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrainCanvasSelectionEvent extends AbstractEventServiceEvent  {

    private IBrainCanvas selectedCanvas;

    public BrainCanvasSelectionEvent(IBrainCanvas canvas) {
        super(canvas);
    }

    public IBrainCanvas getSelectedCanvas() {
        return selectedCanvas;
    }
}
