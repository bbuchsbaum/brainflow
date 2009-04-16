package brainflow.app.toplevel;

import brainflow.core.ImageViewModel;
import brainflow.app.BrainFlowProject;

import javax.swing.event.ListDataEvent;
import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2007
 * Time: 5:27:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrainFlowProjectEvent extends EventObject {

    private ImageViewModel model;

    private BrainFlowProject project;

    private ListDataEvent event;



    public BrainFlowProjectEvent(BrainFlowProject _project, ImageViewModel _model, ListDataEvent _event) {
        super(_project);

        project = _project;
        model = _model;
        event = _event;
    }

    public ImageViewModel getModel() {
        return model;
    }

    public BrainFlowProject getProject() {
        return project;
    }

    public ListDataEvent getListDataEvent() {
        // may be null
        return event;
    }

    
}
